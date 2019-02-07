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

import org.b3log.latke.Keys;
import org.b3log.latke.util.Requests;
import org.b3log.solo.AbstractTestCase;
import org.b3log.solo.model.Comment;
import org.b3log.solo.model.Page;
import org.b3log.solo.util.Solos;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * {@link CommentMgmtService} test case.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.3, Sep 16, 2018
 */
@Test(suiteName = "service")
public class CommentMgmtServiceTestCase extends AbstractTestCase {

    /**
     * Init.
     *
     * @throws Exception exception
     */
    @Test
    public void init() throws Exception {
        super.init();
    }

    /**
     * Add Article Comment.
     *
     * @throws Exception exception
     */
    @Test(dependsOnMethods = "init")
    public void addArticleComment() throws Exception {
        final ArticleQueryService articleQueryService = getArticleQueryService();

        final List<JSONObject> articles = articleQueryService.getRecentArticles(10);

        Assert.assertEquals(articles.size(), 1);

        final CommentQueryService commentQueryService = getCommentQueryService();
        JSONObject paginationRequest = Solos.buildPaginationRequest("1/10/20");
        JSONObject result = commentQueryService.getComments(paginationRequest);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getJSONArray(Comment.COMMENTS).length(), 1);

        final CommentMgmtService commentMgmtService = getCommentMgmtService();
        final JSONObject requestJSONObject = new JSONObject();

        final String articleId = articles.get(0).getString(Keys.OBJECT_ID);
        requestJSONObject.put(Keys.OBJECT_ID, articleId);
        requestJSONObject.put(Comment.COMMENT_NAME, "comment name");
        requestJSONObject.put(Comment.COMMENT_EMAIL, "comment email");
        requestJSONObject.put(Comment.COMMENT_URL, "comment URL");
        requestJSONObject.put(Comment.COMMENT_CONTENT, "comment content");

        final JSONObject addResult = commentMgmtService.addArticleComment(requestJSONObject);
        Assert.assertNotNull(addResult);
        Assert.assertNotNull(addResult.getString(Keys.OBJECT_ID));
        Assert.assertNotNull(addResult.getString(Comment.COMMENT_T_DATE));
        Assert.assertNotNull(addResult.getString(Comment.COMMENT_THUMBNAIL_URL));
        Assert.assertNotNull(addResult.getString(Comment.COMMENT_SHARP_URL));

        result = commentQueryService.getComments(paginationRequest);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getJSONArray(Comment.COMMENTS).length(), 2);
    }

    /**
     * Add Page Comment.
     *
     * @throws Exception exception
     */
    @Test(dependsOnMethods = "addArticleComment")
    public void addPageComment() throws Exception {
        addPage();

        final PageQueryService pageQueryService = getPageQueryService();

        final JSONObject paginationRequest = Solos.buildPaginationRequest("1/10/20");
        JSONObject result = pageQueryService.getPages(paginationRequest);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getJSONArray(Page.PAGES).length(), 1);

        final JSONArray pages = result.getJSONArray(Page.PAGES);

        final CommentQueryService commentQueryService = getCommentQueryService();
        result = commentQueryService.getComments(paginationRequest);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getJSONArray(Comment.COMMENTS).length(),
                2);  // 2 article comments

        final CommentMgmtService commentMgmtService = getCommentMgmtService();
        final JSONObject requestJSONObject = new JSONObject();

        final String pageId = pages.getJSONObject(0).getString(Keys.OBJECT_ID);
        requestJSONObject.put(Keys.OBJECT_ID, pageId);
        requestJSONObject.put(Comment.COMMENT_NAME, "comment name");
        requestJSONObject.put(Comment.COMMENT_EMAIL, "comment email");
        requestJSONObject.put(Comment.COMMENT_URL, "comment URL");
        requestJSONObject.put(Comment.COMMENT_CONTENT, "comment content");

        final JSONObject addResult = commentMgmtService.addPageComment(requestJSONObject);
        Assert.assertNotNull(addResult);
        Assert.assertNotNull(addResult.getString(Keys.OBJECT_ID));
        Assert.assertNotNull(addResult.getString(Comment.COMMENT_T_DATE));
        Assert.assertNotNull(addResult.getString(Comment.COMMENT_THUMBNAIL_URL));
        Assert.assertNotNull(addResult.getString(Comment.COMMENT_SHARP_URL));

        result = commentQueryService.getComments(paginationRequest);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getJSONArray(Comment.COMMENTS).length(),
                3);  // 2 article comments + 1 page comment

        final List<JSONObject> pageComments = commentQueryService.getComments(pageId);
        Assert.assertNotNull(pageComments);
        Assert.assertEquals(pageComments.size(), 1);
    }

    /**
     * Adds a page.
     *
     * @throws Exception exception
     */
    private void addPage() throws Exception {
        final PageMgmtService pageMgmtService = getPageMgmtService();

        final JSONObject requestJSONObject = new JSONObject();
        final JSONObject page = new JSONObject();
        requestJSONObject.put(Page.PAGE, page);

        page.put(Page.PAGE_CONTENT, "page1 content");
        page.put(Page.PAGE_PERMALINK, "page1 permalink");
        page.put(Page.PAGE_TITLE, "page1 title");
        page.put(Page.PAGE_COMMENTABLE, true);
        page.put(Page.PAGE_TYPE, "page");
        page.put(Page.PAGE_OPEN_TARGET, "_self");

        final String pageId = pageMgmtService.addPage(requestJSONObject);

        Assert.assertNotNull(pageId);
    }
}
