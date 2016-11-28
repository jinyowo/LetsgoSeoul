/**
 *  tour_api.js : Tour API에서 직접적으로 정보를 받아오는 함수
 *
 */

var tour = require('./tour');
var request = require('request');
var myKey = 'X%2BQN2%2BwpDBWQiFgODHXp%2FlTVQbpVTMlVGjWsIzIzmKjJYh2xcNusIC%2FeHVRGHzYwRsOqu7AqsZJKcCHMDAb%2Buw%3D%3D';
//var myKey = 'ePhJZR2dULEVHAoBvgtMSpN3z2MkXbwTIbpAM9QaOjYDxnvPovwx9ygEPKh5SrPm33yZcHcyDb7KjT4%2Br%2BgyoA%3D%3D';

// 주변 범위 1500m
var radius = '1500';

// 모바일OS 안드로이드:AND
var mobileOS = 'AND';

// 어플 이름
var appName = 'letsgoseoul';

// 불러온 데이터 갯수
var numOfData = '20';

// 장소 리스트
var foodList = new Array();
var placeList = new Array();

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

    console.log("URL : " + url);
	// url에서 정보 가져오기
	request(url, function(error, response, body) {
        foodList.length = 0;
			if (!error && response.statusCode === 200) {
				var bodyObject = JSON.parse(body);
				console.log("bodyObject = " + bodyObject);

                // resultCode = 0000 -> 정상적으로 결과를 받아옴
				if(bodyObject.response.header.resultCode == 0000) {
                    var numOfItems = 0;
                    if(bodyObject.response.body.items === "") {
                        numOfItems = 0;
                    }
                    else {
                        numOfItems = bodyObject.response.body.items.item.length;
                    }

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

					}
                    console.log(foodList);

                    callback(null, foodList);
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

    console.log("URL : " + url);

    // url에서 정보 가져오기
    request(url, function(error, response, body) {
        placeList.length = 0;
        if (!error && response.statusCode === 200) {
            var bodyObject = JSON.parse(body);

            if(bodyObject.response.header.resultCode == 0000) {
                var numOfItems = 0;

                if(bodyObject.response.body.items === "") {
                    numOfItems = 0;
                }
                else {
                    numOfItems = bodyObject.response.body.items.item.length;
                }

                for (var i = 0; i < numOfItems; i++) {
                    // 각각의 데이터(object)
                    var tmp = new Object();
                    console.log(i + "번째 아이템");

                    // bodyObject들의 정보
                    tmp.name = bodyObject.response.body.items.item[i].title;
                    tmp.contentid = bodyObject.response.body.items.item[i].contentid;
                    tmp.lng = bodyObject.response.body.items.item[i].mapx;
                    tmp.lat = bodyObject.response.body.items.item[i].mapy;
                    tmp.image = bodyObject.response.body.items.item[i].firstimage;

                    //대표 이미지가 없는 경우
                    if(bodyObject.response.body.items.item[i].firstimage == null)
                        tmp.image = 'http://nodetest.iptime.org:3000/public/resources/default_place.png';
                    // 음식 리스트에 추가
                    placeList.push(tmp);

                }
                console.log(placeList);

                callback(null, placeList);
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

            tmp.overview = bodyObject.response.body.items.item.overview;
            tmp.name = bodyObject.response.body.items.item.title;
            tmp.lat = bodyObject.response.body.items.item.mapy;
            tmp.lng = bodyObject.response.body.items.item.mapx;
            tmp.address = bodyObject.response.body.items.item.addr1;

            // 전화번호 정보가 없는 경운
            if(bodyObject.response.body.items.item.tel==null)
                tmp.tel = "";
            else {
                tmp.tel = bodyObject.response.body.items.item.tel;
            }

            // 홈페이지 정보가 없는 경우
            if(bodyObject.response.body.items.item.homepage==null)
                tmp.homepage = "";
            else {
                // HTML포맷으로 받아온 링크에 띄어쓰기를 추가함
                var addBR = bodyObject.response.body.items.item.homepage.replace('<a','<br><a');
                tmp.homepage = addBR;
            }

            console.log(bodyObject.response.body.items.item);

            callback(null, tmp);
        }
    });
};

module.exports.getFoodList = getFoodList;
module.exports.getPlaceList = getPlaceList;
module.exports.getDetail = getDetail;
module.exports.foodList = foodList;
module.exports.placeList = placeList;
