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
    if (AGS.selectedCategory == null || item.categories.indexOf(parseInt(AGS.selectedCategory)) >= 0 ) {
        tr.addClass('success')
    } else {
        tr.addClass('danger')
    }
    tr.attr('data-id', item.id);
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

function change(stat, id, success) {
    var url = null;
    if (stat == 'add') {
        url = AGS.addResourceCategoryURL;
    } else {
        url = AGS.removeResourceCategoryURL
    }

    $.ajax({
        url: url,
        data: {
            category: AGS.selectedCategory,
            resource: id
        },
        type: "post",
        success: success
    })
}

$(function() {
    $(document).on('input',"#modal-search", modalSearch);
    $(document).on('click', '.b-search-modal .result-list table tbody tr', function() {
        var self = $(this);
        if (self.hasClass('success')) {
            change('remove', self.attr('data-id'), function() {
                self.removeClass('success');
                self.addClass('danger');
            });
        } else if (self.hasClass('danger')) {
            change('add', self.attr('data-id'), function() {
                self.addClass('success');
                self.removeClass('danger');
            });
        }
    });

    $("#addModal").on('shown.bs.modal', modalSearch)
});