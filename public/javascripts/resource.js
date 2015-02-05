$(function() {
    $("#resource-rate").raty({
        number: 5,
        score: 3.5,
        path: '/assets/images/',
        halfShow: true,
        readOnly: true
    });
})

$(function() {
    $("#resource-user-rate").raty({
        number: 5,
        score: 3.5,
        path: '/assets/images/',
        halfShow: true,
        readOnly: false
    });
})