const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const dbPool = require('../db/sqldb');

// GET ALL USERS (EN VERSION FINAL QUITAR PASSWORD)
exports.users_list = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if(err) {
            res.status(500).json({ error: err });
        } else {
            const sql = 'SELECT * FROM usuario';
            conn.query(sql, (error, rows, fields) => {
                if(!error) {
                    res.status(200).json(rows);
                }
            });
        }
        conn.release();
    });
}
