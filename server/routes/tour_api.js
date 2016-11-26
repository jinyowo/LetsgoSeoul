/*
 *  tour_api.js : 위도 경도로 관광 api에서 정보를 받아옴
 *
 */
var tour = require('./tour');
var request = require('request');
var myKey = 'ePhJZR2dULEVHAoBvgtMSpN3z2MkXbwTIbpAM9QaOjYDxnvPovwx9ygEPKh5SrPm33yZcHcyDb7KjT4%2Br%2BgyoA%3D%3D';

// 주변 범위
var radius = '1000';

// 모바일OS 안드로이드:AND
var mobileOS = 'AND';

// 어플 이름
var appName = 'letsgoseoul';

// 불러온 데이터 갯수
var numOfData = '20';

// 위도경도
var myLat, myLng;

// 장소 리스트
var foodList = new Array();
var placeList = new Array();

//데이터베이스 객체, 스키마 객체, 모델 객체를 이 모듈에서 사용할 수 있도록 전달함
var init = function(app, config, lat, lng) {
        console.log('tour_api init 호출됨.');

        myLat = lat;
        myLng = lng;

        getFoodList(lat, lng, function() {
                console.log('@@(tour)complete@@');

                //tour.addFoodList();
                //tour.addPlaceList();
        });
};

//food, place list 가져오기
var getFoodList = function(lat, lng, callback) {
	// 불러올 데이터 종류, 음식점:39, 관광지:12
	var contentTypeId = '39';
	// 접근할 url 생성
	var url = 'http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?' +
			'ServiceKey=' + myKey +
			'&contentTypeId=' + contentTypeId +
			'&mapX=' + lng + '&mapY=' + lat +
			'&radius=' + radius +
			'&numOfRows=' + numOfData +
			'&listYN=Y&arrange=E&MobileOS=' + mobileOS +
			'&MobileApp=' + appName + '&over&_type=json';

	// url에서 정보 가져오기
	request(url, function(error, response, body) {
        foodList.length = 0;
			if (!error && response.statusCode === 200) {
				var bodyObject = JSON.parse(body);
				console.log("bodyObject = " + bodyObject);

				if(bodyObject.response.header.resultCode == 0000) {
					var numOfItems = bodyObject.response.body.items.item.length;
					console.log("num of Items = " + numOfItems);

					for (var i = 0; i < numOfItems; i++) {
						// 각각의 데이터(object)
						var tmp = new Object();
						console.log(i + "번째 아이템");
						//console.log(JSON.stringify(bodyObject));
						// bodyObjectect들의 정보
						tmp.name = bodyObject.response.body.items.item[i].title;
						tmp.contentid = bodyObject.response.body.items.item[i].contentid;
						tmp.lng = bodyObject.response.body.items.item[i].mapx;
						tmp.lat = bodyObject.response.body.items.item[i].mapy;
                        tmp.image = bodyObject.response.body.items.item[i].firstimage;

                        //대표 이미지가 없는 경우
                        if(bodyObject.response.body.items.item[i].firstimage == null)
                            tmp.image = 'http://nodetest.iptime.org:3000/public/resources/default_food.png';

                        // 음식 리스트에 추가
						foodList.push(tmp);

						//console.log("title: " + bodyObject.response.body.items.item[i].title + ", tel: " + bodyObject.response.body.items.item[i].tel + ", contentID " + bodyObject.response.body.items.item[i].contentid);

						//console.log(bodyObject.response.body.items.item[i]);
					}
                    console.log(foodList);

                    callback(null, foodList);
                    // console.log(bodyObject.response.body.items.item[5]);
				}
				else {
					console.log(bodyObject.response.header.resultMsg);
				}
			}
	});

};

