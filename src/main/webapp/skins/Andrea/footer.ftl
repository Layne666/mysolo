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
<div class="copyright">
    &copy; ${year} - <a href="${servePath}">${blogTitle}</a>${footerContent}<br/>
    Powered by <a href="https://b3log.org" target="_blank">B3log 开源</a> • <a href="https://solo.b3log.org" target="_blank">Solo</a> ${version}<br/>
    Theme by <a rel="friend" href="http://www.madeincima.eu/" target="_blank">Andrea</a> & <a rel="friend" href="http://vanessa.b3log.org" target="_blank">Vanessa</a>.
</div>
<script type="text/javascript" src="${staticServePath}/js/lib/jquery/jquery.min.js" charset="utf-8"></script>
<script type="text/javascript">
    var latkeConfig = {
        "servePath": "${servePath}",
        "staticServePath": "${staticServePath}",
        "isLoggedIn": "${isLoggedIn?string}"
    };
    
    var Label = {
        "markedAvailable": ${markedAvailable?c},
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
        "em14Label": "${em14Label}"
    };

    // init brush
    var buildBrush = function () {
        $("#brush").height(document.documentElement.scrollHeight - document.documentElement.clientHeight).css("background-position",
        parseInt((document.documentElement.scrollWidth - 910) / 2 - 56) + "px -150px");
    };

    // init
    $(document).ready(function () {
        Util.init();
        Util.replaceSideEm($("#naviComments li .side-comment"));
    
        // brush
        buildBrush();

        $(window).resize(function () {
            buildBrush();
        });

        // bg
        $("#changeBG a").click(function () {
            if (this.className !== 'selected') {
                switch (this.id) {
                    case "greyBG":
                        $("body").css("background-image", "url(/skins/${skinDirName}/images/bg-grey.jpg)");
                        break;
                    case "blueBG":
                        $("body").css("background-image", "url(/skins/${skinDirName}/images/bg-blue.jpg)");
                        break;
                    case "brownBG":
                        $("body").css("background-image", "url(/skins/${skinDirName}/images/bg-brown.jpg)");
                        break;
                }

                $("#changeBG a").removeClass();
                this.className = "selected";
            }
        });

        // page navi
        $(".side-tool li li a").hover(function () {
            if (parseInt($(this).css("padding-left")) === 9) {
                $(this).animate({
                    "padding-left": "54px"
                }, 600 );
            }
        }, function () {
            $(this).animate({
                "padding-left": "9px"
            }, 600 );
        });
    });
</script>
<script type="text/javascript" src="${staticServePath}/js/common${miniPostfix}.js?${staticResourceVersion}" charset="utf-8"></script>
${plugins}