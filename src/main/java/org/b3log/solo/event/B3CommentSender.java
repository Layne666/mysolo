/*
 * Solo - A small and beautiful blogging system written in Java.
 * Copyright (c) 2010-2019, b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.b3log.solo.event;

import jodd.http.HttpRequest;
import org.b3log.latke.Keys;
import org.b3log.latke.Latkes;
import org.b3log.latke.event.AbstractEventListener;
import org.b3log.latke.event.Event;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.ioc.Singleton;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.User;
import org.b3log.latke.util.Strings;
import org.b3log.solo.SoloServletListener;
import org.b3log.solo.model.Article;
import org.b3log.solo.model.Comment;
import org.b3log.solo.model.Option;
import org.b3log.solo.model.UserExt;
import org.b3log.solo.repository.ArticleRepository;
import org.b3log.solo.repository.UserRepository;
import org.b3log.solo.service.PreferenceQueryService;
import org.b3log.solo.util.Solos;
import org.json.JSONObject;

/**
 * This listener is responsible for sending comment to B3log Rhythm. Sees <a href="https://hacpai.com/b3log">B3log 构思</a> for more details.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.1.5, Feb 9, 2019
 * @since 0.5.5
 */
@Singleton
public class B3CommentSender extends AbstractEventListener<JSONObject> {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(B3CommentSender.class);

    /**
     * Preference query service.
     */
    @Inject
    private PreferenceQueryService preferenceQueryService;

    /**
     * User repository.
     */
    @Inject
    private UserRepository userRepository;

    /**
     * Article repository.
     */
    @Inject
    private ArticleRepository articleRepository;

    @Override
    public void action(final Event<JSONObject> event) {
        final JSONObject data = event.getData();

        LOGGER.log(Level.DEBUG, "Processing an event [type={0}, data={1}] in listener [className={2}]",
                event.getType(), data, B3ArticleSender.class.getName());
        try {
            final JSONObject originalComment = data.getJSONObject(Comment.COMMENT);

            final JSONObject preference = preferenceQueryService.getPreference();
            if (null == preference) {
                LOGGER.log(Level.ERROR, "Not found preference");

                return;
            }

            if (Latkes.getServePath().contains("localhost") || Strings.isIPv4(Latkes.getServePath())) {
                LOGGER.log(Level.TRACE, "Solo runs on local server, so should not send this comment[id={0}] to Symphony",
                        originalComment.getString(Keys.OBJECT_ID));
                return;
            }

            final String articleId = originalComment.getString(Comment.COMMENT_ON_ID);
            final JSONObject article = articleRepository.get(articleId);
            final String articleAuthorId = article.optString(Article.ARTICLE_AUTHOR_ID);
            final JSONObject articleAuthor = userRepository.get(articleAuthorId);

            final JSONObject comment = new JSONObject().
                    put("id", originalComment.optString(Keys.OBJECT_ID)).
                    put("articleId", articleId).
                    put("content", originalComment.getString(Comment.COMMENT_CONTENT)).
                    put("authorName", originalComment.optString(Comment.COMMENT_NAME));
            final JSONObject client = new JSONObject().
                    put("title", preference.getString(Option.ID_C_BLOG_TITLE)).
                    put("host", Latkes.getServePath()).
                    put("name", "Solo").
                    put("ver", SoloServletListener.VERSION).
                    put("userName", articleAuthor.optString(User.USER_NAME)).
                    put("userB3Key", articleAuthor.optString(UserExt.USER_B3_KEY));
            final JSONObject requestJSONObject = new JSONObject().
                    put("comment", comment).
                    put("client", client);

            HttpRequest.post("https://rhythm.b3log.org/api/comment").bodyText(requestJSONObject.toString()).
                    header("User-Agent", Solos.USER_AGENT).contentTypeJson().sendAsync();
            LOGGER.log(Level.DEBUG, "Pushed a comment to Sym");
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Pushes a comment to Sym failed: " + e.getMessage());
        }
    }

    /**
     * Gets the event type {@linkplain EventTypes#ADD_COMMENT_TO_ARTICLE}.
     *
     * @return event type
     */
    @Override
    public String getEventType() {
        return EventTypes.ADD_COMMENT_TO_ARTICLE;
    }
}
