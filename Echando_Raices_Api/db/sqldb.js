const mysql = require('mysql');

// REFS:
// https://stackoverflow.com/questions/18496540/node-js-mysql-connection-pooling/18496936
// https://stackoverflow.com/questions/35914736/node-js-and-mysql-connection-pool-does-not-export
// https://github.com/mysqljs/mysql

const pool = mysql.createPool({
    connectionLimit: 10,
    host: process.env.DB_HOST,
    user: process.env.USER,
    password: process.env.PASSWD,
    database: process.env.DB_NAME
});
/* To export getConnection | usage const getConnection = require(../db/sqldb.js) */
// const getConnection = (callback) => {
//     pool.getConnection((err, connection) => {
//         callback(err, connection);
//     });
// };
// module.exports = getConnection;
module.exports = pool;
