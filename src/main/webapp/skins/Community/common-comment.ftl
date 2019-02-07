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
<div id="${comment.oId}">
    <img class="left" alt="${comment.commentName}" src="${comment.commentThumbnailURL}"/>
    <div class="comment-panel left">
        <div class="comment-top">
        <#if "http://" == comment.commentURL>
            <a>${comment.commentName}</a>
        <#else>
            <a href="${comment.commentURL}" target="_blank">${comment.commentName}</a>
        </#if>
        <#if comment.isReply>
            @
            <a href="${servePath}${article.permalink}#${comment.commentOriginalCommentId}"
               onmouseover="page.showComment(this, '${comment.commentOriginalCommentId}', 11);"
               onmouseout="page.hideComment('${comment.commentOriginalCommentId}')">
            ${comment.commentOriginalCommentName}</a>
        </#if>
        ${comment.commentDate2?string("yyyy-MM-dd HH:mm:ss")}
        </div>
        <div class="comment-content article-body">
        ${comment.commentContent}
        </div>
        <div class="clear"></div>
    <#if article.commentable>
        <div class="reply">
            <a rel="nofollow" href="javascript:replyTo('${comment.oId}');">${replyLabel}</a>
        </div>
    </#if>
    </div>
    <div class="clear"></div>
</div>