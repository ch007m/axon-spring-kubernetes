# Axon & Spring Cloud Kubernetes

## Simple case

To test the order service

```bash
cd order-service
mvn clean spring-boot:run  -Daxon.axonserver.suppressDownloadMessage=true
```

Then, to manage your orders, then execute the following commands

```
curl -X POST http://localhost:8080/ship-order
...
curl -X GET http://localhost:8080/all-orders
[{"orderId":"603faca6-0244-4315-a976-bf1bc936c467","product":"Deluxe Chair","orderStatus":"PLACED"},{"orderId":"fe45cf79-66c0-4294-876f-e78b3d68c68b","product":"Deluxe Chair","orderStatus":"SHIPPED"}]%   

or using httpie tool

http -f POST http://localhost:8080/ship-order
http http://localhost:8080/all-orders
```

## Using Eureka

Start the h2 database
```bash
mvn exec:java -pl h2-server
```

Boot the Eureka server

```bash
mvn spring-boot:run -pl eureka-server
```

Try to scale the application and launch 2 Spring Boot applications

```bash
mvn install -pl order-service
mvn spring-boot:run -pl order-service

and 

mvn spring-boot:run -Dserver.port=8081 -pl order-service
```

And now test 

```bash
http -s solarized http://localhost:8080/all-orders

http -s solarized -f POST http://localhost:8080/ship-order
http -s solarized -f POST http://localhost:8080/ship-order
http -s solarized -f POST http://localhost:8080/ship-order

http -s solarized -f POST http://localhost:8081/ship-order

http -s solarized http://localhost:8080/all-orders
http -s solarized http://localhost:8081/all-orders
```

**REMARK**: We only get orders from on of the application. Really strange and weird !!

## Using JGroups

DEPRECATED - DON'T WORK

Try to scale the application and launch 2 Spring Boot applications

```bash
mvn spring-boot:run -Dspring.profiles.active=app-a

and 

mvn spring-boot:run -Dspring.profiles.active=app-b -Dserver.port=8081
```

And now test 

```bash
http -s solarized http://localhost:8080/all-orders

http -s solarized -f POST http://localhost:8080/ship-order
http -s solarized -f POST http://localhost:8080/ship-order
http -s solarized -f POST http://localhost:8080/ship-order

http -s solarized http://localhost:8080/all-orders
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Date: Fri, 17 May 2019 11:27:43 GMT
Transfer-Encoding: chunked

[
    {
        "orderId": "4616b877-301a-4b0c-ba00-9ed3792c2933",
        "orderStatus": "PLACED",
        "product": "Deluxe Chair"
    },
    {
        "orderId": "9be189a3-fc03-4072-b3d1-94651fe7d054",
        "orderStatus": "SHIPPED",
        "product": "Deluxe Chair"
    },
    {
        "orderId": "5efca2aa-c5b8-475e-9fae-ff18b22f180c",
        "orderStatus": "SHIPPED",
        "product": "Deluxe Chair"
    }
]
http -s solarized http://localhost:8081/all-orders
[] // THER IS AN ISSUE
```