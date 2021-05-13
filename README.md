# ri-redis-hackathon-2021
ri-redis-hackathon-2021

## Project Name

System Configuration-as-a-Service (CaaS)

### Current Team
- James Caple
- Brandon Beiler
- Eric Offermann
- Brian Broerman 

### Redis
Redismod Github:
https://github.com/RedisLabsModules/redismod

Redis-Insight GUI:
https://redislabs.com/redis-enterprise/redis-insight/

#### Running Redis locally
```shell
cd redis
docker-compose up
```

#### Connecting local Redis to RedisInsight
1. Download RedisInsight from the link above
2. Install and run the app. 
3. Navigate to http://localhost:8001.
4. Select "I already have a database"
5. Select "Connect to a Redis Database using hostname and port"
6. Enter the following values
    1. host: localhost
    2. port: 6379
    3. name: anything you want here
7. Now, select the redis database you just created in the UI

### API

Make sure REDIS docker container is started first as the API service will try to connect to it.

```shell
cd api
gradle build
gradle bootRun
```

#### POSTMAN

Postman scripts are in the postman directory.

#### API Endpoints

Swagger UI:
http://localhost:8081/swagger-ui.html

POST http://localhost:8081/api/v1/admin/template/create

```
{
    "settingsId": "James",
    "templateName": "Test",
    "templateSettings": {
        "first_name":"fname",
        "last_name":"lname"
    }
}
```
##### Authentication

POST http://localhost:8081/api/v1/users/auth

```
{
     "username": "hackerboi",
     "password": "zer0daylulz"
}
```
Response:

```
{
    "username": "hackerboi",
    "password": "zer0daylulz",
    "authenticated": true,
    "jwt": "35434534kjdfzgkjdfg"
}
```


#### API Security

To enable security on /api/v1/admin API endpoints, uncomment the following line in the SecurityConfiguration.java file:

```//.antMatchers("/api/v1/admin/**").authenticated()```

API requests will then require a Bearer Token header with a valid JWT Token as a value.

### KeyCloak For Authentication

Keycloak docker compose and configuration artifacts are in the keycloak directory.

#### How To Setup KeyCloak

1. Make the keycloak directory your working dir.
1. Make the 'kcdb' directory to persist keycloak data in:
     1. `mkdir kcdb`
     1. `chmod -R 777 kcdb`
1. Set admin credentials in environment variables:
     1. `export kcadmin=admin`
     1. `export kcadmin_password=admin`
1. Start the keycloak docker container 
     1. `docker-compose up`
1. Navigate web browser to http://localhost:8080
1. Log in as admin
1. Click 'Add Realm' to add the CaaS Realm
     1. Import the CaaS Realm from the caa-real-export.json file in this directory to create the realm.
1. Create a user account and set the password in the Credentials Tab (turn off Temporary)
1. The Authentication and JWT Verify Token API Endpoints examples are postman scripts in ./api/postman. 


#### React app

Running below should start the react app on port 3000

```shell
cd app
npm install
npm start
```
#### Run Nginx Proxy
In the /app dir, there is a Dockerfile and docker-compose.yml. 

Open the docker-compose.yml and point to this directory in the volume mount.

You can auto build/start this by running.
```
cd /app
sh local_run.sh
```
TODO: I was getting CORS errs when POSTing to the API, so I was trying to run the Docker to proxy the API while still running the local react app on port :3000, but I was still getting a CORS err.

#### Websocket Updates

You can now use websocket to subscribe to Template updates and receive updates 
as they occur in real-time. On update, a JSON string object is sent to subscribers 
in the *CaaSTemplateUpdate* class format:
```json
CaaSTemplateUpdate
{
   "settingsId": "caas_dev_c779f6be_e58b_4b23_b085_037c8c47f5d2",
   "name": "new template name",
   //what was the name updated to
   "templateSettings": {
      //object with key-values for which settings updated
      "setting1": "newSettingValue",
      "setting9": "anotherNewValue"
   },
   "updateType": TemplateUpdateType,
   "updateField": TemplateUpdateField
}
TemplateUpdateType Enum{
   NAME,
   SETTINGS,
   OBJECT
}
TemplateUpdateField Enum{
   CREATE,
   UPDATE,
   DELETE
}
```

The websocket endpoint sits at: **/api/v1/updates**

Query Parameters are used in the connection string to configure the update listener.
Some are required and some are optional.

| Query Param | required? | Description | Accepted Values |
|---|---|---|---|
| **settingsId** | **yes** | a single template settingsId to track (maybe multiple in the future)  | a single settingsId string |
| **templateUpdateField** | no | The fields to listen for for updates. Comma-seperated for multiple. Empty listens for all changes | NAME, SETTINGS, OBJECT  |
| **templateUpdateType** | no | What type of updates to listen for, such as create, update, and or delete. Comma-seperated for multiple.  Empty listens for all changes | CREATE, UPDATE, DELETE  |


**Example Websocket Connection**

| Query Param | Value |
|---|---|
| **settingsId** | caas_dev_c779f6be_e58b_4b23_b085_037c8c47f5d2 |
| **templateUpdateField** | NAME,SETTINGS | 
| **templateUpdateType** | CREATE,UPDATE | 
```shell
ws://localhost:8081/api/v1/updates?templateUpdateType=CREATE%2CUPDATE&templateUpdateField=NAME%2CSETTINGS&settingsId=caas_dev_c779f6be_e58b_4b23_b085_037c8c47f5d2
```
