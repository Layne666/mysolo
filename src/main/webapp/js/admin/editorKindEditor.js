/*
 * Solo - A small and beautiful blogging system written in Java.
 * Copyright (c) 2010-2019, b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
/**
 * @fileoverview KindEditor
 * @description 修改点：plugins/image/image.js 注释 173-176
 *                     plugins/media/media.js 注释 26 & 28
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @version 1.1.0.2, May 30, 2015
 */
admin.editors.KindEditor = {
    /*
     * @description 初始化编辑器
     * @param conf 编辑器初始化参数
     * @param conf.kind 编辑器类型
     * @param conf.id 编辑器渲染元素 id
     * @param conf.fun 编辑器首次加载完成后回调函数
     */
    init: function (conf) {
        var language = "zh_CN";
        if ("en_US" === Label.localeString) {
            language = "en"
        }

        if (conf.kind && conf.kind === "simple") {
            try {
                this[conf.id] = KindEditor.create('#' + conf.id, {
                    langType: language,
                    resizeType: 0,
                    items: ["bold", "italic", "underline", "strikethrough", "|", "undo", "redo", "|",
                        "insertunorderedlist", "insertorderedlist", "|", "source"
                    ]
                });
            } catch (e) {
                $("#tipMsg").text("KindEditor load fail");
            }
        } else {
            try {
                this[conf.id] = KindEditor.create('#' + conf.id, {
                    'uploadJson' : 'kindeditor/php/upyunUpload.php',
                    langType: language,
                    items: ["formatblock", "fontname", "fontsize", "|", "bold", "italic", "underline", "strikethrough", "forecolor", "|",
                        "link", "unlink", "image", "media", "|", "pagebreak", "emoticons", "code", "/",
                        "undo", "redo", "|", "insertunorderedlist", "insertorderedlist", "indent", "outdent", "|",
                        "justifyleft", "justifycenter", "justifyright", "justifyfull", "|", "plainpaste", "wordpaste", "|",
                        "clearhtml", "source", "preview"
                    ],
                    afterCreate: function () {
                        if (typeof (conf.fun) === "function") {
                            conf.fun();
                        }
                    }
                });
            } catch (e) {
                $("#tipMsg").text("KindEditor load fail");
            }
        }
    },
    /*
     * @description 获取编辑器值
     * @param {string} id 编辑器id
     * @returns {string} 编辑器值
     */
    getContent: function (id) {
        var content = "";
        try {
            content = this[id].html();
        } catch (e) {
            content = $("#" + id).val();
        }
        return content;
    },
    /*
     * @description 设置编辑器值
     * @param {string} id 编辑器 id
     * @param {string} content 设置编辑器值
     */
    setContent: function (id, content) {
        try {
            this[id].html(content);
        } catch (e) {
            $("#" + id).val(content);
        }
    },
    /*
     * @description 移除编辑器
     * @param {string} id 编辑器 id
     */
    remove: function (id) {
        this[id].remove();
    }
};
