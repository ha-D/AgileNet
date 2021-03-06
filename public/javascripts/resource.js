$(function(){
    GregorianDateToJalali();
    submitComment();
    listen();
});

function listen(){
    listenToLike();
    listentTodislike();
    listenToCommentOnComment();
    listenToAddOrRemove("remove-", "/filterComment", "primary", "danger", "info", "warning", "ok");
    listenToAddOrRemove("ok-", "/undoFilterComment", "danger", "primary", "warning", "info", "remove");
}

$(function() {
    $("#resource-rate").raty({
        number: 5,
        score: rate,
        path: '/assets/images/',
        halfShow: true,
        readOnly: true
    });
})
$(function() {
    $("#resource-user-rate").raty({
        number: 5,
        score: userRate,
        path: '/assets/images/',
        halfShow: true,
        readOnly: false,
        click: clicked
    });
})

function clicked(){
    $.ajax({
        url: "/rateResource",
        type: 'POST',
        //dataType: 'html',
        data: {rate: arguments[0],
            resource: resourceId},
        beforeSend: function() {
        },
        success: function(data, textStatus, xhr) {
            $('#resource-rate').raty('readOnly', false);
            $('#resource-rate').raty('score', parseInt(data['rate']));
            $('#resource-rate').raty('readOnly', true);
        },
        error: function(xhr, textStatus, errorThrown) {
        }
    });
}

function submitComment(){
    $("#submit-comment").click(function(){
        var commentBody = $("#comment-body").val();
        $.ajax({
            url: "/addComment",
            type: 'POST',
            data: {id: resourceId,
                body: commentBody,
                type: "on resource"},
            beforeSend: function() {
            },
            success: function(data, textStatus, xhr) {
                $("#comment-body").val("");
                console.log(data);

                var panel = $('<div/>', {
                    class: 'panel panel-primary',
                    id: "comment-" + data['id']
                }).prependTo('#comments-container');

                var panelHeader = $('<div/>', {
                    class: 'panel-heading'
                });
                panel.append(panelHeader);
                panelHeader.append($('<span/>', {
                    class: 'margin-left-10',
                    text: userName
                }));
                panelHeader.append($('<span/>', {
                    text: todayInJalali()
                }));
                var likeSpan = $('<span/>', {
                    class: 'float left',
                    id: 'span-like-' + data['id']
                });
                panelHeader.append(likeSpan);
                likeSpan.append($('<span/>', {
                    class: 'margin-left-10',
                    text: 0,
                    id: "comment-num-of-likes-" + data['id']
                }));
                likeSpan.append($('<span/>', {
                    class: 'glyphicon glyphicon-thumbs-up clickable',
                    id: "like-" + data['id']
                }));
                if(isAdmin)
                    likeSpan.append($('<span/>', {
                        id: "remove-" + data['id'],
                        class: 'glyphicon glyphicon-remove margin-right-10 clickable'
                    }));
                panelBody = $('<div/>', {
                    class: "panel-body",
                    text: commentBody
                });
                panel.append(panelBody);

                var subComment = $('<div/>', {
                    class: "float left"
                });
                panelBody.append(subComment);
                subComment.append($('<span/>', {
                    class: "glyphicon glyphicon-comment clickable",
                    id: "comment-icon-" + data['id']
                }));

                listen();
            },
            error: function(xhr, textStatus, errorThrown) {
            }
        });
    });
}


function GregorianDateToJalali(){
    $(".datetime").each(function(){
        var JDate = require('jdate');
        var jdate = new JDate(parseInt($(this).text()));
        $(this).text(jdate.format('dddd DD MMMM YYYY'));
    });
}

function todayInJalali(){
    var JDate = require('jdate');
    var jdate = new JDate();
    return jdate.format('dddd DD MMMM YYYY');
}

function listenToLike(){
    $("[id^='like-']").click(function(e){
        vote(e.target.id, "like-", 1);
    })
}


function listentTodislike(){
    $("[id^='dislike-']").click(function(e){
        vote(e.target.id, "dislike-", -1);
    });
}

