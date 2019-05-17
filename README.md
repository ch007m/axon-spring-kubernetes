# Axon & Spring Cloud Kubernetes

To test the order service

```bash
cd order-service
mvn clean spring-boot:run  -Daxon.axonserver.suppressDownloadMessage=true
```

Then, to order a product, then execute the following commands

```
curl -X POST http://localhost:8080/ship-order
...
curl -X GET http://localhost:8080/all-orders
[{"orderId":"603faca6-0244-4315-a976-bf1bc936c467","product":"Deluxe Chair","orderStatus":"PLACED"},{"orderId":"fe45cf79-66c0-4294-876f-e78b3d68c68b","product":"Deluxe Chair","orderStatus":"SHIPPED"}]%   
```