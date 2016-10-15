/*
 * 설정
 */
module.exports = {
	server_port: 3000,
	db_url: 'mongodb://localhost:27017/shopping',
	db_schemas: [
	    {file:'./user_schema', collection:'users', schemaName:'UserSchema', modelName:'UserModel'}
	],
	route_info: [
	    //===== User =====//
	    {file:'./index', path:'/', method:'index', type:'get'},					// user.login 
	    {file:'./user', path:'/process/login', method:'login', type:'post'},					// user.login 
	    {file:'./user', path:'/process/adduser', method:'adduser', type:'post'},				// user.adduser 
	    {file:'./user', path:'/process/listuser', method:'listuser', type:'post'}			// user.listuser 
	]
}