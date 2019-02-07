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
<#macro head title>
<meta charset="utf-8" />
<title>${title}</title>
<#nested>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="author" content="${blogTitle?html}" />
<meta name="generator" content="Solo" />
<meta name="owner" content="B3log Team" />
<meta name="revised" content="${blogTitle?html}, ${year}" />
<meta name="copyright" content="B3log" />
<meta http-equiv="Window-target" content="_top" />
<link type="text/css" rel="stylesheet" href="${staticServePath}/skins/${skinDirName}/css/base.css?${staticResourceVersion}" charset="utf-8" />
<link href="${servePath}/blog-articles-rss.do" title="RSS" type="application/rss+xml" rel="alternate" />
<link rel="icon" type="image/png" href="${servePath}/favicon.png" />
<link rel="manifest" href="${servePath}/manifest.json">
<link rel="search" type="application/opensearchdescription+xml" title="${title}" href="/opensearch.xml">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/github-markdown-css">
<link rel="stylesheet" href="${staticServePath}/skins/${skinDirName}/css/DPlayer.min.css">
<script type="text/javascript" src="${staticServePath}/js/lib/jquery/jquery-3.1.0.min.js" charset="utf-8"></script>
<script src="https://cdn.jsdelivr.net/npm/flv.js/dist/flv.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/hls.js/dist/hls.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/dashjs/dist/dash.all.min.js"></script>
<script src="https://cdn.jsdelivr.net/webtorrent/latest/webtorrent.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/pearplayer"></script>
<link rel="stylesheet" href="${staticServePath}/skins/${skinDirName}/css/APlayer.min.css">
<script src="https://cdn.jsdelivr.net/npm/vconsole/dist/vconsole.min.js"></script>
<script src="${staticServePath}/skins/${skinDirName}/js/DPlayer.min.js"></script>
<script src="${staticServePath}/skins/${skinDirName}/js/APlayer.min.js"></script>
<script src="${staticServePath}/skins/${skinDirName}/js/modernizr.js"></script>
<script	src="https://cdn.jsdelivr.net/npm/color-thief-don@2.0.2/src/color-thief.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue"></script>
<script src="${staticServePath}/skins/${skinDirName}/js/jquery.pjax.js"></script>

${htmlHead}
</#macro>