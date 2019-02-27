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
package org.b3log.solo.processor;

import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.User;
import org.b3log.latke.repository.Transaction;
import org.b3log.latke.servlet.HttpMethod;
import org.b3log.latke.servlet.RequestContext;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.util.Ids;
import org.b3log.latke.util.Strings;
import org.b3log.solo.model.Article;
import org.b3log.solo.model.Comment;
import org.b3log.solo.model.Common;
import org.b3log.solo.model.UserExt;
import org.b3log.solo.repository.ArticleRepository;
import org.b3log.solo.repository.CommentRepository;
import org.b3log.solo.repository.UserRepository;
import org.b3log.solo.service.ArticleMgmtService;
import org.b3log.solo.service.ArticleQueryService;
import org.b3log.solo.service.CommentMgmtService;
import org.b3log.solo.service.PreferenceQueryService;
import org.json.JSONObject;

import java.util.Date;

/**
 * Receiving articles and comments from B3log community. Visits <a href="https://hacpai.com/b3log">B3log 构思</a> for more details.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 2.0.1.0, Feb 18, 2019
 * @since 0.5.5
 */
@RequestProcessor
public class B3Receiver {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(B3Receiver.class);

    /**
     * User repository.
     */
    @Inject
    private UserRepository userRepository;

    /**
     * Comment repository.
     */
    @Inject
    private static CommentRepository commentRepository;

    /**
     * Article repository.
     */
    @Inject
    private ArticleRepository articleRepository;

    /**
     * Comment management service.
     */
    @Inject
    private CommentMgmtService commentMgmtService;

    /**
     * Preference query service.
     */
    @Inject
    private PreferenceQueryService preferenceQueryService;

    /**
     * Article management service.
     */
    @Inject
    private ArticleMgmtService articleMgmtService;

    /**
     * Article query service.
     */
    @Inject
    private ArticleQueryService articleQueryService;

