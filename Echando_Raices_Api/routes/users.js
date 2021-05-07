const express = require('express');
const router = express.Router();

const UsersController = require('../controllers/users.controller');

// GET ALL USERS
router.get('/', UsersController.users_getAll);

// GET SPECIFIED USER BY ID
router.get('/:userId', UsersController.users_getBydId);

module.exports = router;
