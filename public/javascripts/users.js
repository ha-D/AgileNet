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
                url: '/users/roles/remove/',
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
                url: '/users/roles/add/',
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
                url: '/users/suspend/',
                method: 'POST',
                data:{
                    user: id
                }
            });
        }
    });
});