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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.b3log.latke.Keys;
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
import org.b3log.solo.event.B3ArticleSender;
import org.b3log.solo.event.EventTypes;
import org.b3log.solo.model.*;
import org.b3log.solo.repository.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.b3log.solo.model.Article.*;

/**
 * Article management service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.3.0.0, Feb 10, 2019
 * @since 0.3.5
 */
@Service
public class ArticleMgmtService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ArticleMgmtService.class);

    /**
     * Article query service.
     */
    @Inject
    private ArticleQueryService articleQueryService;

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
     * Tag repository.
     */
    @Inject
    private TagRepository tagRepository;

    /**
     * Archive date repository.
     */
    @Inject
    private ArchiveDateRepository archiveDateRepository;

    /**
     * Archive date-Article repository.
     */
    @Inject
    private ArchiveDateArticleRepository archiveDateArticleRepository;

    /**
     * Tag-Article repository.
     */
    @Inject
    private TagArticleRepository tagArticleRepository;

    /**
     * Comment repository.
     */
    @Inject
    private CommentRepository commentRepository;

    /**
     * Preference query service.
     */
    @Inject
    private PreferenceQueryService preferenceQueryService;

    /**
     * Permalink query service.
     */
    @Inject
    private PermalinkQueryService permalinkQueryService;

    /**
     * Event manager.
     */
    @Inject
    private EventManager eventManager;

    /**
     * Language service.
     */
    @Inject
    private LangPropsService langPropsService;

    /**
     * Statistic management service.
     */
    @Inject
    private StatisticMgmtService statisticMgmtService;

    /**
     * Statistic query service.
     */
    @Inject
    private StatisticQueryService statisticQueryService;

    /**
     * Tag management service.
     */
    @Inject
    private TagMgmtService tagMgmtService;

    /**
     * Pushes an article specified by the given article id to community.
     *
     * @param articleId the given article id
     */
    public void pushArticleToCommunity(final String articleId) {
        try {
            final JSONObject article = articleRepository.get(articleId);
            if (null == article) {
                return;
            }

            article.put(Common.POST_TO_COMMUNITY, true);

            final JSONObject data = new JSONObject().put(ARTICLE, article);
            B3ArticleSender.pushArticleToRhy(data);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Pushes an article [id=" + articleId + "] to community failed", e);
        }
    }

    /**
     * Article comment count +1 for an article specified by the given article id.
     *
     * @param articleId the given article id
     * @throws JSONException       json exception
     * @throws RepositoryException repository exception
     */
    public void incArticleCommentCount(final String articleId) throws JSONException, RepositoryException {
        final JSONObject article = articleRepository.get(articleId);
        final JSONObject newArticle = new JSONObject(article, JSONObject.getNames(article));
        final int commentCnt = article.getInt(Article.ARTICLE_COMMENT_COUNT);
        newArticle.put(Article.ARTICLE_COMMENT_COUNT, commentCnt + 1);
        articleRepository.update(articleId, newArticle);
    }

    /**
     * Cancels publish an article by the specified article id.
     *
     * @param articleId the specified article id
     * @throws ServiceException service exception
     */
    public void cancelPublishArticle(final String articleId) throws ServiceException {
        final Transaction transaction = articleRepository.beginTransaction();

        try {
            final JSONObject article = articleRepository.get(articleId);
            article.put(ARTICLE_IS_PUBLISHED, false);
            articleRepository.update(articleId, article);

            transaction.commit();
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.ERROR, "Cancels publish article failed", e);

            throw new ServiceException(e);
        }
    }

    /**
     * Puts an article specified by the given article id to top or cancel top.
     *
     * @param articleId the given article id
     * @param top       the specified flag, {@code true} to top, {@code false} to
     *                  cancel top
     * @throws ServiceException service exception
     */
    public void topArticle(final String articleId, final boolean top) throws ServiceException {
        final Transaction transaction = articleRepository.beginTransaction();

        try {
            final JSONObject topArticle = articleRepository.get(articleId);
            topArticle.put(ARTICLE_PUT_TOP, top);
            articleRepository.update(articleId, topArticle);

            transaction.commit();
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.ERROR, "Can't put the article[oId{0}] to top", articleId);
            throw new ServiceException(e);
        }
    }

    /**
     * Updates an article by the specified request json object.
     *
     * @param requestJSONObject the specified request json object, for example,
     *                          {
     *                          "article": {
     *                          "oId": "",
     *                          "articleTitle": "",
     *                          "articleAbstract": "",
     *                          "articleContent": "",
     *                          "articleTags": "tag1,tag2,tag3", // optional, default set "待分类"
     *                          "articlePermalink": "", // optional
     *                          "articleIsPublished": boolean,
     *                          "articleSignId": "", // optional
     *                          "articleCommentable": boolean,
     *                          "articleViewPwd": ""
     *                          }
     *                          }
     * @throws ServiceException service exception
     */
    public void updateArticle(final JSONObject requestJSONObject) throws ServiceException {
        final Transaction transaction = articleRepository.beginTransaction();

        try {
            final JSONObject article = requestJSONObject.getJSONObject(ARTICLE);
            String tagsString = article.optString(Article.ARTICLE_TAGS_REF);
            tagsString = Tag.formatTags(tagsString);
            if (StringUtils.isBlank(tagsString)) {
                tagsString = "待分类";
            }
            article.put(Article.ARTICLE_TAGS_REF, tagsString);

            final String articleId = article.getString(Keys.OBJECT_ID);
            // Set permalink
            final JSONObject oldArticle = articleRepository.get(articleId);
            final String permalink = getPermalinkForUpdateArticle(oldArticle, article, oldArticle.optLong(ARTICLE_CREATED));
            article.put(ARTICLE_PERMALINK, permalink);

            processTagsForArticleUpdate(oldArticle, article);

            if (!oldArticle.getString(Article.ARTICLE_PERMALINK).equals(permalink)) { // The permalink has been updated
                // Updates related comments' links
                processCommentsForArticleUpdate(article);
            }

            // Fill auto properties
            fillAutoProperties(oldArticle, article);
            // Set date
            article.put(ARTICLE_UPDATED, oldArticle.getLong(ARTICLE_UPDATED));
            final long now = System.currentTimeMillis();

            // The article to update has no sign
            if (!article.has(Article.ARTICLE_SIGN_ID)) {
                article.put(Article.ARTICLE_SIGN_ID, "0");
            }

            if (article.getBoolean(ARTICLE_IS_PUBLISHED)) { // Publish it
                if (articleQueryService.hadBeenPublished(oldArticle)) {
                    // Edit update date only for published article
                    article.put(ARTICLE_UPDATED, now);
                } else { // This article is a draft and this is the first time to publish it
                    article.put(ARTICLE_CREATED, now);
                    article.put(ARTICLE_UPDATED, now);
                    article.put(ARTICLE_HAD_BEEN_PUBLISHED, true);
                }
            } else { // Save as draft
                if (articleQueryService.hadBeenPublished(oldArticle)) {
                    // Save update date only for published article
                    article.put(ARTICLE_UPDATED, now);
                } else {
                    // Reset create/update date to indicate this is an new draft
                    article.put(ARTICLE_CREATED, now);
                    article.put(ARTICLE_UPDATED, now);
                }
            }

            final boolean publishNewArticle = !oldArticle.getBoolean(ARTICLE_IS_PUBLISHED) && article.getBoolean(ARTICLE_IS_PUBLISHED);

            // Update
            final boolean postToCommunity = article.optBoolean(Common.POST_TO_COMMUNITY, true);
            article.remove(Common.POST_TO_COMMUNITY); // Do not persist this property
            articleRepository.update(articleId, article);
            article.put(Common.POST_TO_COMMUNITY, postToCommunity); // Restores the property

            if (publishNewArticle) {
                // Fire add article event
                final JSONObject eventData = new JSONObject();
                eventData.put(ARTICLE, article);
                eventManager.fireEventAsynchronously(new Event<>(EventTypes.ADD_ARTICLE, eventData));
            } else {
                // Fire update article event
                final JSONObject eventData = new JSONObject();
                eventData.put(ARTICLE, article);
                eventManager.fireEventAsynchronously(new Event<>(EventTypes.UPDATE_ARTICLE, eventData));
            }

            transaction.commit();
        } catch (final ServiceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.ERROR, "Updates an article failed", e);

            throw e;
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.ERROR, "Updates an article failed", e);

            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Adds an article from the specified request json object.
     *
     * @param requestJSONObject the specified request json object, for example,
     *                          {
     *                          "article": {
     *                          "articleAuthorId": "",
     *                          "articleTitle": "",
     *                          "articleAbstract": "",
     *                          "articleContent": "",
     *                          "articleTags": "tag1,tag2,tag3",
     *                          "articleIsPublished": boolean,
     *                          "articlePermalink": "", // optional
     *                          "postToCommunity": boolean, // optional, default is true
     *                          "articleSignId": "" // optional, default is "0",
     *                          "articleCommentable": boolean,
     *                          "articleViewPwd": "",
     *                          "oId": "" // optional, generate it if not exists this key
     *                          }
     *                          }
     * @return generated article id
     * @throws ServiceException service exception
     */
    public String addArticle(final JSONObject requestJSONObject) throws ServiceException {
        final Transaction transaction = articleRepository.beginTransaction();

        try {
            final JSONObject article = requestJSONObject.getJSONObject(Article.ARTICLE);
            final String ret = addArticleInternal(article);
            transaction.commit();

            return ret;
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Adds the specified article for internal invocation purposes.
     *
     * @param article the specified article
     * @return generated article id
     * @throws ServiceException service exception
     */
    public String addArticleInternal(final JSONObject article) throws ServiceException {
        String ret = article.optString(Keys.OBJECT_ID);
        if (StringUtils.isBlank(ret)) {
            ret = Ids.genTimeMillisId();
            article.put(Keys.OBJECT_ID, ret);
        }

        try {
            String tagsString = article.optString(Article.ARTICLE_TAGS_REF);
            tagsString = Tag.formatTags(tagsString);
            if (StringUtils.isBlank(tagsString)) {
                tagsString = "待分类";
            }
            article.put(Article.ARTICLE_TAGS_REF, tagsString);
            final String[] tagTitles = tagsString.split(",");
            final JSONArray tags = tag(tagTitles, article);

            article.put(Article.ARTICLE_COMMENT_COUNT, 0);
            article.put(Article.ARTICLE_VIEW_COUNT, 0);
            if (!article.has(Article.ARTICLE_CREATED)) {
                article.put(Article.ARTICLE_CREATED, System.currentTimeMillis());
            }
            article.put(Article.ARTICLE_UPDATED, article.optLong(Article.ARTICLE_CREATED));
            article.put(Article.ARTICLE_PUT_TOP, false);

            addTagArticleRelation(tags, article);

            archiveDate(article);

            final String permalink = getPermalinkForAddArticle(article);
            article.put(Article.ARTICLE_PERMALINK, permalink);

            final String signId = article.optString(Article.ARTICLE_SIGN_ID, "1");
            article.put(Article.ARTICLE_SIGN_ID, signId);

            article.put(Article.ARTICLE_HAD_BEEN_PUBLISHED, false);
            if (article.optBoolean(Article.ARTICLE_IS_PUBLISHED)) {
                // Publish it directly
                article.put(Article.ARTICLE_HAD_BEEN_PUBLISHED, true);
            }

            article.put(Article.ARTICLE_RANDOM_DOUBLE, Math.random());

            final boolean postToCommunity = article.optBoolean(Common.POST_TO_COMMUNITY, true);
            article.remove(Common.POST_TO_COMMUNITY); // Do not persist this property

            articleRepository.add(article);

            article.put(Common.POST_TO_COMMUNITY, postToCommunity); // Restores the property

            if (article.optBoolean(Article.ARTICLE_IS_PUBLISHED)) {
                final JSONObject eventData = new JSONObject();
                eventData.put(Article.ARTICLE, article);
                eventManager.fireEventAsynchronously(new Event<>(EventTypes.ADD_ARTICLE, eventData));
            }

            article.remove(Common.POST_TO_COMMUNITY);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Adds an article failed", e);

            throw new ServiceException(e);
        }

        return ret;
    }

    /**
     * Removes the article specified by the given id.
     *
     * @param articleId the given id
     * @throws ServiceException service exception
     */
    public void removeArticle(final String articleId) throws ServiceException {
        final Transaction transaction = articleRepository.beginTransaction();
        try {
            unArchiveDate(articleId);
            removeTagArticleRelations(articleId);
            articleRepository.remove(articleId);
            commentRepository.removeComments(articleId);
            transaction.commit();
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.ERROR, "Removes an article[id=" + articleId + "] failed", e);
            throw new ServiceException(e);
        }
    }

    /**
     * Updates the random values of articles fetched with the specified update
     * count.
     *
     * @param updateCnt the specified update count
     * @throws ServiceException service exception
     */
    public void updateArticlesRandomValue(final int updateCnt) throws ServiceException {
        final Transaction transaction = articleRepository.beginTransaction();

        try {
            final List<JSONObject> randomArticles = articleRepository.getRandomly(updateCnt);

            for (final JSONObject article : randomArticles) {
                article.put(Article.ARTICLE_RANDOM_DOUBLE, Math.random());

                articleRepository.update(article.getString(Keys.OBJECT_ID), article);
            }

            transaction.commit();
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.WARN, "Updates article random value failed");

            throw new ServiceException(e);
        }
    }

    /**
     * Increments the view count of the article specified by the given article id.
     *
     * @param articleId the given article id
     * @throws ServiceException service exception
     */
    public void incViewCount(final String articleId) throws ServiceException {
        JSONObject article;

        try {
            article = articleRepository.get(articleId);

            if (null == article) {
                return;
            }
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Gets article [id=" + articleId + "] failed", e);

            return;
        }

        final Transaction transaction = articleRepository.beginTransaction();

        try {
            article.put(Article.ARTICLE_VIEW_COUNT, article.getInt(Article.ARTICLE_VIEW_COUNT) + 1);
            articleRepository.update(articleId, article);

            transaction.commit();
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.WARN, "Updates article view count failed");

            throw new ServiceException(e);
        }
    }

    /**
     * Un-archive an article specified by the given specified article id.
     *
     * @param articleId the given article id
     * @throws ServiceException service exception
     */
    private void unArchiveDate(final String articleId) throws ServiceException {
        try {
            final JSONObject archiveDateArticleRelation = archiveDateArticleRepository.getByArticleId(articleId);
            final String archiveDateId = archiveDateArticleRelation.getString(ArchiveDate.ARCHIVE_DATE + "_" + Keys.OBJECT_ID);
            final int articleCount = archiveDateArticleRepository.getArticleCount(archiveDateId);
            if (1 > articleCount) {
                archiveDateRepository.remove(archiveDateId);
            }

            archiveDateArticleRepository.remove(archiveDateArticleRelation.getString(Keys.OBJECT_ID));
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Unarchive date for article[id=" + articleId + "] failed", e);

            throw new ServiceException(e);
        }
    }

    /**
     * Processes comments for article update.
     *
     * @param article the specified article to update
     * @throws Exception exception
     */
    private void processCommentsForArticleUpdate(final JSONObject article) throws Exception {
        final String articleId = article.getString(Keys.OBJECT_ID);

        final List<JSONObject> comments = commentRepository.getComments(articleId, 1, Integer.MAX_VALUE);
        for (final JSONObject comment : comments) {
            final String commentId = comment.getString(Keys.OBJECT_ID);
            final String sharpURL = Comment.getCommentSharpURLForArticle(article, commentId);
            comment.put(Comment.COMMENT_SHARP_URL, sharpURL);
            if (StringUtils.isBlank(comment.optString(Comment.COMMENT_ORIGINAL_COMMENT_ID))) {
                comment.put(Comment.COMMENT_ORIGINAL_COMMENT_ID, "");
            }
            if (StringUtils.isBlank(comment.optString(Comment.COMMENT_ORIGINAL_COMMENT_NAME))) {
                comment.put(Comment.COMMENT_ORIGINAL_COMMENT_NAME, "");
            }

            commentRepository.update(commentId, comment);
        }
    }

    /**
     * Processes tags for article update.
     * <p>
     * <ul>
     * <li>Un-tags old article, decrements tag reference count</li>
     * <li>Removes old article-tag relations</li>
     * <li>Saves new article-tag relations with tag reference count</li>
     * </ul>
     *
     * @param oldArticle the specified old article
     * @param newArticle the specified new article
     * @throws Exception exception
     */
    private void processTagsForArticleUpdate(final JSONObject oldArticle, final JSONObject newArticle) throws Exception {
        final String oldArticleId = oldArticle.getString(Keys.OBJECT_ID);
        final List<JSONObject> oldTags = tagRepository.getByArticleId(oldArticleId);
        final String tagsString = newArticle.getString(Article.ARTICLE_TAGS_REF);
        String[] tagStrings = tagsString.split(",");
        final List<JSONObject> newTags = new ArrayList<>();

        for (int i = 0; i < tagStrings.length; i++) {
            final String tagTitle = tagStrings[i].trim();
            JSONObject newTag = tagRepository.getByTitle(tagTitle);

            if (null == newTag) {
                newTag = new JSONObject();
                newTag.put(Tag.TAG_TITLE, tagTitle);
            }
            newTags.add(newTag);
        }

        final List<JSONObject> tagsDropped = new ArrayList<>();
        final List<JSONObject> tagsNeedToAdd = new ArrayList<>();
        final List<JSONObject> tagsUnchanged = new ArrayList<>();

        for (final JSONObject newTag : newTags) {
            final String newTagTitle = newTag.getString(Tag.TAG_TITLE);

            if (!tagExists(newTagTitle, oldTags)) {
                LOGGER.log(Level.DEBUG, "Tag need to add[title={0}]", newTagTitle);
                tagsNeedToAdd.add(newTag);
            } else {
                tagsUnchanged.add(newTag);
            }
        }
        for (final JSONObject oldTag : oldTags) {
            final String oldTagTitle = oldTag.getString(Tag.TAG_TITLE);

            if (!tagExists(oldTagTitle, newTags)) {
                LOGGER.log(Level.DEBUG, "Tag dropped[title={0}]", oldTag);
                tagsDropped.add(oldTag);
            } else {
                tagsUnchanged.remove(oldTag);
            }
        }

        LOGGER.log(Level.DEBUG, "Tags unchanged [{0}]", tagsUnchanged);

        final String[] tagIdsDropped = new String[tagsDropped.size()];
        for (int i = 0; i < tagIdsDropped.length; i++) {
            final JSONObject tag = tagsDropped.get(i);
            final String id = tag.getString(Keys.OBJECT_ID);
            tagIdsDropped[i] = id;
        }

        removeTagArticleRelations(oldArticleId, 0 == tagIdsDropped.length ? new String[]{"l0y0l"} : tagIdsDropped);

        tagStrings = new String[tagsNeedToAdd.size()];
        for (int i = 0; i < tagStrings.length; i++) {
            final JSONObject tag = tagsNeedToAdd.get(i);
            final String tagTitle = tag.getString(Tag.TAG_TITLE);
            tagStrings[i] = tagTitle;
        }
        final JSONArray tags = tag(tagStrings, newArticle);

        addTagArticleRelation(tags, newArticle);
    }

    /**
     * Removes tag-article relations by the specified article id and tag ids of the relations to be removed.
     * <p>
     * Removes all relations if not specified the tag ids.
     * </p>
     *
     * @param articleId the specified article id
     * @param tagIds    the specified tag ids of the relations to be removed
     * @throws JSONException       json exception
     * @throws RepositoryException repository exception
     */
    private void removeTagArticleRelations(final String articleId, final String... tagIds)
            throws JSONException, RepositoryException {
        final List<String> tagIdList = Arrays.asList(tagIds);
        final List<JSONObject> tagArticleRelations = tagArticleRepository.getByArticleId(articleId);

        for (int i = 0; i < tagArticleRelations.size(); i++) {
            final JSONObject tagArticleRelation = tagArticleRelations.get(i);
            String relationId;

            if (tagIdList.isEmpty()) { // Removes all if un-specified
                relationId = tagArticleRelation.getString(Keys.OBJECT_ID);
                tagArticleRepository.remove(relationId);

            } else {
                if (tagIdList.contains(tagArticleRelation.getString(Tag.TAG + "_" + Keys.OBJECT_ID))) {
                    relationId = tagArticleRelation.getString(Keys.OBJECT_ID);
                    tagArticleRepository.remove(relationId);
                }
            }

            final String tagId = tagArticleRelation.optString(Tag.TAG + "_" + Keys.OBJECT_ID);
            final int articleCount = tagArticleRepository.getArticleCount(tagId);
            if (1 > articleCount) {
                tagRepository.remove(tagId);
            }
        }
    }

    /**
     * Adds relation of the specified tags and article.
     *
     * @param tags    the specified tags
     * @param article the specified article
     * @throws RepositoryException repository exception
     */
    private void addTagArticleRelation(final JSONArray tags, final JSONObject article) throws RepositoryException {
        for (int i = 0; i < tags.length(); i++) {
            final JSONObject tag = tags.optJSONObject(i);
            final JSONObject tagArticleRelation = new JSONObject();

            tagArticleRelation.put(Tag.TAG + "_" + Keys.OBJECT_ID, tag.optString(Keys.OBJECT_ID));
            tagArticleRelation.put(Article.ARTICLE + "_" + Keys.OBJECT_ID, article.optString(Keys.OBJECT_ID));

            tagArticleRepository.add(tagArticleRelation);
        }
    }

    /**
     * Tags the specified article with the specified tag titles.
     *
     * @param tagTitles the specified tag titles
     * @param article   the specified article
     * @return an array of tags
     * @throws RepositoryException repository exception
     */
    private JSONArray tag(final String[] tagTitles, final JSONObject article) throws RepositoryException {
        final JSONArray ret = new JSONArray();

        for (int i = 0; i < tagTitles.length; i++) {
            final String tagTitle = tagTitles[i].trim();
            JSONObject tag = tagRepository.getByTitle(tagTitle);
            String tagId;

            if (null == tag) {
                LOGGER.log(Level.TRACE, "Found a new tag[title={0}] in article[title={1}]",
                        tagTitle, article.optString(Article.ARTICLE_TITLE));
                tag = new JSONObject();
                tag.put(Tag.TAG_TITLE, tagTitle);
                tagId = tagRepository.add(tag);
                tag.put(Keys.OBJECT_ID, tagId);
            } else {
                tagId = tag.optString(Keys.OBJECT_ID);
                LOGGER.log(Level.TRACE, "Found a existing tag[title={0}, id={1}] in article[title={2}]",
                        tag.optString(Tag.TAG_TITLE), tag.optString(Keys.OBJECT_ID), article.optString(Article.ARTICLE_TITLE));
                final JSONObject tagTmp = new JSONObject();
                tagTmp.put(Keys.OBJECT_ID, tagId);
                tagTmp.put(Tag.TAG_TITLE, tagTitle);
                tagRepository.update(tagId, tagTmp);
            }

            ret.put(tag);
        }

        return ret;
    }

    /**
     * Archive the create date with the specified article.
     *
     * @param article the specified article, for example,
     *                {
     *                ....,
     *                "oId": "",
     *                "articleCreateDate": java.util.Date,
     *                ....
     *                }
     * @throws RepositoryException repository exception
     */
    private void archiveDate(final JSONObject article) throws RepositoryException {
        final long created = article.optLong(Article.ARTICLE_CREATED);
        final String createDateString = DateFormatUtils.format(created, "yyyy/MM");
        JSONObject archiveDate = archiveDateRepository.getByArchiveDate(createDateString);
        if (null == archiveDate) {
            archiveDate = new JSONObject();
            try {
                archiveDate.put(ArchiveDate.ARCHIVE_TIME, DateUtils.parseDate(createDateString, new String[]{"yyyy/MM"}).getTime());
                archiveDateRepository.add(archiveDate);
            } catch (final ParseException e) {
                LOGGER.log(Level.ERROR, e.getMessage(), e);
                throw new RepositoryException(e);
            }
        }

        final JSONObject archiveDateArticleRelation = new JSONObject();
        archiveDateArticleRelation.put(ArchiveDate.ARCHIVE_DATE + "_" + Keys.OBJECT_ID, archiveDate.optString(Keys.OBJECT_ID));
        archiveDateArticleRelation.put(Article.ARTICLE + "_" + Keys.OBJECT_ID, article.optString(Keys.OBJECT_ID));
        archiveDateArticleRepository.add(archiveDateArticleRelation);
    }

    /**
     * Fills 'auto' properties for the specified article and old article.
     * <p>
     * Some properties of an article are not been changed while article
     * updating, these properties are called 'auto' properties.
     * </p>
     * <p>
     * The property(named {@value org.b3log.solo.model.Article#ARTICLE_RANDOM_DOUBLE}) of the specified
     * article will be regenerated.
     * </p>
     *
     * @param oldArticle the specified old article
     * @param article    the specified article
     * @throws JSONException json exception
     */
    private void fillAutoProperties(final JSONObject oldArticle, final JSONObject article) throws JSONException {
        final long created = oldArticle.getLong(ARTICLE_CREATED);
        article.put(ARTICLE_CREATED, created);
        article.put(ARTICLE_COMMENT_COUNT, oldArticle.getInt(ARTICLE_COMMENT_COUNT));
        article.put(ARTICLE_VIEW_COUNT, oldArticle.getInt(ARTICLE_VIEW_COUNT));
        article.put(ARTICLE_PUT_TOP, oldArticle.getBoolean(ARTICLE_PUT_TOP));
        article.put(ARTICLE_HAD_BEEN_PUBLISHED, oldArticle.getBoolean(ARTICLE_HAD_BEEN_PUBLISHED));
        article.put(ARTICLE_AUTHOR_ID, oldArticle.getString(ARTICLE_AUTHOR_ID));
        article.put(ARTICLE_RANDOM_DOUBLE, Math.random());
    }

    /**
     * Gets article permalink for adding article with the specified article.
     *
     * @param article the specified article
     * @return permalink
     * @throws ServiceException if invalid permalink occurs
     */
    private String getPermalinkForAddArticle(final JSONObject article) throws ServiceException {
        final long date = article.optLong(Article.ARTICLE_CREATED);
        String ret = article.optString(Article.ARTICLE_PERMALINK);
        if (StringUtils.isBlank(ret)) {
            ret = "/articles/" + DateFormatUtils.format(date, "yyyy/MM/dd") + "/" + article.optString(Keys.OBJECT_ID) + ".html";
        }

        if (!ret.startsWith("/")) {
            ret = "/" + ret;
        }

        if (PermalinkQueryService.invalidArticlePermalinkFormat(ret)) {
            throw new ServiceException(langPropsService.get("invalidPermalinkFormatLabel"));
        }

        if (permalinkQueryService.exist(ret)) {
            throw new ServiceException(langPropsService.get("duplicatedPermalinkLabel"));
        }

        return ret.replaceAll(" ", "-");
    }

    /**
     * Gets article permalink for updating article with the specified old article, article, created at.
     *
     * @param oldArticle the specified old article
     * @param article    the specified article
     * @param created    the specified created
     * @return permalink
     * @throws ServiceException if invalid permalink occurs
     * @throws JSONException    json exception
     */
    private String getPermalinkForUpdateArticle(final JSONObject oldArticle, final JSONObject article, final long created)
            throws ServiceException, JSONException {
        final String articleId = article.getString(Keys.OBJECT_ID);
        String ret = article.optString(ARTICLE_PERMALINK).trim();
        final String oldPermalink = oldArticle.getString(ARTICLE_PERMALINK);

        if (!oldPermalink.equals(ret)) {
            if (StringUtils.isBlank(ret)) {
                ret = "/articles/" + DateFormatUtils.format(created, "yyyy/MM/dd") + "/" + articleId + ".html";
            }

            if (!ret.startsWith("/")) {
                ret = "/" + ret;
            }

            if (PermalinkQueryService.invalidArticlePermalinkFormat(ret)) {
                throw new ServiceException(langPropsService.get("invalidPermalinkFormatLabel"));
            }

            if (!oldPermalink.equals(ret) && permalinkQueryService.exist(ret)) {
                throw new ServiceException(langPropsService.get("duplicatedPermalinkLabel"));
            }
        }

        return ret.replaceAll(" ", "-");
    }

    /**
     * Determines whether the specified tag title exists in the specified tags.
     *
     * @param tagTitle the specified tag title
     * @param tags     the specified tags
     * @return {@code true} if it exists, {@code false} otherwise
     * @throws JSONException json exception
     */
    private static boolean tagExists(final String tagTitle, final List<JSONObject> tags) throws JSONException {
        for (final JSONObject tag : tags) {
            if (tag.getString(Tag.TAG_TITLE).equals(tagTitle)) {
                return true;
            }
        }

        return false;
    }
}
