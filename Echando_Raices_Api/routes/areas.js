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

// GET AREA TYPE BY ID
router.get('/props/area-types/:areaTypeId', AreasController.areas_getAreaTypeById);

// GET ALL ADDRESSES
router.get('/props/addresses', AreasController.areas_getAllAddresses);

// GET ADDRESS BY ID
router.get('/props/addresses/:addressId', AreasController.areas_getAddressById);

// GET ALL CITIES
router.get('/props/cities', AreasController.areas_getAllCities);

// GET CITY BY ID
router.get('/props/cities/:cityId', AreasController.areas_getCityById);

// GET ALL STATES
router.get('/props/states', AreasController.areas_getAllStates);

// GET CITIES IN SPECIFIED STATE
router.get('/props/states/:stateId/cities', AreasController.areas_getCitiesByStateId);

// POST (CREATE) NEW AREA
router.post('/', verifyToken, AreasController.areas_insert);

// POST NEW AREA TYPE
router.post('/props/area-types', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), AreasController.areas_insertAreaType);

// POST NEW ADDRESS
router.post('/props/addresses', verifyToken, AreasController.areas_insertAddress);

// UPDATE SPECIFIED AREA BY ID
router.patch('/:areaId', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), AreasController.areas_updateById);

// DELETE SPECIFIED AREA BY ID
router.delete('/:areaId', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), AreasController.areas_deleteById);

module.exports = router;
