# Echando Raices v2

## Android App

## NodeJS Backend API
npm install

npm start (.env file required)

### Users Route
* Get all Users                     ->  _GET /users_
* Get specified User by ID          ->  _GET /users/:userId_
* Create new User                   -> _POST /users/signup_
* Login User                        -> _POST /users/login_
* Update specified User             -> _PATCH /users/:userId_
* Delete Specified User by ID       -> _DELETE /users/:userId_
* Get Specified User's forestations -> _GET /users/:userId/forestations/:forestationId_

### Areas Route
* Get all Areas                     ->  _GET /areas_
* Get specified Area by ID          ->  _GET /areas/:areaId_
* Get all Area Types                ->  _GET /areas/props/area-types_
* Post (Create) new Area            -> _POST /areas_
* Post new Area Type                -> _POST /areas/props/area-types_
* Update specified Area by ID       -> _PATCH /areas/:areaId_
* Delete Specified Area by ID       -> _DELETE /areas/:areaId_

### Forestation Route
* Get all Forestations              ->  _GET /forestations_
* Get specified Forestation by ID   ->  _GET /forestations/:forestationId_
* Get all Plant Types               ->  _GET /forestations/props/plant-types_
* Get Image data by ID              -> _GET /forestations/props/images/:imageId_
* Post (Create) new Forestation     -> _POST /forestations_
* Post new Plant Type               -> _POST /forestations/props/plant-types_
* Update Forestation by ID          -> _PATCH /forestations/:forestationId_
* Delete Forestation by ID          -> _DELETE /forestations/:areforestationId_
