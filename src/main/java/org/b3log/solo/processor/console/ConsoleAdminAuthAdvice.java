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
package org.b3log.solo.processor.console;

import org.b3log.latke.Keys;
import org.b3log.latke.ioc.Singleton;
import org.b3log.latke.servlet.RequestContext;
import org.b3log.latke.servlet.advice.ProcessAdvice;
import org.b3log.latke.servlet.advice.RequestProcessAdviceException;
import org.b3log.solo.util.Solos;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

/**
 * The common auth check before advice for admin console.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.1.3, Oct 5, 2018
 * @since 2.9.5
 */
@Singleton
public class ConsoleAdminAuthAdvice extends ProcessAdvice {

    @Override
    public void doAdvice(final RequestContext context) throws RequestProcessAdviceException {
        if (!Solos.isAdminLoggedIn(context)) {
            final JSONObject exception401 = new JSONObject();
            exception401.put(Keys.MSG, "Unauthorized to request [" + context.requestURI() + "]");
            exception401.put(Keys.STATUS_CODE, HttpServletResponse.SC_UNAUTHORIZED);

            throw new RequestProcessAdviceException(exception401);
        }
    }
}
