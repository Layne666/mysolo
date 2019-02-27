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
<footer class="footer">
    <div class="fn-clear">
        <div class="fn-left">
            Theme <a rel="friend" href="https://github.com/b3log/solo-skins" target="_blank">yilia</a>
            by
            <a rel="friend" href="http://litten.me/" target="_blank">Litten</a> &
            <a rel="friend" href="http://vanessa.b3log.org" target="_blank">Vanessa</a>
        </div>
        <span class="fn-right">
            ${viewCount1Label}${statistic.statisticBlogViewCount}
            &nbsp;
            ${articleCount1Label}${statistic.statisticPublishedBlogArticleCount}
            &nbsp; 
            ${commentCount1Label}${statistic.statisticPublishedBlogCommentCount}
            &nbsp; 
            ${onlineVisitor1Label}${onlineVisitorCnt}
        </span>
    </div>
    <div class="fn-clear">
        <a href="${servePath}">${blogTitle}</a> 
        &copy; ${year}
        ${footerContent}
        <span class="fn-right">
            Powered by <a href="https://b3log.org" target="_blank">B3log 开源</a> •
            <a href="https://solo.b3log.org" target="_blank">Solo</a> ${version}
        </span>
    </div>
    <span onclick="Util.goTop()" class="icon-goup"></span>
</footer>


<script type="text/javascript" src="${staticServePath}/js/lib/jquery/jquery.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${staticServePath}/js/common${miniPostfix}.js?${staticResourceVersion}" charset="utf-8"></script>
<script type="text/javascript" src="${staticServePath}/skins/${skinDirName}/js/${skinDirName}${miniPostfix}.js?${staticResourceVersion}" charset="utf-8"></script>
<script type="text/javascript">
    var latkeConfig = {
        "servePath": "${servePath}",
        "staticServePath": "${staticServePath}",
        "isLoggedIn": "${isLoggedIn?string}",
        "userName": "${userName}"
    };

    var Label = {
        "markedAvailable": ${markedAvailable?c},
        "skinDirName": "${skinDirName}"
    };
    Util.initSW();
    Util.parseMarkdown();
    Util.killIE(8)
</script>
${plugins}
