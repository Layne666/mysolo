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
<div id="${comment.oId}" class="comment-body">
    <div class="comment-panel">
        <div class="left comment-author">
            <div>
                <img alt="${comment.commentName}" src="${comment.commentThumbnailURL}"/>
            </div>
        <#if "http://" == comment.commentURL>
            <a class="left" title="${comment.commentName}">${comment.commentName}</a>
        <#else>
            <a href="${comment.commentURL}" target="_blank" title="${comment.commentName}">${comment.commentName}</a>
        </#if>
        </div>
        <div class="left comment-info">
            <div class="left">
            ${comment.commentDate2?string("yyyy-MM-dd HH:mm:ss")}
            <#if comment.isReply>
                @
                <a href="${servePath}${article.permalink}#${comment.commentOriginalCommentId}"
                   onmouseover="page.showComment(this, '${comment.commentOriginalCommentId}', 3);"
                   onmouseout="page.hideComment('${comment.commentOriginalCommentId}')">${comment.commentOriginalCommentName}</a>
            </#if>
            </div>
        <#if article.commentable>
            <div class="right">
                <a rel="nofollow" class="no-underline"
                   href="javascript:replyTo('${comment.oId}');">${replyLabel}</a>
            </div>
        </#if>
            <div class="clear">
            </div>
            <div class="comment-content article-body">
            ${comment.commentContent}
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>