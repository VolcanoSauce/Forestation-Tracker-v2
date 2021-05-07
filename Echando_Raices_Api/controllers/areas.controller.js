const dbPool = require('../db/sqldb');

// GET ALL AREAS
exports.areas_getAll = (req, res, next) => {
    dbPool.getConnection((err, conn) => {
        if(err)
            res.status(500).json({ error: err});
        else {
            const sql = 'SELECT * FROM espacio;';
            conn.query(sql, (error, rows, fields) => {
                if(!error) {
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
                }
            });
        }
        conn.release();
    });
}
