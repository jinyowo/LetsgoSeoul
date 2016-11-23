/*
 * 데이터베이스 관련 객체들을 init() 메소드로 설정
 *
 */
var config = require('../config');
var tour_api = require('./tour_api');

var database;
var TourSchema;
var TourModel;

var foodList;
// var placeList;

// 데이터베이스 객체, 스키마 객체, 모델 객체를 이 모듈에서 사용할 수 있도록 전달함
var init = function(db) {
	console.log('tour init 호출됨.');

	database = db;
	TourSchema = database[config.db_schemas[2].schemaName];
	TourModel = database[config.db_schemas[2].modelName];

}
// /tourapi/inputLocation
// graph API에서 불러온 정보를 데이터베이스에 add하는 함수
var inputLocation = function(req, res) {
	console.log('tour 모듈 안에 있는 inputLocation 호출됨.');

    var paramLat = req.param('lat');
    var paramLng = req.param('lng');
    //
    // res.writeHead('200', {'Content-Type':'application/json, text/html;charset=utf8'});
    // res.write('<h3>');
    // res.write("<Location = " + paramLat+ " , " + paramLng);
    // res.write('</h3>');

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

    //place list
    //tour_api.getPlaceList(paramLat, paramLng);

};

var place = function(req, res) {
    console.log("place 불림");
    console.log(req.param('lat') + " , " + req.param('lng'));

    res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
    res.write('<h2>place</h2>');
    res.end();
}

var printFoodList = function(req, res) {
	console.log('tour 모듈 안에 있는 printFoodList 호출됨.');

			// if (foodList) {
			// 	res.writeHead('200', {'Content-Type' : 'application/json, text/html; charset=utf8'});
			// 	res.write(JSON.stringify(foodList));
			// 	res.end();
			// } else {
			// 	res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
			// 	res.write('<h2>facebook 리스트 조회  실패</h2>');
			// 	res.write("<br><br><a href='/'>Back to Main Page</a>");
			// 	res.end();
			// }

};
//
// var placelist = function(req, res) {
// 	console.log('tour 모듈 안에 있는 placelist 호출됨.');
//
// 	if (database) {
// 		TourModel.findAll(function(err, results) {
// 			if (err) {
// 				callback(err, null);
// 				return;
// 			}
//
// 			if (results) {
// 				console.dir(results);
// 				res.writeHead('200', {'Content-Type' : 'application/json, text/html; charset=utf8'});
// 				//res.write(JSON.stringify(results));
// 				res.write("<br><br><a href='/'>Back to Main Page</a>");
// 				res.end();
// 			} else {
// 				res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
// 				res.write('<h2>facebook 리스트 조회  실패</h2>');
// 				res.write("<br><br><a href='/'>Back to Main Page</a>");
// 				res.end();
// 			}
// 		});
// 	} else {
// 		res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
// 		res.write('<h2>데이터베이스 연결 실패</h2>');
// 		res.write("<br><br><a href='/'>Back to Main Page</a>");
// 		res.end();
// 	}
//
// };



module.exports.init = init;
module.exports.inputLocation =  inputLocation;
module.exports.place = place;
// module.exports.foodlist = foodlist;
// module.exports.placelist = placelist;