    /**
     * Adds or updates an article with the specified request.
     * <p>
     * Request json:
     * <pre>
     * {
     *     "article": {
     *         "id": "",
     *          "title": "",
     *          "content": "",
     *          "contentHTML": "",
     *          "tags": "tag1,tag2,tag3"
     *     },
     *     "client": {
     *         "userName": "",
     *         "userB3Key": ""
     *     }
     * }
     * </pre>
     * </p>
     * <p>
     * Renders the response with a json object, for example,
     * <pre>
     * {
     *     "sc": boolean,
     *     "oId": "", // Generated article id
     *     "msg": ""
     * }
     * </pre>
     * </p>
     *
     * @param context the specified http request context
     */
    @RequestProcessing(value = "/apis/symphony/article", method = {HttpMethod.POST, HttpMethod.PUT})
    public void postArticle(final RequestContext context) {
        final JSONObject ret = new JSONObject().put(Keys.CODE, 0);
        context.renderJSON(ret);

        final JSONObject requestJSONObject = context.requestJSON();
        LOGGER.log(Level.INFO, "Adds an article from Sym [" + requestJSONObject.toString() + "]");

        try {
            final JSONObject client = requestJSONObject.optJSONObject("client");
            final String articleAuthorName = client.optString(User.USER_NAME);
            final JSONObject articleAuthor = userRepository.getByUserName(articleAuthorName);
            if (null == articleAuthor) {
                ret.put(Keys.CODE, 1);
                ret.put(Keys.MSG, "No found user [" + articleAuthorName + "]");

                return;
            }

            final String b3Key = client.optString(UserExt.USER_B3_KEY);
            final String key = articleAuthor.optString(UserExt.USER_B3_KEY);
            if (!StringUtils.equals(key, b3Key)) {
                ret.put(Keys.CODE, 1);
                ret.put(Keys.MSG, "Wrong key");

                return;
            }

            final JSONObject symArticle = requestJSONObject.optJSONObject(Article.ARTICLE);
            final String articleId = symArticle.optString("id");
            final JSONObject oldArticle = articleQueryService.getArticleById(articleId);
            if (null == oldArticle) {
                final JSONObject article = new JSONObject().
                        put(Keys.OBJECT_ID, symArticle.optString("id")).
                        put(Article.ARTICLE_TITLE, symArticle.optString("title")).
                        put(Article.ARTICLE_CONTENT, symArticle.optString("content")).
                        put(Article.ARTICLE_TAGS_REF, symArticle.optString("tags"));
                article.put(Article.ARTICLE_AUTHOR_ID, articleAuthor.getString(Keys.OBJECT_ID));
                final String articleContent = article.optString(Article.ARTICLE_CONTENT);
                article.put(Article.ARTICLE_ABSTRACT, Article.getAbstract(articleContent));
                article.put(Article.ARTICLE_IS_PUBLISHED, true);
                article.put(Common.POST_TO_COMMUNITY, false); // Do not send to rhythm
                article.put(Article.ARTICLE_COMMENTABLE, true);
                article.put(Article.ARTICLE_VIEW_PWD, "");
                final String content = article.getString(Article.ARTICLE_CONTENT);
                article.put(Article.ARTICLE_CONTENT, content);
                final JSONObject addRequest = new JSONObject().put(Article.ARTICLE, article);
                articleMgmtService.addArticle(addRequest);

                return;
            }

            final String articleContent = symArticle.optString("content");
            oldArticle.put(Article.ARTICLE_ABSTRACT, Article.getAbstract(articleContent));
            oldArticle.put(Article.ARTICLE_CONTENT, articleContent);
            oldArticle.put(Article.ARTICLE_TITLE, symArticle.optString("title"));
            oldArticle.put(Article.ARTICLE_TAGS_REF, symArticle.optString("tags"));
            oldArticle.put(Common.POST_TO_COMMUNITY, false); // Do not send to rhythm
            final JSONObject updateRequest = new JSONObject().put(Article.ARTICLE, oldArticle);
            articleMgmtService.updateArticle(updateRequest);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);
            ret.put(Keys.CODE, 1).put(Keys.MSG, e.getMessage());
        }
    }

    /**
     * Adds a comment with the specified request.
     * <p>
     * Request json:
     * <pre>
     * {
     *     "comment": {
     *         "articleId": "",
     *         "content": "",
     *         "contentHTML": "",
     *         "ua": "",
     *         "ip": "",
     *         "authorName": "",
     *         "authorURL": "",
     *         "authorAvatarURL": "",
     *         "isArticleAuthor": true,
     *         "time": 1457784330398
     *     },
     *     "client": {
     *         "userName": "88250",
     *         "userB3Key": ""
     *     }
     * }
     * </pre>
     * </p>
     * <p>
     * Renders the response with a json object, for example,
     * <pre>
     * {
     *     "sc": true
     * }
     * </pre>
     * </p>
     *
     * @param context the specified http request context
     */
    @RequestProcessing(value = "/apis/symphony/comment", method = HttpMethod.PUT)
    public void addComment(final RequestContext context) {
        final JSONObject ret = new JSONObject().put(Keys.CODE, 0);
        context.renderJSON(ret);

        final JSONObject requestJSONObject = context.requestJSON();

        LOGGER.log(Level.INFO, "Adds a comment from Sym [" + requestJSONObject.toString() + "]");

        final Transaction transaction = commentRepository.beginTransaction();
        try {
            final JSONObject symCmt = requestJSONObject.optJSONObject(Comment.COMMENT);
            final JSONObject symClient = requestJSONObject.optJSONObject("client");
            final String articleAuthorName = symClient.optString(User.USER_NAME);
            final JSONObject articleAuthor = userRepository.getByUserName(articleAuthorName);
            if (null == articleAuthor) {
                ret.put(Keys.CODE, 1);
                ret.put(Keys.MSG, "No found user [" + articleAuthorName + "]");

                return;
            }

            final String b3Key = symClient.optString(UserExt.USER_B3_KEY);
            final String key = articleAuthor.optString(UserExt.USER_B3_KEY);
            if (!StringUtils.equals(key, b3Key)) {
                ret.put(Keys.CODE, 1);
                ret.put(Keys.MSG, "Wrong key");

                return;
            }

            final String articleId = symCmt.getString("articleId");
            final JSONObject article = articleRepository.get(articleId);
            if (null == article) {
                ret.put(Keys.CODE, 1);
                ret.put(Keys.MSG, "Not found the specified article [id=" + articleId + "]");

                return;
            }

            final String commentName = symCmt.getString("authorName");
            String commentURL = symCmt.optString("authorURL");
            if (!Strings.isURL(commentURL)) {
                commentURL = "";
            }
            final String commentThumbnailURL = symCmt.getString("authorAvatarURL");
            String commentContent = symCmt.getString("content"); // Markdown

            // Step 1: Add comment
            final JSONObject comment = new JSONObject();
            final String commentId = Ids.genTimeMillisId();
            comment.put(Keys.OBJECT_ID, commentId);
            comment.put(Comment.COMMENT_NAME, commentName);
            comment.put(Comment.COMMENT_EMAIL, "");
            comment.put(Comment.COMMENT_URL, commentURL);
            comment.put(Comment.COMMENT_THUMBNAIL_URL, commentThumbnailURL);
            comment.put(Comment.COMMENT_CONTENT, commentContent);
            final Date date = new Date();
            comment.put(Comment.COMMENT_CREATED, date.getTime());
            comment.put(Comment.COMMENT_ORIGINAL_COMMENT_ID, "");
            comment.put(Comment.COMMENT_ORIGINAL_COMMENT_NAME, "");
            comment.put(Comment.COMMENT_ON_ID, articleId);
            comment.put(Comment.COMMENT_ON_TYPE, Article.ARTICLE);
            final String commentSharpURL = Comment.getCommentSharpURLForArticle(article, commentId);
            comment.put(Comment.COMMENT_SHARP_URL, commentSharpURL);
            commentRepository.add(comment);
            articleMgmtService.incArticleCommentCount(articleId);
            try {
                final JSONObject preference = preferenceQueryService.getPreference();
                commentMgmtService.sendNotificationMail(article, comment, null, preference);
            } catch (final Exception e) {
                LOGGER.log(Level.WARN, "Send mail failed", e);
            }
            transaction.commit();
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);
            ret.put(Keys.CODE, 1).put(Keys.MSG, e.getMessage());
        }
    }
}
