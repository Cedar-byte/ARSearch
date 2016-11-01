// information about server communication. This sample webservice is provided by Wikitude and returns random dummy places near given location
//var ServerInformation = {
//	POIDATA_SERVER: "https://example.wikitude.com/GetSamplePois/",
//	POIDATA_SERVER_ARG_LAT: "lat",
//	POIDATA_SERVER_ARG_LON: "lon",
//	POIDATA_SERVER_ARG_NR_POIS: "nrPois"
//};

// implementation of AR-Experience (aka "World")
var World = {
	// you may request new   data from server periodically, however: in this sample data is only requested once
	// 你可以定期从服务器请求新数据, however:在这个示例中数据只请求一次
	isRequestingData: false,

	// true once data was fetched  一旦数据被获取
	initiallyLoadedData: false,

	// different POI-Marker assets 不同的POI-Marker图片资源 初始化
	markerDrawable_selected: null,// 选中
	markerDrawable_idle: null,// 默认
	markerDrawable_directionIndicator: null,// 方向指示器

	// list of AR.GeoObjects that are currently shown in the scene / World
	markerList: [],

	// The last selected marker 最后选择的Marker
	currentMarker: null,

	locationUpdateCounter: 0,
	updatePlacemarkDistancesEveryXLocationUpdates: 10,

	// called to inject new POI data 注入新的POI数据
	loadPoisFromJsonData: function loadPoisFromJsonDataFn(poiData) {

		// show radar & set click-listener 显示雷达 & 设置点击事件监听
		PoiRadar.show();
//		$('#radarContainer').unbind('click');
//		$("#radarContainer").click(PoiRadar.clickedRadar); // 雷达的点击事件

		// empty list of visible markers
		World.markerList = [];

		// start loading marker assets 开始加载marker的图片资源
//        World.markerDrawable_idle = new AR.ImageResource("assets/marker_idle.png");
		World.markerDrawable_selected = new AR.ImageResource("assets/marker_selected.png");
		// 指示器图片
		World.markerDrawable_directionIndicator = new AR.ImageResource("assets/indi.png");

		// loop through POI-information and create an AR.GeoObject (=Marker) per POI
		for (var currentPlaceNr = 0; currentPlaceNr < poiData.length; currentPlaceNr++) {
		World.markerDrawable_idle = new AR.ImageResource(poiData[currentPlaceNr].description);
			var singlePoi = {
				"id": poiData[currentPlaceNr].id,
				"latitude": parseFloat(poiData[currentPlaceNr].latitude),
				"longitude": parseFloat(poiData[currentPlaceNr].longitude),
				"altitude": parseFloat(poiData[currentPlaceNr].altitude),
				"title": poiData[currentPlaceNr].name,
				"description": poiData[currentPlaceNr].description
			};
			World.markerList.push(new Marker(singlePoi));
		}

		// updates distance information of all placemarks
		World.updateDistanceToUserValues();

//		World.updateStatusMessage(currentPlaceNr + ' places loaded');// 显示Marker数量
	},

	// sets/updates distances of all makers so they are available way faster than calling (time-consuming) distanceToUser() method all the time
	updateDistanceToUserValues: function updateDistanceToUserValuesFn() {
		for (var i = 0; i < World.markerList.length; i++) {
			World.markerList[i].distanceToUser = World.markerList[i].markerObject.locations[0].distanceToUser();
		}
	},

	// updates status message shon in small "i"-button aligned bottom center
	updateStatusMessage: function updateStatusMessageFn(message, isWarning) {

		var themeToUse = isWarning ? "e" : "c";
		var iconToUse = isWarning ? "alert" : "info";

		$("#status-message").html(message);
		$("#popupInfoButton").buttonMarkup({
			theme: themeToUse
		});
		$("#popupInfoButton").buttonMarkup({
			icon: iconToUse
		});
	},

	// location updates, fired every time you call architectView.setLocation() in native environment
	locationChanged: function locationChangedFn(lat, lon, alt, acc) {

		// request data if not already present
		if (!World.initiallyLoadedData) {
			World.requestDataFromServer(lat, lon);
			World.initiallyLoadedData = true;
		} else if (World.locationUpdateCounter === 0) {
			// update placemark distance information frequently, you max also update distances only every 10m with some more effort
			World.updateDistanceToUserValues();
		}

		// helper used to update placemark information every now and then (e.g. every 10 location upadtes fired)
		World.locationUpdateCounter = (++World.locationUpdateCounter % World.updatePlacemarkDistancesEveryXLocationUpdates);
	},

	// fired when user pressed maker in cam 当用户选中Marker时：
	onMarkerSelected: function onMarkerSelectedFn(marker) {


//		World.currentMarker = marker;
		// 与native code交互
        var architectSdkUrl = "architectsdk://markerselected?id=" + encodeURIComponent(marker.poiData.id) + "&title=" + encodeURIComponent(marker.poiData.title) + "&description=" + encodeURIComponent(marker.poiData.description);
		/*
			The urlListener of the native project intercepts this call and parses the arguments.
			This is the only way to pass information from JavaSCript to your native code.
			Ensure to properly encode and decode arguments.
			Note: you must use 'document.location = "architectsdk://...' to pass information from JavaScript to native.
			! This will cause an HTTP error if you didn't register a urlListener in native architectView !
		*/
		document.location = architectSdkUrl;


//		// update panel values 更新面板值(显示内容到pop中)
//		$("#poi-detail-title").html(marker.poiData.title);
//		$("#poi-detail-description").html(marker.poiData.description);
//
//		var distanceToUserValue = (marker.distanceToUser > 999) ? ((marker.distanceToUser / 1000).toFixed(2) + " km") : (Math.round(marker.distanceToUser) + " m");
//
//		$("#poi-detail-distance").html(distanceToUserValue);
//
//		// show panel
//		$("#panel-poidetail").panel("open", 123);
//
//		$( ".ui-panel-dismiss" ).unbind("mousedown");
//
//		$("#panel-poidetail").on("panelbeforeclose", function(event, ui) {
//			World.currentMarker.setDeselected(World.currentMarker);
//		});
	},

	// screen was clicked but no geo-object was hit 屏幕被点击了但geo-object没有被点击
	onScreenClick: function onScreenClickFn() {
		// you may handle clicks on empty AR space too
	},

	// returns distance in meters of placemark with maxdistance * 1.1
	getMaxDistance: function getMaxDistanceFn() {

		// sort palces by distance so the first entry is the one with the maximum distance
		World.markerList.sort(World.sortByDistanceSortingDescending);

		// use distanceToUser to get max-distance
		var maxDistanceMeters = World.markerList[0].distanceToUser;

		// return maximum distance times some factor >1.0 so ther is some room left and small movements of user don't cause places far away to disappear
		return maxDistanceMeters * 1.1;
	},

	// request POI data  访问网络请求Poi数据
//	requestDataFromServer: function requestDataFromServerFn(lat, lon) {
//
//		// set helper var to avoid requesting places while loading
//		World.isRequestingData = true;
//		World.updateStatusMessage('Requesting places from web-service');
//
//		// server-url to JSON content provider
//		var serverUrl = ServerInformation.POIDATA_SERVER + "?" + ServerInformation.POIDATA_SERVER_ARG_LAT + "=" + lat + "&" + ServerInformation.POIDATA_SERVER_ARG_LON + "=" + lon + "&" + ServerInformation.POIDATA_SERVER_ARG_NR_POIS + "=20";
//
//		var jqxhr = $.getJSON(serverUrl, function(data) {
//			World.loadPoisFromJsonData(data);
//		})
//			.error(function(err) {
//				World.updateStatusMessage("Invalid web-service response.", true);
//				World.isRequestingData = false;
//			})
//			.complete(function() {
//				World.isRequestingData = false;
//			});
//	},

	// helper to sort places by distance
	sortByDistanceSorting: function(a, b) {
		return a.distanceToUser - b.distanceToUser;
	},

	// helper to sort places by distance, descending
	sortByDistanceSortingDescending: function(a, b) {
		return b.distanceToUser - a.distanceToUser;
	}

};

/* forward locationChanges to custom function */
AR.context.onLocationChanged = World.locationChanged;

/* forward clicks in empty area to World */
AR.context.onScreenClick = World.onScreenClick;