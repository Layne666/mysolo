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
<div class="fn-clear">
    <span class="fn-right">
        <a href="javascript:Finding.tabNav('link')" title="${permalinkLabel}" class="icon-sitemap current"></a>
        &nbsp;
        <a href="javascript:Finding.tabNav('toc')" title="${tocLabel}" class="icon-list fn-none"> &nbsp; </a>
        <#if isLoggedIn>
        <a href="${servePath}/admin-index.do#main" title="${adminLabel}" class="icon-setting"></a>
        &nbsp; 
        <a title="${logoutLabel}" class="icon-logout" href="${logoutURL}"></a>
        <#else>
        <a title="${loginLabel}" href="${loginURL}" class="icon-login"></a>
        &nbsp; 
        <a href="${servePath}/register" title="${registerLabel}" class="icon-register"></a>
        </#if>
    </span>
</div>
<ul>
    <#list pageNavigations as page>
    <li>
        <a href="${page.pagePermalink}" target="${page.pageOpenTarget}"><#if page.pageIcon != ''><img class="page-icon" src="${page.pageIcon}"></#if>${page.pageTitle}</a>
    </li>
    </#list>
    <li>
        <a href="${servePath}/dynamic.html">${dynamicLabel}</a>
    </li>
    <li>
        <a href="${servePath}/category.html">${categoryLabel}</a>
    </li>
    <li>
        <a href="${servePath}/tags.html">${allTagsLabel}</a>
    </li>
    <li>
        <a href="${servePath}/archives.html">${archiveLabel}</a>
    </li>
    <li>
        <a href="${servePath}/links.html">${linkLabel}</a>
    </li>
    <li>
        <a rel="alternate" href="${servePath}/rss.xml">${subscribeLabel}</a>
    </li>
    <Li>
        <a href="${servePath}/search?keyword=">Search</a>
    </Li>
</ul>
<div class="count">
    <span>
        ${viewCount1Label}
        ${statistic.statisticBlogViewCount}
    </span> &nbsp; &nbsp;
    <span>
        ${articleCount1Label}
        ${statistic.statisticPublishedBlogArticleCount}
    </span><br/>
    <span>
        ${commentCount1Label}
        ${statistic.statisticPublishedBlogCommentCount}
    </span> &nbsp; &nbsp;
    <span>
        ${onlineVisitor1Label}
        ${onlineVisitorCnt}
    </span>
</div>