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

```shell
POST http://localhost:8081/api/v1/admin/template/create

{
    "adminName": "James",
    "templateName": "Test",
    "templateData": [
        {"first_name":"fname"},
        {"last_name":"lname"}
    ]
}
```

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
