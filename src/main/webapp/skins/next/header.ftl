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
<header class="header">
    <div class="header-line"></div>
    <div class="fn-clear wrapper">
        <div class="logo-wrap">
            <a href="${servePath}" rel="start">
                <span class="logo-line-before"><i></i></span>
                <span class="site-title">${blogTitle}</span>
                <span class="logo-line-after"><i></i></span>
            </a>
        </div>

        <div class="site-nav-toggle fn-right" onclick="$('.header-line').toggle();$('nav').children('.menu').slideToggle();">
            <span class="btn-bar"></span>
            <span class="btn-bar"></span>
            <span class="btn-bar"></span>
        </div>

        <nav>
            <ul class="menu">
                <#list pageNavigations as page>
                <li class="menu-item">
                    <#if page.pageIcon != ''><img class="page-icon" src="${page.pageIcon}"></#if>
                    <a href="${page.pagePermalink}" target="${page.pageOpenTarget}" rel="section">
                        ${page.pageTitle}
                    </a>
                </li>
                </#list>  
                <li class="menu-item">
                    <a href="${servePath}/dynamic.html" rel="section">
                        ${dynamicLabel}
                    </a>
                </li>
                <li class="menu-item">
                    <a href="${servePath}/tags.html" rel="section">
                        ${allTagsLabel}
                    </a>  
                </li>
                <li class="menu-item">
                    <a href="${servePath}/archives.html">
                        ${archiveLabel}
                    </a>
                </li>
                <li class="menu-item">
                    <a rel="alternate" href="${servePath}/rss.xml" rel="section">
                        RSS
                    </a>
                </li>
            </ul>

            <div class="site-search">
                <form action="${servePath}/search">
                    <input placeholder="${searchLabel}" id="search" type="text" name="keyword"/>
                    <input type="submit" value="" class="fn-none" />
                </form>
            </div>
        </nav>
    </div>
</header>