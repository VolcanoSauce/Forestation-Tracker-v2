# Echando Raices v2
git clone https://github.com/fqtransforma/UABC-2021.1-Flash.git

## Android App
./Echando_Raices/

## NodeJS Backend API
./Echando_Raices_Api/

npm install

npm start (.env file required)

API @: 
http://ec2-54-227-98-150.compute-1.amazonaws.com:3600/

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

## Rubrica Caracteristicas Conceptuales de la Herramienta
1. La herramienta refleja un análisis consistente con la lógica el problema propuesto e implementa un diseño orientado a sistemas distribuidos consistente con la lógica de la solución propuesta
    * _El sistema es una solucion al problema propuesto, implementando un diseño orientado a sistemas distribuidos, ya que es una solucion de partes separadas trabajando junto, unas de estas partes siendo: rutas, controladores, middlewares, etc. El backend del servicio web API utilizando el Patron REST de arquitectura, que permite gran escalabilidad, de tal forma que puede servir a multiples frontends en diferentes plataformas y dispositivos._

2. La herramienta adopta una arquitectura orientada a sistemas distribuidos y utiliza servicios en la nube para su implementación.
    * _La parte backend de esta solución utiliza una arquitectura REST (orientada a sistemas distribuidos), utiliza servicios como base de datos, express.js y otros middlewares desarrollados por nosotros_

3. La herramienta considera un diseño de procesamiento balanceado consistente con la arquitectura adoptada
    * _Esta solución hace uso de ejecucion de procesos asíncronos, con la intención de tener un flujo de ejecución contínuo y un procesamiento balanceado. La arquitectura y tecnología utilizada permite la escalabilidad en poder de procesamiento y otros aspectos a través de "Load Balancers" con NGINX._

4. La herramienta implementa su propio protocolo de comunicación entre componentes dentro de un ambiente distribuido.
    * _Nuestra solución hace uso de distintos tipos de comunicación entre las partes del sistema, la comunicación entre la aplicación de Android (frontend) y el Web API (backend), se hace a través de llamadas HTTP con contenido exclusivamente de tipo JSON, para matener consistencia a través de todas las plataformas a las que se sirve. Además la comunicación en la aplicación backend hace uso de módulos y middlewares con controladores para distintas tareas del sistema, por ejemplo, en la autenticación y autorizacion._

5. La herramienta considera estrategias de seguridad dentro de un ambiente distribuido.
    * _Esta solución y principalmente en el servicio web API (backend), hace uso de técnicas de autenticación y autorización a través de Json Web Tokens (JWT), el web API también incluye un sistema middleware de permisos que restringe accesos indebidos y permite accesos apropiados. Para datos críticos como contraseñas, se utilizan métodos de encriptación con sal (salting)._

