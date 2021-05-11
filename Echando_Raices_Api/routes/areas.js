const express = require('express');
const router = express.Router();
const verifyToken = require('../middleware/verifyToken');

const AreaController = require('../controllers/areas.controller');

// GET ALL AREAS (Token verification TESTING)
router.get('/', verifyToken, AreaController.areas_getAll);

module.exports = router;
