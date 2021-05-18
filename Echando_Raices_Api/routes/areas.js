const express = require('express');
const router = express.Router();
const verifyToken = require('../middleware/verifyToken');
const checkPerm = require('../middleware/checkPermission');
const AreasController = require('../controllers/areas.controller');

// GET ALL AREAS (Token verification TESTING)
router.get('/', AreasController.areas_getAll);

// GET SPECIFIED AREA BY ID
router.get('/:areaId', AreasController.areas_getById);

// GET ALL AREA TYPES
router.get('/props/area-types', AreasController.areas_getAllAreaTypes);

// POST (CREATE) NEW AREA
router.post('/', verifyToken, AreasController.areas_insert);

// POST NEW AREA TYPE
router.post('/props/area-types', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), AreasController.areas_insertAreaType);

// UPDATE SPECIFIED AREA BY ID
router.patch('/:areaId', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), AreasController.areas_updateById);

// DELETE SPECIFIED AREA BY ID
router.delete('/:areaId', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), AreasController.areas_deleteById);

module.exports = router;
