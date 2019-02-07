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
<div class="side">
    <div class="overlay">
        <a onclick="$('.side .toc').show()" href="javascript:void(0)" class="toc-btn">${tocLabel}</a>
    </div>
    <header class="content">
        <a href="${servePath}">
            <img class="avatar" src="${adminUser.userAvatar}" title="${userName}"/>
        </a>
        <hgroup>
            <h1>
                <a href="${servePath}">${blogTitle}</a>
            </h1>
        </hgroup>
        <#if "" != noticeBoard>
        <p class="subtitle">
            ${blogSubtitle}
        </p>
        </#if>
        <nav>
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
                    <a href="${servePath}/tags.html">${allTagsLabel}</a>
                </li>
                <li>
                    <a href="${servePath}/archives.html">${archiveLabel}</a>
                </li>
                <li>
                    <a href="${servePath}/links.html">${linkLabel}</a>
                </li>
                <li>
                    <a href="${servePath}/search?keyword=">
                        Search
                    </a>
                </li>
            </ul>
        </nav>
    </header>
    <footer>
        <#if noticeBoard??>
        <div>${noticeBoard}</div>
        </#if>
        <#if isLoggedIn>
        <a href="${servePath}/admin-index.do#main" title="${adminLabel}" class="icon-setting"></a>
        &nbsp; &nbsp; 
        <a title="${logoutLabel}" class="icon-logout" href="${logoutURL}"></a>
        <#else>
        <a title="${loginLabel}" href="${loginURL}" class="icon-login"></a>
        &nbsp; &nbsp; 
        <a href="${servePath}/register" title="${registerLabel}" class="icon-register"></a>
        </#if> &nbsp; &nbsp; 
        <a rel="alternate" href="${servePath}/rss.xml" title="${subscribeLabel}" class="icon-rss"></a>
    </footer>
</div>