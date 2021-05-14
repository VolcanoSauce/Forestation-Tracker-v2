const express = require('express');
const router = express.Router();
const verifyToken = require('../middleware/verifyToken');
const checkPerm = require('../middleware/checkPermission');

const ForestationsController = require('../controllers/forestations.controller');

// GET ALL FORESTATIONS
router.get('/', ForestationsController.forestations_getAll);

// GET SPECIFIED FORESTATION BY ID
router.get('/:forestationId', ForestationsController.forestations_getById);

module.exports = router;