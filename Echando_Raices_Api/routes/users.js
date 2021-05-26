const express = require('express');
const router = express.Router();
const verifyToken = require('../middleware/verifyToken');
const checkPerm = require('../middleware/checkPermission');

const UsersController = require('../controllers/users.controller');

// GET ALL USERS
router.get('/', UsersController.users_getAll);

// GET SPECIFIED USER BY ID
router.get('/:userId', UsersController.users_getBydId);

// GET AREAS LINKED TO USER BY USER ID
router.get('/:userId/areas', UsersController.users_getUserAreas);

// POST (ADD) NEW USER
router.post('/signup', UsersController.users_insert);

// USER LOGIN
router.post('/login', UsersController.users_login);

// POST NEW USER/AREA LINK
router.post('/:userId/areas', verifyToken, checkPerm.onlySameUserOrAdminRequired, UsersController.users_insertUserAreaLink);

// UPDATE SPECIFIED USER BY ID
router.patch('/:userId', verifyToken, checkPerm.onlySameUserOrAdminRequired, UsersController.users_updateById);

// DELETE SPECIFIED USER BY ID
router.delete('/:userId', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), UsersController.users_deleteById);

// GET SPECIFIED USER'S FORESTATIONS
router.get('/:userId/forestations', UsersController.users_getForestationsById);

// UPDATE SPECIFIED USER'S FORESTATION BY ID
router.patch('/:userId/forestations/:forestationId', verifyToken, checkPerm.onlySameUserOrAdminRequired, UsersController.users_updateForestationBydId);

module.exports = router;
