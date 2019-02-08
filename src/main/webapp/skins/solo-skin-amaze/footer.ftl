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
<footer class="footer blog-footer">
    <div class="blog-text-center">
        &copy; ${year}
        ${footerContent}
        <a href="${servePath}">${blogTitle}</a>  &nbsp;•&nbsp;
        <a href="https://solo.b3log.org" target="_blank">Solo</a> ${version} &nbsp;•&nbsp;
        Powered by <a href="https://b3log.org" target="_blank">B3log</a> 开源 &nbsp;
    </div>
</footer>
<div class="cd-top" onclick="Util.goTop()"></div>
<script>
	$(document).ready(function(){  
	    var p=0,t=0;  
	    
	    $(window).scroll(function(){  
            p = $(this).scrollTop(); 
            var b = $(".navbar-custom").height();
            //åå°é¡¶é¨
            if ($(window).scrollTop() > 150) {
    	        $(".cd-top").fadeIn(100);
    	    } else {
    	        $(".cd-top").fadeOut(100);
    	    }
            //å¯¼èªæ 
            if (t <= p) {//ä¸æ»  
            	$(".navbar-custom").removeClass("is-visible");
                if (p > b && !$(".navbar-custom").hasClass("is-fixed")) {
                    $(".navbar-custom").addClass("is-fixed")
                }
            } else{//ä¸æ»  
                if (p > 0 && $(".navbar-custom").hasClass("is-fixed")) {
                    $(".navbar-custom").addClass("is-visible")
                } else {
                    $(".navbar-custom").removeClass("is-visible is-fixed")
                }
            }  
            setTimeout(function(){t = p;},0);         
		});  
	});  
</script>

<script type="text/javascript" src="${staticServePath}/js/common${miniPostfix}.js?${staticResourceVersion}" charset="utf-8"></script>
<script type="text/javascript" src="${staticServePath}/skins/${skinDirName}/js/common.js?${staticResourceVersion}" charset="utf-8"></script>
<script src="https://cdn.jsdelivr.net/npm/stats.js"></script>
<script src="${staticServePath}/skins/${skinDirName}/js/UserDPlayer.js"></script>
<script type="text/javascript">
    var latkeConfig = {
        "servePath": "${servePath}",
        "staticServePath": "${staticServePath}",
        "isLoggedIn": "${isLoggedIn?string}",
        "userName": "${userName}"
    };

    var Label = {
        "skinDirName": "${skinDirName}",
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

    Util.parseMarkdown('content-reset');
</script>
${plugins}
