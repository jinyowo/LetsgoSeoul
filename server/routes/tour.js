/*
 *
 *
 */
var config = require('../config');
var tour_api = require('./tour_api');

// var foodList;
// var placeList;

// /tourapi/inputLocation
// Tour API에서 불러온 정보를 데이터베이스에 add하는 함수
var foodlist = function(req, res) {
	console.log('tour 모듈 안에 있는 foodlist 호출됨.');

    var paramLat = req.param('lat');
    var paramLng = req.param('lng');

    console.log("<Location = " + paramLat+ " , " + paramLng);
    /*add database*/

    //food list
    tour_api.getFoodList(paramLat, paramLng, function(err, results) {
        console.log('getFoodList');
        if(err) {
            throw err;
        }

        if(results) {
            //foodList = tour_api.foodList;
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

    //food list
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
