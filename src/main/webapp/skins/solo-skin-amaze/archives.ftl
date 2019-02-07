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
        <@head title="${blogTitle}">
        <meta name="keywords" content="${metaKeywords},${archiveLabel}"/>
        <meta name="description" content="${metaDescription},${archiveLabel}"/>
        </@head>
    </head>
    <body>
    <div id="main">
       <#include "header.ftl">
	       <div class="am-g am-g-fixed blog-fixed">
	            <div class="am-u-lg-8 am-u-sm-12" id="main">
	                <main class="archives-articles">
	                    <span class="title">
	                         <h3><i class="icon-inbox"></i>
	                             &nbsp;${statistic.statisticPublishedBlogArticleCount} ${articleLabel}</h3>
	                    </span>
	                    <#if 0 != archiveDates?size>
	                        <ul class="archives-list list">
	                        <#list archiveDates as archiveDate>
	                            <li class="archives-list-item">
	                                <#if "en" == localeString?substring(0, 2)>
	                                    <a data-pjax class="post-title" href="${servePath}/archives/${archiveDate.archiveDateYear}/${archiveDate.archiveDateMonth}">
	                                        ${archiveDate.monthName} ${archiveDate.archiveDateYear}(${archiveDate.archiveDatePublishedArticleCount})
	                                    </a>
	                                <#else>
	                                    <a data-pjax class="post-title" href="${servePath}/archives/${archiveDate.archiveDateYear}/${archiveDate.archiveDateMonth}">
	                                        ${archiveDate.archiveDateYear} ${yearLabel} ${archiveDate.archiveDateMonth} ${monthLabel}(${archiveDate.archiveDatePublishedArticleCount})
	                                    </a>
	                                </#if>
	                            </li>
	                        </#list>
	                        </ul>
	                    </#if>
	                </main>
	            </div>
	            <#include "side.ftl">
	        </div>
        <#include "footer.ftl">
       </div>
        <#include "audio.ftl">
    </body>
</html>