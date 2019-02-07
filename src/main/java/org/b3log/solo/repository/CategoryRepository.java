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
package org.b3log.solo.repository;

import org.b3log.latke.Keys;
import org.b3log.latke.repository.*;
import org.b3log.latke.repository.annotation.Repository;
import org.b3log.solo.model.Category;
import org.b3log.solo.model.Tag;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

/**
 * Category repository.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.2.0.2, Sep 30, 2018
 * @since 2.0.0
 */
@Repository
public class CategoryRepository extends AbstractRepository {

    /**
     * Public constructor.
     */
    public CategoryRepository() {
        super(Category.CATEGORY);
    }

    /**
     * Gets a category by the specified category title.
     *
     * @param categoryTitle the specified category title
     * @return a category, {@code null} if not found
     * @throws RepositoryException repository exception
     */
    public JSONObject getByTitle(final String categoryTitle) throws RepositoryException {
        final Query query = new Query().setFilter(new PropertyFilter(Category.CATEGORY_TITLE, FilterOperator.EQUAL, categoryTitle)).setPageCount(1);
        final JSONObject result = get(query);
        final JSONArray array = result.optJSONArray(Keys.RESULTS);
        if (0 == array.length()) {
            return null;
        }

        return array.optJSONObject(0);
    }

    /**
     * Gets a category by the specified category URI.
     *
     * @param categoryURI the specified category URI
     * @return a category, {@code null} if not found
     * @throws RepositoryException repository exception
     */
    public JSONObject getByURI(final String categoryURI) throws RepositoryException {
        final Query query = new Query().setFilter(new PropertyFilter(Category.CATEGORY_URI, FilterOperator.EQUAL, categoryURI)).setPageCount(1);
        final JSONObject result = get(query);
        final JSONArray array = result.optJSONArray(Keys.RESULTS);
        if (0 == array.length()) {
            return null;
        }

        return array.optJSONObject(0);
    }

    /**
     * Gets the maximum order.
     *
     * @return order number, returns {@code -1} if not found
     * @throws RepositoryException repository exception
     */
    public int getMaxOrder() throws RepositoryException {
        final Query query = new Query().addSort(Category.CATEGORY_ORDER, SortDirection.DESCENDING);
        final JSONObject result = get(query);
        final JSONArray array = result.optJSONArray(Keys.RESULTS);
        if (0 == array.length()) {
            return -1;
        }

        return array.optJSONObject(0).optInt(Category.CATEGORY_ORDER);
    }

    /**
     * Gets the upper category of the category specified by the given id.
     *
     * @param id the given id
     * @return upper category, returns {@code null} if not found
     * @throws RepositoryException repository exception
     */
    public JSONObject getUpper(final String id) throws RepositoryException {
        final JSONObject category = get(id);
        if (null == category) {
            return null;
        }

        final Query query = new Query().setFilter(new PropertyFilter(Category.CATEGORY_ORDER, FilterOperator.LESS_THAN, category.optInt(Category.CATEGORY_ORDER))).
                addSort(Category.CATEGORY_ORDER, SortDirection.DESCENDING).setPage(1, 1);
        final JSONObject result = get(query);
        final JSONArray array = result.optJSONArray(Keys.RESULTS);
        if (1 != array.length()) {
            return null;
        }

        return array.optJSONObject(0);
    }

    /**
     * Gets the under category of the category specified by the given id.
     *
     * @param id the given id
     * @return under category, returns {@code null} if not found
     * @throws RepositoryException repository exception
     */
    public JSONObject getUnder(final String id) throws RepositoryException {
        final JSONObject category = get(id);
        if (null == category) {
            return null;
        }

        final Query query = new Query().setFilter(new PropertyFilter(Category.CATEGORY_ORDER, FilterOperator.GREATER_THAN, category.optInt(Category.CATEGORY_ORDER))).
                addSort(Category.CATEGORY_ORDER, SortDirection.ASCENDING).setPage(1, 1);
        final JSONObject result = get(query);
        final JSONArray array = result.optJSONArray(Keys.RESULTS);
        if (1 != array.length()) {
            return null;
        }

        return array.optJSONObject(0);
    }

    /**
     * Gets a category by the specified order.
     *
     * @param order the specified order
     * @return category, returns {@code null} if not found
     * @throws RepositoryException repository exception
     */
    public JSONObject getByOrder(final int order) throws RepositoryException {
        final Query query = new Query().setFilter(new PropertyFilter(Category.CATEGORY_ORDER, FilterOperator.EQUAL, order));
        final JSONObject result = get(query);
        final JSONArray array = result.optJSONArray(Keys.RESULTS);
        if (0 == array.length()) {
            return null;
        }

        return array.optJSONObject(0);
    }

    /**
     * Gets most used categories (contains the most tags) with the specified number.
     *
     * @param num the specified number
     * @return a list of most used categories, returns an empty list if not found
     * @throws RepositoryException repository exception
     */
    public List<JSONObject> getMostUsedCategories(final int num) throws RepositoryException {
        final Query query = new Query().addSort(Category.CATEGORY_ORDER, SortDirection.ASCENDING).
                setPage(1, num).setPageCount(1);
        final List<JSONObject> ret = getList(query);
        Collections.sort(ret, (o1, o2) -> Collator.getInstance(java.util.Locale.CHINA).compare(o1.optString(Tag.TAG_TITLE), o2.optString(Tag.TAG_TITLE)));

        return ret;
    }
}
