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
<div class="footer">
    <div class="container fn-clear">
        <div class="left">
            <span>&copy; ${year}</span> - <a href="${servePath}">${blogTitle}</a>${footerContent}
            Powered by <a href="https://b3log.org" target="_blank">B3log 开源</a> • <a href="https://solo.b3log.org" target="_blank">Solo</a> ${version}&nbsp;&nbsp;
            Theme <a rel="friend" href="https://github.com/b3log/solo-skins" target="_blank">timeline</a>
            by <a rel="friend" target="_blank" href="http://themify.me/demo/themes/postline/">Postline</a> & <a rel="friend" href="http://vanessa.b3log.org" target="_blank">Vanessa</a>.
        </div>
        <div class="right fn-clear">
            <span class="left">
                <span>
                    ${viewCount1Label}
                    ${statistic.statisticBlogViewCount}
                    &nbsp;&nbsp;
                </span>
                <span>
                    ${articleCount1Label}
                    ${statistic.statisticPublishedBlogArticleCount}
                    &nbsp;&nbsp;
                </span>
                <span>
                    ${commentCount1Label}
                    ${statistic.statisticPublishedBlogCommentCount}
                </span>
            </span>
            <span class="ico-translate" onclick="timeline.translate()"></span>
        </div>
    </div>
</div>
<div class="ico-top none" onclick="Util.goTop()" title="TOP"></div>
<script type="text/javascript">
                var latkeConfig = {
                    "servePath": "${servePath}",
                    "isLoggedIn": "${isLoggedIn?string}",
                    "staticServePath": "${staticServePath}"
                };

                var Label = {
                    "markedAvailable": ${markedAvailable?c},
                    "tagLabel": "${tagLabel}",
                    "viewLabel": "${viewLabel}",
                    "commentLabel": "${commentLabel}",
                    "noCommentLabel": "${noCommentLabel}",
                    "topArticleLabel": "${topArticleLabel}",
                    "authorLabel": "${authorLabel}",
                    "updatedLabel": "${updatedLabel}",
                    "contentLabel": "${contentLabel}",
                    "abstractLabel": "${abstractLabel}",
                    "moreLabel": "${moreLabel}",
                    "adminLabel": "${adminLabel}",
                    "logoutLabel": "${logoutLabel}",
                    "skinDirName": "${skinDirName}",
                    "loginLabel": "${loginLabel}",
                    "em00Label": "${em00Label}",
                    "em01Label": "${em01Label}",
                    "em02Label": "${em02Label}",
                    "em03Label": "${em03Label}",
                    "em04Label": "${em04Label}",
                    "em05Label": "${em05Label}",
                    "em06Label": "${em06Label}",
                    "em07Label": "${em07Label}",
                    "em08Label": "${em08Label}",
                    "em09Label": "${em09Label}",
                    "em10Label": "${em10Label}",
                    "em11Label": "${em11Label}",
                    "em12Label": "${em12Label}",
                    "em13Label": "${em13Label}",
                    "em14Label": "${em14Label}",
                    "localeString": "${localeString}",
                    "yearLabel": "${yearLabel}",
                    "monthLabel": "${monthLabel}"
                };
</script>
<script type="text/javascript" src="${staticServePath}/js/lib/jquery/jquery.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${staticServePath}/js/common${miniPostfix}.js?${staticResourceVersion}" charset="utf-8"></script>
<script type="text/javascript" src="${staticServePath}/skins/${skinDirName}/js/${skinDirName}${miniPostfix}.js?${staticResourceVersion}" charset="utf-8"></script>
${plugins}
