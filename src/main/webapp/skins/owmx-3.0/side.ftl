<#--

    Solo - A small and beautiful blogging system written in Java.
    Copyright (c) 2010-2019, b3log.org & hacpai.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

-->
<aside>
    <nav>
        <h4>Search</h4>
        <form action="${servePath}/search">
            <input style="width: 194px" id="search" type="text" name="keyword" />
            <input type="submit" value="" class="none" />
        </form>
    </nav>
    <nav>
        <h4>Navigation</h4>
        <ul>
            <li>
                <a rel="nofollow" class="home" href="${servePath}">${indexLabel}</a>
            </li>
            <#list pageNavigations as page>
            <li>
                <a href="${page.pagePermalink}" target="${page.pageOpenTarget}">
                    <#if page.pageIcon != ''><img class="page-icon" src="${page.pageIcon}"></#if>${page.pageTitle}
                </a>
            </li>
            </#list>
            <li>
                <a href="${servePath}/tags.html">${allTagsLabel}</a>
            </li>
            <li>
                <a rel="alternate" href="${servePath}/rss.xml">
                    RSS
                    <img src="${staticServePath}/images/feed.png" alt="RSS"/>
                </a>
            </li>
            <li>
                <a class="lastNavi" href="javascript:void(0);"></a>
            </li>
        </ul>
    </nav>
    <#if "" != noticeBoard>
    <h4>${noticeBoardLabel}</h4>
    <div id="c">
        <p>
            ${noticeBoard}
        </p>
    </div>
    </#if>
    <#if 0 != recentComments?size>
    <h4>${recentCommentsLabel}</h4>
    <ul class="aside-comments">
        <#list recentComments as comment>
        <li>
            <img class="left" title='${comment.commentName}'
                 alt='${comment.commentName}'
                 src='${comment.commentThumbnailURL}' width="32" height="32"/>
            <div class="left">
                <div>
                    <a target="_blank" href="${comment.commentURL}">
                        ${comment.commentName}
                    </a>
                </div>
                <div>
                    <a rel="nofollow" class='side-comment' href="${servePath}${comment.commentSharpURL}">
                        ${comment.commentContent}
                    </a>
                </div>
            </div>
            <div class='clear'></div>
        </li>
        </#list>
    </ul>
    </#if>
    <#if 0 != mostCommentArticles?size>
    <h4>${mostCommentArticlesLabel}</h4>
    <ul id="mostCommentArticles">
        <#list mostCommentArticles as article>
        <li>
            <a rel="nofollow" title="${article.articleTitle}"
               href="${servePath}${article.articlePermalink}">
                <sup>[${article.articleCommentCount}]</sup>
                ${article.articleTitle}
            </a>
        </li>
        </#list>
    </ul>
    </#if>
    <#if 0 != mostViewCountArticles?size>
    <h4>${mostViewCountArticlesLabel}</h4>
    <ul id="mostViewCountArticles">
        <#list mostViewCountArticles as article>
        <li>
            <a rel="nofollow" title="${article.articleTitle}"
               href="${servePath}${article.articlePermalink}">
                <sup>[${article.articleViewCount}]</sup>
                ${article.articleTitle}
            </a>
        </li>
        </#list>
    </ul>
    </#if>

    <#if 0 != mostUsedCategories?size>
        <h4>${categoryLabel}</h4>
        <ul>
            <#list mostUsedCategories as category>
                <li>
                    <a href="${servePath}/category/${category.categoryURI}"
                       title="${category.categoryTitle} (${category.categoryTagCnt})">
                        ${category.categoryTitle}</a>(${category.categoryTagCnt})
                </li>
            </#list>
        </ul>
    </#if>

    <#if 0 != mostUsedTags?size>
    <h4>${tagsLabel}</h4>
    <ul class="navi-tags">
        <#list mostUsedTags as tag>
        <li>
            <a rel="tag" title="${tag.tagTitle}(${tag.tagPublishedRefCount})" href="${servePath}/tags/${tag.tagTitle?url('UTF-8')}">
                <sup>[${tag.tagPublishedRefCount}]</sup>
                ${tag.tagTitle}</a>
        </li>
        </#list>
    </ul>
    </#if>
    <#if 0 != links?size>
    <h4>${linkLabel}</h4>
    <ul id="sideLink" class="navi-tags">
        <#list links as link>
        <li>
            <a rel="friend" href="${link.linkAddress}" title="${link.linkTitle}" target="_blank">
                <img alt="${link.linkTitle}" 
                     src="${faviconAPI}<#list link.linkAddress?split('/') as x><#if x_index=2>${x}<#break></#if></#list>" width="16" height="16" /></a>
            <a rel="friend" href="${link.linkAddress}" title="${link.linkTitle}" target="_blank">
                ${link.linkTitle}
            </a>
        </li>
        </#list>
    </ul>
    </#if>
    <#if 0 != archiveDates?size>
    <h4>${archiveLabel}</h4>
    <ul>
        <#list archiveDates as archiveDate>
        <li>
            <#if "en" == localeString?substring(0, 2)>
            <a href="${servePath}/archives/${archiveDate.archiveDateYear}/${archiveDate.archiveDateMonth}"
               title="${archiveDate.monthName} ${archiveDate.archiveDateYear}(${archiveDate.archiveDatePublishedArticleCount})">
                ${archiveDate.monthName} ${archiveDate.archiveDateYear}</a>(${archiveDate.archiveDatePublishedArticleCount})
            <#else>
            <a href="${servePath}/archives/${archiveDate.archiveDateYear}/${archiveDate.archiveDateMonth}"
               title="${archiveDate.archiveDateYear} ${yearLabel} ${archiveDate.archiveDateMonth} ${monthLabel}(${archiveDate.archiveDatePublishedArticleCount})">
                ${archiveDate.archiveDateYear} ${yearLabel} ${archiveDate.archiveDateMonth} ${monthLabel}</a>(${archiveDate.archiveDatePublishedArticleCount})
            </#if>
        </li>
        </#list>
    </ul>
    </#if>
</aside>