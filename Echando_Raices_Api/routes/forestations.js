const express = require('express');
const router = express.Router();
const verifyToken = require('../middleware/verifyToken');
const checkPerm = require('../middleware/checkPermission');
const upload = require('../middleware/upload');

const ForestationsController = require('../controllers/forestations.controller');
const UploadController = require('../controllers/upload.controller');

// GET ALL FORESTATIONS
router.get('/', ForestationsController.forestations_getAll);

// GET SPECIFIED FORESTATION BY ID
router.get('/:forestationId', ForestationsController.forestations_getById);

// GET ALL PLANT TYPES
router.get('/props/plant-types', ForestationsController.forestations_getAllPlantTypes);

// GET SPECIFIED PLANT TYPE BY ID
router.get('/props/plant-types/:plantTypeId', ForestationsController.forestations_getPlantTypeById);

// GET IMAGE DATA BY ID
router.get('/props/images/:imageId', ForestationsController.forestations_getImageDataById);

// POST NEW FORESTATION
router.post('/', verifyToken, ForestationsController.forestations_insert);

// POST NEW PLANT TYPE
router.post('/props/plant-types', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), ForestationsController.forestations_insertPlantType);

// POST NEW IMAGE
router.post('/:forestationId/props/images/upload', verifyToken, checkPerm.onlySameUserOrAdminRequired, upload.single('file'), UploadController.uploadFiles);

// UPDATE SPECIFIED FORESTATION BY ID
router.patch('/:forestationId', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), ForestationsController.forestations_updateById);

// DELETE SPECIFIED FORESTATION BY ID
router.delete('/:forestationId', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), ForestationsController.forestations_deleteById);

module.exports = router;