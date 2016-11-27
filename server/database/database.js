/* database */
var mongodb = require('mongodb');
var mongoose = require('mongoose');

/* user init을 위해 추가 */
var user = require('../routes/user');
var facebook = require('../routes/facebook');

//database object에  db, schma, model 추
var database = {};

database.init = function(app, config) {
	console.log('database init()호출');

	connect(app, config);
}

//db에 연결하고 응답 객체의 속성으로 db객체 추가
function connect(app, config) {
	console.log('database connect()호출됨');

	// 데이터베이스 연결 : config의 설정 사용
	mongoose.connect(config.db_url);
	database.db = mongoose.connection;

	database.db.on('error', console.error.bind(console, 'mongoose connection error.'));
	database.db.on('open', function () {
		console.log('데이터베이스에 연결되었습니다. : ' + config.db_url);

		// config에 등록된 스키마 및 모델 객체 생성
		createSchema(app, config);

	});
	database.db.on('disconnected', connect);
}

function createSchema(app, config) {
	var schemaLen = config.db_schemas.length;
	console.log('설정에 정의된 스키마의 수 : %d', schemaLen);

	for (var i = 0; i < schemaLen; i++) {
		var curItem = config.db_schemas[i];

		// 모듈 파일에서 모듈 불러온 후 createSchema() 함수 호출하기
		var curSchema = require(curItem.file).createSchema(mongoose);
		console.log('%s 모듈을 불러들인 후 스키마 정의함.', curItem.file);

		// User 모델 정의
		var curModel = mongoose.model(curItem.collection, curSchema);
		console.log('%s 컬렉션을 위해 모델 정의함.', curItem.collection);

		// database 객체에 속성으로 추가
		database[curItem.schemaName] = curSchema;
		database[curItem.modelName] = curModel;
		console.log('스키마 이름 [%s], 모델 이름 [%s] 이 database 객체의 속성으로 추가됨.', curItem.schemaName, curItem.modelName);


	}

	app.set('database', database);

	//user database 설정
	user.init(database);
	facebook.init(database);

	console.log('database 객체가 app 객체의 속성으로 추가됨.');
}

module.exports = database;
