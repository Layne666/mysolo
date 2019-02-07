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

import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.Latkes;
import org.b3log.latke.event.AbstractEventListener;
import org.b3log.latke.event.Event;
import org.b3log.latke.ioc.BeanManager;
import org.b3log.latke.ioc.Singleton;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.util.Strings;
import org.b3log.solo.mail.MailService;
import org.b3log.solo.mail.MailServiceFactory;
import org.b3log.solo.model.Comment;
import org.b3log.solo.model.Option;
import org.b3log.solo.model.Page;
import org.b3log.solo.repository.CommentRepository;
import org.b3log.solo.service.PreferenceQueryService;
import org.b3log.solo.util.Solos;
import org.json.JSONObject;

/**
 * This listener is responsible for processing page comment reply.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.2.6, Sep 25, 2018
 * @since 0.3.1
 */
@Singleton
public class PageCommentReplyNotifier extends AbstractEventListener<JSONObject> {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(PageCommentReplyNotifier.class);

    /**
     * Mail service.
     */
    private MailService mailService = MailServiceFactory.getMailService();

    @Override
    public void action(final Event<JSONObject> event) {
        final JSONObject eventData = event.getData();
        final JSONObject comment = eventData.optJSONObject(Comment.COMMENT);
        final JSONObject page = eventData.optJSONObject(Page.PAGE);

        LOGGER.log(Level.DEBUG, "Processing an event [type={0}, data={1}] in listener [className={2}]",
                event.getType(), eventData, PageCommentReplyNotifier.class.getName());
        final String originalCommentId = comment.optString(Comment.COMMENT_ORIGINAL_COMMENT_ID);

        if (StringUtils.isBlank(originalCommentId)) {
            LOGGER.log(Level.DEBUG, "This comment[id={0}] is not a reply", comment.optString(Keys.OBJECT_ID));
            return;
        }

        if (!Solos.isMailConfigured()) {
            return;
        }

        final BeanManager beanManager = BeanManager.getInstance();
        final PreferenceQueryService preferenceQueryService = beanManager.getReference(PreferenceQueryService.class);
        final CommentRepository commentRepository = beanManager.getReference(CommentRepository.class);

        try {
            final String commentEmail = comment.getString(Comment.COMMENT_EMAIL);
            final JSONObject originalComment = commentRepository.get(originalCommentId);

            final String originalCommentEmail = originalComment.getString(Comment.COMMENT_EMAIL);
            if (originalCommentEmail.equalsIgnoreCase(commentEmail)) {
                return;
            }

            if (!Strings.isEmail(originalCommentEmail)) {
                return;
            }

            final JSONObject preference = preferenceQueryService.getPreference();
            if (null == preference) {
                LOGGER.log(Level.ERROR, "Not found preference");

                return;
            }

            final String blogTitle = preference.getString(Option.ID_C_BLOG_TITLE);
            final String adminEmail = preference.getString(Option.ID_C_ADMIN_EMAIL);

            final String commentContent = comment.getString(Comment.COMMENT_CONTENT);
            final String commentSharpURL = comment.getString(Comment.COMMENT_SHARP_URL);
            final MailService.Message message = new MailService.Message();

            message.setFrom(adminEmail);
            message.addRecipient(originalCommentEmail);
            final JSONObject replyNotificationTemplate = preferenceQueryService.getReplyNotificationTemplate();
            final String mailSubject = replyNotificationTemplate.getString("subject").replace("${blogTitle}", blogTitle);

            message.setSubject(mailSubject);
            final String pageTitle = page.getString(Page.PAGE_TITLE);
            final String pageLink = Latkes.getServePath() + page.getString(Page.PAGE_PERMALINK);
            final String commentName = comment.getString(Comment.COMMENT_NAME);
            final String commentURL = comment.getString(Comment.COMMENT_URL);
            String commenter;

            if (!"http://".equals(commentURL)) {
                commenter = "<a target=\"_blank\" " + "href=\"" + commentURL + "\">" + commentName + "</a>";
            } else {
                commenter = commentName;
            }

            final String mailBody = replyNotificationTemplate.getString("body").replace("${postLink}", pageLink).replace("${postTitle}", pageTitle).replace("${replier}", commenter).replace("${replyURL}", Latkes.getServePath() + commentSharpURL).replace(
                    "${replyContent}", commentContent);

            message.setHtmlBody(mailBody);
            LOGGER.log(Level.DEBUG, "Sending a mail[mailSubject={0}, mailBody=[{1}] to [{2}]",
                    mailSubject, mailBody, originalCommentEmail);

            mailService.send(message);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);
        }
    }

    /**
     * Gets the event type {@linkplain EventTypes#ADD_COMMENT_TO_PAGE}.
     *
     * @return event type
     */
    @Override
    public String getEventType() {
        return EventTypes.ADD_COMMENT_TO_PAGE;
    }
}
