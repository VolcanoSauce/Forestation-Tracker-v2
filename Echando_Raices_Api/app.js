const express = require('express');
const app = express();

require('dotenv').config();

global.__basedir = __dirname;

// Import routes
const usersRoute = require('./routes/users');
const areasRoute = require('./routes/areas');
const forestationsRoute = require('./routes/forestations');

// Connect to DB (Using MySQL Pooling instead)
// const db = mysql.createConnection({
//     host: process.env.DB_HOST,
//     user: process.env.USER,
//     password: process.env.PASSWD,
//     database: process.env.DB_NAME
// });
// db.connect(err => {
//     if (err)
//         throw err;
//     console.log('DB Running id: ' + db.threadId);
// });

// Parse JSON content requests
app.use(express.json());
// Parse x-www-form-urlencoded content requests
app.use(express.urlencoded({ extended: true }));

// Enable CORS
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header(
        'Access-Control-Allow-Headers',
        'Origin, X-Requested-With, Content-Type, Accept, Authorization'
    );
    // res.header('Access-Control-Allow-Credentials', 'true');
    if (req.method === 'OPTIONS') {
        res.header('Access-Control-Allow-Methods', 'PUT, POST, PATCH, DELETE, GET');
        return res.status(200).json({});
    }
    next();
});

// Middlewares
app.use('/users', usersRoute);
app.use('/areas', areasRoute);
app.use('/forestations', forestationsRoute);

// Routes
app.get('/', (req, res, next) => {
    res.json({ message: 'Bienvenido al API de Echando Raices!' });
});

app.use((req, res, next) => {
    const error = new Error('Not Found');
    error.status = 404;
    next(error);
});

app.use((error, req, res, next) => {
    res.status(error.status || 500);
    res.json({
        error: {
            message: error.message
        }
    })
});

const PORT = process.env.PORT || 3600;
app.listen(PORT, () => console.info(`Server started on ${PORT}`));
