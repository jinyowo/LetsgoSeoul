/**
 *  tour API에서 받아온 데이터리스트를 부르는 메소드들
 *
 *  foodlist : 음식점 정보 리스트를 받아옴
 *  placelist : 명소 정보 리스트를 받아옴
 *  printDetail : 음식점이나 명소에 대한 상세설명을 받아옴
 */
var config = require('../config');
var tour_api = require('./tour_api');

// /tourapi/foodlist
var foodlist = function(req, res) {
	console.log('tour 모듈 안에 있는 foodlist 호출됨.');

    var paramLat = req.param('lat');
    var paramLng = req.param('lng');

    //food list
    tour_api.getFoodList(paramLat, paramLng, function(err, results) {
        console.log('getFoodList');
        if(err) {
            throw err;
        }
        if(results) {
            res.writeHead('200', {'Content-Type':'application/json, text/html;charset=utf8'});
            res.write(JSON.stringify(results));
            res.end();
        }
        else {
            res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
            res.write('<h2>데이터 불러오기 실패</h2>');
            res.end();
        }

    });
};

var placelist = function(req, res) {
    console.log('tour 모듈 안에 있는 placelist 호출됨.');

    var paramLat = req.param('lat');
    var paramLng = req.param('lng');

    //place list
    tour_api.getPlaceList(paramLat, paramLng, function(err, results) {
        console.log('getPlaceList');
        if(err) {
            throw err;
        }

        if(results) {
            res.writeHead('200', {'Content-Type':'application/json, text/html;charset=utf8'});
            res.write(JSON.stringify(results));
            res.end();
        }
        else {
            res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
            res.write('<h2>데이터 불러오기 실패</h2>');
            res.end();
        }

    });
};

// contentID로 상세설명을 받아옴
var printDetail = function(req, res) {
	console.log('tour 모듈 안에 있는 printDetail 호출됨.');

    var paramID = req.param('contentid');

    tour_api.getDetail(paramID, function(err, results) {
        console.log('getDetail');
        if (err) {
            throw err;
        }

        if (results) {
            res.writeHead('200', {'Content-Type': 'application/json, text/html;charset=utf8'});
            res.write(JSON.stringify(results));
            res.end();
        }
        else {
            res.writeHead('200', {'Content-Type': 'text/html;charset=utf8'});
            res.write('<h2>데이터 불러오기 실패</h2>');
            res.end();
        }

    });
};

module.exports.foodlist =  foodlist;
module.exports.placelist = placelist;
module.exports.printDetail = printDetail;
