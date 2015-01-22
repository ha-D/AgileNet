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
                url: '/users/makeOrdinary',
                method: 'POST',
                data:{
                    id: id
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
                url: '/users/makeExpert',
                method: 'POST',
                data:{
                    id: id
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
                url: '/users/makeSuspended',
                method: 'POST',
                data:{
                    id: id
                }
            });
        }
    });
});