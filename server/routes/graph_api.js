/*
 *  graph_api.js : Facebook graph API에서 장소 정보를 받아와서,
 * 	필터링, 정렬 하여 TOP10을 골라냄.
 * 
 * 	list : 최초 장소 리스트(name, city, lat, lng, checkins)
 *	top_10_list : TOP10 장소 리스
 */
var FB = require('fb');
var facebook = require('./facebook');

//장소 리스트
var list = new Array();
var top_10_list = new Array();

//데이터베이스 객체, 스키마 객체, 모델 객체를 이 모듈에서 사용할 수 있도록 전달함
var init = function(app, config) {
	console.log('graph_api init 호출됨.');
	
	searchLocation(app, config, function()
			{
				console.log('@@complete@@');
				
				facebook.addlocation();
			});
}

//sort 함수
function checkinsSort(a, b) {
	if (a.checkins === b.checkins) {
		return 0;
	}
	return a.checkins < b.checkins ? 1 : -1;
}

//데이터를 불러오는 함수
function searchLocation(app, config, callback) {
	
FB.setAccessToken('EAAIFef3CbEcBAJse1tmfQyC46ttSw7pC3qakz7YVNsZCAjWdakk0ZCN9RiKZBEZBZB06AOPRIDOqHZBw1HZCFivvU14zWiUVQL1D9GEwmO3V4sn91NZCvxsZCZBKiEEyR43YAmASPxDVkQNBPaeFGPqFFeYFuBgJ7iCMoZD');

// search?type=place&q=seoul&fields=location, checkins&limit=500
FB.api(	'/search',	'GET',
		{ 	"type" : "place", 
			"q" : "seoul",
			"fields" : "location, checkins, name",
			"limit" : "1000"	},
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
					
					// 지워야되는 리스트 추가
					removeFaultData(list);
					//console.log("합치기 전: "+list.length);

					// 같은 위치 합치기
					mergeSameLocation(list);
					list.sort(checkinsSort);

					//console.log(list);
					//console.log("************");
					// top 10 list 만들기
					for(i=1; i<=10; i++) {
						top_10_list[i] = list[i];

						//console.log("#" +i +" = " +top_10_list[i].name);
					}
					console.log("************");
					console.log(top_10_list);
					console.log("************");
					callback(list.length);
					//var jsonInfo = JSON.stringify(list);
				});
}
//쓸모없는 데이터를 수동으로 삭제해줌
function removeFaultData(list) {
	for (i = 0; i < list.length; i++) {
		if (list[i].name.indexOf("Hotel") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("hotel") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name
				.indexOf("រាងកាយខ្ញុំគឺអាចសើចបានតែមិនប្រាកដថាចិត្តខ្ញុំសើចដែរនោះទេ") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name
				.indexOf("រដូវបុណ្យភ្ជុំកូននឹកពុកម៉ែណាស់") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Hyatt") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("조선") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("콘래드") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("팰리스") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("SK텔레콤") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Kimchicrew") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Nami") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Ritz-Carlton") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Hospital") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Hilton") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("호텔") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Incheon") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("COEX") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("orld") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Station") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Parnas") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("몰") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Ellui") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Pullman") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Jeju") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Novotel") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Fish") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
		if (list[i].name.indexOf("Central") !== -1) {
			list.splice(i, 1);
			i--;
			continue;
		}
	}
}

//같은 장소 합치기
function mergeSameLocation(list) {
for (i = 1; i < list.length; i++) {
	var pivo = list[i];
	for (var j = i + 1; j < list.length; j++) {
		if ((pivo.lat > list[j].lat) && (pivo.lng > list[j].lng)) {
			if ((pivo.lat - list[j].lat <= 0.005) && (pivo.lng - list[j].lng) <= 0.005) {
				pivo.checkins += list[j].checkins;
				list.splice(j,1);
				j--;
				continue;
			}
		}
		if ((pivo.lat > list[j].lat) && (pivo.lng < list[j].lng)) {
			if ((pivo.lat - list[j].lat <= 0.005) && (list[j].lng - pivo.lng) <= 0.005) {
				pivo.checkins += list[j].checkins;
				list.splice(j,1);
				j--;
				continue;
			}
		}
		if ((pivo.lat < list[j].lat) && (pivo.lng > list[j].lng)) {
			if ((list[j].lat - pivo.lat <= 0.005) && (pivo.lng - list[j].lng) <= 0.005) {
				pivo.checkins += list[j].checkins;
				list.splice(j,1);
				j--;
				continue;
			}
		}
		if ((pivo.lat < list[j].lat) && (pivo.lng < list[j].lng)) {
			if ((list[j].lat - pivo.lat <= 0.005) && (list[j].lng - pivo.lng) <= 0.005) {
				pivo.checkins += list[j].checkins;
				list.splice(j,1);
				j--;
				continue;
			}
		}
	}
}
}

module.exports.list = list;
module.exports.top_10_list = top_10_list;
module.exports.init = init;
