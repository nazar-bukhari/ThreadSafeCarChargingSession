#Electric Car Charging Session

This Application represents a store for car charging session entities.
It will hold all records in memory and provides REST API.

JavaDOC is included in the project path in a compressed file.

###Tech Stack Used  

+ Java 8
+ Spring Boot
+ Gradle
+ Junit
+ Mockito

###Use the following commands:

> To Build: ./gradlew build

> To run test cases: ./gradlew test

> To run: ./gradle run

###Sample Request/Response For API Endpoints

> POST http://localhost:8000/chargingSessions

Request:
```
{
{
"stationId":"Test-123"
}
}
```
Response:
```
{
    "id": "6d439a77-54ca-4cfb-8815-c305496dc07c",
    "stationId": "Test-123",
    "startedAt": "2021-05-26T08:51:54.224946",
    "status": "IN_PROGRESS"
}
```

> GET http://localhost:8000/chargingSessions

Response:
```$xslt
[
    {
        "id": "6d439a77-54ca-4cfb-8815-c305496dc07c",
        "stationId": "Test-123",
        "startedAt": "2021-05-26T08:51:54.224946",
        "stoppedAt": null,
        "status": "IN_PROGRESS"
    },
    {
        "id": "df1c5e65-a23a-4236-a917-509dc7a173e4",
        "stationId": "Test-456",
        "startedAt": "2021-05-26T08:53:09.916412",
        "stoppedAt": null,
        "status": "IN_PROGRESS"
    }
]
```

> PUT http://localhost:8000/chargingSessions/{id}
> Example:http://localhost:8000/chargingSessions/df1c5e65-a23a-4236-a917-509dc7a173e4 

Response:
```$xslt
{
    "id": "df1c5e65-a23a-4236-a917-509dc7a173e4",
    "stationId": "Test-456",
    "startedAt": "2021-05-26T08:53:09.916412",
    "stoppedAt": "2021-05-26T08:53:25.220355",
    "status": "FINISHED"
}
```

> GET http://localhost:8000/chargingSessions/summary

Response:
```$xslt
{
    "totalCount": 2,
    "startedCount": 1,
    "stoppedCount": 1
}
```