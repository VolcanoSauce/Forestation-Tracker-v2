const express = require('express');
const router = express.Router();
const verifyToken = require('../middleware/verifyToken');
const checkPerm = require('../middleware/checkPermission');

const ForestationsController = require('../controllers/forestations.controller');

// GET ALL FORESTATIONS
router.get('/', ForestationsController.forestations_getAll);

// GET SPECIFIED FORESTATION BY ID
router.get('/:forestationId', ForestationsController.forestations_getById);

// POST NEW PLANT TYPE
router.post('/plant-type', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), ForestationsController.forestation_insertPlantType);

// POST NEW FORESTATION
router.post('/', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), ForestationsController.forestation_insert);

// UPDATE SPECIFIED FORESTATION BY ID
router.patch('/:forestationId', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), ForestationsController.forestation_updateById);

// DELETE SPECIFIED FORESTATION BY ID
router.delete('/:forestationId', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), ForestationsController.forestation_deleteById);

module.exports = router;