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

```shell
POST http://localhost:8082/api/v1/admin/template/create

{
    "adminName": "James",
    "templateName": "Test",
    "templateData": [
        {"first_name":"fname"},
        {"last_name":"lname"}
    ]
}```