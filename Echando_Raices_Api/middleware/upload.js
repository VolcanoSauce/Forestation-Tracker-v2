const multer = require("multer");

const imageFilter = (req, file, cb) => {
    if(file.mimetype.startsWith("image")) {
        cb(null, true);
    } else 
        cb("Please upload only images.", false);
}

var storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, __basedir + '/resources/static/assets/uploads/');
    },
    filename: (req, file, cb) => {
        cb(null, `${Date.now()}-echando_raices-${file.originalname}`);
    },
});

var uploadFile = multer({ storage: storage, limits: {fileSize: 1024 * 1024 * 5}, fileFilter: imageFilter });
module.exports = uploadFile;
