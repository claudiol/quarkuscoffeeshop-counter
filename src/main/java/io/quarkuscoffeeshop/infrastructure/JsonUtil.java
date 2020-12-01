package io.quarkuscoffeeshop.infrastructure;

import io.quarkuscoffeeshop.counter.domain.Order;
import io.quarkuscoffeeshop.domain.*;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@RegisterForReflection
public class JsonUtil {

    static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static final Jsonb jsonb = JsonbBuilder.create();

    public static String toJson(Object object) {
        return jsonb.toJson(object);
    }

    public static String toDashboardUpdateReadyJson(String payload) {
        logger.debug("converting OrderReadyUpdate from {}", payload);
        OrderUpEvent orderUpEvent = jsonb.fromJson(payload, OrderUpEvent.class);
        return jsonb.toJson(new OrderReadyUpdate(orderUpEvent));
    }

    public static PlaceOrderCommand createPlaceOrderCommandFromJson(String payload) {
        logger.debug("converting to PlaceOrderCommand from {}", payload);
        return jsonb.fromJson(payload, PlaceOrderCommand.class);
    }

    public static String toInProgressUpdate(final LineItemEvent lineItemEvent) {
        return jsonb.toJson(new InQueueUpdate(lineItemEvent));
    }

}
