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
package org.b3log.solo.service;

import jodd.http.HttpRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.Latkes;
import org.b3log.latke.event.Event;
import org.b3log.latke.event.EventManager;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.Transaction;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.util.Ids;
import org.b3log.latke.util.Strings;
import org.b3log.solo.event.EventTypes;
import org.b3log.solo.mail.MailService;
import org.b3log.solo.mail.MailServiceFactory;
import org.b3log.solo.model.*;
import org.b3log.solo.repository.ArticleRepository;
import org.b3log.solo.repository.CommentRepository;
import org.b3log.solo.repository.PageRepository;
import org.b3log.solo.repository.UserRepository;
import org.b3log.solo.util.Emotions;
import org.b3log.solo.util.Markdowns;
import org.b3log.solo.util.Solos;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.Date;

/**
 * Comment management service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.3.3.5, Feb 21, 2019
 * @since 0.3.5
 */
@Service
public class CommentMgmtService {

    /**
     * Comment mail HTML body.
     */
    public static final String COMMENT_MAIL_HTML_BODY = "<p>{articleOrPage} [<a href=\"" + "{articleOrPageURL}\">" + "{title}</a>]"
            + " received a new comment:</p>" + "{commenter}: <span><a href=\"{commentSharpURL}\">" + "{commentContent}</a></span>";

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(CommentMgmtService.class);

    /**
     * Default user thumbnail.
     */
    private static final String DEFAULT_USER_THUMBNAIL = "default-user-thumbnail.png";

    /**
     * Minimum length of comment name.
     */
    private static final int MIN_COMMENT_NAME_LENGTH = 2;

    /**
     * Maximum length of comment name.
     */
    private static final int MAX_COMMENT_NAME_LENGTH = 20;

    /**
     * Minimum length of comment content.
     */
    private static final int MIN_COMMENT_CONTENT_LENGTH = 2;

    /**
     * Maximum length of comment content.
     */
    private static final int MAX_COMMENT_CONTENT_LENGTH = 500;

    /**
     * Event manager.
     */
    @Inject
    private static EventManager eventManager;

    /**
     * Article management service.
     */
    @Inject
    private ArticleMgmtService articleMgmtService;

    /**
     * Comment repository.
     */
    @Inject
    private CommentRepository commentRepository;

    /**
     * Article repository.
     */
    @Inject
    private ArticleRepository articleRepository;

    /**
     * User repository.
     */
    @Inject
    private UserRepository userRepository;

    /**
     * Statistic management service.
     */
    @Inject
    private StatisticMgmtService statisticMgmtService;

    /**
     * Page repository.
     */
    @Inject
    private PageRepository pageRepository;

    /**
     * Preference query service.
     */
    @Inject
    private PreferenceQueryService preferenceQueryService;

    /**
     * Language service.
     */
    @Inject
    private LangPropsService langPropsService;

    /**
     * Mail service.
     */
    private MailService mailService = MailServiceFactory.getMailService();

