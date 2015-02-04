AGS = {};
AGS.selection = "";
AGS.resourceTypeImages = {
	'book': '/assets/images/resources/book.png',
	'article': '/assets/images/resources/article.png',
	'website': '/assets/images/resources/website.png',
	'video': '/assets/images/resources/video.png'
};
AGS.topicData = {

};

AGS.selectedCategory = null;

var testSearchResult = [
	{
		name: 'اصول چابک بودن',
		user: 'محمد طاهری',
		description: 'یک کتبا بسیار خوب درباره‌ی چابک و فلان و یه سری توضیحات دیگه که هیلی مهمن یکم دیگه فکر کنم بس باشه',
		date: '۱۱ شهریور، ۱۳۹۱',
		resourceType: 'book',
		rating: 3
	},
	{
		name: 'فیلم آموزش tfs',
		user: 'اصغر اکبری',
		description: 'در این فیلم شما با نحوه‌ی اسفتاده از tfs آشنا می‌شوید و می‌بینید چقد مزخرفه',
		date: '۲۳ مهر ۱۳۹۳',
		resourceType: 'video',
		rating: 2
	},
	{
		name: 'سایت چابک‌نت',
		user: 'عطیه اشعری',
		description: 'یک سایت بسیار داقون در رابطه با همین',
		date: '۵ فروردین ۱۳۹۱',
		resourceType: 'website',
		rating: 4.5
	}
]

function render(template, data) {
	return $("<div>").html(template(data)).contents();
}

function setTopicFilters(selection, animate) {
	function intializeList(items) {
		var wrapper = $(".topic.filter-list-wrapper");
		var oldUiList = $(".topic.filter-list");
		var uiList = $("<ul>").addClass("topic filter-list");

		_.each(_.keys(items), function(item) {
			var li = $('<li>').addClass('topic filter');
			var mainLi = $("<span>").addClass('filter-main').text(items[item].name)
			li.attr('data-item', selection + " " + item);
			li.append(mainLi);
			if (items[item].subcategories !== undefined) {
				var nextBtn = $("<span>")
				.addClass("filter-next glyphicon glyphicon-chevron-right");
				li.append(nextBtn);
			}
			uiList.append(li);


		});

		if (animate == 'forward') {
			wrapper.append(uiList);
			var x = 0;
			var interval = setInterval(function() {
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
		var historyList = $(".topic-container .filter-history-list");
		historyList.html("");
		var current = AGS.topicData;

		var classList = ['first', 'second', 'third', 'fourth', 'fifth'].reverse();

		var historyItem = render(AGS.filterHistoryItemTemplate, {
			order: classList.pop(),
			name: "*",
			selection: ""
		});

		historyList.append(historyItem);

		var revList = [];
		while (selectionList.length > 0) {
			var next = parseInt(selectionList.pop());

			revList.push(next);
			var selection = revList.slice(0).join(' ');

			var historyItem = render(AGS.filterHistoryItemTemplate, {
				order: classList.pop(),
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
		intializeList(current);
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

	var query = $('.search-box input').val();
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

	$(".topic-container").on('click', 
		'.filter.topic .filter-next', function() {
 		var selection = $(this).parent().attr('data-item').trim();
 		setTopicFilters(selection, 'forward');
 		AGS.selection = selection;
 	}).on('click', '.topic.filter-history.item', function() {
 		var selection = $(this).attr('data-selection').trim();
 		if (selection != AGS.selection) {
	 		setTopicFilters(selection, 'back');
	 		AGS.selection = selection;
 		}
 	}).on('click', '.topic.filter .filter-main', function() {
		$('.topic.filter.selected').removeClass('selected');
		$(this).parent().addClass('selected');
		var items = $(this).parent().attr('data-item').split(' ');
		AGS.selectedCategory = items[items.length - 1];
		loadResults(true);
	});


	$("#search-form").submit(function(){
		loadResults(true);
		console.log("HEY")
		return false;
	});
	//setSearchResults(testSearchResult, true);

	loadCategories();
	loadResults(true);
});