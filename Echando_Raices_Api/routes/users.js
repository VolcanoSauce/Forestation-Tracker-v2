const express = require('express');
const router = express.Router();
const verifyToken = require('../middleware/verifyToken');
const checkPerm = require('../middleware/checkPermission');

const UsersController = require('../controllers/users.controller');

// GET ALL USERS
router.get('/', UsersController.users_getAll);

// GET SPECIFIED USER BY ID
router.get('/:userId', UsersController.users_getBydId);

// POST (ADD) NEW USER
router.post('/signup', UsersController.users_insert);

// USER LOGIN
router.post('/login', UsersController.users_login);

// UPDATE SPECIFIED USER BY ID
router.patch('/:userId', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_TIER_1), checkPerm.onlySameUserOrAdminRequired, UsersController.users_updateById);

// DELETE SPECIFIED USER BY ID
router.delete('/:userId', verifyToken, checkPerm.minPermissionLevelRequired(process.env.USER_ADMIN), UsersController.users_deleteById);

module.exports = router;
