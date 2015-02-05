AGS = {};
AGS.selection = "";

AGS.topicData = {
};

AGS.selectedCategory = null;

function render(template, data) {
	return $("<div>").html(template(data)).contents();
}

function createPlusFilter() {
	$("<li>").addClass("topic filter add")
}

function createTopicFilterElement(selection, itemId, itemName, subcategories) {
	var li = $('<li>').addClass('topic filter');
	var mainLi = $("<span>").addClass('filter-main').text(itemName)
	li.attr('data-item', selection + " " + itemId);
	li.append(mainLi);
	//if (subcategories) {
		var nextBtn = $("<span>")
			.addClass("filter-next glyphicon glyphicon-chevron-right");
		li.append(nextBtn);
	//}
	return li;
}

function setTopicFilters(selection, animate) {
	function intializeList(items) {
		var wrapper = $(".topic.filter-list-wrapper");
		var oldUiList = $(".topic.filter-list");
		var uiList = $("<ul>").addClass("topic filter-list");

		if (_.keys(items).length > 0) {
			_.each(_.keys(items), function(item) {
				uiList.append(createTopicFilterElement(selection, item, items[item].name, items[item].subcategories));
			});
		} else {
			var li = $('<li>').addClass('topic filter empty').text("دسته‌ای وجود ندارد");
			uiList.append(li);
		}

		if (animate == 'forward') {
			var x = 0;
			var interval = setInterval(function() {
				if (x == 0) {
					wrapper.append(uiList);
				}

				if (x == 100) {
					clearInterval(interval);
					oldUiList.remove();
				}

				uiList.css('width', (x) + "%");
				oldUiList.css('width', (100 - x) + "%");

				x = x + 5;
			}, 10);

		} else if (animate == "back") {
			wrapper.prepend(uiList);
			var x = 0;
			var interval = setInterval(function() {
				if (x == 100) {
					clearInterval(interval);
					oldUiList.remove();
				}
				uiList.css('width', (x) + "%");
				oldUiList.css('width', (100-x) + "%");
				x = x + 5;
			}, 10);
		} else {
			oldUiList.remove();
			wrapper.append(uiList);
		}
	}

	function setHistory(selectionList) {
		var historyList = $(".filter-history-list");
		historyList.html("");
		var current = AGS.topicData;

		var classList = ['first', 'second', 'third', 'fourth']
		classList = classList.concat('fifth').concat(classList.reverse())
		var count = 0;

		var historyItem = render(AGS.filterHistoryItemTemplate, {
			order: classList[count++],
			name: "",
			selection: ""
		});

		historyList.append(historyItem);

		var revList = [];
		while (selectionList.length > 0) {
			var next = parseInt(selectionList.pop());

			revList.push(next);
			var selection = revList.slice(0).join(' ');

			var classOrder = classList[count++];
			if (selectionList.length == 0) {
				classOrder += " last";
			}

			var historyItem = render(AGS.filterHistoryItemTemplate, {
				order: classOrder,
				name: current[next].name,
				selection: selection
			});

			historyList.append(historyItem);
			current = current[next].subcategories;
		}
	}

	if (selection == "") {
		intializeList(AGS.topicData);
		setHistory([]);
		AGS.selectedCategory = null;
	} else {
		var selectionList = selection.trim().split(' ');
		selectionList.reverse();
		AGS.selectedCategory = selectionList[0];
		setHistory(selectionList.slice(0));
		var current = AGS.topicData;
		while (selectionList.length > 0) {
			var next = parseInt(selectionList.pop());
			current = current[next].subcategories;
		}
		//if (current) {
			intializeList(current);
		//} else {
		//	AGS.noBackAnim = true;
		//}
	}
}

