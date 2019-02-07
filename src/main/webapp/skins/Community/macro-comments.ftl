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
<#macro comments commentList article>
<div id="comments">
    <#list commentList as comment>
    <#include "common-comment.ftl"/>
    </#list>
</div>
<#if article.commentable>
<div class="comment-title">
    ${postCommentsLabel}
</div>
<table id="commentForm" class="comment" cellpadding="0" cellspacing="0" width="100%">
    <tbody>
        <#if !isLoggedIn>
        <tr>
            <th width="200px">
                <span class="comment-label">
                    ${commentNameLabel}
                </span>
                <span class="arrow-right"></span>
            </th>
            <td>
                <input type="text" id="commentName"/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="comment-label">
                    ${commentEmailLabel}
                </span>
                <span class="arrow-right"></span>
            </th>
            <td>
                <input type="text" id="commentEmail"/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="comment-label">
                    ${commentURLLabel}
                </span>
                <span class="arrow-right"></span>
            </th>
            <td>
                <input type="text" id="commentURL"/>
            </td>
        </tr>
        </#if>
        <tr>
            <th>
                <span class="comment-label">
                    ${commentEmotionsLabel}
                </span>
                <span class="arrow-right"></span>
            </th>
            <td id="emotions">
                <span class="em00" title="${em00Label}"></span>
                <span class="em01" title="${em01Label}"></span>
                <span class="em02" title="${em02Label}"></span>
                <span class="em03" title="${em03Label}"></span>
                <span class="em04" title="${em04Label}"></span>
                <span class="em05" title="${em05Label}"></span>
                <span class="em06" title="${em06Label}"></span>
                <span class="em07" title="${em07Label}"></span>
                <span class="em08" title="${em08Label}"></span>
                <span class="em09" title="${em09Label}"></span>
                <span class="em10" title="${em10Label}"></span>
                <span class="em11" title="${em11Label}"></span>
                <span class="em12" title="${em12Label}"></span>
                <span class="em13" title="${em13Label}"></span>
                <span class="em14" title="${em14Label}"></span>
            </td>
        </tr>
        <tr>
            <th valign="top">
                <span class="comment-label">
                    ${commentContentLabel}
                </span>
                <span class="arrow-right"></span>
            </th>
            <td>
                <textarea rows="10" id="comment"></textarea>
            </td>
        </tr>
        <#if !isLoggedIn>
        <tr>
            <th>
                <span class="comment-label">
                    ${captchaLabel}
                </span>
                <span class="arrow-right"></span>
            </th>
            <td>
                <input type="text" id="commentValidate"/>
                <img id="captcha" alt="validate" src="${servePath}/captcha" />
            </td>
        </tr>
        </#if>
        <tr>
            <td colspan="2">
                <span style="margin-top: 20px;" class="right error-msg" id="commentErrorTip"></span>
                <input id="submitCommentButton" type="button" onclick="page.submitComment();" value="${submmitCommentLabel}"/>
            </td>
        </tr>
    </tbody>
</table>
</#if>
</#macro>

<#macro comment_script oId>
<script type="text/javascript" src="${staticServePath}/js/page${miniPostfix}.js?${staticResourceVersion}" charset="utf-8"></script>
<script type="text/javascript">
    var page = new Page({
        "nameTooLongLabel": "${nameTooLongLabel}",
        "mailCannotEmptyLabel": "${mailCannotEmptyLabel}",
        "mailInvalidLabel": "${mailInvalidLabel}",
        "commentContentCannotEmptyLabel": "${commentContentCannotEmptyLabel}",
        "captchaCannotEmptyLabel": "${captchaCannotEmptyLabel}",
        "captchaErrorLabel": "${captchaErrorLabel}",
        "loadingLabel": "${loadingLabel}",
        "oId": "${oId}",
        "skinDirName": "${skinDirName}",
        "blogHost": "${blogHost}",
        "randomArticles1Label": "${randomArticles1Label}",
        "externalRelevantArticles1Label": "${externalRelevantArticles1Label}"
    });
    var replyTo = function (id) {
        var commentFormHTML = "<table width='100%' cellspacing='0' cellpadding='0' class='comment' id='replyForm'>";
        page.addReplyForm(id, commentFormHTML);
    };

    (function () {
        page.load();
        page.replaceCommentsEm("#comments .comment-content");
            <#nested>
        })();
</script>
</#macro>