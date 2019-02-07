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

import org.b3log.latke.ioc.Inject;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.solo.repository.OptionRepository;
import org.json.JSONObject;

/**
 * Option query service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.3, Dec 3, 2018
 * @since 0.6.0
 */
@Service
public class OptionQueryService {

    /**
     * Option repository.
     */
    @Inject
    private OptionRepository optionRepository;

    /**
     * Gets an option with the specified option id.
     *
     * @param optionId the specified option id
     * @return an option, returns {@code null} if not found
     */
    public JSONObject getOptionById(final String optionId) {
        try {
            return optionRepository.get(optionId);
        } catch (final RepositoryException e) {
            return null;
        }
    }

    /**
     * Gets options with the specified category.
     * <p>
     * All options with the specified category will be merged into one json object as the return value.
     * </p>
     *
     * @param category the specified category
     * @return all options with the specified category, for example,
     * <pre>
     * {
     *     "${optionId}": "${optionValue}",
     *     ....
     * }
     * </pre>, returns {@code null} if not found
     */
    public JSONObject getOptions(final String category) {
        try {
            return optionRepository.getOptions(category);
        } catch (final Exception e) {
            return null;
        }
    }
}
