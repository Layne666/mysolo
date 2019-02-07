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
        <@head title="${allTagsLabel} - ${blogTitle}">
        <meta name="keywords" content="${metaKeywords},${allTagsLabel}"/>
        <meta name="description" content="<#list tags as tag>${tag.tagTitle}<#if tag_has_next>,</#if></#list>"/>
        </@head>
    </head>
    <body>
    <div id="main">
        <#include "header.ftl">
	        <div class="am-g am-g-fixed blog-fixed">
	            <div class="am-u-lg-8 am-u-sm-12" id="main">
	                <main class="tags-list">
	                    <div class="title">
	                        <h3>${sumLabel} ${tags?size} ${tagLabel}</h3>
	                    </div>
	                    <div class="tags">
	                        <#list tags as tag>
	                            <a data-pjax rel="tag" data-count="${tag.tagPublishedRefCount}" class="tag"
	                               href="${servePath}/tags/${tag.tagTitle?url('UTF-8')}">
	                                <span>${tag.tagTitle}</span>
	                                (<b>${tag.tagPublishedRefCount}</b>)
	                            </a>
	                        </#list>
	                    </div>
	                </main>
	            </div>
	            <#include "side.ftl">
	        </div>
        <#include "footer.ftl">
        </div>
        <#include "audio.ftl">
    </body>
</html>