
//===== 모듈 불러들이기 =====//
var express = require('express')
  , http = require('http')
  , path = require('path');

var schedule = require('node-schedule');

var database = require('./database/database');
var graph_api = require('./routes/facebook_api');
var tour_api = require('./routes/tour_api');

var user = require('./routes/user');
var route_loader = require('./routes/route_loader');

var bodyParser = require('body-parser');
var cookieParser = require('cookie-parser');
var expressSession = require('express-session');
var expressErrorHandler = require('express-error-handler');

var config = require('./config');


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

// 프로세스 종료 시에 데이터베이스 연결 해제
process.on('SIGTERM', function () {
    console.log("프로세스가 종료됩니다.");
    app.close();
});

app.on('close', function () {
	console.log("Express 서버 객체가 종료됩니다.");
	if (database) {
		database.close();
	}
});

// 스케줄링 객체에 대한 핸들을 가지는 변수
var scheduleHandle = null;

// 외부 서버로부터 컨텐츠를 제공받음
var contentsReceiver = function() {
	console.log("\n\n=====컨텐츠 리시버 시작=====");
	graph_api.init();
	console.log("\n\n=====컨텐츠 리시버 종료=====");
}

// 서버 시작
http.createServer(app).listen(app.get('port'), function(){
	console.log('서버가 시작되었습니다. 포트 : ' + app.get('port'));

	// 서버시작과 함꼐 데이터베이스 초기화
	database.init(app, config);

	//서버 시작과 함께 최초 한번 컨텐츠를 받아옴
	contentsReceiver();

	// 이후 스케줄된 시간에 맞춰 컨텐츠를 받아옴 // SECOND, MINUTE, HOUR ,DAY OF MONTH, DAY OF WEEK
	scheduleHandle = schedule.scheduleJob('0 0 24 * * *', contentsReceiver); // 매 24:00:00초에 데이터를 한번 갱신

});
