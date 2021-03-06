package ir.aminer.potadoshack.server.listeneres;

import ir.aminer.potadoshack.core.error.Error;
import ir.aminer.potadoshack.core.event.Listener;
import ir.aminer.potadoshack.core.event.ListenerMethod;
import ir.aminer.potadoshack.core.event.events.CancelOrderEvent;
import ir.aminer.potadoshack.core.event.events.PlaceOrderEvent;
import ir.aminer.potadoshack.core.event.events.ViewOrdersEvent;
import ir.aminer.potadoshack.core.network.packets.PrimitivePacket;
import ir.aminer.potadoshack.core.network.packets.ResponsePacket;
import ir.aminer.potadoshack.core.order.Order;
import ir.aminer.potadoshack.server.User;

import java.io.IOException;
import java.util.Map;

public class OrdersListener implements Listener {

    @ListenerMethod
    public void onPlaceOrder(PlaceOrderEvent event) throws IOException {
        User user = event.getUser();
        if (user == null)
            return;

        if (event.getData().getOrder().getStatus().equals(Order.Status.FINALIZED))
            user.addOrder(event.getData().getOrder());
        else {
            event.getSender().sendError(Error.INVALID_ORDER_STATE);
            return;
        }
        user.save();

        for (Map.Entry<Integer, Order> orders : user.getOrders().entrySet())
            System.out.println(orders.getValue().getCart());

        event.getSender().sendResponse(new PrimitivePacket<>(event.getData().getOrder().getCode()), ResponsePacket.Status.OK);
    }


    @ListenerMethod
    public void onViewOrders(ViewOrdersEvent event) throws IOException {
        User user = event.getUser();
        if (user == null)
            return;
        event.getData().setOrders(user.getOrders());
        event.getSender().sendResponse(event.getData(), ResponsePacket.Status.OK);
    }

    @ListenerMethod
    public void onCancelOrder(CancelOrderEvent event) throws IOException {
        User user = event.getUser();
        if (user == null)
            return;

        user.removeOrder(event.getData().getOrderCode());
        user.save();
        event.getSender().sendResponse(new PrimitivePacket<>("OK"), ResponsePacket.Status.OK);
    }
}
