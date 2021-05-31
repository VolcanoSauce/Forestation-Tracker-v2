const fs = require("fs");
const dbPool = require('../db/sqldb');

const uploadFiles = async (req, res, next) => {
    try {
        console.log(req.file);
        if(req.file == undefined)
            return status(400).json({ message: 'You must select a file.'});
            
        dbPool.getConnection((err, conn) => {
            if(err) {
                conn.release();
                return res.status(500).json({ error: err });
            }
            let today = new Date();
            const data = fs.readFileSync(__basedir + '/resources/static/assets/uploads/' + req.file.filename);
            const newImage = {
                tipo_imagen: req.file.mimetype,
                nombre: req.file.originalname,
                creado: today,
                actualizado: today
            }
            /** LA CONEXION DE MYSQL SE CRASHEA CON ARCHIVOS MAYORES A 350KB 
             *  PORQUE TARDA ALGO DE TIEMPO EN SUBIR Y EL MYSQL NO SE ESPERA **/
            conn.query('INSERT INTO imagen SET data = BINARY(?), ?', [data, newImage], (err2, results, fields) => {
                if(err2)
                    throw err2;

                // fs.writeFileSync(
                //     __basedir + '/resources/static/assets/tmp/' + newImage.nombre,
                //     newImage.data
                // );

                // Delete Temp stored image
                fs.unlink(__basedir + '/resources/static/assets/uploads/' + req.file.filename, (err3) => {
                    if(err3)
                        throw err3;

                    console.log('File deleted!');
                });

                res.status(201).json({
                    message: 'Image has been uploaded',
                    uploadedImage: {
                        _id: results.insertId,
                        imageType: newImage.tipo_imagen,
                        name: newImage.nombre,
                        data: newImage.data,
                        createdAt: newImage.creado,
                        updatedAt: newImage.actualizado
                    }
                });
            });
            conn.release();
        });
    } catch (error) {
        console.log(error);
        return res.status(409).json({ message: `Error while trying to upload images: ${error}` });
    }
}

module.exports = {
    uploadFiles
}