function createResultElements(resultList) {
	return _.map(resultList, function(result) {
		if (!result.image) {
			result.image = AGS.resourceTypeImages[result.resourceType];
		}

		if (result.description && result.description.length > 200) {
			result.description = result.description.slice(0, 200) + "...";
		}

		var html = render(AGS.searchItemTemplate, result);
		var div = $("<div>").html(html).contents();
		var rating = div.find('.search.result > .rating');
		rating.raty({
			number: 5,
			score: result.rating,
			path: AGS.imagePath,
			halfShow    : true,
			readOnly    : true
		});
		return div;
	});
}

function setSearchResults(results, clear) {
	if (clear) {
		$(".search-result-container").html("");
	}

	var elements = createResultElements(results);
	$(".search-result-container").append(elements);
}

function loadCategories() {
	$.ajax({
		url: AGS.loadCategoryURL,
		type: 'get',
		dataType: 'json',
		success: function(result) {
			AGS.topicData = result;
			setTopicFilters("");
		}
	})
}

function createQuery() {
	var request = {};

	var resourceTypes = [];
	$('.filter.type').each(function(index) {
		if ($(this).hasClass('selected')) {
			resourceTypes.push($(this).attr('data-val'));
		}
	});

	if (resourceTypes.length > 0) {
		request.resourceType = resourceTypes;
	}

	if (AGS.selectedCategory) {
		request.category = AGS.selectedCategory;
	}

	var query = $('#search-form input').val();
	if (query) {
		request.query = query;
	}

	return request;
}

function loadResults(clear) {
	var request = createQuery();

	$.ajax({
		url: AGS.searchURL,
		type: 'post',
		dataType: 'json',
		data: request,
		success: function(result) {
			setSearchResults(result.results, clear);
		}
	})
}

function addNewCategory() {
	var category = $('#newcat-form input').val();
	$('#newcat-form input').val("");

	$.ajax({
		url: AGS.addCategoryURL,
		type: 'post',
		dataType: 'json',
		data: {
			parent: AGS.selectedCategory,
			name: category
		},
		success: function(result) {
			var uiList = $(".topic .filter-list");
			if (uiList.find('.empty')) {
				uiList.html("");
			}
			uiList.append(createTopicFilterElement(AGS.selection, result.id, category));
			AGS.topicData = result.categories;
		}
	});

}

$(function() {
	initialize(AGS);

	var source = $("#search-result-template").html();
	AGS.searchItemTemplate = Handlebars.compile(source);

	source = $("#filter-history-item-template").html();
	AGS.filterHistoryItemTemplate = Handlebars.compile(source);

	$(".filter.type").click(function() {
		if ($(this).hasClass('selected')) {
			$(this).removeClass('selected');
		} else {
			$(this).addClass('selected');
		}
		loadResults(true);
	});

	function categoryClick() {

		$('.topic.filter.selected').removeClass('selected');
		$(this).parent().addClass('selected');
		var items = $(this).parent().attr('data-item').split(' ');

		AGS.selectedCategory = items[items.length - 1];

		var selection = $(this).parent().attr('data-item').trim();

		setTopicFilters(selection, 'forward');
		AGS.selection = selection;

		loadResults(true);
	}

	$(".b-resource")
		.on('click', '.filter.topic .filter-next', categoryClick)
		.on('click', '.topic.filter .filter-main', categoryClick)
		.on('click', '.topic.filter-history.item', function() {
			var selection = $(this).attr('data-selection').trim();
			var animation = "back";

			var part = AGS.selection.slice(0, AGS.selection.lastIndexOf(' '));
			if (selection == part && AGS.noBackAnim) {
				animation = "";
				AGS.noBackAnim = false;
			}

			if (selection != AGS.selection) {
				setTopicFilters(selection, animation);
				AGS.selection = selection;
			}

			loadResults(true);
		});


	$("#search-form").submit(function(){
		loadResults(true);
		return false;
	});

	$("#newcat-form").submit(function(){
		addNewCategory();
		return false;
	});

	//setSearchResults(testSearchResult, true);
	//setTopicFilters("");
	loadCategories();
	loadResults(true);
});