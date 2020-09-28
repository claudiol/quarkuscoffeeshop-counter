package io.quarkuscoffeeshop.counter.domain;

import io.quarkuscoffeeshop.domain.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class OrderTest {

    @Test
    public void testOrderCreatedEventFromBeveragesOnly() {

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                OrderSource.WEB,
                new HashMap<String, MenuItem>(){{
                    put("Kirk", new MenuItem(Item.CAPPUCCINO, BigDecimal.valueOf(3.75)));
                    put("Spock", new MenuItem(Item.COFFEE_WITH_ROOM, BigDecimal.valueOf(3.75)));
                }},
                null);
        OrderCreatedEvent orderCreatedEvent = Order.handlePlaceOrderCommand(placeOrderCommand);
        Assert.assertNotNull(orderCreatedEvent);
        Assert.assertNotNull(orderCreatedEvent.events);
        Assert.assertEquals(2, orderCreatedEvent.events.size());
        orderCreatedEvent.events.forEach(e -> {
            Assert.assertEquals(OrderInEvent.class, e.getClass());
            Assert.assertTrue(e.name.equals("Dopey") || e.name.equals("Spock"));
            Assert.assertEquals(EventType.BEVERAGE_ORDER_IN, e.eventType);
        });
    }

    @Test
    public void testProcessCreateOrderCommandFoodOnly() {

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                OrderSource.WEB,
                new HashMap<String, MenuItem>(){{
                    put("Kirk", new MenuItem(Item.CROISSANT, BigDecimal.valueOf(3.75)));
                    put("Spock", new MenuItem(Item.CAKEPOP, BigDecimal.valueOf(3.75)));
                }},
                null);
        OrderCreatedEvent orderCreatedEvent = Order.handlePlaceOrderCommand(placeOrderCommand);

        Assert.assertNotNull(orderCreatedEvent);
        Assert.assertNotNull(orderCreatedEvent.events);
        Assert.assertEquals(2, orderCreatedEvent.events.size());
        orderCreatedEvent.events.forEach(e -> {
            Assert.assertEquals(OrderInEvent.class, e.getClass());
            Assert.assertTrue(e.name.equals("Kirk") || e.name.equals("Spock"));
            Assert.assertEquals(EventType.KITCHEN_ORDER_IN, e.eventType);
        });
    }

    @Test
    public void testOrderInBeveragesAndFood() {

        PlaceOrderCommand placeOrderCommand = new PlaceOrderCommand(
                OrderSource.WEB,
                new HashMap<String, MenuItem>(){{
                    put("Kirk", new MenuItem(Item.COFFEE_BLACK, BigDecimal.valueOf(3.75)));
                    put("Spock", new MenuItem(Item.CAPPUCCINO, BigDecimal.valueOf(3.75)));
                }},
                new HashMap<String, MenuItem>(){{
                    put("Kirk", new MenuItem(Item.CROISSANT, BigDecimal.valueOf(3.75)));
                    put("Spock", new MenuItem(Item.CAKEPOP, BigDecimal.valueOf(3.75)));
                }});
        OrderCreatedEvent orderCreatedEvent = Order.handlePlaceOrderCommand(placeOrderCommand);

        Assert.assertNotNull(orderCreatedEvent);
        Assert.assertNotNull(orderCreatedEvent.events);
        Assert.assertEquals(4, orderCreatedEvent.events.size());
        int beveragOrders = 0;
        int kitchenOrders = 0;
        orderCreatedEvent.events.forEach(e -> {
            Assert.assertEquals(OrderInEvent.class, e.getClass());
            Assert.assertTrue(e.name.equals("Kirk") || e.name.equals("Spock"));
        });
        Assert.assertEquals(2, orderCreatedEvent.events.stream().filter(
                e -> e.eventType.equals(EventType.KITCHEN_ORDER_IN)).count());
        Assert.assertEquals(2, orderCreatedEvent.events.stream().filter(
                e -> e.eventType.equals(EventType.BEVERAGE_ORDER_IN)).count());
    }

}
