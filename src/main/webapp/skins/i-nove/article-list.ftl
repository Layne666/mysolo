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
<#list articles as article>
<div class="article">
    <h2 class="article-title">
        <a rel="bookmark" class="no-underline" href="${servePath}${article.articlePermalink}">
            ${article.articleTitle}
        </a>
        <#if article.hasUpdated>
        <sup class="red">
            ${updatedLabel}
        </sup>
        </#if>
        <#if article.articlePutTop>
        <sup class="red">
            ${topArticleLabel}
        </sup>
        </#if>
    </h2>
    <div class="margin5">
        <div class="article-date left">
            <a rel="nofollow" class="left" title="${article.authorName}" href="${servePath}/authors/${article.authorId}">
                <span class="authorIcon"></span>
                ${article.authorName}
            </a>
            <span class="dateIcon"></span>
            <span class="left">
                <#if article.hasUpdated>
                ${article.articleUpdateDate?string("yyyy-MM-dd HH:mm:ss")}
                <#else>
                ${article.articleCreateDate?string("yyyy-MM-dd HH:mm:ss")}
                </#if>
            </span>
        </div>
        <div class="right">
            <a rel="nofollow" href="${servePath}${article.articlePermalink}#comments" class="left">
                <span class="left articles-commentIcon" title="${commentLabel}"></span>
                ${article.articleCommentCount}
            </a>
        </div>
        <div class="clear"></div>
    </div>
    <div class="article-abstract article-body">
        ${article.articleAbstract}
        <div class="clear"></div>
    </div>
    <div class="article-footer">
        <a rel="nofollow" href="${servePath}${article.articlePermalink}" class="left">
            <span class="left article-browserIcon" title="${viewLabel}"></span>
            ${article.articleViewCount}
        </a>
        <div class="left">
            <span class="tagsIcon" title="${tagLabel}"></span>
            <#list article.articleTags?split(",") as articleTag>
            <span>
                <a rel="tag" href="${servePath}/tags/${articleTag?url('UTF-8')}">
                    ${articleTag}</a><#if articleTag_has_next>,</#if>
            </span>
            </#list>
        </div>
        <div class="clear"></div>
    </div>
</div>
</#list>
<#if 0 != paginationPageCount>
<div class="pagination">
    <#if 1 != paginationPageNums?first>
    <a href="${servePath}${path}">${firstPageLabel}</a>
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
    <a href="${servePath}${path}?p=${paginationPageCount}">${lastPageLabel}</a>
    </#if>
    &nbsp;&nbsp;${sumLabel} ${paginationPageCount} ${pageLabel}
</div>
</#if>