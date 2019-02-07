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
import org.b3log.solo.AbstractTestCase;
import org.b3log.solo.model.Option;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * {@link OptionMgmtService} test case.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.1, Jan 29, 2019
 * @since 0.6.0
 */
@Test(suiteName = "service")
public class OptionMgmtServiceTestCase extends AbstractTestCase {

    /**
     * Add.
     *
     * @throws Exception exception
     */
    @Test
    public void add() throws Exception {
        final OptionMgmtService optionMgmtService = getOptionMgmtService();

        final JSONObject option = new JSONObject();
        option.put(Keys.OBJECT_ID, Option.ID_C_BLOG_TITLE);
        option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_PREFERENCE);
        option.put(Option.OPTION_VALUE, 0L);

        final String id = optionMgmtService.addOrUpdateOption(option);
        //System.out.println(id);
        Assert.assertNotNull(id);

        final JSONObject opt = getOptionQueryService().getOptionById(Option.ID_C_BLOG_TITLE);
        Assert.assertEquals(opt.getInt(Option.OPTION_VALUE), 0L);
    }

    /**
     * Update.
     *
     * @throws Exception exception
     */
    @Test
    public void update() throws Exception {
        final OptionMgmtService optionMgmtService = getOptionMgmtService();

        JSONObject option = new JSONObject();
        option.put(Keys.OBJECT_ID, Option.ID_C_BLOG_TITLE);
        option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_PREFERENCE);
        option.put(Option.OPTION_VALUE, 0L);

        final String id = optionMgmtService.addOrUpdateOption(option); // Add
        //System.out.println(id);
        Assert.assertNotNull(id);

        option = new JSONObject();
        option.put(Keys.OBJECT_ID, Option.ID_C_BLOG_TITLE);
        option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_PREFERENCE);
        option.put(Option.OPTION_VALUE, 1L);

        optionMgmtService.addOrUpdateOption(option); // Update

        final JSONObject opt = getOptionQueryService().getOptionById(Option.ID_C_BLOG_TITLE);
        Assert.assertEquals(opt.getInt(Option.OPTION_VALUE), 1L);
    }

    /**
     * Remove.
     *
     * @throws Exception exception
     */
    @Test
    public void remove() throws Exception {
        final OptionMgmtService optionMgmtService = getOptionMgmtService();

        final JSONObject option = new JSONObject();
        option.put(Keys.OBJECT_ID, Option.ID_C_BLOG_TITLE);
        option.put(Option.OPTION_CATEGORY, Option.CATEGORY_C_PREFERENCE);
        option.put(Option.OPTION_VALUE, 0L);

        final String id = optionMgmtService.addOrUpdateOption(option);
        Assert.assertNotNull(id);

        optionMgmtService.removeOption(id);

        final JSONObject opt = getOptionQueryService().getOptionById(id);
        Assert.assertNull(opt);
    }
}
