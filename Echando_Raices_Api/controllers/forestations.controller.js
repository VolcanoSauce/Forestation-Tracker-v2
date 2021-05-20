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
                            image: row.imagen_id,
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
                                date: row.fecha,
                                image: row.imagen_id
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
            } else
                res.status(400).json({ error: error });
        });
        conn.release();
    });
}

// GET ALL PLANT TYPES
exports.forestations_getAllPlantTypes = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if(err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        const sql = 'SELECT * FROM tipo_planta';
        conn.query(sql, (err2, rows, fields) => {
            if(!err2) {
                const response = {
                    plant_types: rows.map(row => {
                        return {
                            _id: row.id_tipo_planta,
                            name: row.nombre
                        }
                    })
                }
                res.status(200).json(response);
            } else
                res.status(400).json({ error: error });
        });
        conn.release();
    });
}

// GET IMAGE DATA BY ID
exports.forestations_getImageDataById = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        const id = req.params.imageId;
        const sql = 'SELECT * FROM imagen WHERE idimagen = ' + conn.escape(id);
        conn.query(sql, (err2, rows, fields) => {
            if(!err2) {
                if(rows.length > 0) {
                    const response = {
                        image: rows.map(row => {
                            return {
                                _id: row.idimagen,
                                imageType: row.tipo_imagen,
                                name: row.nombre,
                                data: row.data,
                                createdAt: row.creado,
                                updatedAt: row.actualizado
                            }
                        })[0]
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

// POST NEW FORESTATION
exports.forestations_insert = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        if(req.body && req.body.plant_count && req.body.coords && req.body.plant_type && req.body.userId && req.body.areaId) {
            let today = new Date();
            var newForestation = {
                num_plantas: req.body.plant_count,
                coordenadas: req.body.coords,
                tipo_planta_id: req.body.plant_type,
                usuario_id: req.body.userId,
                espacio_id: req.body.areaId,
                fecha: today
            }
            conn.query('INSERT INTO forestacion SET ?', newForestation, (err2, results, fields) => {
                if(err2)
                throw err2;
                res.status(201).json({
                    message: 'Created forestation entry successfully',
                    createdForestation: {
                        _id: results.insertId,
                        plant_count: newForestation.num_plantas,
                        coords: newForestation.coordenadas,
                        plant_type: newForestation.tipo_planta_id,
                        userId: newForestation.usuario_id,
                        areaId: newForestation.espacio_id,
                        date: today
                    }
                });
            });
        } else 
            res.status(400).json({ message: 'Missing request body data' });
        conn.release();
    });
}

// POST NEW PLANT TYPE
exports.forestations_insertPlantType = (req, res, next) => {
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

// UPDATE SPECIFIED FORESTATION BY ID (WIP)
exports.forestations_updateById = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        res.status(200).json({ message: 'Work In Progress' });
        conn.release();
    });
}

// DELETE SPECIFIED FORESTATION BY ID (WIP)
exports.forestations_deleteById = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if (err) {
            conn.release();
            return res.status(500).json({ error: err });
        }
        res.status(200).json({ message: 'Work In Progress' });
        conn.release();
    });
}
