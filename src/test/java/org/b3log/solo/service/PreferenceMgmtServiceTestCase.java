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

import org.b3log.solo.AbstractTestCase;
import org.b3log.solo.model.Option;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * {@link PreferenceMgmtService} test case.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.4, May 29, 2018
 */
@Test(suiteName = "service")
public class PreferenceMgmtServiceTestCase extends AbstractTestCase {

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
     * Update Preference.
     *
     * @throws Exception exception
     */
    @Test(dependsOnMethods = "init")
    public void updatePreference() throws Exception {
        final PreferenceMgmtService preferenceMgmtService = getPreferenceMgmtService();
        final PreferenceQueryService preferenceQueryService = getPreferenceQueryService();
        JSONObject preference = preferenceQueryService.getPreference();

        Assert.assertEquals(preference.getString(Option.ID_C_BLOG_TITLE), "Admin 的个人博客");

        preference.put(Option.ID_C_BLOG_TITLE, "updated blog title");
        preferenceMgmtService.updatePreference(preference);

        preference = preferenceQueryService.getPreference();
        Assert.assertEquals(preference.getString(Option.ID_C_BLOG_TITLE), "updated blog title");
    }

    /**
     * Update Reply Notification Template.
     *
     * @throws Exception exception
     */
    @Test(dependsOnMethods = "init")
    public void updateReplyNotificationTemplate() throws Exception {
        final PreferenceMgmtService preferenceMgmtService = getPreferenceMgmtService();
        final PreferenceQueryService preferenceQueryService = getPreferenceQueryService();
        JSONObject replyNotificationTemplate = preferenceQueryService.getReplyNotificationTemplate();

        Assert.assertEquals(replyNotificationTemplate.toString(), Option.DefaultPreference.DEFAULT_REPLY_NOTIFICATION_TEMPLATE);

        replyNotificationTemplate.put("subject", "updated subject");
        preferenceMgmtService.updateReplyNotificationTemplate(replyNotificationTemplate);

        replyNotificationTemplate = preferenceQueryService.getReplyNotificationTemplate();
        Assert.assertEquals(replyNotificationTemplate.getString("subject"), "updated subject");
    }
}
