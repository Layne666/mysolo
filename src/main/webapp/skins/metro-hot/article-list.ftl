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
<div class="article-list fn-clear">
    <#list articles as article>
    <div>
        <div class="article-abstract">
            <div class="fn-clear">
                <div class="article-date" data-ico="&#xe200;">
                    <#if article.hasUpdated>
                    ${article.articleUpdateDate?string("yy-MM-dd HH:mm")}
                    <#else>
                    ${article.articleCreateDate?string("yy-MM-dd HH:mm")}
                    </#if>  
                </div>
                <div class="fn-right">
                    <a rel="nofollow" data-ico="&#xe14e;" href="${servePath}${article.articlePermalink}#comments">
                        ${article.articleCommentCount}
                    </a>
                    <a rel="nofollow" data-ico="&#xe185;" href="${servePath}${article.articlePermalink}">
                        ${article.articleViewCount}
                    </a>
                    <a rel="nofollow" data-ico="&#x0060;" href="${servePath}/authors/${article.authorId}">
                        ${article.authorName}
                    </a>
                </div>
            </div>

            <h2 class="article-title">
                <#if article.hasUpdated>
                <span>
                    [${updatedLabel}]
                </span>
                </#if>
                <#if article.articlePutTop>
                <span>
                    [${topArticleLabel}]
                </span>
                </#if>
                <a rel="bookmark" title="${article.articleTitle}" href="${servePath}${article.articlePermalink}">
                    ${article.articleTitle}
                </a>
            </h2>
            <div class="article-body">
                ${article.articleAbstract}
            </div>
            <div data-ico="&#x003b;" title="${tagLabel}" class="article-tags">
                <#list article.articleTags?split(",") as articleTag>
                <a  rel="tag" href="${servePath}/tags/${articleTag?url('UTF-8')}">
                    ${articleTag}</a><#if articleTag_has_next>, </#if>
                </#list>
            </div>
        </div>
    </div>
    </#list>
</div>

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
    </#if>
</div>

