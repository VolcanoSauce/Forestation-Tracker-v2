const express = require('express');
const router = express.Router();

const AreaController = require('../controllers/areas.controller');

// GET ALL AREAS
router.get('/', AreaController.areas_getAll);

module.exports = router;
