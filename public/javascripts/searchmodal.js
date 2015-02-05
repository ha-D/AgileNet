function createTableRow(item) {
    var resourceTypes = {
        'book': 'کتاب',
        'video': 'ویدیو',
        'article': 'مقاله',
        'website': 'وب‌سایت'
    };
    var tr = $("<tr>")
        .append($("<td>").html(item.name))
        .append($("<td>").html(resourceTypes[item.resourceType]))
        .append($("<td>").html(item.date))
        .append($("<td>").html(item.rating));
    if (item.id in AGS.resultIds) {
        tr.addClass('success')
    } else {
        tr.addClass('error')
    }
    return tr;
}

function modalSearch() {
    var query = $("#modal-search").val();
    var table = $(".b-search-modal .result-list table tbody");
    table.html("");
    $.ajax({
        url: AGS.searchURL,
        type: 'post',
        dataType: 'json',
        data: {
            query: query
        },
        success: function (result) {
            _.each(result.results, function(item) {
                table.append(createTableRow(item));
            });

        }
    });
}

$(function() {
    $(document).on('input',"#modal-search", modalSearch);
});