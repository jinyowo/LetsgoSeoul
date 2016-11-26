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
var init = function() {
    console.log('graph_api init 호출됨.');

    searchLocation(function() {
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
function searchLocation(callback) {

    FB.setAccessToken('EAAIFef3CbEcBAJse1tmfQyC46ttSw7pC3qakz7YVNsZCAjWdakk0ZCN9RiKZBEZBZB06AOPRIDOqHZBw1HZCFivvU14zWiUVQL1D9GEwmO3V4sn91NZCvxsZCZBKiEEyR43YAmASPxDVkQNBPaeFGPqFFeYFuBgJ7iCMoZD');

    // search?type=place&q=seoul&fields=location, checkins&limit=500
    FB.api('/search', 'GET', {
            "type": "place",
            "q": "seoul",
            "fields": "location, checkins, name",
            "limit": "1000"
        },
        function(res) {
            // 받아온 데이터를 체크인 수로 내림차순 정렬!
            res.data.sort(checkinsSort);

            // 데이터 전체에서 city가 seoul인 데이터 추출
            for (var i = 0; i < res.data.length; i++) {
                if (res.data[i].location.city === 'Seoul') {
                    // 각각의 데이터(object)
                    var info_list = new Object();
                    // object들의 정보
                    info_list.name = res.data[i].name;
                    info_list.city = res.data[i].location.city;
                    info_list.lat = res.data[i].location.latitude;
                    info_list.lng = res.data[i].location.longitude;
                    info_list.checkins = res.data[i].checkins;

                    // list에 추가
                    list.push(info_list);
                }
            }

            // 지워야되는 리스트 추가
            removeFaultData(list);
            //console.log("합치기 전: "+list.length);

            // 같은 위치 합치기
            mergeSameLocation(list);

            //합친 걸 체크인수로 다시 정렬
            list.sort(checkinsSort);

            // top 10 list 만들기
            for (i = 1; i <= 10; i++) {
                top_10_list[i] = list[i];

                //console.log("#" +i +" = " +top_10_list[i].name);
            }
            

            // list 이름 정리
		    for (i = 1; i <= 10; i++) {
		    	top_10_list[i].name = top_10_list[i].name.replace(/\./g, '');
		    	top_10_list[i].name = top_10_list[i].name.replace(/\,/g, '');
		    	top_10_list[i].name = top_10_list[i].name.replace(/[\u4E00-\u9FFF]+/g, '');
		    	top_10_list[i].name = top_10_list[i].name.replace(/\-/g, '');
		    	top_10_list[i].name = top_10_list[i].name.replace(/\//g, '');
		    }

		    for (i = 1; i <= 10; i++) {
		    	var str = top_10_list[i].name.split(" ");
			
		    	for (j = 0; j < str.length; j++) {
		    		if (j === 0) {
						if (!(str[j] === 'Seoul' || str[j] === 'seoul' ||
								str[j] === 'S' || str[j] === 's' ||
								str[j] === 'South' || str[j] === 'south' ||
								str[j] === 'Korea' || str[j] === 'korea')) {
							top_10_list[i].name = str[j];
						}
		    		}

		    		else {
		    			if (!(str[j] === 'Seoul' || str[j] === 'seoul' ||
		    					str[j] === 'S' || str[j] === 's' ||
		    					str[j] === 'South' || str[j] === 'south' ||
		    					str[j] === 'Korea' || str[j] === 'korea')) {
		    				if (str[j] === 'Tower') {
		    					top_10_list[i].name = top_10_list[i].name + " Seoul " + str[j];
		    				}
		    				
		    				else {
		    					top_10_list[i].name = top_10_list[i].name + " " + str[j];
		    				}
		    			}
		    		}
		    	}
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

    var removeWord = [
        "រាងកាយខ្ញុំគឺអាចសើចបានតែមិនប្រាកដថាចិត្តខ្ញុំសើចដែរនោះទេ",
        "ประเทศเกาหลีใ", "រដូវបុណ្យភ្ជុំកូននឹកពុកម៉ែណា", "Hotel", "hotel",
        "캐리", "Hyatt", "조선", "콘래드", "팰리스", "COEX", "orld", "Station",
        "Jeju", "Novotel", "Central", "SK텔레콤", "Kimchicrew", "Nami",
        "Ritz-Carlton", "Hospital", "Hilton", "호텔", "Incheon", "Parnas",
        "몰", "Ellui", "Pullman", "Fish"
    ];

    //removeWord 포함되면 지움
    for (i = 0; i < list.length; i++) {
        for (j = 0; j < list.length; j++) {
            if (list[i].name.indexOf(removeWord[j]) !== -1) {
                list.splice(i, 1);
                i--;
            }
        }

        // 한국을 벗어나는 지역 제거
        if(list[i].lat<35 || list[i].lat>38 || list[i].lng<123 || list[i].lng>128) {
			list.splice(i, 1);
			i--;
		}
    }
}

//같은 장소 합치기
function mergeSameLocation(list) {

    for (i = 1; i < list.length; i++) {
        var pivo = list[i];
        for (var j = i + 1; j < list.length; j++) {
            if ((Math.abs(list[j].lat - pivo.lat) <= 0.01) &&
                (Math.abs(list[j].lng - pivo.lng) <= 0.01)) {
                pivo.checkins += list[j].checkins;
                list.splice(j, 1);
                j--;
            }
        }
    }
}

module.exports.list = list;
module.exports.top_10_list = top_10_list;
module.exports.init = init;
