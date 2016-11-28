/**
 * 
 * facebook schema 객체를 모듈로 만들기
 */

var Schema = {};

Schema.createSchema = function(mongoose) {
	
	// 스키마 정의
	var FacebookSchema = mongoose.Schema({
	    id: {type: Number, required: true},
	    checkins: {type: Number, required: true},
	    name: {type: String, index: 'hashed'},
	    lat: {type: Number, 'default': -1},
	    lng: {type: Number, 'default': -1}
	});
	
	// 스키마에 static 메소드 추가 : id로 find
    FacebookSchema.static('findById', function(id, callback) {
		return this.find({id:id}, callback);
	});

	//모든 도큐멘트를 검색하는 메소드. 체크인수로 내림차순 정렬되어 검색함(체크인이 많으면 먼저 검색)
    FacebookSchema.static('findAll', function(callback) {
		return this.find({}, callback).sort({checkins:-1});
	});

	return FacebookSchema;
};

// module.exports에 FacebookSchma 객체 직접 할당
module.exports = Schema;

