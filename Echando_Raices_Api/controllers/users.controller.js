const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const dbPool = require('../db/sqldb');

// GET ALL USERS
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
            res.status(500).json({ error: err });
        else {
            const id = req.params.userId;
            const sql = 'SELECT * FROM usuario WHERE idusuario = ' + conn.escape(id) + ';';
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
        }
        conn.release();
    });
}

// POST (ADD) NEW USER
exports.users_insert = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if(err)
            res.status(500).json({ error: err });
        else {
            // Check if Email already exists, if not create new User
            const email = req.body.email;
            if (/^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()\.,;\s@\"]+\.{0,1})+[^<>()\.,;:\s@\"]{2,})$/.test(email)) {
                conn.query('SELECT * FROM usuario WHERE email = ?', [email], async (error, rows, fields) => {
                    if(error)
                        throw error;

                    if(rows.length == 0) {
                        const salt = await bcrypt.genSalt(10);
                        const hashedPassword = await bcrypt.hash(req.body.password, salt);
                        var newUser = {
                            email: req.body.email,
                            password: hashedPassword,
                            permiso: process.env.USER_TIER_1
                        };

                        if(typeof req.body.name !== 'undefined' && req.body.name !== null)
                            newUser.nombre = req.body.name;
                        if(typeof req.body.last_name !== 'undefined' && req.body.last_name !== null)
                            newUser.primer_apellido = req.body.last_name;
                        if (typeof req.body.phone_num !== 'undefined' && req.body.phone_num !== null)
                            newUser.telefono = req.body.phone_num;

                        conn.query('INSERT INTO usuario SET ?', newUser, (error2, results, fields2) => {
                            if(error2)
                                throw error2;
                            res.status(201).json({
                                message: 'Created user successfully',
                                createdUser: {
                                    _id: results.insertId,
                                    email: newUser.email,
                                    permissionLevel: newUser.permiso,
                                    request: {
                                        type: 'GET',
                                        url: 'http://' + process.env.API_HOST + ':' + process.env.PORT + '/users/' + results.insertId
                                    }
                                }
                            });
                        });
                    } else {
                        res.status(409).json({ message: 'Email already in use' });
                    }
                });
            }
        }
        conn.release();
    });
}


// USER LOGIN
exports.users_login = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err)
            res.status(500).json({ error: err });
        else {
            // Check if Email already exists, if not create new User
            const email = req.body.email;
            if (/^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()\.,;\s@\"]+\.{0,1})+[^<>()\.,;:\s@\"]{2,})$/.test(email)) {
                conn.query('SELECT * FROM usuario WHERE email = ?', [email], (error, rows, fields) => {
                    if (error)
                        throw error;

                    if (rows.length > 0 && typeof req.body.password !== 'undefined' && req.body.password !== null) {
                        bcrypt.compare(req.body.password, rows[0].password, (passError, result) => {
                            if(passError)
                                return res.status(401).json({ message: 'Authentication Failed' });
                            
                            if(result) {
                                const token = jwt.sign({
                                    userId: rows[0].idusuario,
                                    permissionLevel: rows[0].permiso
                                }, process.env.TOKEN_SECRET, { expiresIn: '1h' });
                                
                                return res.status(200).json({
                                    message: 'Authentication Successful',
                                    token: token
                                });
                            }

                            res.status(401).json({ message: 'Authentication Failed' });
                        });
                    } else {
                        res.status(401).json({ message: 'Authentication Failed' });
                    }
                });
            }
        }
        conn.release();
    });
}
