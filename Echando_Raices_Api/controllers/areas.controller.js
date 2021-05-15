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
            } else {
                res.status(400).json({ error: error });
            }
        });
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
        }
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
        res.status(200).json({ message: 'Work In Progress' });
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
