# Axon & Spring Cloud Kubernetes

## Simple case

To test the order service

```bash
cd order-service
mvn compile spring-boot:run  -Daxon.axonserver.suppressDownloadMessage=true
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

Start the h2 database server and Web server at the address http://localhost:9090
```bash
mvn compile exec:java -pl h2-server
```

Boot the Eureka server (= port 8761)

```bash
mvn compile exec:java -pl eureka-server
```

Try to scale the application and launch 2 Spring Boot applications

```bash
mvn install -pl order-service
mvn spring-boot:run -pl order-service -Dserver.port=8080

and 

mvn spring-boot:run -pl order-service -Dserver.port=8081
```

And now test 

```bash
http -s solarized http://localhost:8080/all-orders

http -s solarized -f POST http://localhost:8080/ship-order
http -s solarized -f POST http://localhost:8080/ship-order

http -s solarized -f POST http://localhost:8081/ship-order
http -s solarized -f POST http://localhost:8081/ship-order

http -s solarized http://localhost:8081/all-orders
http -s solarized http://localhost:8080/all-orders
```

## Using Eureka with a LoadBalancer

Load balance the Command requests using the Spring Cloud Gateway (= Eureka Client, server is running on port 8760)

```bash
mvn compile exec:java -pl gateway
```

And test to verify if the `commands` are load balanced between the 2 applications

```
http -s solarized http://localhost:8760/all-orders

http -s solarized -f POST http://localhost:8760/ship-order
http -s solarized -f POST http://localhost:8760/ship-order
http -s solarized -f POST http://localhost:8760/ship-order
http -s solarized -f POST http://localhost:8760/ship-order

http -s solarized http://localhost:8760/all-orders
```

**REMARKS**

Orders are only created by one application and not at load balanced

```bash
Security framework of XStream not initialized, XStream is probably vulnerable.
2019-05-20 10:24:22.446  INFO 7548 --- [rder.service]-0] d.s.a.o.s.OrderedProductsEventHandler    : Order shipped :: 42163b48-0f24-4725-b64e-b83fa96d2863by machine ::8081
2019-05-20 10:24:23.468  INFO 7548 --- [rder.service]-0] d.s.a.o.s.OrderedProductsEventHandler    : Order shipped :: ce6c52a6-28f1-4289-86fe-6fe54fbf27a0by machine ::8081
2019-05-20 10:24:23.640  INFO 7548 --- [rder.service]-0] d.s.a.o.s.OrderedProductsEventHandler    : Order shipped :: 84d30bf9-cee6-40bc-b19c-e62002f3d168by machine ::8081
2019-05-20 10:24:24.660  INFO 7548 --- [rder.service]-0] d.s.a.o.s.OrderedProductsEventHandler    : Order shipped :: d7a1ae61-17d5-497a-980d-5c380f26e9c6by machine ::8081
```

We get the 4 records created but if we gall twice the GW !

```bash
http -s solarized http://localhost:8760/all-orders
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Date: Mon, 20 May 2019 08:25:39 GMT
transfer-encoding: chunked

[]

http -s solarized http://localhost:8760/all-orders
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Date: Mon, 20 May 2019 08:25:41 GMT
transfer-encoding: chunked

[
    {
        "orderId": "84d30bf9-cee6-40bc-b19c-e62002f3d168",
        "orderStatus": "SHIPPED",
        "product": "Deluxe Chair"
    },
    {
        "orderId": "ce6c52a6-28f1-4289-86fe-6fe54fbf27a0",
        "orderStatus": "SHIPPED",
        "product": "Deluxe Chair"
    },
    {
        "orderId": "42163b48-0f24-4725-b64e-b83fa96d2863",
        "orderStatus": "SHIPPED",
        "product": "Deluxe Chair"
    },
    {
        "orderId": "d7a1ae61-17d5-497a-980d-5c380f26e9c6",
        "orderStatus": "SHIPPED",
        "product": "Deluxe Chair"
    }
]
```

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
