/**
 * USERS
 */
var activityPage = 0;

$(function(){
    var o=$('.oUsers'), s=$('.sUsers'), e=$('.eUsers');

    $('.user').draggable({
        revert: 'invalid'
    });

    o.droppable({
        accept: ".sUsers .user, .eUsers .user",
        hoverClass: "hover",
        drop: function( event, ui ) {
            var el = ui.draggable[0];

            el.parentElement.removeChild(el);
            o.append(el);
            el.setAttribute('class', 'user btn btn-primary');
            el.setAttribute('style', 'position: relative;');

            var id =el.getAttribute("data-id");
            $.ajax({
                url: '/settings/roles/remove/',
                method: 'POST',
                data:{
                    user: id,
                    role: "expert"
                }
            });
        }
    });
    e.droppable({
        accept: ".sUsers .user, .oUsers .user",
        hoverClass: "hover",
        drop: function( event, ui ) {
            var el = ui.draggable[0];
            el.parentElement.removeChild(el);
            e.append(el);
            el.setAttribute('class', 'user btn btn-success ui-draggable');
            el.setAttribute('style', 'position: relative;');
            var id =el.getAttribute("data-id");
            $.ajax({
                url: '/settings/roles/add/',
                method: 'POST',
                data:{
                    user: id,
                    role: "expert"
                }
            });
        }
    });
    s.droppable({
        accept: ".oUsers .user, .eUsers .user",
        hoverClass: "hover",
        drop: function( event, ui ) {
            var el = ui.draggable[0];
            el.parentElement.removeChild(el);
            s.append(el);
            el.setAttribute('class', 'user btn btn-danger ui-draggable');
            el.setAttribute('style', 'position: relative;');
            var id =el.getAttribute("data-id");
            $.ajax({
                url: '/settings/users/suspend/',
                method: 'POST',
                data:{
                    user: id
                }
            });
        }
    });
});


/**
 * Categories
 */

$(function () {
    var root = $('#cats');
    for (var i = 0; i < cats.length; i++) {
        var c = cats[i];
        appendItem(root, c);
    }
    var el = $("<div class='add-root btn btn-success glyphicon glyphicon-plus add' onclick='showAddModal()'></div>");
    $('#categories .panel-body').append(el);
});

function appendItem(p, c) {
    var el = $("<div class='item' data-id='" + c.id + "'><div class='name'>" + c.name + "</div><div title='اضافه کردن زیر عنوان' class='add btn btn-success glyphicon glyphicon-plus' onclick='showAddModal("+ c.id+",\""+ c.name+"\")'></div><div title='حذف' class='remove btn btn-danger glyphicon glyphicon-trash' onclick='showRemoveModal("+ c.id+",\""+ c.name+"\")'></div></div>");
    p.append(el);
    if (c.children)
        for (var i = 0; i < c.children.length; i++)
            appendItem(el, c.children[i]);
}

var currentId;

function showRemoveModal(id, name){
    currentId=id;
    $('#deleteModalCatName').html(name);
    $('#deleteModal').modal();
}

function removeCat(){
    $.ajax({
        url: '/categories/remove',
        method: 'POST',
        data:{
            category: currentId
        },
        success: function(data){
            console.log(data);
            var el = $('[data-id='+currentId+']');
            el.remove();
            $('#deleteModal').modal('hide');
        }
    });
}

function showAddModal(id, name){
    currentId=id;
    $('#catName').val("");
    $('#addModal').modal();
}

function addCat(){
    var name = $('#catName').val();
    if(!name)
        return;
    $.ajax({
        url: '/categories/add',
        method: 'POST',
        data:{
            parent: currentId,
            name: name
        },
        success: function(data){
            console.log(data);
            var p = currentId? $('[data-id='+currentId+']'): $('#cats');
            appendItem(p, {name:name, id: data.id});
            $('#addModal').modal('hide');
        }
    });
}

function setUserActivites(data) {
    var table = $(".activity.table tbody");
    table.html("");
    _.each(data.results, function(row) {
        var tr = $("<tr>");

        var icon = $("<span>").addClass("glyphicon").attr("aria-hidden", "true");
        if (row.type == "rate") {
            icon.addClass("glyphicon-star")
        } else if (row.type == "comment") {
            icon.addClass("glyphicon-comment")
        }
        tr.append($("<td>").append(icon));

        tr.append($("<td>").html(row.date));

        var content = "";
        if (row.type == "rate") {
            content =
                row.user +
                " به مرجع " +
                '<a href="/resource/' + row.resourceId + '">' + row.resourceName + '</a>' +
                " امتیاز "+
                row.value +
                " داد."
        } else if (row.type == "comment") {
            content =
                row.user +
                " به مرجع " +
                '<a href="/resource/' + row.resourceId + '">' + row.resourceName + '</a>' +
                " نظر "+
                " داد."
        }
        tr.append($("<td>").html(content));
        table.append(tr);
    });

    if (data.lastPage) {
        $('.activity.page.older').addClass("disabled");
    } else {
        $('.activity.page.older').removeClass("disabled");
    }

    if (activityPage == 0) {
        $('.activity.page.newer').addClass("disabled");
    } else {
        $('.activity.page.newer').removeClass("disabled");
    }
}

function loadUserActivities() {
    $.ajax({
        url: '/settings/activities',
        method: 'GET',
        data:{
            pageNumber: activityPage
        },
        success: function(data){
            setUserActivites(data);
        }
    });
}

$(function() {
    $('.activity.page.newer').click(function() {
        if (! $(this).hasClass('disabled')) {
            activityPage -= 1;
            loadUserActivities();
        }
    });

    $('.activity.page.older').click(function() {
        if (! $(this).hasClass('disabled')) {
            activityPage += 1;
            loadUserActivities();
        }
    })

    loadUserActivities();
});