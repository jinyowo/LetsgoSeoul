/**
* http://localhost:3000/ 에 접속하면 처음으로 나오는 소개 페이지
*
*/

var index= function(req, res) {
	console.log('user 모듈 안에 있는 adduser 호출됨.');

	res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
	res.write('<h1>Node.js Server</h1>');
	res.write('<div><h2>Page Inforomation</h2></div>');
	res.write("<div><a href='/public/adduser.html'>Add user.</a>");
	res.write("<br><a href='/public/login.html'>Login.</a>");
	res.write("<br><a href='/public/listuser.html'>Show user list.</a>");
	res.write("<br><a href='/public/facebook.html'>Add location & Show location list.</a></div>");
	res.write("<br><a href='/public/tour.html'>Tour api.</a></div>");
	res.end();

};

module.exports.index = index;
