const dbPool = require('../db/sqldb');

// GET ALL AREAS
exports.areas_getAll = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if(err) {
            conn.release();
            return res.status(500).json({ error: err});
        }
        const sql = 'SELECT * FROM espacio;';
        conn.query(sql, (error, rows, fields) => {
            if (!error) {
                const response = {
                    areas: rows.map(row => {
                        return {
                            _id: row.idespacio,
                            name: row.nombre,
                            email: row.email,
                            phone_num: row.telefono,
                            area_type: row.tipo_espacio_id,
                            address: row.direccion_id,
                            request: {
                                type: 'GET',
                                url: 'http://' + process.env.API_HOST + ':' + process.env.PORT + '/areas/' + row.idespacio
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

// GET SPECIFIED AREA BY ID
exports.areas_getById = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if(err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        const id = req.params.areaId;
        const sql = 'SELECT * FROM espacio WHERE idespacio = ' + conn.escape(id);
        conn.query(sql, (err2, rows, fields) => {
            if (!err2) {
                if (rows.length > 0) {
                    const response = {
                        area: rows.map(row => {
                            return {
                                _id: row.idespacio,
                                name: row.nombre,
                                email: row.email,
                                phone_num: row.telefono,
                                area_type: row.tipo_espacio_id,
                                address: row.direccion_id
                            }
                        })[0],
                        request: {
                            type: 'GET',
                            url: 'http://' + process.env.API_HOST + ':' + process.env.PORT + '/areas'
                        }
                    }
                    res.status(200).json(response);
                } else
                    res.status(404).json({ message: 'No valid entry for specified ID' });
            } else
                res.status(400).json({ error: error });
        });
        conn.release();
    });
}

// GET ALL AREA TYPES
exports.areas_getAllAreaTypes = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if(err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        const sql = 'SELECT * FROM tipo_espacio';
        conn.query(sql, (err2, rows, fields) => {
            if(!err2) {
                const response = {
                    area_types: rows.map(row => {
                        return {
                            _id: row.id_tipo_espacio,
                            name: row.nombre
                        }
                    })
                }
                res.status(200).json(response);
            } else
                res.status(400).json({ error: error });
        })
        conn.release();
    });
}

// GET ALL ADDRESSES
exports.areas_getAllAddresses = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        const sql = 'SELECT * FROM direccion';
        conn.query(sql, (err2, rows, fields) => {
            if(!err2) {
                const response = {
                    addresses: rows.map(row => {
                        return {
                            _id: row.iddireccion,
                            address: row.direccion,
                            address2: row.direccion2,
                            cityId: row.ciudad_id
                        }
                    })
                }
                res.status(200).json(response);
            } else
                res.status(400).json({ error: error });
        })
        conn.release();
    });
}

// GET ALL CITIES
exports.areas_getAllCities = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        const sql = 'SELECT * FROM ciudad';
        conn.query(sql, (err2, rows, fields) => {
            if (!err2) {
                const response = {
                    cities: rows.map(row => {
                        return {
                            _id: row.idciudad,
                            name: row.nombre,
                            stateId: row.estado_id
                        }
                    })
                }
                res.status(200).json(response);
            } else
                res.status(400).json({ error: error });
        })
        conn.release();
    });
}

// GET ALL STATES
exports.areas_getAllStates = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        const sql = 'SELECT * FROM estado';
        conn.query(sql, (err2, rows, fields) => {
            if (!err2) {
                const response = {
                    states: rows.map(row => {
                        return {
                            _id: row.idestado,
                            name: row.nombre
                        }
                    })
                }
                res.status(200).json(response);
            } else
                res.status(400).json({ error: error });
        })
        conn.release();
    });
}

// GET ALL CITIES IN SPECIFIED STATE
exports.areas_getCitiesByStateId = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        const stateId = req.params.stateId;
        const sql = 'SELECT * FROM ciudad WHERE estado_id = ' + conn.escape(stateId);
        conn.query(sql, (err2, rows, fields) => {
            if (!err2) {
                const response = {
                    cities: rows.map(row => {
                        return {
                            _id: row.idciudad,
                            name: row.nombre,
                            stateId: row.estado_id
                        }
                    })
                }
                res.status(200).json(response);
            } else
                res.status(400).json({ error: error });
        })
        conn.release();
    });
}

// POST (CREATE) NEW AREA
exports.areas_insert = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        if(req.body && req.body.name && req.body.areaTypeId && req.body.addressId) {
            var newArea = {
                nombre: req.body.name,
                tipo_espacio_id: req.body.areaTypeId,
                direccion_id: req.body.addressId
            }
            if(req.body.email)
                newArea.email = req.body.email;
            if(req.body.phone_num)
                newArea.telefono = req.body.phone_num;
            conn.query('INSERT INTO espacio SET ?', newArea, (err2, results, fields) => {
                if(err2)
                    throw err2;
                res.status(201).json({
                    message: 'Created area successfully',
                    createdArea: {
                        _id: results.insertId,
                        name: newArea.nombre,
                        email: newArea.email,
                        phone_num: newArea.telefono,
                        areaTyepId: newArea.tipo_espacio_id,
                        addressId: newArea.direccion_id
                    }
                });
            });
        } else
            res.status(400).json({ message: 'Missing request body data' });
        conn.release();
    });
}

// POST NEW AREA TYPE
exports.areas_insertAreaType = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if(err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        if(req.body && req.body.name) {
            const newAreaType = { nombre: req.body.name };
            conn.query('INSERT INTO tipo_espacio SET ?', newAreaType, (err2, results, fields) => {
                if(err2)
                    throw err2;
                res.status(201).json({
                    message: 'Created area type successfully',
                    createdAreaType: {
                        _id: results.insertId,
                        name: newAreaType.nombre
                    }
                });
            });
        } else
            res.status(400).json({ message: 'Missing request body data' });
        conn.release();
    });
}

// UPDATE SPECIFIED AREA BY ID
exports.areas_updateById = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        res.status(200).json({ message: 'Work In Progress' });
        conn.release();
    });
}

// DELETE SPECIFIED AREA BY ID
exports.areas_deleteById = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        res.status(200).json({ message: 'Work In Progress' });
        conn.release();
    });
}
