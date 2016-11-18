///////↓↓↓↓↓========== 주변 음식점 데이터 가져오는 부분 ==========↓↓↓↓↓/////
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
var url = 'http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?'
       + 'ServiceKey=' + myKey
       + '&contentTypeId=' + contentTypeId
       + '&mapX=' + lat + '&mapY=' + lng
       + '&radius=' + radius
       + '&numOfRows=' + countFood
       + '&listYN=Y&arrange=E&MobileOS=' + mobileOS
       + '&MobileApp=' + appName + '&over&_type=json';

//// url에서 정보 가져오기
request(url, function(error, response, body) {
if (!error && response.statusCode === 200) {
// string -> obj
var obj = JSON.parse(body);

for(var i=0; i<countFood; i++) {
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

console.log("title: "+obj.response.body.items.item[i].title+", tel: "+obj.response.body.items.item[i].tel+", contentID "+obj.response.body.items.item[i].contentid);

console.log(obj.response.body.items.item[i]);
}
console.log(food_list);
console.log(obj.response.body.items.item[5]);
}
});
///////↑↑↑↑↑========== 주변 음식점 데이터 가져오는 부분 ==========↑↑↑↑↑/////

/////////↓↓↓↓↓========== 주변 관광지 데이터 가져오는 부분 ==========↓↓↓↓↓/////
//주소로부터 데이터 가져오기
var request = require('request');
//http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=ePhJZR2dULEVHAoBvgtMSpN3z2MkXbwTIbpAM9QaOjYDxnvPovwx9ygEPKh5SrPm33yZcHcyDb7KjT4%2Br%2BgyoA%3D%3D&contentTypeId=39&mapX=126.981611&mapY=37.568477&radius=1000&pageNo=1&numOfRows=10&listYN=Y&arrange=A&MobileOS=ETC&MobileApp=AppTesting&over&_type=json
//관광공사 API 접근 인증키
var myKey = 'ePhJZR2dULEVHAoBvgtMSpN3z2MkXbwTIbpAM9QaOjYDxnvPovwx9ygEPKh5SrPm33yZcHcyDb7KjT4%2Br%2BgyoA%3D%3D';
//mapx
var lat = '126.981611';
//mapy
var lng = '37.568477';
//주변 범위
var radius = '1000';
//모바일OS 안드로이드:AND
var mobileOS = 'AND';
//어플 이름
var appName = 'letsgoseoul';
//불러온 데이터 갯수
var countSpot = '20';
//불러올 데이터 종류, 음식점:39, 관광지:12
var contentTypeId = '12';
//접근할 url 생성
var url = 'http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?'
       + 'ServiceKey=' + myKey
       + '&contentTypeId=' + contentTypeId
       + '&mapX=' + lat + '&mapY=' + lng
       + '&radius=' + radius
       + '&numOfRows=' + countSpot
       + '&listYN=Y&arrange=E&MobileOS=' + mobileOS
       + '&MobileApp=' + appName + '&over&_type=json';

//url에서 정보 가져오기
request(url, function(error, response, body) {
if (!error && response.statusCode === 200) {
// string -> obj
var obj = JSON.parse(body);
// console.log(obj);
for(var i=0; i<countSpot; i++) {
// 각각의 데이터(object)
var tmp = new Object();
// object들의 정보
tmp.name = obj.response.body.items.item[i].title;
tmp.tel = obj.response.body.items.item[i].tel;
tmp.contentid = obj.response.body.items.item[i].contentid;
tmp.lng = obj.response.body.items.item[i].mapx;
tmp.lat = obj.response.body.items.item[i].mapy;

// 관광지 리스트에 추가
spot_list.push(tmp);

console.log("title: "+obj.response.body.items.item[i].title+",
tel: "+obj.response.body.items.item[i].tel+", contentID:
"+obj.response.body.items.item[i].contentid);
console.log(obj.response.body.items.item[i]);
}
console.log(spot_list);
console.log(obj.response.body.items.item[5].contentid);
}
});
/////////↑↑↑↑↑========== 주변 관광지 데이터 가져오는 부분 ==========↑↑↑↑↑/////

/////////↓↓↓↓↓========== 상세 정보 가져오는 부분 ==========↓↓↓↓↓/////
// 주소로부터 데이터 가져오기
var request = require('request');
// http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=ePhJZR2dULEVHAoBvgtMSpN3z2MkXbwTIbpAM9QaOjYDxnvPovwx9ygEPKh5SrPm33yZcHcyDb7KjT4%2Br%2BgyoA%3D%3D&contentTypeId=39&mapX=126.981611&mapY=37.568477&radius=1000&pageNo=1&numOfRows=10&listYN=Y&arrange=A&MobileOS=ETC&MobileApp=AppTesting&over&_type=json
// 관광공사 API 접근 인증키
var myKey = 'ePhJZR2dULEVHAoBvgtMSpN3z2MkXbwTIbpAM9QaOjYDxnvPovwx9ygEPKh5SrPm33yZcHcyDb7KjT4%2Br%2BgyoA%3D%3D';
// 모바일OS 안드로이드:AND
var mobileOS = 'AND';
// 어플 이름
var appName = 'letsgoseoul';
// 가게 id
var contentid = '264311'; // 음식점:133855, 관광지:264311
// 접근할 url 생성
// http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?ServiceKey=인증키
// &contentId=126508&defaultYN=Y&addrinfoYN=Y&overviewYN=Y&MobileOS=ETC&MobileApp=AppTesting
var url = 'http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?'
       + 'ServiceKey='
       + myKey
       + '&contentId='
       + contentid
       + '&defaultYN=Y&overviewYN=Y&addinfoYN=Y&mapinfoYN=Y'
       + '&MobileOS='
       + mobileOS + '&MobileApp=' + appName + '&_type=json';

//// url에서 정보 가져오기
request(url, function(error, response, body) {
   if (!error && response.statusCode === 200) {
       // string -> obj
       var obj = JSON.parse(body);
       var overview = obj.response.body.items.item.overview;

       console.log(obj.response.body.items.item);
   }
});
/////////↑↑↑↑↑========== 상세 정보 가져오는 부분 ==========↑↑↑↑↑/////
