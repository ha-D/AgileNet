$(function () {
    var root = $('#cats');
    for (var i = 0; i < cats.length; i++) {
        var c = cats[i];
        appendItem(root, c);
    }
    var el = $("<div class='add-root btn btn-success glyphicon glyphicon-plus add'></div>");
    root.append(el);
});

function appendItem(p, c, l) {
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
    var el = $('[data-id='+currentId+']');
    el.remove();
    $('#deleteModal').modal('hide');
}

function showAddModal(id, name){
    currentId=id;
    $('#addModal').modal();
}

function addCat(){
    var p = $('[data-id='+currentId+']');
    $('#addModal').modal('hide');
}
