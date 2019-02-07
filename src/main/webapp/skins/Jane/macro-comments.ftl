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
<div class="comments__title">
    ${commentLabel}
</div>

<ul id="comments">
    <#list commentList as comment>
        <#include 'common-comment.ftl'/>
    </#list>
</ul>

<#if article.commentable>
<table id="commentForm" class="form">
    <tbody>
    <#if !isLoggedIn>
    <tr>
        <td>
            <input placeholder="${commentNameLabel}" type="text" id="commentName"/>
        </td>
    </tr>
    <tr>
        <td>
            <input placeholder="${commentEmailLabel}" type="email" id="commentEmail"/>
        </td>
    </tr>
    <tr>
        <td>
            <input placeholder="${commentURLLabel}" type="url" id="commentURL"/>
        </td>
    </tr>
    </#if>
    <tr>
        <td id="emotions" class="emotions">
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
        <td>
            <textarea rows="5" cols="96" id="comment"></textarea>
        </td>
    </tr>
    <#if !isLoggedIn>
    <tr>
        <td>
            <input style="width:50%" placeholder="${captchaLabel}" type="text"
                   id="commentValidate"/>
            <img class="comments__captcha" id="captcha" alt="validate" src="${servePath}/captcha"/>
        </td>
    </tr>
    </#if>
    <tr>
        <td colspan="2" align="right">
            <span class="ft__red" id="commentErrorTip"></span>
            <button class="fn__none" id="cancelCommentButton" onclick="$('#replyForm').remove();page.currentCommentId = ''">${cancelLabel}</button>
            <button id="submitCommentButton" onclick="page.submitComment();">${submmitCommentLabel}</button>
        </td>
    </tr>
    </tbody>
</table>
</#if>
</#macro>

<#macro comment_script oId>
<script type="text/javascript" src="${staticServePath}/js/page${miniPostfix}.js?${staticResourceVersion}"
        charset="utf-8"></script>
<script type="text/javascript">
    var page = new Page({
        'nameTooLongLabel': "${nameTooLongLabel}",
        'mailCannotEmptyLabel': "${mailCannotEmptyLabel}",
        'mailInvalidLabel': "${mailInvalidLabel}",
        'commentContentCannotEmptyLabel': "${commentContentCannotEmptyLabel}",
        'captchaCannotEmptyLabel': "${captchaCannotEmptyLabel}",
        'loadingLabel': "${loadingLabel}",
        'oId': "${oId}",
        'skinDirName': "${skinDirName}",
        'blogHost': "${blogHost}",
        'randomArticles1Label': "${randomArticles1Label}",
        'externalRelevantArticles1Label': "${externalRelevantArticles1Label}",
    })
    var replyTo = function (id) {
        var commentFormHTML = '<table class=\'form comments__reply comments__content\' id=\'replyForm\'>'
        page.addReplyForm(id, commentFormHTML)
    };
    $(document).ready(function () {
        page.load({
            language: {
                theme: 'tomorrow-night-eighties'
            }
        })
        // emotions
        page.replaceCommentsEm('#comments .content-reset')
        <#nested>
        Skin.initToc()
    })
</script>
</#macro>