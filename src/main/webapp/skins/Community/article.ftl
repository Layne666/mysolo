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
<#include "macro-comments.ftl">
<!DOCTYPE html>
<html>
    <head>
        <@head title="${article.articleTitle} - ${blogTitle}">
        <meta name="keywords" content="${article.articleTags}" />
        <meta name="description" content="${article.articleAbstract?html}" />
        </@head>
        <#if previousArticlePermalink??>
            <link rel="prev" title="${previousArticleTitle}" href="${servePath}${previousArticlePermalink}">
        </#if>
        <#if nextArticlePermalink??>
            <link rel="next" title="${nextArticleTitle}" href="${servePath}${nextArticlePermalink}">
        </#if>
            <!-- Open Graph -->
            <meta property="og:locale" content="zh_CN"/>
            <meta property="og:type" content="article"/>
            <meta property="og:title" content="${article.articleTitle}"/>
            <meta property="og:description" content="${article.articleAbstract?html}"/>
            <meta property="og:image" content="${article.authorThumbnailURL}"/>
            <meta property="og:url" content="${servePath}${article.articlePermalink}"/>
            <meta property="og:site_name" content="Solo"/>
            <!-- Twitter Card -->
            <meta name="twitter:card" content="summary"/>
            <meta name="twitter:description" content="${article.articleAbstract?html}"/>
            <meta name="twitter:title" content="${article.articleTitle}"/>
            <meta name="twitter:image" content="${article.authorThumbnailURL}"/>
            <meta name="twitter:url" content="${servePath}${article.articlePermalink}"/>
            <meta name="twitter:site" content="@DL88250"/>
            <meta name="twitter:creator" content="@DL88250"/>
    </head>
    <body>
        ${topBarReplacement}
        <div class="header">
            <#include "header.ftl">
        </div>
        <div class="content">
            <div class="marginBottom40">
                <div class="article-header">
                    <div class="article-date">
                        <#if article.hasUpdated>
                        ${article.articleUpdateDate?string("yyyy-MM-dd HH")}
                        <#else>
                        ${article.articleCreateDate?string("yyyy-MM-dd HH")}
                        </#if>
                    </div>
                    <div class="arrow-right"></div>
                    <div class="clear"></div>
                    <ul>
                        <li>
                            <span class="left">
                                by&nbsp;
                            </span>
                            <a rel="nofollow" class="left" title="${article.authorName}" href="${servePath}/authors/${article.authorId}">
                                ${article.authorName}
                            </a>
                            <span class="clear"></span>
                        </li>
                        <li>
                            <a rel="nofollow" href="${servePath}${article.articlePermalink}" title="${viewLabel}">
                                ${viewLabel} (${article.articleViewCount})
                            </a>
                        </li>
                        <li>
                            <a rel="nofollow" title="${commentLabel}" href="${servePath}${article.articlePermalink}#comments">
                                ${commentLabel} (${article.articleCommentCount})
                            </a>
                        </li>
                    </ul>
                </div>
                <div class="article-main article-detail-body">
                    <h2 class="title">
                        <a href="${servePath}${article.articlePermalink}">${article.articleTitle}</a>
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
                    <div class="article-body">
                        ${article.articleContent}
                        <#if "" != article.articleSign.signHTML?trim>
                        <div class="marginTop12">
                            ${article.articleSign.signHTML}
                        </div>
                        </#if>
                    </div>
                    <div class="tags">
                        <span class="tag-icon" title="${tagsLabel}"></span>
                        ${tags1Label}
                        <#list article.articleTags?split(",") as articleTag>
                        <a rel="tag" href="${servePath}/tags/${articleTag?url('UTF-8')}">
                            ${articleTag}</a><#if articleTag_has_next>,</#if>
                        </#list>
                    </div>
                </div>
                <div class="clear"></div>
                <div class="article-detail-footer">
                    <#if nextArticlePermalink??>
                    <a href="${servePath}${nextArticlePermalink}" class="left">${nextArticle1Label} ${nextArticleTitle}</a>
                    </#if>
                    <#if previousArticlePermalink??>
                    <a href="${servePath}${previousArticlePermalink}" class="right">${previousArticle1Label} ${previousArticleTitle}</a>
                    </#if>
                    <div class="clear"></div>
                    <div id="randomArticles" class="left article-relative"></div>
                    <div id="relevantArticles" class="article-relative left" style="width: 48%;"></div>
                    <div class="clear"></div>
                    <div id="externalRelevantArticles" class="article-relative"></div>
                </div>
            </div>
            <@comments commentList=articleComments article=article></@comments>
        </div>
        <div>
            <#include "side.ftl">
        </div>
        <div class="footer">
            <#include "footer.ftl">
        </div>
        <@comment_script oId=article.oId>
        page.tips.externalRelevantArticlesDisplayCount = "${externalRelevantArticlesDisplayCount}";
        <#if 0 != randomArticlesDisplayCount>
        page.loadRandomArticles();
        </#if>
        <#if 0 != relevantArticlesDisplayCount>
        page.loadRelevantArticles('${article.oId}', '<h4>${relevantArticles1Label}</h4>');
        </#if>
        <#if 0 != externalRelevantArticlesDisplayCount>
        page.loadExternalRelevantArticles("<#list article.articleTags?split(",") as articleTag>${articleTag}<#if articleTag_has_next>,</#if></#list>");
        </#if>
        </@comment_script>    
    </body>
</html>
