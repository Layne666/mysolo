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
<dl>
    <#list articles as article>
    <dd class="article">
        <div class="date">
            <div class="month">${article.articleCreateDate?string("MM")}</div>
            <div class="day">${article.articleCreateDate?string("dd")}</div>
        </div>
        <div class="left">
            <h2>
                <a rel="bookmark" href="${servePath}${article.articlePermalink}" title="${tags1Label}${article.articleTags}">
                    ${article.articleTitle}
                </a>
                <#if article.hasUpdated>
                <sup>
                    ${updatedLabel}
                </sup>
                </#if>
                <#if article.articlePutTop>
                <sup>
                    ${topArticleLabel}
                </sup>
                </#if>
            </h2>
            <div class="article-date">
                <#if article.hasUpdated>
                ${article.articleUpdateDate?string("yyyy HH:mm:ss")}
                <#else>
                ${article.articleCreateDate?string("yyyy HH:mm:ss")}
                </#if>
                by
                <a rel="nofollow" class="underline" title="${article.authorName}" href="${servePath}/authors/${article.authorId}">
                    ${article.authorName}</a> |
                <a rel="nofollow" class="underline" href="${servePath}${article.articlePermalink}#comments">
                    ${article.articleCommentCount}${commentLabel}
                </a>
            </div>
        </div>
        <div class="clear"></div>
        <div class="article-abstract article-body">
            ${article.articleAbstract}
            <div class="clear"></div>
            <a class="right underline" href="${servePath}${article.articlePermalink}">
                ${readmore2Label}...
            </a>
            <span class="clear"></span>
        </div>
    </dd>
    </#list>
</dl>
<#if 0 != paginationPageCount>
<div class="pagination right">
    <#if 1 != paginationPageNums?first>
    <a href="${servePath}${path}" title="${firstPageLabel}"><<</a>
    <a id="previousPage" href="${servePath}${path}?p=${paginationPreviousPageNum}">${previousPageLabel}</a>
    </#if>
    <#list paginationPageNums as paginationPageNum>
    <#if paginationPageNum == paginationCurrentPageNum>
    <a href="${servePath}${path}?p=${paginationPageNum}" class="selected">${paginationPageNum}</a>
    <#else>
    <a href="${servePath}${path}?p=${paginationPageNum}">${paginationPageNum}</a>
    </#if>
    </#list>
    <#if paginationPageNums?last != paginationPageCount>
    <a id="nextPage" href="${servePath}${path}?p=${paginationNextPageNum}">${nextPagePabel}</a>
    <a title="${lastPageLabel}" href="${servePath}${path}?p=${paginationPageCount}">>></a>
    </#if>
    &nbsp;&nbsp;${sumLabel} ${paginationPageCount} ${pageLabel}
</div>
<div class="clear"></div>
</#if>