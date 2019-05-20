package dev.snowdrop.axon.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import dev.snowdrop.axon.order.coreapi.events.OrderConfirmedEvent;
import dev.snowdrop.axon.order.coreapi.events.OrderPlacedEvent;
import dev.snowdrop.axon.order.coreapi.events.OrderShippedEvent;
import dev.snowdrop.axon.order.coreapi.queries.FindAllOrderedProductsQuery;
import dev.snowdrop.axon.order.coreapi.queries.OrderedProduct;

@Service
public class OrderedProductsEventHandler {

    private final Map<String, OrderedProduct> orderedProducts = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(OrderedProductsEventHandler.class);

    @Autowired
    Environment environment;

    @EventHandler
    public void on(OrderPlacedEvent event) {
        String orderId = event.getOrderId();
        orderedProducts.put(orderId, new OrderedProduct(orderId, event.getProduct()));
    }

    @EventHandler
    public void on(OrderConfirmedEvent event) {
        orderedProducts.computeIfPresent(event.getOrderId(), (orderId, orderedProduct) -> {
            orderedProduct.setOrderConfirmed();
            logger.info("Order shipped :: " + orderId + "by machine ::" + environment.getProperty("local.server.port"));
            return orderedProduct;
        });
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        orderedProducts.computeIfPresent(event.getOrderId(), (orderId, orderedProduct) -> {
            orderedProduct.setOrderShipped();

            return orderedProduct;
        });
    }

    @QueryHandler
    public List<OrderedProduct> handle(FindAllOrderedProductsQuery query) {
        return new ArrayList<>(orderedProducts.values());
    }

}