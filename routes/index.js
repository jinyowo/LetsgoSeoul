var index= function(req, res) {
	console.log('user 모듈 안에 있는 adduser 호출됨.');
	
	res.writeHead('200', {'Content-Type':'text/html;charset=utf8'});
	res.write('<h2>first page</h2>');
	res.end();
	
};

module.exports.index = index;