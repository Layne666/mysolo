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
<!DOCTYPE html>
<html>
<head>
    <@head title="${blogTitle}">
    <#if metaKeywords??>
    <meta name="keywords" content="${metaKeywords}"/>
    </#if>
    <#if metaDescription??>
    <meta name="description" content="${metaDescription}"/>
    </#if>
    </@head>
</head>
<body>
<#include "header.ftl">
<main class="main">
    <div class="wrapper">
        <div class="content">
        <#include "article-list.ftl">
        </div>
    <#include "side.ftl">
    </div>
</main>
<#include "footer.ftl">
</body>
</html>
