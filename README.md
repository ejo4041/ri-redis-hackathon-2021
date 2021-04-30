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
}```