    /**
     * Sends a notification mail to administrator for notifying the specified article or page received the specified
     * comment and original comment.
     *
     * @param articleOrPage   the specified article or page
     * @param comment         the specified comment
     * @param originalComment original comment, if not exists, set it as {@code null}
     * @param preference      the specified preference
     * @throws Exception exception
     */
    public void sendNotificationMail(final JSONObject articleOrPage,
                                     final JSONObject comment,
                                     final JSONObject originalComment,
                                     final JSONObject preference) throws Exception {
        if (!Solos.isMailConfigured()) {
            return;
        }

        final String commentEmail = comment.getString(Comment.COMMENT_EMAIL);
        final String commentId = comment.getString(Keys.OBJECT_ID);
        final String commentContent = comment.getString(Comment.COMMENT_CONTENT);

        final String adminEmail = preference.getString(Option.ID_C_ADMIN_EMAIL);

        if (adminEmail.equalsIgnoreCase(commentEmail)) {
            LOGGER.log(Level.DEBUG, "Do not send comment notification mail to admin itself[{0}]", adminEmail);

            return;
        }

        if (Latkes.getServePath().contains("localhost") || Strings.isIPv4(Latkes.getServePath())) {
            LOGGER.log(Level.INFO, "Solo runs on local server, so should not send mail");

            return;
        }

        if (null != originalComment && comment.has(Comment.COMMENT_ORIGINAL_COMMENT_ID)) {
            final String originalEmail = originalComment.getString(Comment.COMMENT_EMAIL);
            if (originalEmail.equalsIgnoreCase(adminEmail)) {
                LOGGER.log(Level.DEBUG, "Do not send comment notification mail to admin while the specified comment[{0}] is an reply",
                        commentId);
                return;
            }
        }

        final String blogTitle = preference.getString(Option.ID_C_BLOG_TITLE);
        boolean isArticle = true;
        String title = articleOrPage.optString(Article.ARTICLE_TITLE);
        if (StringUtils.isBlank(title)) {
            title = articleOrPage.getString(Page.PAGE_TITLE);
            isArticle = false;
        }

        final String commentSharpURL = comment.getString(Comment.COMMENT_SHARP_URL);
        final MailService.Message message = new MailService.Message();
        message.setFrom(adminEmail);
        message.addRecipient(adminEmail);
        String mailSubject;
        String articleOrPageURL;
        String mailBody;

        if (isArticle) {
            mailSubject = blogTitle + ": New comment on article [" + title + "]";
            articleOrPageURL = Latkes.getServePath() + articleOrPage.getString(Article.ARTICLE_PERMALINK);
            mailBody = COMMENT_MAIL_HTML_BODY.replace("{articleOrPage}", "Article");
        } else {
            mailSubject = blogTitle + ": New comment on page [" + title + "]";
            articleOrPageURL = Latkes.getServePath() + articleOrPage.getString(Page.PAGE_PERMALINK);
            mailBody = COMMENT_MAIL_HTML_BODY.replace("{articleOrPage}", "Page");
        }

        message.setSubject(mailSubject);
        final String commentName = comment.getString(Comment.COMMENT_NAME);
        final String commentURL = comment.getString(Comment.COMMENT_URL);
        String commenter;

        if (!"http://".equals(commentURL)) {
            commenter = "<a target=\"_blank\" " + "href=\"" + commentURL + "\">" + commentName + "</a>";
        } else {
            commenter = commentName;
        }

        mailBody = mailBody.replace("{articleOrPageURL}", articleOrPageURL).replace("{title}", title).replace("{commentContent}", commentContent).replace("{commentSharpURL}", Latkes.getServePath() + commentSharpURL).replace(
                "{commenter}", commenter);
        message.setHtmlBody(mailBody);

        LOGGER.log(Level.DEBUG, "Sending a mail[mailSubject={0}, mailBody=[{1}] to admin[email={2}]",
                mailSubject, mailBody, adminEmail);

        mailService.send(message);
    }

