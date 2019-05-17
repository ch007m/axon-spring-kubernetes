package dev.snowdrop.axon.product.commandmodel;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;

    @CommandHandler
    public ProductAggregate(String info) {
    }

    protected ProductAggregate() {
        // Required by Axon to build a default Aggregate prior to Event Sourcing
    }

}