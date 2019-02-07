<#--

    Solo - A beautiful, simple, stable, fast Java blogging system.
    Copyright (c) 2010-2018, b3log.org & hacpai.com

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
<!DOCTYPE html>
<html>
    <head>
        <@head title="${archiveDate.archiveDateMonth} ${archiveDate.archiveDateYear} (${archiveDate.archiveDatePublishedArticleCount}) - ${blogTitle}">
        <meta name="keywords" content="${metaKeywords},${archiveDate.archiveDateYear}${archiveDate.archiveDateMonth}"/>
        <meta name="description" content="<#list articles as article>${article.articleTitle}<#if article_has_next>,</#if></#list>"/>
        </@head>
    </head>
    <body>
    <div id="main">
        <#include "header.ftl">
	        <div class="am-g am-g-fixed blog-fixed">
	            <div class="am-u-lg-8 am-u-sm-12" id="main">
	                <main class="archives-articles">
	                    <div class="title">
	                        <h3 class="tip">
	                            <i class="icon-inbox"></i>
	                            &nbsp;
	                            <#if "en" == localeString?substring(0, 2)>
	                            ${archiveDate.archiveDateMonth} ${archiveDate.archiveDateYear}
	                            <#else>
	                                ${archiveDate.archiveDateYear} ${yearLabel} ${archiveDate.archiveDateMonth} ${monthLabel}
	                            </#if>
	                            - ${archiveDate.archiveDatePublishedArticleCount} ${articleLabel}
	                        </h3>
	                    </div>
	                    <#include "article-list.ftl">
	                </main>
	            </div>
	            <#include "side.ftl">
	        </div>
        <#include "footer.ftl">
        </div>
        <#include "audio.ftl">
    </body>
</html>