var getPlaceList = function(lat, lng, callback) {
    // 불러올 데이터 종류, 음식점:39, 관광지:12
    var contentTypeId = '12';
    // 접근할 url 생성
    var url = 'http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?' +
        'ServiceKey=' + myKey +
        '&contentTypeId=' + contentTypeId +
        '&mapX=' + lng + '&mapY=' + lat +
        '&radius=' + radius +
        '&numOfRows=' + numOfData +
        '&listYN=Y&arrange=E&MobileOS=' + mobileOS +
        '&MobileApp=' + appName + '&over&_type=json';

    console.log(url);

    // url에서 정보 가져오기
    request(url, function(error, response, body) {
        placeList.length = 0;
        if (!error && response.statusCode === 200) {
            var bodyObject = JSON.parse(body);
            console.log("bodyObject = " + bodyObject);

            if(bodyObject.response.header.resultCode == 0000) {
                var numOfItems = bodyObject.response.body.items.item.length;
                console.log("num of Items = " + numOfItems);

                for (var i = 0; i < numOfItems; i++) {
                    // 각각의 데이터(object)
                    var tmp = new Object();
                    console.log(i + "번째 아이템");
                    // console.log(JSON.stringify(bodyObject));
                    // bodyObject들의 정보
                    tmp.name = bodyObject.response.body.items.item[i].title;
                    tmp.contentid = bodyObject.response.body.items.item[i].contentid;
                    tmp.lng = bodyObject.response.body.items.item[i].mapx;
                    tmp.lat = bodyObject.response.body.items.item[i].mapy;
                    tmp.image = bodyObject.response.body.items.item[i].firstimage;

                    //대표 이미지가 없는 경우
                    if(bodyObject.response.body.items.item[i].firstimage == null)
                        tmp.image = 'http://localhost:3000/public/resources/default_place.png';
                    // 음식 리스트에 추가
                    placeList.push(tmp);
                    //console.log("title: " + bodyObject.response.body.items.item[i].title + ", tel: " + bodyObject.response.body.items.item[i].tel + ", contentID " + bodyObject.response.body.items.item[i].contentid);
                    //console.log(bodyObject.response.body.items.item[i]);
                }
                console.log(placeList);

                callback(null, placeList);
                // console.log(bodyObject.response.body.items.item[5]);
            }
            else {
                console.log(bodyObject.response.header.resultMsg);
            }
        }
    });

};


var getDetail = function(id, callback) {
    // 가게 id
    var contentid = id; // 음식점:133855, 관광지:264311
    // 접근할 url 생성
    var url = 'http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?'
        + 'ServiceKey='
        + myKey
        + '&contentId='
        + contentid
        + '&defaultYN=Y&overviewYN=Y&addrinfoYN=Y&mapinfoYN=Y'
        + '&MobileOS='
        + mobileOS + '&MobileApp=' + appName + '&_type=json';

    request(url, function(error, response, body) {
        if (!error && response.statusCode === 200) {
            // string -> obj
            var bodyObject = JSON.parse(body);
            var tmp = new Object();

            //예외처리
            tmp.overview = bodyObject.response.body.items.item.overview;
            tmp.name = bodyObject.response.body.items.item.title;
            tmp.lat = bodyObject.response.body.items.item.mapy;
            tmp.lng = bodyObject.response.body.items.item.mapx;
            tmp.address = bodyObject.response.body.items.item.addr1;

            if(bodyObject.response.body.items.item.firstimage == null)
                tmp.image = 'http://tong.visitkorea.or.kr/cms/resource/24/1717724_image2_1.jpg';
            else {
                tmp.image = bodyObject.response.body.items.item.firstimage;
            }

            if(bodyObject.response.body.items.item.tel==null)
                tmp.tel = '';
            else {
                tmp.tel = bodyObject.response.body.items.item.tel;
            }

            if(bodyObject.response.body.items.item.homepage==null)
                tmp.homepage = '';
            else {
                tmp.homepage = bodyObject.response.body.items.item.homepage;
            }

            console.log(bodyObject.response.body.items.item);
            console.log(bodyObject.response.body.items.item.address);

            callback(null, tmp);
        }
    });
};


module.exports.init = init;
module.exports.getFoodList = getFoodList;
module.exports.getPlaceList = getPlaceList;
module.exports.getDetail = getDetail;
module.exports.foodList = foodList;
module.exports.placeList = placeList;
