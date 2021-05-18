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
                                    permissionLevel: row.permiso
                                }
                            })[0],
                            request: {
                                type: 'GET',
                                url: 'http://' + process.env.API_HOST + ':' + process.env.PORT + '/users'
                            }
                        }
                        res.status(200).json(response);
                    } else
                        res.status(404).json({ message: 'No valid entry for specified ID' });
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
            if (req.body && req.body.email && /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()\.,;\s@\"]+\.{0,1})+[^<>()\.,;:\s@\"]{2,})$/.test(req.body.email)) {
                const email = req.body.email;
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
            } else
                res.status(400).json({ message: 'Missing request body data' });
        }
        conn.release();
    });
}

// UPDATE SPECIFIED USER BY ID
exports.users_updateById = (req, res, next) => {
    dbPool.getConnection(async(err, conn) => {
        if(err) {
            conn.release();
            return res.status(500).json({ error: err });
        }

        const id = req.params.userId;
        if(id) {
            let sql = 'UPDATE usuario SET';
            const ops = req.body;
            // SQL Builder
            for (const key in ops) {
                if (Object.hasOwnProperty.call(ops, key)) {
                    const element = ops[key];
                    let col = 'skip';
                    switch (key) {
                        case 'password':
                            col = 'password';
                            const salt = await bcrypt.genSalt(10);
                            element = await bcrypt.hash(ops[key], salt);
                            break;

                        case 'name':
                            col = 'nombre';
                            break;

                        case 'last_name':
                            col = 'primer_apellido';
                            break;

                        case 'phone_num':
                            col = 'telefono';
                            break;
                        default:
                            break;
                    }
                    if(col != 'skip') {
                        sql += ' ' + conn.escapeId(col);
                        sql += ' = ' + conn.escape(element);
                        sql += ',';
                    }
                }
            }
            sql = sql.slice(0, -1);
            sql += ' WHERE idusuario = ' + conn.escape(id);
            
            conn.query(sql, (error, result, fields) => {
                if(error) {
                    res.status(404).json({ message: 'No valid entry for specified ID' });
                    throw error;
                }

                res.status(200).json({
                    message: 'User updated',
                    request: {
                        type: 'GET',
                        url: 'http://' + process.env.API_HOST + ':' + process.env.PORT + '/users/' + id,
                        body: {
                            password: 'String',
                            name: 'String',
                            last_name: 'String',
                            phone_num: 'String'
                        }
                    }
                });
            });
        } else 
            res.status(404).json({ message: 'No valid entry for specified ID' });

        conn.release();
    });
}

// DELETE SPECIFIED USER BY ID
exports.users_deleteById = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if(err) {
            conn.release();
            return res.status(500).json({ error: err });
        }

        const id = req.params.userId;
        const sql = 'DELETE FROM usuario WHERE idusuario = ' + conn.escape(id);
        conn.query(sql, (error, result, fields) => {
            if(error) {
                res.status(404).json({ message: 'No valid entry for specified ID' });
                throw error;
            }

            res.status(200).json({
                message: 'User deleted',
                request: {
                    type: 'POST',
                    url: 'http://' + process.env.API_HOST + ':' + process.env.PORT + '/users/signup',
                    body: {
                        email: 'String',
                        password: 'String',
                        name: 'String',
                        last_name: 'String',
                        phone_num: 'String'
                    }
                }
            });

        });

        conn.release();
    });
}

// GET SPECIFIED USER'S FORESTATIONS
exports.users_getForestationsById = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        const userId = req.params.userId;
        const sql = 'SELECT * FROM forestacion WHERE usuario_id = ' + conn.escape(userId);
        conn.query(sql, (err2, rows, fields) => {
            if (!err2) {
                const response = {
                    forestations: rows.map(row => {
                        return {
                            _id: row.idforestacion,
                            plant_count: row.num_plantas,
                            coords: row.coordenadas,
                            plant_type: row.tipo_planta_id,
                            userId: row.usuario_id,
                            areaId: row.espacio_id,
                            date: row.fecha,
                            request: {
                                type: 'GET',
                                url: 'http://' + process.env.API_HOST + ':' + process.env.PORT + '/forestations/' + row.idforestacion
                            }
                        }
                    })
                }
                res.status(200).json(response);
            } else {
                res.status(400).json({ error: error });
            }
        });
        conn.release();
    });
}

// UPDATE SPECIFIED USER'S FORESTATION BY ID
exports.users_updateForestationBydId = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        res.status(200).json({ message: 'Work In Progress' });
        conn.release();
    });
}
