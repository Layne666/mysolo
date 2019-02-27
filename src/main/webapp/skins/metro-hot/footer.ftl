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
<script type="text/javascript">
    var latkeConfig = {
        "servePath": "${servePath}",
        "isLoggedIn": "${isLoggedIn?string}",
        "staticServePath": "${staticServePath}"
    };
    
    var Label = {
        "markedAvailable": ${markedAvailable?c},
        "tag1Label": "${tag1Label}",
        "viewLabel": "${viewLabel}",
        "commentLabel": "${commentLabel}",
        "topArticleLabel": "${topArticleLabel}",
        "updatedLabel": "${updatedLabel}",
        "contentLabel": "${contentLabel}",
        "abstractLabel": "${abstractLabel}",
        "adminLabel": "${adminLabel}",
        "logoutLabel": "${logoutLabel}",
        "skinDirName": "${skinDirName}"
    };
</script>
<script type="text/javascript" src="${staticServePath}/js/lib/jquery/jquery.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${staticServePath}/js/common${miniPostfix}.js?${staticResourceVersion}" charset="utf-8"></script>
<script type="text/javascript" src="${staticServePath}/skins/${skinDirName}/js/${skinDirName}${miniPostfix}.js?${staticResourceVersion}" charset="utf-8"></script>
<script>
    Util.initSW();
    Util.parseMarkdown();
</script>
${plugins}
