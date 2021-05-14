
exports.minPermissionLevelRequired = (required_permission_level) => {
    return (req, res, next) => {
        if(req.user.permissionLevel >= required_permission_level)
            return next();
        else
            return res.status(403).json({ message: 'Forbidden' });
    }
}

exports.onlySameUserOrAdminRequired = (req, res, next) => {
    if(req.params && req.params.userId && (req.user.userId === req.params.userId)) {
        return next();
    } else if(req.body && req.body.userId && (req.user.userId === req.body.userId)) {
        return next();
    } else {
        if (req.user.permissionLevel >= process.env.USER_ADMIN)
            return next();
        else
            return res.status(403).json({ message: 'Forbidden' });
    }
}

exports.onlyDifferentUserRequired = (req, res, next) => {
    if(req.params && req.params.userId && (req.user.userId != req.params.userId))
        return next();

    if(req.body && req.body.userId && (req.user.userId != req.body.userId))
        return next();

    return res.status(403).json({ message: 'Forbidden' });
}
