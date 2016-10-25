/*
 * 데이터베이스 관련 객체들을 init() 메소드로 설정
 * 
 */
var config = require('../config');
var graph_api = require('./graph_api');

var database;
var FacebookSchema;
var FacebookModel;

// 데이터베이스 객체, 스키마 객체, 모델 객체를 이 모듈에서 사용할 수 있도록 전달함
var init = function(db) {
	console.log('facebook  init 호출됨.');
	
	database = db;
	FacebookSchema = database[config.db_schemas[1].schemaName];
	FacebookModel = database[config.db_schemas[1].modelName];
	
}


function addlocation() {
	console.log('facebook 모듈 안에 있는 addLocation 호출됨.');
	
	var paramId;
	var list = graph_api.list;
	
	if (database) {
		for(var i=1; i<=10; i++)
		{
			/*이미 존재하는 데이터의 경우 삽입을 안하도록 구*/
			addLocation(database, i, list[i].checkins, list[i].name, list[i].lat, list[i].lng, function(err, result) {
			if (err) {throw err;}
			
			if (result) {
				console.log('facebook data 추가 성공!');
				console.dir(result);
			} else {
				console.log('facebook data 추가 실패!');
			}
			});
		}
	} else {
		console.log('db연결실패!');
	}
	
};

var listlocation = function(req, res) {
	console.log('facebook 모듈 안에 있는 listlocation 호출됨.');
	
	if (database) {
		// 1. 모든 사용자 검색
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
	
	facebook.isNew = false;
	console.log(facebook.id);
	// save()로 저장
	facebook.save(function(err) {
		if (err) {
			callback(err, null);
			return;
		}
		
	    console.log("장소 데이터 추가함.");
	    callback(null, facebook);
	     
	});
}


module.exports.init = init;
module.exports.addlocation = addlocation;
module.exports.listlocation = listlocation;

