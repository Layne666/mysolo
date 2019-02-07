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
<#include "macro-head.ftl">
<#include "macro-side.ftl">
<!DOCTYPE html>
<html>
    <head>
        <@head title="${archiveDate.archiveDateMonth} ${archiveDate.archiveDateYear} (${archiveDate.archiveDatePublishedArticleCount}) - ${blogTitle}">
        <meta name="keywords" content="${metaKeywords},${archiveDate.archiveDateYear}${archiveDate.archiveDateMonth}"/>
        <meta name="description" content="<#list articles as article>${article.articleTitle}<#if article_has_next>,</#if></#list>"/>
        </@head>
    </head>
    <body>
        ${topBarReplacement}
        <div class="wrapper">
            <div id="header">
                <#include "header.ftl" />
                <div class="sub-nav fn-clear">
                    <h2>
                        ${archive1Label}
                        <#if "en" == localeString?substring(0, 2)>
                        ${archiveDate.archiveDateMonth} ${archiveDate.archiveDateYear} (${archiveDate.archiveDatePublishedArticleCount})
                        <#else>
                        ${archiveDate.archiveDateYear} ${yearLabel} ${archiveDate.archiveDateMonth} ${monthLabel} (${archiveDate.archiveDatePublishedArticleCount})
                        </#if>
                    </h2>
                    <#if 0 != paginationPageCount>
                    <div class="pagination">
                        <#if 1 != paginationPageNums?first>
                        <a id="previousPage" href="${servePath}${path}?p=${paginationPreviousPageNum}"
                           title="${previousPageLabel}"><</a>
                        </#if>
                        <#list paginationPageNums as paginationPageNum>
                        <#if paginationPageNum == paginationCurrentPageNum>
                        <span>${paginationPageNum}</span>
                        <#else>
                        <a href="${servePath}${path}?p=${paginationPageNum}">${paginationPageNum}</a>
                        </#if>
                        </#list>
                        <#if paginationPageNums?last != paginationPageCount>
                        <a id="nextPage" href="${servePath}${path}?p=${paginationNextPageNum}" title="${nextPagePabel}">></a>
                        </#if>
                    </div>
                    </#if>
                </div>
            </div> 
            <div class="fn-clear">
                <div class="main">
                    <#include "article-list.ftl"/>
                    <#include "copyright.ftl"/>
                </div>
                <@side isArticle=false />
            </div>
        </div>
        <span id="goTop" onclick="Util.goTop()" data-ico="&#xe042;" class="side-tile"></span>
        <#include "footer.ftl"/>
    </body>
</html>
