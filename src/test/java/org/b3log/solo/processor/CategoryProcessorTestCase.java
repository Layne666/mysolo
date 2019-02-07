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
import org.b3log.solo.AbstractTestCase;
import org.b3log.solo.MockHttpServletRequest;
import org.b3log.solo.MockHttpServletResponse;
import org.b3log.solo.model.Category;
import org.b3log.solo.model.Option;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 * {@link CategoryProcessor} test case.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Dec 11, 2018
 * @since 2.9.8
 */
@Test(suiteName = "processor")
public class CategoryProcessorTestCase extends AbstractTestCase {

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
     * showCategoryArticles.
     *
     * @throws Exception exception
     */
    @Test(dependsOnMethods = "init")
    public void showCategoryArticles() throws Exception {
        MockHttpServletRequest request = mockRequest();
        request.setRequestURI("/console/category/");
        request.setMethod("POST");
        final JSONObject requestJSON = new JSONObject();
        requestJSON.put(Category.CATEGORY_T_TAGS, "Solo");
        requestJSON.put(Category.CATEGORY_TITLE, "分类1");
        requestJSON.put(Category.CATEGORY_URI, "cate1");

        final BufferedReader reader = new BufferedReader(new StringReader(requestJSON.toString()));
        request.setReader(reader);

        mockAdminLogin(request);

        MockHttpServletResponse response = mockResponse();
        mockDispatcherServletService(request, response);

        request = mockRequest();
        request.setRequestURI("/category/cate1");
        request.setMethod("GET");
        request.setAttribute(Keys.TEMAPLTE_DIR_NAME, Option.DefaultPreference.DEFAULT_SKIN_DIR_NAME);
        response = mockResponse();
        mockDispatcherServletService(request, response);

        final String content = response.body();
        Assert.assertTrue(StringUtils.contains(content, "<title>分类1 - Admin 的个人博客</title>"));
    }
}