    /**
     * Checks the specified comment adding request.
     * <p>
     * XSS process (name) in this method.
     * </p>
     *
     * @param requestJSONObject the specified comment adding request, for example,
     *                          {
     *                          "type": "", // "article"/"page"
     *                          "oId": "",
     *                          "commentName": "",
     *                          "commentEmail": "",
     *                          "commentURL": "",
     *                          "commentContent": "",
     *                          }
     * @return check result, for example, <pre>
     * {
     *     "sc": boolean,
     *     "msg": "" // Exists if "sc" equals to false
     * }
     * </pre>
     */
    public JSONObject checkAddCommentRequest(final JSONObject requestJSONObject) {
        final JSONObject ret = new JSONObject();

        try {
            ret.put(Keys.STATUS_CODE, false);
            final JSONObject preference = preferenceQueryService.getPreference();

            if (null == preference || !preference.optBoolean(Option.ID_C_COMMENTABLE)) {
                ret.put(Keys.MSG, langPropsService.get("notAllowCommentLabel"));

                return ret;
            }

            final String id = requestJSONObject.optString(Keys.OBJECT_ID);
            final String type = requestJSONObject.optString(Common.TYPE);

            if (Article.ARTICLE.equals(type)) {
                final JSONObject article = articleRepository.get(id);

                if (null == article || !article.optBoolean(Article.ARTICLE_COMMENTABLE)) {
                    ret.put(Keys.MSG, langPropsService.get("notAllowCommentLabel"));

                    return ret;
                }
            } else {
                final JSONObject page = pageRepository.get(id);

                if (null == page || !page.optBoolean(Page.PAGE_COMMENTABLE)) {
                    ret.put(Keys.MSG, langPropsService.get("notAllowCommentLabel"));

                    return ret;
                }
            }

            String commentName = requestJSONObject.getString(Comment.COMMENT_NAME);
            if (MAX_COMMENT_NAME_LENGTH < commentName.length() || MIN_COMMENT_NAME_LENGTH > commentName.length()) {
                LOGGER.log(Level.WARN, "Comment name is too long[{0}]", commentName);
                ret.put(Keys.MSG, langPropsService.get("nameTooLongLabel"));

                return ret;
            }

            final String commentURL = requestJSONObject.optString(Comment.COMMENT_URL);

            if (!Strings.isURL(commentURL) || StringUtils.contains(commentURL, "<")) {
                LOGGER.log(Level.WARN, "Comment URL is invalid[{0}]", commentURL);
                ret.put(Keys.MSG, langPropsService.get("urlInvalidLabel"));

                return ret;
            }

            String commentContent = requestJSONObject.optString(Comment.COMMENT_CONTENT);

            if (MAX_COMMENT_CONTENT_LENGTH < commentContent.length() || MIN_COMMENT_CONTENT_LENGTH > commentContent.length()) {
                LOGGER.log(Level.WARN, "Comment conent length is invalid[{0}]", commentContent.length());
                ret.put(Keys.MSG, langPropsService.get("commentContentCannotEmptyLabel"));

                return ret;
            }

            ret.put(Keys.STATUS_CODE, true);

            commentContent = Emotions.toAliases(commentContent);
            requestJSONObject.put(Comment.COMMENT_CONTENT, commentContent);

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.WARN, "Checks add comment request[" + requestJSONObject.toString() + "] failed", e);

            ret.put(Keys.STATUS_CODE, false);
            ret.put(Keys.MSG, langPropsService.get("addFailLabel"));

            return ret;
        }
    }

    /**
     * Adds page comment with the specified request json object.
     *
     * @param requestJSONObject the specified request json object, for example,
     *                          {
     *                          "oId": "", // page id
     *                          "commentName": "",
     *                          "commentEmail": "",
     *                          "commentURL": "", // optional
     *                          "commentContent": "",
     *                          "commentOriginalCommentId": "" // optional
     *                          }
     * @return add result, for example,      <pre>
     * {
     *     "oId": "", // generated comment id
     *     "commentDate": "", // format: yyyy-MM-dd HH:mm:ss
     *     "commentOriginalCommentName": "" // optional, corresponding to argument "commentOriginalCommentId"
     *     "commentThumbnailURL": "",
     *     "commentSharpURL": "",
     *     "commentContent": "",
     *     "commentName": "",
     *     "commentURL": "", // optional
     *     "isReply": boolean,
     *     "page": {},
     *     "commentOriginalCommentId": "" // optional
     *     "commentable": boolean,
     *     "permalink": "" // page.pagePermalink
     * }
     * </pre>
     * @throws ServiceException service exception
     */
    public JSONObject addPageComment(final JSONObject requestJSONObject) throws ServiceException {
        final JSONObject ret = new JSONObject();
        ret.put(Common.IS_REPLY, false);

        final Transaction transaction = commentRepository.beginTransaction();

        try {
            final String pageId = requestJSONObject.getString(Keys.OBJECT_ID);
            final JSONObject page = pageRepository.get(pageId);
            ret.put(Page.PAGE, page);
            final String commentName = requestJSONObject.getString(Comment.COMMENT_NAME);
            final String commentEmail = requestJSONObject.getString(Comment.COMMENT_EMAIL).trim().toLowerCase();
            final String commentURL = requestJSONObject.optString(Comment.COMMENT_URL);
            final String commentContent = requestJSONObject.getString(Comment.COMMENT_CONTENT);

            final String originalCommentId = requestJSONObject.optString(Comment.COMMENT_ORIGINAL_COMMENT_ID);
            ret.put(Comment.COMMENT_ORIGINAL_COMMENT_ID, originalCommentId);
            // Step 1: Add comment
            final JSONObject comment = new JSONObject();

            comment.put(Comment.COMMENT_ORIGINAL_COMMENT_ID, "");
            comment.put(Comment.COMMENT_ORIGINAL_COMMENT_NAME, "");

            JSONObject originalComment = null;

            comment.put(Comment.COMMENT_NAME, commentName);
            comment.put(Comment.COMMENT_EMAIL, commentEmail);
            comment.put(Comment.COMMENT_URL, commentURL);
            comment.put(Comment.COMMENT_CONTENT, commentContent);
            final JSONObject preference = preferenceQueryService.getPreference();
            final Date date = new Date();

            comment.put(Comment.COMMENT_CREATED, date.getTime());
            ret.put(Comment.COMMENT_T_DATE, DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
            ret.put("commentDate2", date);

            ret.put(Common.COMMENTABLE, preference.getBoolean(Option.ID_C_COMMENTABLE) && page.getBoolean(Page.PAGE_COMMENTABLE));
            ret.put(Common.PERMALINK, page.getString(Page.PAGE_PERMALINK));

            if (StringUtils.isNotBlank(originalCommentId)) {
                originalComment = commentRepository.get(originalCommentId);
                if (null != originalComment) {
                    comment.put(Comment.COMMENT_ORIGINAL_COMMENT_ID, originalCommentId);
                    final String originalCommentName = originalComment.getString(Comment.COMMENT_NAME);

                    comment.put(Comment.COMMENT_ORIGINAL_COMMENT_NAME, originalCommentName);
                    ret.put(Comment.COMMENT_ORIGINAL_COMMENT_NAME, originalCommentName);

                    ret.put(Common.IS_REPLY, true);
                } else {
                    LOGGER.log(Level.WARN, "Not found orginal comment[id={0}] of reply[name={1}, content={2}]", originalCommentId,
                            commentName, commentContent);
                }
            }
            setCommentThumbnailURL(comment);
            ret.put(Comment.COMMENT_THUMBNAIL_URL, comment.getString(Comment.COMMENT_THUMBNAIL_URL));
            comment.put(Comment.COMMENT_ON_ID, pageId);
            comment.put(Comment.COMMENT_ON_TYPE, Page.PAGE);
            final String commentId = Ids.genTimeMillisId();
            ret.put(Keys.OBJECT_ID, commentId);
            final String commentSharpURL = Comment.getCommentSharpURLForPage(page, commentId);
            ret.put(Comment.COMMENT_NAME, commentName);
            ret.put(Comment.COMMENT_CONTENT, commentContent);
            ret.put(Comment.COMMENT_URL, commentURL);

            ret.put(Comment.COMMENT_SHARP_URL, commentSharpURL);
            comment.put(Comment.COMMENT_SHARP_URL, commentSharpURL);
            comment.put(Keys.OBJECT_ID, commentId);
            commentRepository.add(comment);
            incPageCommentCount(pageId);
            try {
                sendNotificationMail(page, comment, originalComment, preference);
            } catch (final Exception e) {
                LOGGER.log(Level.WARN, "Send mail failed", e);
            }

            final JSONObject eventData = new JSONObject();
            eventData.put(Comment.COMMENT, comment);
            eventData.put(Page.PAGE, page);
            eventManager.fireEventAsynchronously(new Event<>(EventTypes.ADD_COMMENT_TO_PAGE, eventData));

            transaction.commit();
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new ServiceException(e);
        }

        return ret;
    }

    /**
     * Adds an article comment with the specified request json object.
     *
     * @param requestJSONObject the specified request json object, for example,
     *                          {
     *                          "oId": "", // article id
     *                          "commentName": "",
     *                          "commentEmail": "",
     *                          "commentURL": "", // optional
     *                          "commentContent": "",
     *                          "commentOriginalCommentId": "" // optional
     *                          }
     * @return add result, for example,      <pre>
     * {
     *     "oId": "", // generated comment id
     *     "commentDate": "", // format: yyyy-MM-dd HH:mm:ss
     *     "commentOriginalCommentName": "" // optional, corresponding to argument "commentOriginalCommentId"
     *     "commentThumbnailURL": "",
     *     "commentSharpURL": "",
     *     "commentContent": "",
     *     "commentName": "",
     *     "commentURL": "", // optional
     *     "isReply": boolean,
     *     "article": {},
     *     "commentOriginalCommentId": "", // optional
     *     "commentable": boolean,
     *     "permalink": "" // article.articlePermalink
     * }
     * </pre>
     * @throws ServiceException service exception
     */
    public JSONObject addArticleComment(final JSONObject requestJSONObject) throws ServiceException {
        final JSONObject ret = new JSONObject();
        ret.put(Common.IS_REPLY, false);

        final Transaction transaction = commentRepository.beginTransaction();

        try {
            final String articleId = requestJSONObject.getString(Keys.OBJECT_ID);
            final JSONObject article = articleRepository.get(articleId);
            ret.put(Article.ARTICLE, article);
            final String commentName = requestJSONObject.getString(Comment.COMMENT_NAME);
            final String commentEmail = requestJSONObject.getString(Comment.COMMENT_EMAIL).trim().toLowerCase();
            final String commentURL = requestJSONObject.optString(Comment.COMMENT_URL);
            final String commentContent = requestJSONObject.getString(Comment.COMMENT_CONTENT);

            final String originalCommentId = requestJSONObject.optString(Comment.COMMENT_ORIGINAL_COMMENT_ID);
            ret.put(Comment.COMMENT_ORIGINAL_COMMENT_ID, originalCommentId);
            // Step 1: Add comment
            final JSONObject comment = new JSONObject();

            comment.put(Comment.COMMENT_ORIGINAL_COMMENT_ID, "");
            comment.put(Comment.COMMENT_ORIGINAL_COMMENT_NAME, "");

            JSONObject originalComment = null;

            comment.put(Comment.COMMENT_NAME, commentName);
            comment.put(Comment.COMMENT_EMAIL, commentEmail);
            comment.put(Comment.COMMENT_URL, commentURL);
            comment.put(Comment.COMMENT_CONTENT, commentContent);
            comment.put(Comment.COMMENT_ORIGINAL_COMMENT_ID, requestJSONObject.optString(Comment.COMMENT_ORIGINAL_COMMENT_ID));
            comment.put(Comment.COMMENT_ORIGINAL_COMMENT_NAME, requestJSONObject.optString(Comment.COMMENT_ORIGINAL_COMMENT_NAME));
            final JSONObject preference = preferenceQueryService.getPreference();
            final Date date = new Date();

            comment.put(Comment.COMMENT_CREATED, date.getTime());
            ret.put(Comment.COMMENT_T_DATE, DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
            ret.put("commentDate2", date);

            ret.put(Common.COMMENTABLE, preference.getBoolean(Option.ID_C_COMMENTABLE) && article.getBoolean(Article.ARTICLE_COMMENTABLE));
            ret.put(Common.PERMALINK, article.getString(Article.ARTICLE_PERMALINK));

            ret.put(Comment.COMMENT_NAME, commentName);
            String cmtContent = Emotions.convert(commentContent);
            cmtContent = Markdowns.toHTML(cmtContent);
            cmtContent = Jsoup.clean(cmtContent, Whitelist.relaxed());
            ret.put(Comment.COMMENT_CONTENT, cmtContent);
            ret.put(Comment.COMMENT_URL, commentURL);

            if (StringUtils.isNotBlank(originalCommentId)) {
                originalComment = commentRepository.get(originalCommentId);
                if (null != originalComment) {
                    comment.put(Comment.COMMENT_ORIGINAL_COMMENT_ID, originalCommentId);
                    final String originalCommentName = originalComment.getString(Comment.COMMENT_NAME);

                    comment.put(Comment.COMMENT_ORIGINAL_COMMENT_NAME, originalCommentName);
                    ret.put(Comment.COMMENT_ORIGINAL_COMMENT_NAME, originalCommentName);

                    ret.put(Common.IS_REPLY, true);
                } else {
                    LOGGER.log(Level.WARN, "Not found orginal comment[id={0}] of reply[name={1}, content={2}]",
                            originalCommentId, commentName, commentContent);
                }
            }
            setCommentThumbnailURL(comment);
            ret.put(Comment.COMMENT_THUMBNAIL_URL, comment.getString(Comment.COMMENT_THUMBNAIL_URL));
            comment.put(Comment.COMMENT_ON_ID, articleId);
            comment.put(Comment.COMMENT_ON_TYPE, Article.ARTICLE);
            final String commentId = Ids.genTimeMillisId();

            comment.put(Keys.OBJECT_ID, commentId);
            ret.put(Keys.OBJECT_ID, commentId);
            final String commentSharpURL = Comment.getCommentSharpURLForArticle(article, commentId);
            comment.put(Comment.COMMENT_SHARP_URL, commentSharpURL);
            ret.put(Comment.COMMENT_SHARP_URL, commentSharpURL);

            commentRepository.add(comment);
            articleMgmtService.incArticleCommentCount(articleId);

            try {
                sendNotificationMail(article, comment, originalComment, preference);
            } catch (final Exception e) {
                LOGGER.log(Level.WARN, "Send mail failed", e);
            }

            final JSONObject eventData = new JSONObject();
            eventData.put(Comment.COMMENT, comment);
            eventData.put(Article.ARTICLE, article);
            eventManager.fireEventAsynchronously(new Event<>(EventTypes.ADD_COMMENT_TO_ARTICLE, eventData));

            transaction.commit();
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new ServiceException(e);
        }

        return ret;
    }

    /**
     * Removes a comment of a page with the specified comment id.
     *
     * @param commentId the given comment id
     * @throws ServiceException service exception
     */
    public void removePageComment(final String commentId) throws ServiceException {
        final Transaction transaction = commentRepository.beginTransaction();

        try {
            final JSONObject comment = commentRepository.get(commentId);
            final String pageId = comment.getString(Comment.COMMENT_ON_ID);
            commentRepository.remove(commentId);
            decPageCommentCount(pageId);

            transaction.commit();
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.ERROR, "Removes a comment of a page failed", e);
            throw new ServiceException(e);
        }
    }

    /**
     * Removes a comment of an article with the specified comment id.
     *
     * @param commentId the given comment id
     * @throws ServiceException service exception
     */
    public void removeArticleComment(final String commentId)
            throws ServiceException {
        final Transaction transaction = commentRepository.beginTransaction();

        try {
            final JSONObject comment = commentRepository.get(commentId);
            final String articleId = comment.getString(Comment.COMMENT_ON_ID);
            commentRepository.remove(commentId);
            decArticleCommentCount(articleId);

            transaction.commit();
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.ERROR, "Removes a comment of an article failed", e);
            throw new ServiceException(e);
        }
    }

    /**
     * Page comment count +1 for an page specified by the given page id.
     *
     * @param pageId the given page id
     * @throws JSONException       json exception
     * @throws RepositoryException repository exception
     */
    public void incPageCommentCount(final String pageId)
            throws JSONException, RepositoryException {
        final JSONObject page = pageRepository.get(pageId);
        final JSONObject newPage = new JSONObject(page, JSONObject.getNames(page));
        final int commentCnt = page.getInt(Page.PAGE_COMMENT_COUNT);

        newPage.put(Page.PAGE_COMMENT_COUNT, commentCnt + 1);
        pageRepository.update(pageId, newPage);
    }

    /**
     * Article comment count -1 for an article specified by the given article id.
     *
     * @param articleId the given article id
     * @throws JSONException       json exception
     * @throws RepositoryException repository exception
     */
    private void decArticleCommentCount(final String articleId)
            throws JSONException, RepositoryException {
        final JSONObject article = articleRepository.get(articleId);
        final JSONObject newArticle = new JSONObject(article, JSONObject.getNames(article));
        final int commentCnt = article.getInt(Article.ARTICLE_COMMENT_COUNT);

        newArticle.put(Article.ARTICLE_COMMENT_COUNT, commentCnt - 1);

        articleRepository.update(articleId, newArticle);
    }

    /**
     * Page comment count -1 for an page specified by the given page id.
     *
     * @param pageId the given page id
     * @throws JSONException       json exception
     * @throws RepositoryException repository exception
     */
    private void decPageCommentCount(final String pageId)
            throws JSONException, RepositoryException {
        final JSONObject page = pageRepository.get(pageId);
        final JSONObject newPage = new JSONObject(page, JSONObject.getNames(page));
        final int commentCnt = page.getInt(Page.PAGE_COMMENT_COUNT);

        newPage.put(Page.PAGE_COMMENT_COUNT, commentCnt - 1);
        pageRepository.update(pageId, newPage);
    }

    /**
     * Sets commenter thumbnail URL for the specified comment.
     * <p>
     * Try to set thumbnail URL using:
     * <ol>
     * <li>User avatar</li>
     * <li>Gravatar service</li>
     * <ol>
     * </p>
     *
     * @param comment the specified comment
     * @throws Exception exception
     */
    public void setCommentThumbnailURL(final JSONObject comment) throws Exception {
        final String commentEmail = comment.getString(Comment.COMMENT_EMAIL);

        // 1. user avatar
        final JSONObject user = userRepository.getByEmail(commentEmail);
        if (null != user) {
            final String avatar = user.optString(UserExt.USER_AVATAR);
            if (StringUtils.isNotBlank(avatar)) {
                comment.put(Comment.COMMENT_THUMBNAIL_URL, avatar);

                return;
            }
        }

        // 2. Gravatar
        String thumbnailURL = Solos.getGravatarURL(commentEmail.toLowerCase(), "128");
        final URL gravatarURL = new URL(thumbnailURL);

        int statusCode = HttpServletResponse.SC_OK;
        try {
            statusCode = HttpRequest.get(thumbnailURL).header("User-Agent", Solos.USER_AGENT).send().statusCode();
        } catch (final Exception e) {
            LOGGER.log(Level.DEBUG, "Can not fetch thumbnail from Gravatar [commentEmail={0}]", commentEmail);
        } finally {
            if (HttpServletResponse.SC_OK != statusCode) {
                thumbnailURL = Latkes.getStaticServePath() + "/images/" + DEFAULT_USER_THUMBNAIL;
            }
        }

        comment.put(Comment.COMMENT_THUMBNAIL_URL, thumbnailURL);
    }
}
