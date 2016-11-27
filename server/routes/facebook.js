/*
 * 데이터베이스 관련 객체들을 init() 메소드로 설정
 *
 */
var config = require('../config');
var graph_api = require('./facebook_api');
var tour_api = require('./tour_api');

var database;
var FacebookSchema;
var FacebookModel;
var list = graph_api.top_10_list;

// 데이터베이스 객체, 스키마 객체, 모델 객체를 이 모듈에서 사용할 수 있도록 전달함
var init = function(db) {
	console.log('facebook  init 호출됨.');

	database = db;
	FacebookSchema = database[config.db_schemas[1].schemaName];
	FacebookModel = database[config.db_schemas[1].modelName];

}

// graph API에서 불러온 정보를 데이터베이스에 add하는 함수
function addlocation() {
	console.log('facebook 모듈 안에 있는 addLocation 호출됨.');

	if (database) {
		for(var i=1; i<=10; i++)
		{
			addLocation(database, list[i].id, list[i].checkins, list[i].name, list[i].lat, list[i].lng, function(err, result) {

			if (err) {throw err;}
			if (result) {
				console.log('facebook data 추가 성공!');
			} else {
				console.log('facebook data 추가 실패!');
			}
			});
		}
	} else {
		console.log('db연결실패!');
	}

};

//데이터베이스에 있는 페이스북 리스트 받아오기
var listlocation = function(req, res) {
	console.log('facebook 모듈 안에 있는 listlocation 호출됨.');

	if (database) {
		FacebookModel.findAll(function(err, results) {
			if (err) {
				callback(err, null);
				return;
			}

			if (results) {
				console.dir(results);

				res.writeHead('200', {'Content-Type' : 'application/json, text/html; charset=utf8'});
				res.write(JSON.stringify(results));
				res.write("<br><br><a href='/'>Back to Main Page</a>");
				res.end();
			} else {
				res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
				res.write('<h2>facebook 리스트 조회  실패</h2>');
				res.write("<br><br><a href='/'>Back to Main Page</a>");
				res.end();
			}
		});
	} else {
		res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
		res.write('<h2>데이터베이스 연결 실패</h2>');
		res.write("<br><br><a href='/'>Back to Main Page</a>");
		res.end();
	}

};

//장소를 등록하는 함수
var addLocation = function(database, id, checkins, name, lat, lng, callback) {
	console.log('addLocation 호출됨.');

	// FacebookModel 인스턴스 생성
	var facebook = new FacebookModel({"id":id, "checkins":checkins, "name":name, "lat":lat, "lng":lng});

	// 해당 id가 이미 데이터베이스에 존재하는 id면 기존의 document를 update하고
	// 새로운 id면 새로운 document를 insert한다
	FacebookModel.findById(id, function(err, results) {
		if (err) {
			callback(err, null);
			return;
		}

		if (results.length > 0) {
			console.log(id+ '이미 존재하는 장소정보');
			// update()로 갱신
			facebook.update(
					{id : id},
					{name: name,
					checkins:checkins,
					lat:lat,
					lng:lng},
					function(err) { console.log(err); });
			console.log('update');

		} else {
	    	console.log(id + "새로운 장소정보");
	    	// save()로 저장
	    	facebook.save(function(err) {
	    		if (err) {
	    			callback(err, null);
	    			return;
	    		}
	    	console.log("장소 데이터 추가함.");
	    	});
		}

	    callback(null, facebook);

	});
}


module.exports.init = init;
module.exports.addlocation = addlocation;
module.exports.listlocation = listlocation;
