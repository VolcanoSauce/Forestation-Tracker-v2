const dbPool = require('../db/sqldb');

// GET ALL FORESTATIONS
exports.forestations_getAll = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        const sql = 'SELECT * FROM forestacion';
        conn.query(sql, (error, rows, fields) => {
            if(!error) {
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

// GET SPECIFIED FORESTATION BY ID
exports.forestations_getById = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }

        const id = req.params.forestationId;
        const sql = 'SELECT * FROM forestacion WHERE idforestacion = ' + conn.escape(id);
        conn.query(sql, (error, rows, fields) => {
            if(!error) {
                if(rows.length > 0) {
                    const response = {
                        forestation: rows.map(row => {
                            return {
                                _id: row.idforestacion,
                                plant_count: row.num_plantas,
                                coords: row.coordenadas,
                                plant_type: row.tipo_planta_id,
                                userId: row.usuario_id,
                                areaId: row.espacio_id,
                                date: row.fecha
                            }
                        })[0],
                        request: {
                            type: 'GET',
                            url: 'http://' + process.env.API_HOST + ':' + process.env.PORT + '/forestations'
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

// POST NEW PLANT TYPE
exports.forestation_insertPlantType = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        if (req.body && req.body.name) {
            const newPlantType = { nombre: req.body.name };
            conn.query('INSERT INTO tipo_planta SET ?', newPlantType, (err2, results, fields) => {
                if (err2)
                    throw err2;
                res.status(201).json({
                    message: 'Created plant type successfully',
                    createdPlantType: {
                        _id: results.insertId,
                        name: newPlantType.nombre
                    }
                });
            });
        } else
            res.status(400).json({ message: 'Missing request body data' });
        conn.release();
    });
}

// POST NEW FORESTATION
exports.forestation_insert = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        res.status(200).json({ message: 'Work In Progress' });
        conn.release();
    });
}

// UPDATE SPECIFIED FORESTATION BY ID
exports.forestation_updateById = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        res.status(200).json({ message: 'Work In Progress' });
        conn.release();
    });
}

// DELETE SPECIFIED FORESTATION BY ID
exports.forestation_deleteById = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        res.status(200).json({ message: 'Work In Progress' });
        conn.release();
    });
}
