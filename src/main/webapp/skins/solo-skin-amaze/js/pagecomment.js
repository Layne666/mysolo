var page = new Page({
                            "nameTooLongLabel": "${nameTooLongLabel}",
                            "mailCannotEmptyLabel": "${mailCannotEmptyLabel}",
                            "mailInvalidLabel": "${mailInvalidLabel}",
                            "commentContentCannotEmptyLabel": "${commentContentCannotEmptyLabel}",
                            "captchaCannotEmptyLabel": "${captchaCannotEmptyLabel}",
                            "loadingLabel": "${loadingLabel}",
                            "oId": "${oId}",
                            "skinDirName": "${skinDirName}",
                            "blogHost": "${blogHost}",
                            "randomArticles1Label": "${randomArticles1Label}",
                            "externalRelevantArticles1Label": "${externalRelevantArticles1Label}"
                        });
var replyTo = function (id) {
        var commentFormHTML = "<table class='form comment-reply' id='replyForm'>";
        page.addReplyForm(id, commentFormHTML);
    };
    (function () {
        page.load();
        Skin.initArticle("${tocLabel}", "${siteViewLabel}");
        // emotions
        page.replaceCommentsEm("#comments .content-reset");
    })();