$(function () {
    var root = $('#cats');
    for (var i = 0; i < cats.length; i++) {
        var c = cats[i];
        appendItem(root, c);
    }
    var el = $("<div class='add-root btn btn-success glyphicon glyphicon-plus add' onclick='showAddModal()'></div>");
    $('body').append(el);
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
            appendItem(p, {name:name, id: data});
            $('#addModal').modal('hide');
        }
    });
}
