
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

var database = require('./database/database');
var user = require('./routes/user');
var route_loader = require('./routes/route_loader');

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
route_loader.init(app);


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
	database.init(app, config);
	//connectDB();
	// init 호출
	//user.init(database, UserSchema, UserModel);
	
   
});

