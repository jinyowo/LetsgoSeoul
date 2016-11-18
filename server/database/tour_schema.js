/**
 *
 * tour api 객체를 모듈로 만들기
 *
 */

var Schema = {};

Schema.createSchema = function(mongoose) {

	// 스키마 정의
	var TourSchma = mongoose.Schema({
	    id: {type: Number, required: true},
	    name: {type: String, index: 'hashed'},
	    lat: {type: Number, 'default': -1},
	    lng: {type: Number, 'default': -1},
      image: {type: String, 'default': 'image'}
	});

	// 스키마에 static 메소드 추가
	TourSchma.static('findById', function(id, callback) {
		return this.find({id:id}, callback);
	});

	TourSchma.static('findAll', function(callback) {
		return this.find({}, callback);
	});

	console.log('TourSchema 정의함.');

	return TourSchma;
};

// module.exports에 TourSchma 객체 직접 할당
module.exports = Schema;
