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
<li id="${comment.oId}" class="fn-clear">
    <div class="fn-left avatar-warp">
        <img class="avatar-48" title="${comment.commentName}" src="${comment.commentThumbnailURL}">
    </div>
    <div class="fn-left" style="width: 90%">
        <div class="fn-clear post-meta">
                <span class="fn-left">
                    <#if "http://" == comment.commentURL>
                        <a>${comment.commentName}</a>
                    <#else>
                        <a href="${comment.commentURL}" target="_blank">${comment.commentName}</a>
                    </#if>
                    <#if comment.isReply>
                        @
                    <a href="${servePath}${article.permalink}#${comment.commentOriginalCommentId}"
                       onmouseover="page.showComment(this, '${comment.commentOriginalCommentId}', 23);"
                       onmouseout="page.hideComment('${comment.commentOriginalCommentId}')"
                    >${comment.commentOriginalCommentName}</a>
                    </#if>
                        <time>${comment.commentDate2?string("yyyy-MM-dd HH:mm")}</time>
                </span>
        <#if article.commentable>
            <a class="fn-right" href="javascript:replyTo('${comment.oId}')">${replyLabel}</a>
        </#if>
        </div>
        <div class="comment-content post-content">
        ${comment.commentContent}
        </div>
    </div>
</li>