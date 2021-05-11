const express = require('express');
const router = express.Router();

const UsersController = require('../controllers/users.controller');

// GET ALL USERS
router.get('/', UsersController.users_getAll);

// GET SPECIFIED USER BY ID
router.get('/:userId', UsersController.users_getBydId);

// POST (ADD) NEW USER
router.post('/signup', UsersController.users_insert);

// USER LOGIN
// router.post('/login', UsersController.users_login);

module.exports = router;
