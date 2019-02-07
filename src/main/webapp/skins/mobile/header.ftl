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
<!--TODO i18n-->
<!-- New noscript check, we need js on now folks -->
<noscript>
<div id="noscript-wrap">
    <div id="noscript">
        <h2>Notice</h2>
        <p>JavaScript for Mobile Safari is currently turned off.</p>
        <p>Turn it on in <em>Settings &rsaquo; Safari</em><br /> to view this website.</p>
    </div>
</div>
</noscript>
<!-- Prowl: if DM is sent, let's tell the user what happened -->
<!-- #start The Search Overlay -->
<#--
<div id="wptouch-search"> 
    <div id="wptouch-search-inner">
        <form target="_blank" id="searchform" action="http://zhannei.baidu.com/cse/site">
            <input id="search" placeholder="Search..." type="text" name="q" />
            <input type="submit" id="search-submit" value="" />
            <input type="hidden" name="cc" value="${serverHost}">
            <a href="javascript:void(0)"><img class="head-close" src="${staticServePath}/skins/${skinDirName}/themes/core/core-images/head-close.png" alt="close" /></a>
        </form>
    </div>
</div>
-->
<div id="wptouch-menu" class="dropper"> 		
    <div id="wptouch-menu-inner">
        <div id="menu-head">
            <div id="tabnav">
                <a href="#head-pages"><img src="${staticServePath}/skins/${skinDirName}/images/icon-pool/Pages.png" alt=""/></a>
                <a href="#head-tags"><img src="${staticServePath}/skins/${skinDirName}/images/icon-pool/Tags.png" alt=""/></a>
                <a href="#head-cats"><img src="${staticServePath}/skins/${skinDirName}/images/icon-pool/Archives.png" alt=""/></a>
                <a href="#head-category"><img src="${staticServePath}/skins/${skinDirName}/images/icon-pool/category.png" alt=""/></a>
            </div>

            <ul id="head-pages">
                <li id="admin" data-login="${isLoggedIn?string}"><a href="${servePath}/admin-index.do#main"><img src="${staticServePath}/skins/${skinDirName}/images/icon-pool/Home.png" alt=""/>Admin</a></li>
                <#list pageNavigations as page>
                <li><a href="${page.pagePermalink}" target="${page.pageOpenTarget}"><#if page.pageIcon != ''><img class="page-icon" src="${page.pageIcon}"></#if>${page.pageTitle}</a></li>
                </#list>           
                <li><a rel="alternate" href="${servePath}/rss.xml"><img src="${staticServePath}/skins/${skinDirName}/images/icon-pool/RSS.png" alt="" />RSS Feed</a></li>
                <li><a href="${servePath}/search?keyword=">Search</a></li>
            </ul>
            <ul id="head-tags">
                <#if 0 != mostUsedTags?size>
                <#list mostUsedTags as tag>
                <li><a href="${servePath}/tags/${tag.tagTitle?url('UTF-8')}">${tag.tagTitle} <span>(${tag.tagPublishedRefCount})</span></a></li>
                </#list>
                </#if>
            </ul>
            <ul id="head-cats">
                <#if 0 != archiveDates?size>
                <#list archiveDates as archiveDate>
                <li>
                    <#if "en" == localeString?substring(0, 2)>
                    <a href="${servePath}/archives/${archiveDate.archiveDateYear}/${archiveDate.archiveDateMonth}">
                        ${archiveDate.monthName} ${archiveDate.archiveDateYear} <span>(${archiveDate.archiveDatePublishedArticleCount})</span></a>
                    <#else>
                    <a href="${servePath}/archives/${archiveDate.archiveDateYear}/${archiveDate.archiveDateMonth}">
                        ${archiveDate.archiveDateYear} ${yearLabel} ${archiveDate.archiveDateMonth} ${monthLabel} <span>(${archiveDate.archiveDatePublishedArticleCount})</span></a>
                    </#if>
                </li>
                </#list>
                </#if>
            </ul>

            <ul id="head-category">
                <#if 0 != mostUsedCategories?size>
                    <#list mostUsedCategories as category>
                        <li>
                            <a href="${servePath}/category/${category.categoryURI}">
                                ${category.categoryTitle}</a>
                        </li>
                    </#list>
                </#if>
            </ul>

        </div>
    </div>
</div>

<div id="headerbar">
    <div id="headerbar-title">
        <!-- This fetches the admin selection logo icon for the header, which is also the bookmark icon -->
        <img id="logo-icon" src="${staticServePath}/skins/${skinDirName}/images/icon-pool/Apps.png" alt="aln" />
        <a rel="nofollow" href="${servePath}">${blogTitle}</a>
    </div>
    <div id="headerbar-menu">
        <a href="javascript:void(0)"></a>
    </div>
</div>


<div id="drop-fade">

    <#--<a id="searchopen" class="top" href="javascript:void(0)">${searchLabel}</a>-->
    <!-- #start the Prowl Message Area -->
    <div id="prowl-message" style="display:none">
        <div id="push-style-bar"></div><!-- filler to get the styling just right -->
        <img src="${staticServePath}/skins/${skinDirName}/themes/core/core-images/push-icon.png" alt="push icon" />
        <h4>Send a Message</h4>
        <p>This message will be pushed to the admin's iPhone instantly.</p><!--TODO instant msg-->

        <form id="prowl-direct-message" method="post" action="/blog/">
            <p>
                <input name="prowl-msg-name"  id="prowl-msg-name" type="text" />
                <label for="prowl-msg-name">Name</label>
            </p>

            <p>
                <input name="prowl-msg-email" id="prowl-msg-email" type="text" />
                <label for="prowl-msg-email">E-Mail</label>
            </p>

            <textarea name="prowl-msg-message"></textarea>
            <input type="hidden" name="wptouch-prowl-message" value="1" /> 
            <input type="hidden" name="_nonce" value="3690953c13" />			
            <input type="submit" name="prowl-submit" value="Send Now" id="prowl-submit" />
        </form>
        <div class="clearer"></div>
    </div>
</div>