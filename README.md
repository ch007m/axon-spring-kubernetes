# Axon & Spring Cloud Kubernetes

To test the order service

```bash
cd order-service
mvn clean spring-boot:run  -Daxon.axonserver.suppressDownloadMessage=true
```

Open your browser and execute one of these queries

```
POST http://localhost:8080/ship-order
POST http://localhost:8080/ship-unconfirmed-order
GET http://localhost:8080/all-orders
```