function vote(id, startsWith, rate){
    var commentId = id.split(startsWith)[1];
    $.ajax({
        url: "/rateComment",
        type: 'POST',
        data: {rate: rate,
            comment: commentId},
        beforeSend: function() {
        },
        success: function(data, textStatus, xhr) {
            $("#comment-num-of-likes-"+commentId).text(data['rate']);
            if (rate > 0){
                $("#"+id).switchClass("glyphicon-thumbs-up", "glyphicon-thumbs-down");
                $("#"+id).attr("id", "dislike-"+commentId);
                listentTodislike();
            }else{
                $("#"+id).switchClass("glyphicon-thumbs-down", "glyphicon-thumbs-up");
                $("#"+id).attr("id", "like-"+commentId);
                listenToLike();
            }

        },
        error: function(xhr, textStatus, errorThrown) {
        }
    });
}

function listenToCommentOnComment(){
    $("[id^='comment-icon-']").click(function(e){
        $("#comment-on-comment").remove();
        var parentCommentId = (e.target.id).split('comment-icon-')[1];
        var div = $('<div/>', {
            id: "comment-on-comment",
            class: "margin-top-40"
        });
        $("#comment-" + parentCommentId).after(div);
        div.append($('<textarea/>',{
            class: "form-control",
            id: "comment-on-comment-body",
            rows: 3
        }));
        div.append($('<button/>', {
            type: "button",
            id: "submit-comment-on-comment",
            class: "btn btn-primary margin-top-10 margin-bottom-20",
            text: "ارسال"
        }));
        submitCommentOnComment(parentCommentId);
    });
}

function submitCommentOnComment(id){
    $("#submit-comment-on-comment").click(function(){
        var commentBody = $("#comment-on-comment-body").val();
        $.ajax({
            url: "/addComment",
            type: 'POST',
            data: {id: id,
                body: commentBody,
                type: "on comment"},
            beforeSend: function() {
            },
            success: function(data, textStatus, xhr) {
                $("#comment-on-comment").remove();

                var panel = $('<div/>', {
                    class: 'panel panel-info margin-right-40',
                    id: 'subComment-' + data['id']
                });
                $("#comment-" + id).after(panel);

                var panelHeader = $('<div/>', {
                    class: 'panel-heading'
                });
                panel.append(panelHeader);
                panelHeader.append($('<span/>', {
                    class: 'margin-left-10',
                    text: userName
                }));
                panelHeader.append($('<span/>', {
                    text: todayInJalali()
                }));
                var likeSpan = $('<span/>', {
                    class: 'float left',
                    id: 'span-like-' + data['id']
                });
                panelHeader.append(likeSpan);
                likeSpan.append($('<span/>', {
                    class: 'margin-left-10',
                    text: 0,
                    id: "comment-num-of-likes-" + data['id']
                }));
                likeSpan.append($('<span/>', {
                    class: 'glyphicon glyphicon-thumbs-up clickable',
                    id: "like-" + data['id']
                }));
                if(isAdmin)
                    likeSpan.append($('<span/>', {
                        id: "remove-" + data['id'],
                        class: 'glyphicon glyphicon-remove margin-right-10 clickable'
                    }));
                var panelBody = $('<div/>', {
                    class: "panel-body",
                    text: commentBody
                });
                panel.append(panelBody);

                listen();
            },
            error: function(xhr, textStatus, errorThrown) {
            }
        });
    });
}


function listenToAddOrRemove(addOrRemove, url, fromC, toC, fromSubC, toSubC, changed){
    $('[id^=' + addOrRemove + ']').click(function(e){
        var commentId = e.target.id.split(addOrRemove)[1];

        $.ajax({
            url: url,
            type: 'POST',
            data: {id: commentId},
            beforeSend: function() {
            },
            success: function(data, textStatus, xhr) {
                var prefix;
                if ($('#comment-' + commentId).exists()){
                    prefix = '#comment-';
                    $(prefix + commentId).removeClass('panel-' + fromC);
                    $(prefix + commentId).addClass('panel-' + toC);
                }
                else{
                    prefix = '#subComment-';
                    $(prefix + commentId).removeClass('panel-' + fromSubC);
                    $(prefix + commentId).addClass('panel-' + toSubC);
                }

                $('#' + addOrRemove + commentId).remove();
                $("#span-like-" + commentId).append($('<span/>', {
                    id: changed + '-' + commentId,
                    class: 'glyphicon glyphicon-' + changed + ' margin-right-10 clickable'
                }));

                listen();
            },
            error: function(xhr, textStatus, errorThrown) {
            }
        });
    });
}

$.fn.exists = function () {
    return this.length !== 0;
}