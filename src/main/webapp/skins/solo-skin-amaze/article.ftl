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
<#include "macro-head.ftl">
<#include "macro-comments.ftl">
<#include "../../common-template/macro-comment_script.ftl">
<!DOCTYPE html>
<html>
    <head>
        <@head title="${article.articleTitle} - ${blogTitle}">
        <meta name="keywords" content="${article.articleTags}" />
        <meta name="description" content="${article.articleAbstract?html}" />
        </@head>
        <#if previousArticlePermalink??>
            <link rel="prev" title="${previousArticleTitle}" href="${servePath}${previousArticlePermalink}">
        </#if>
        <#if nextArticlePermalink??>
            <link rel="next" title="${nextArticleTitle}" href="${servePath}${nextArticlePermalink}">
        </#if>
            <!-- Open Graph -->
            <meta property="og:locale" content="zh_CN"/>
            <meta property="og:type" content="article"/>
            <meta property="og:title" content="${article.articleTitle}"/>
            <meta property="og:description" content="${article.articleAbstract?html}"/>
            <meta property="og:image" content="${article.authorThumbnailURL}"/>
            <meta property="og:url" content="${servePath}${article.articlePermalink}"/>
            <meta property="og:site_name" content="Solo"/>
            <!-- Twitter Card -->
            <meta name="twitter:card" content="summary"/>
            <meta name="twitter:description" content="${article.articleAbstract?html}"/>
            <meta name="twitter:title" content="${article.articleTitle}"/>
            <meta name="twitter:image" content="${article.authorThumbnailURL}"/>
            <meta name="twitter:url" content="${servePath}${article.articlePermalink}"/>
            <meta name="twitter:site" content="@DL88250"/>
            <meta name="twitter:creator" content="@DL88250"/>
    </head>
    <body>
    <div id="main">
        <nav class="navbar navbar-default navbar-custom navbar-fixed-top">
            <div class="container-fluid">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header page-scroll">
                    <button type="button" class="navbar-toggle">
                        <span class="sr-only"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a data-pjax class="navbar-brand" href="/">
                        ${blogTitle}
                    </a>
                </div>

                <div id="huxblog_navbar">
                    <div class="navbar-collapse">
                        <ul class="nav navbar-nav navbar-right">
                        <#list pageNavigations as page>
                        <li>
                            <a data-pjax href="${page.pagePermalink}" target="${page.pageOpenTarget}" rel="section">
                                ${page.pageTitle}
                            </a>
                        </li>
                        </#list>
                        <li>
                            <a data-pjax href="${servePath}/tags.html" rel="section">
                                ${allTagsLabel}
                            </a>
                        </li>
                        <li>
                            <a data-pjax href="${servePath}/archives.html">
                                ${archiveLabel}
                            </a>
                        </li>
                        <li>
                            <a data-pjax rel="archive" href="${servePath}/links.html">
                                ${linkLabel}
                            </a>
                        </li>
                        <#if isLoggedIn>
                        <li>
                            <a href="${servePath}/admin-index.do#main" title="${adminLabel}">
                                ${adminLabel}
                            </a>
                            </li>
                            <li>
                            <a href="${logoutURL}">
                                ${logoutLabel}
                            </a>
                            </li>
                            <#else>
                            <li>
                            <a href="${loginURL}">
                                ${loginLabel}
                            </a>
                            </li>
                            <li>
                            <a href="${servePath}/register">
                                ${registerLabel}
                            </a>
                            </li>
                        </#if>
                        </ul>
                    </div>
                </div>
                <!-- /.navbar-collapse -->
            </div>
        <!-- /.container -->
        </nav>
	        <header class="intro-header" style="background-image: url('/skins/${skinDirName}/images/header.jpg')">
	            <div class="container">
	                <div class="row">
	                <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
	                    <div class="post-heading">
	                        <h1>${article.articleTitle}</h1>
	                        <span class="meta">
	                            @${article.authorName} &nbsp;
	                            ${article.articleCreateDate?string("yyyy-MM-dd")} &nbsp;
	                            <div class="comments-view" style="display: inline-block">
	                                <a href="${servePath}${article.articlePermalink}#comments" class="article-comments">${article.articleCommentCount} ${commentLabel}</a> &nbsp;
	                                ${article.articleViewCount} ${viewLabel}
	                            </div>
	                            </span>
	                        <div class="tags post-tags">
	                            <#list article.articleTags?split(",") as articleTag>
	                                <a data-pjax class="tag" rel="tag" href="${servePath}/tags/${articleTag?url('UTF-8')}">
	                                    ${articleTag}</a>&nbsp;
	                            </#list>
	                        </div>
	                    </div>
	                </div>
	            </div>
	        </header>
	        <div class="container">
	            <div class="am-g am-g-fixed blog-fixed">
	                <div class="am-u-lg-12 am-u-sm-12">
	                    <article class="am-article blog-article-p article-trigger">
	                        <div id="post-content" class="am-article-bd article-body">
	                            ${article.articleContent}
	                            <#if "" != article.articleSign.signHTML?trim>
	                                <div>
	                                    ${article.articleSign.signHTML}
	                                </div>
	                            </#if>
	                        </div>
	                    </article>
	                    <hr>
	                </div>
	            </div>
		       
	            <div class="comment-container">
	                <div class="comments-container-inner">
	                    <@comments commentList=articleComments article=article></@comments>
	                </div>
	            </div>
	        </div>
	        <div id="directory-content" class="directory-content">
        		<div id="directory"></div>
    		</div>
        <#include "footer.ftl">
        <@comment_script oId=article.oId commentable=article.commentable>
        page.tips.externalRelevantArticlesDisplayCount = "${externalRelevantArticlesDisplayCount}";
        <#if 0 != randomArticlesDisplayCount>
        page.loadRandomArticles();
        </#if>
        <#if 0 != externalRelevantArticlesDisplayCount>
        page.loadExternalRelevantArticles("<#list article.articleTags?split(",") as articleTag>${articleTag}<#if articleTag_has_next>,</#if></#list>"
            , "<header class='title'><h2>${externalRelevantArticlesLabel}</h2></header>");
        </#if>
        <#if 0 != relevantArticlesDisplayCount>
        page.loadRelevantArticles('${article.oId}', '<h4>${relevantArticlesLabel}</h4>');
        </#if>
        </@comment_script>   
        </div>
        <#include "audio.ftl">
    </body>
    <script>
        $(function() {
            $('article h1, article h2, article h3, article h4, article h5').find('a').removeAttr('target')
        })
    </script>
    <script src="${staticServePath}/skins/${skinDirName}/js/jquery.pjax.js"></script>
    <script>
		$(document).pjax('[data-pjax] a, a[data-pjax]', '#main',{
			fragment:'#main'
		});
		/* $(document).on('pjax:complete',function(){
			$.getScript("${staticServePath}/skins/${skinDirName}/js/UserDPlayer.js");
		}); */
	</script>
	<script>
	!function(a,b){"use strict";"function"==typeof define&&define.amd?define([],b):"object"==typeof exports?module.exports=b():a.Headroom=b()}(this,function(){"use strict";function a(a){this.callback=a,this.ticking=!1}function b(a){return a&&"undefined"!=typeof window&&(a===window||a.nodeType)}function c(a){if(arguments.length<=0)throw new Error("Missing arguments in extend function");var d,e,f=a||{};for(e=1;e<arguments.length;e++){var g=arguments[e]||{};for(d in g)"object"!=typeof f[d]||b(f[d])?f[d]=f[d]||g[d]:f[d]=c(f[d],g[d])}return f}function d(a){return a===Object(a)?a:{down:a,up:a}}function e(a,b){b=c(b,e.options),this.lastKnownScrollY=0,this.elem=a,this.tolerance=d(b.tolerance),this.classes=b.classes,this.offset=b.offset,this.scroller=b.scroller,this.initialised=!1,this.onPin=b.onPin,this.onUnpin=b.onUnpin,this.onTop=b.onTop,this.onNotTop=b.onNotTop,this.onBottom=b.onBottom,this.onNotBottom=b.onNotBottom}var f={bind:!!function(){}.bind,classList:"classList"in document.documentElement,rAF:!!(window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame)};return window.requestAnimationFrame=window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame,a.prototype={constructor:a,update:function(){this.callback&&this.callback(),this.ticking=!1},requestTick:function(){this.ticking||(requestAnimationFrame(this.rafCallback||(this.rafCallback=this.update.bind(this))),this.ticking=!0)},handleEvent:function(){this.requestTick()}},e.prototype={constructor:e,init:function(){if(e.cutsTheMustard)return this.debouncer=new a(this.update.bind(this)),this.elem.classList.add(this.classes.initial),setTimeout(this.attachEvent.bind(this),100),this},destroy:function(){var a=this.classes;this.initialised=!1;for(var b in a)a.hasOwnProperty(b)&&this.elem.classList.remove(a[b]);this.scroller.removeEventListener("scroll",this.debouncer,!1)},attachEvent:function(){this.initialised||(this.lastKnownScrollY=this.getScrollY(),this.initialised=!0,this.scroller.addEventListener("scroll",this.debouncer,!1),this.debouncer.handleEvent())},unpin:function(){var a=this.elem.classList,b=this.classes;!a.contains(b.pinned)&&a.contains(b.unpinned)||(a.add(b.unpinned),a.remove(b.pinned),this.onUnpin&&this.onUnpin.call(this))},pin:function(){var a=this.elem.classList,b=this.classes;a.contains(b.unpinned)&&(a.remove(b.unpinned),a.add(b.pinned),this.onPin&&this.onPin.call(this))},top:function(){var a=this.elem.classList,b=this.classes;a.contains(b.top)||(a.add(b.top),a.remove(b.notTop),this.onTop&&this.onTop.call(this))},notTop:function(){var a=this.elem.classList,b=this.classes;a.contains(b.notTop)||(a.add(b.notTop),a.remove(b.top),this.onNotTop&&this.onNotTop.call(this))},bottom:function(){var a=this.elem.classList,b=this.classes;a.contains(b.bottom)||(a.add(b.bottom),a.remove(b.notBottom),this.onBottom&&this.onBottom.call(this))},notBottom:function(){var a=this.elem.classList,b=this.classes;a.contains(b.notBottom)||(a.add(b.notBottom),a.remove(b.bottom),this.onNotBottom&&this.onNotBottom.call(this))},getScrollY:function(){return void 0!==this.scroller.pageYOffset?this.scroller.pageYOffset:void 0!==this.scroller.scrollTop?this.scroller.scrollTop:(document.documentElement||document.body.parentNode||document.body).scrollTop},getViewportHeight:function(){return window.innerHeight||document.documentElement.clientHeight||document.body.clientHeight},getElementPhysicalHeight:function(a){return Math.max(a.offsetHeight,a.clientHeight)},getScrollerPhysicalHeight:function(){return this.scroller===window||this.scroller===document.body?this.getViewportHeight():this.getElementPhysicalHeight(this.scroller)},getDocumentHeight:function(){var a=document.body,b=document.documentElement;return Math.max(a.scrollHeight,b.scrollHeight,a.offsetHeight,b.offsetHeight,a.clientHeight,b.clientHeight)},getElementHeight:function(a){return Math.max(a.scrollHeight,a.offsetHeight,a.clientHeight)},getScrollerHeight:function(){return this.scroller===window||this.scroller===document.body?this.getDocumentHeight():this.getElementHeight(this.scroller)},isOutOfBounds:function(a){var b=a<0,c=a+this.getScrollerPhysicalHeight()>this.getScrollerHeight();return b||c},toleranceExceeded:function(a,b){return Math.abs(a-this.lastKnownScrollY)>=this.tolerance[b]},shouldUnpin:function(a,b){var c=a>this.lastKnownScrollY,d=a>=this.offset;return c&&d&&b},shouldPin:function(a,b){var c=a<this.lastKnownScrollY,d=a<=this.offset;return c&&b||d},update:function(){var a=this.getScrollY(),b=a>this.lastKnownScrollY?"down":"up",c=this.toleranceExceeded(a,b);this.isOutOfBounds(a)||(a<=this.offset?this.top():this.notTop(),a+this.getViewportHeight()>=this.getScrollerHeight()?this.bottom():this.notBottom(),this.shouldUnpin(a,c)?this.unpin():this.shouldPin(a,c)&&this.pin(),this.lastKnownScrollY=a)}},e.options={tolerance:{up:0,down:0},offset:0,scroller:window,classes:{pinned:"headroom--pinned",unpinned:"headroom--unpinned",top:"headroom--top",notTop:"headroom--not-top",bottom:"headroom--bottom",notBottom:"headroom--not-bottom",initial:"headroom"}},e.cutsTheMustard="undefined"!=typeof f&&f.rAF&&f.bind&&f.classList,e});
	</script>
	<script>
	var postDirectoryBuild=function(){var b=function a(l,h){var k=[],e=typeof h==="object",g=typeof h==="string",j,f,d;for(f=0,d=l.length;f<d;f++){j=l[f];if((j.nodeType===1||j.nodeType===9)&&(!h||e&&h.test(j.tagName.toLowerCase())||g&&j.tagName.toLowerCase()===h)){k.push(j)}}return k},c=function(e,f,r){var n=[],m=[],h,p,d,j,l,s,o,g,k,q;h=(function(i,C,A){var w=b(i.childNodes,/^h\d$/),x=[],v=1,B=1,z=0,D=1,t="directory"+(Math.random()+"").replace(/\D/,""),B,y,u;while(w.length){u=w.shift();C.push(u.innerHTML);y=+u.tagName.match(/\d/)[0];if(y>v){x.push(1);B+=1}else{if(y===B||y>B&&y<=v){x.push(0);B=B}else{if(y<B){x.push(y-B);B=y}}}z+=x[x.length-1];v=y;u.id=u.id||(t+D++);A.push(u.id)}if(z!==0&&x[0]===1){x[0]=0}return x})(e,n,m);j=p=document.createElement("ul");q=document.createElement("span");q.style="color:#F38181;font-weight:600;";q.innerHTML="目录";p.appendChild(q);dirNum=[];for(g=0,k=h.length;g<k;g++){d=h[g];if(d===1){l=document.createElement("ul");if(!j.lastElementChild){j.appendChild(document.createElement("li"))}j.lastElementChild.appendChild(l);j=l;dirNum.push(0)}else{if(d<0){d*=2;while(d++){if(d%2){dirNum.pop()}j=j.parentNode}}}dirNum[dirNum.length-1]++;s=document.createElement("li");o=document.createElement("a");o.href="#"+m[g];o.innerHTML=!r?n[g]:dirNum.join(".")+" "+n[g];s.appendChild(o);j.appendChild(s)}f.appendChild(p)};c(document.getElementById("post-content"),document.getElementById("directory"),false)};postDirectoryBuild();
	</script>
	<script>
	    var postDirectory = new Headroom(document.getElementById("directory-content"), {
	    tolerance: 5,
	    offset: 240,
	    classes: {
	        initial: "initial",
	        pinned: "pinned",
	        unpinned: "unpinned"
	    }
	});
	postDirectory.init();
	</script>
</html>
