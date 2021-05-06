const express = require('express');
const router = express.Router();

const UsersController = require('../controllers/users.controller');

// GET ALL USERS
router.get('/', UsersController.users_list);

module.exports = router;
