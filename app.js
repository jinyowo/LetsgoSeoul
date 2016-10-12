
/**
 * 데이터베이스 사용하기
 * 
 * 라우팅과 몽고디비 관련 함수를 모듈로 구성하기
 * 
 */

//===== 모듈 불러들이기 =====//
var express = require('express')
  , http = require('http')
  , path = require('path');

var user = require('./routes/user');

var bodyParser = require('body-parser');
var cookieParser = require('cookie-parser');
var expressSession = require('express-session');
var expressErrorHandler = require('express-error-handler');

var mongodb = require('mongodb');
var mongoose = require('mongoose');

var config = require('./config');

// crypto 모듈 불러들이기
//var crypto = require('crypto');


//===== Express 서버 객체 만들기 =====//
var app = express();


//===== 서버 변수 설정 및 static으로 public 폴더 설정  =====//
console.log('config.server_port : %d', config.server_port);
app.set('port', config.server_port);

app.use('/public', express.static(path.join(__dirname, 'public')));

//===== body-parser, cookie-parser, express-session 사용 설정 =====//
app.use(bodyParser.urlencoded({extended: true}));

app.use(cookieParser());
app.use(expressSession({
	secret:'my key',
	resave:true,
	saveUninitialized:true
}));

//===== 라우터 미들웨어 사용 =====//
//app.use(app.router);


//===== 데이터베이스 연결 =====//

var database;
var UserSchema;
var UserModel;

//데이터베이스에 연결하고 응답 객체의 속성으로 db 객체 추가
function connectDB() {
	// 데이터베이스 연결 정보
	var databaseUrl = 'mongodb://localhost:27017/shopping';
	
	// 데이터베이스 연결
	mongoose.connect(databaseUrl);
	database = mongoose.connection;
	
	database.on('error', console.error.bind(console, 'mongoose connection error.'));	
	database.on('open', function () {
		console.log('데이터베이스에 연결되었습니다. : ' + databaseUrl);
		
		// user 스키마 및 모델 객체 생성
		createUserSchema();
		
	});
	database.on('disconnected', connectDB);

	
	// 1. app 객체에 database 속성 추가
	app.set('database', database);
		
}

// user 스키마 및 모델 객체 생성
function createUserSchema() {

	// 2. user_schema.js 모듈 불러오기
	UserSchema = require('./database/user_schema').createSchema(mongoose);
	
	
	// User 모델 정의
	UserModel = mongoose.model("users", UserSchema);
	console.log('UserModel 정의함.');
	
	
	// 3. UserSchema와 users 모델을 app에 추가
	app.set('UserSchema', UserSchema);
	app.set('UserModel', UserModel);
	
	// init 호출
	user.init(database, UserSchema, UserModel);
	
}

// 4. 로그인 처리 함수를 라우팅 모듈을 호출하는 것으로 수정
app.post('/process/login', user.login);

// 5. 사용자 추가 함수를 라우팅 모듈을 호출하는 것으로 수정
app.post('/process/adduser', user.adduser);

// 6. 사용자 리스트 함수를 라우팅 모듈을 호출하는 것으로 수정
app.post('/process/listuser', user.listuser);



//===== 404 에러 페이지 처리 =====//
var errorHandler = expressErrorHandler({
 static: {
   '404': './public/404.html'
 }
});

app.use( expressErrorHandler.httpError(404) );
app.use( errorHandler );


//===== 서버 시작 =====//

//// 프로세스 종료 시에 데이터베이스 연결 해제
//process.on('SIGTERM', function () {
//    console.log("프로세스가 종료됩니다.");
//    app.close();
//});
//
//app.on('close', function () {
//	console.log("Express 서버 객체가 종료됩니다.");
//	if (database) {
//		database.close();
//	}
//});

http.createServer(app).listen(app.get('port'), function(){
	console.log('서버가 시작되었습니다. 포트 : ' + app.get('port'));

	// 데이터베이스 연결
	connectDB();
   
});

