# Echando Raices v2
git clone https://github.com/fqtransforma/UABC-2021.1-Flash.git
## Android App
./Echando_Raices/

## NodeJS Backend API
./Echando_Raices_Api/

npm install

npm start (.env file required)

### Users Route
* Get all Users                     ->  _GET /users_
* Get specified User by ID          ->  _GET /users/:userId_
* Get areas linked to user          -> _GET /users/:userId/areas_
* Create new User                   -> _POST /users/signup_
* Login User                        -> _POST /users/login_
* Insert new User-Area Link         -> _POST /users/:userId/areas_
* Update specified User             -> _PATCH /users/:userId_
* Delete Specified User by ID       -> _DELETE /users/:userId_
* Get Specified User's forestations -> _GET /users/:userId/forestations_
* Update User's Forestation by ID   -> _UPDATE /users/:userId/forestations/:forestationId_

### Areas Route
* Get all Areas                     -> _GET /areas_
* Get specified Area by ID          -> _GET /areas/:areaId_
* Get all Area Types                -> _GET /areas/props/area-types_
* Get Area Type by ID               -> _GET /areas/props/area-types/:areaTypeId_
* Get All Addresses                 -> _GET /areas/props/addresses_
* Get Address by ID                 -> _GET /areas/props/addresses/:addressId_
* Get All Cities                    -> _GET /areas/props/cities_
* Get City by ID                    -> _GET /areas/props/cities/:cityId_
* Get All States                    -> _GET /areas/props/states_
* Get Cities in Specified State     -> _GET /areas/props/states/:stateId/cities_
* Post (Create) new Area            -> _POST /areas_
* Post new Area Type                -> _POST /areas/props/area-types_
* Post new Address                  -> _POST /areas/props/addresses_
* Update specified Area by ID       -> _PATCH /areas/:areaId_
* Delete Specified Area by ID       -> _DELETE /areas/:areaId_

### Forestation Route
* Get all Forestations              ->  _GET /forestations_
* Get specified Forestation by ID   ->  _GET /forestations/:forestationId_
* Get all Plant Types               ->  _GET /forestations/props/plant-types_
* Get specified plant type by ID    -> _GET /forestations/props/plant-types/:plantTypeId_
* Get Image data by ID              -> _GET /forestations/props/images/:imageId_
* Post (Create) new Forestation     -> _POST /forestations_
* Post new Plant Type               -> _POST /forestations/props/plant-types_
* Update Forestation by ID          -> _PATCH /forestations/:forestationId_
* Delete Forestation by ID          -> _DELETE /forestations/:areforestationId_
