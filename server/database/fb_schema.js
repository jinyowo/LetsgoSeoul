/**
 * 
 * facebook 객체를 모듈로 만들기
 */

var Schema = {};

Schema.createSchema = function(mongoose) {
	
	// 스키마 정의
	var FacebookSchma = mongoose.Schema({
	    id: {type: Number, required: true},
	    checkins: {type: Number, required: true},
	    name: {type: String, index: 'hashed'},
	    lat: {type: Number, 'default': -1},
	    lng: {type: Number, 'default': -1}
	});
	
	// 스키마에 static 메소드 추가
	FacebookSchma.static('findById', function(id, callback) {
		return this.find({id:id}, callback);
	});
	
	FacebookSchma.static('findAll', function(callback) {
		return this.find({}, callback);
	});
	
	console.log('FacebookSchema 정의함.');

	return FacebookSchma;
};

// module.exports에 UserSchema 객체 직접 할당
module.exports = Schema;

