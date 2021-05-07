const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const dbPool = require('../db/sqldb');

// GET ALL USERS (EN VERSION FINAL QUITAR PASSWORD)
exports.users_getAll = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if(err)
            res.status(500).json({ error: err });
        else {
            const sql = 'SELECT * FROM usuario;';
            conn.query(sql, (error, rows, fields) => {
                if(!error) {
                    const response = {
                        users: rows.map(row => {
                            return {
                                _id: row.idusuario,
                                email: row.email,
                                name: row.nombre,
                                last_name: row.primer_apellido,
                                phone_num: row.telefono,
                                permissionLevel: row.permiso,
                                request: {
                                    type: 'GET',
                                    url: 'http://' + process.env.API_HOST + ':' + process.env.PORT + '/users/' + row.idusuario
                                }
                            }
                        })
                    }
                    res.status(200).json(response);
                }
            });
        }
        conn.release();
    });
}

// GET SPECIFIED USER BY ID
exports.users_getBydId = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if(err)
            return res.status(500).json({ error: err });
        
        const id = req.params.userId;
        const sql = 'SELECT * FROM usuario WHERE idusuario = ' + id + ';';
        conn.query(sql, (error, rows, fields) => {
            if(!error) {
                if(rows.length > 0) {
                    const response = {
                        user: rows.map(row => {
                            return {
                                _id: row.idusuario,
                                email: row.email,
                                name: row.nombre,
                                last_name: row.primer_apellido,
                                phone_num: row.telefono,
                                permissionLevel: row.permiso,
                                request: {
                                    type: 'GET',
                                    url: 'http://' + process.env.API_HOST + ':' + process.env.PORT + '/users'
                                }
                            }
                        })[0] // <- so it isn't sent as json array
                    }
                    res.status(200).json(response);
                } else {
                    res.status(404).json({ message: 'No valid entry for specified ID' });
                }
            }
        });
    })

}
