const jwt = require('jsonwebtoken');

module.exports = (req, res, next) => {
    var token;
    if(req.headers.authorization && req.headers.authorization.split(' ')[0] === 'Bearer')
        token = req.header('Authorization').split(' ')[1];

    if(!token)
        return res.status(401).json({ message: 'Access Denied'});
    try {
        const verified = jwt.verify(token, process.env.TOKEN_SECRET);
        req.user = verified;
        next();
    } catch (err) {
        res.status(401).json({ message: 'Inavlid Token'});
    }
}
