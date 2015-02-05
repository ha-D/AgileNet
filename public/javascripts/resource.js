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