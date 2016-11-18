/*
 *  tour_api.js :
 *
 */
var tour = require('./tour');

//장소 리스트
var foodList = new Array();
var placeList = new Array();

//데이터베이스 객체, 스키마 객체, 모델 객체를 이 모듈에서 사용할 수 있도록 전달함
var init = function(app, config) {
    console.log('tour_api init 호출됨.');

    getFoodList(app, config, function() {
        console.log('@@(tour)complete@@');

        tour.moveApiToDatabase();
    });
}

//food list를 가져오는 함수
function getFoodList(app, config, callback) {
    // 주소로부터 데이터 가져오기
    var request = require('request');
    //http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=ePhJZR2dULEVHAoBvgtMSpN3z2MkXbwTIbpAM9QaOjYDxnvPovwx9ygEPKh5SrPm33yZcHcyDb7KjT4%2Br%2BgyoA%3D%3D&contentTypeId=39&mapX=126.981611&mapY=37.568477&radius=1000&pageNo=1&numOfRows=10&listYN=Y&arrange=A&MobileOS=ETC&MobileApp=AppTesting&over&_type=json
    // 관광공사 API 접근 인증키
    var myKey = 'ePhJZR2dULEVHAoBvgtMSpN3z2MkXbwTIbpAM9QaOjYDxnvPovwx9ygEPKh5SrPm33yZcHcyDb7KjT4%2Br%2BgyoA%3D%3D';
    // mapx
    var lat = '126.981611';
    // mapy
    var lng = '37.568477';
    // 주변 범위
    var radius = '1000';
    // 모바일OS 안드로이드:AND
    var mobileOS = 'AND';
    // 어플 이름
    var appName = 'letsgoseoul';
    // 불러온 데이터 갯수
    var countFood = '20';
    // 불러올 데이터 종류, 음식점:39, 관광지:12
    var contentTypeId = '39';
    // 접근할 url 생성
    var url = 'http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?' +
        'ServiceKey=' + myKey +
        '&contentTypeId=' + contentTypeId +
        '&mapX=' + lat + '&mapY=' + lng +
        '&radius=' + radius +
        '&numOfRows=' + countFood +
        '&listYN=Y&arrange=E&MobileOS=' + mobileOS +
        '&MobileApp=' + appName + '&over&_type=json';

    //// url에서 정보 가져오기
    request(url, function(error, response, body) {
        if (!error && response.statusCode === 200) {
            // string -> obj
            var obj = JSON.parse(body);

            for (var i = 0; i < countFood; i++) {
                // 각각의 데이터(object)
                var tmp = new Object();
                // object들의 정보
                tmp.name = obj.response.body.items.item[i].title;
                tmp.tel = obj.response.body.items.item[i].tel;
                tmp.contentid = obj.response.body.items.item[i].contentid;
                tmp.lng = obj.response.body.items.item[i].mapx;
                tmp.lat = obj.response.body.items.item[i].mapy;

                // 음식 리스트에 추가
                food_list.push(tmp);

                console.log("title: " + obj.response.body.items.item[i].title + ", tel: " + obj.response.body.items.item[i].tel + ", contentID " + obj.response.body.items.item[i].contentid);

                console.log(obj.response.body.items.item[i]);
            }
            console.log(food_list);
            console.log(obj.response.body.items.item[5]);
        }
    });

    // search?type=place&q=seoul&fields=location, checkins&limit=500
    FB.api('/search', 'GET', {
            "type": "place",
            "q": "seoul",
            "fields": "location, checkins, name",
            "limit": "1000"
        },
        function(app) {
            // 받아온 데이터를 체크인 수로 내림차순 정렬!
            app.data.sort(checkinsSort);

            // 데이터 전체에서 city가 seoul인 데이터 추출
            for (var i = 0; i < app.data.length; i++) {
                if (app.data[i].location.city === 'Seoul') {
                    // 각각의 데이터(object)
                    var info_list = new Object();
                    // object들의 정보
                    info_list.name = app.data[i].name;
                    info_list.city = app.data[i].location.city;
                    info_list.lat = app.data[i].location.latitude;
                    info_list.lng = app.data[i].location.longitude;
                    info_list.checkins = app.data[i].checkins;

                    // list에 추가
                    list.push(info_list);
                }
            }

            callback(list.length);
            //var jsonInfo = JSON.stringify(list);
        });
}


module.exports.list = list;
module.exports.top_10_list = top_10_list;
module.exports.init = init;
