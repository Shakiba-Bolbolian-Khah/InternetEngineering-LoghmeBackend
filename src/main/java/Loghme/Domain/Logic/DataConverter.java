package Loghme.Domain.Logic;

import Loghme.DataSource.*;
import Loghme.PresentationController.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class DataConverter {
    private static DataConverter instance;

    public static DataConverter getInstance() {
        if (instance == null)
            instance = new DataConverter();
        return instance;
    }

    public ArrayList<Restaurant> DAOtoRestaurantList(ArrayList<RestaurantDAO> restaurantDAOS){
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        for(RestaurantDAO restaurantDAO: restaurantDAOS){
            restaurants.add(DAOtoRestaurant(restaurantDAO));
        }
        return restaurants;
    }

    public Restaurant DAOtoRestaurant(RestaurantDAO restaurantDAO){
        ArrayList<Food> menu = new ArrayList<>();
        for(FoodDAO foodDAO: restaurantDAO.getMenu()){
            menu.add(DAOtoFood(foodDAO));
        }
        return new Restaurant(restaurantDAO.getName(), restaurantDAO.getId(),
                restaurantDAO.getLocation(), menu, restaurantDAO.getLogo());
    }

    public Food DAOtoFood(FoodDAO foodDAO){
        return new Food(foodDAO.getName(), foodDAO.getDescription(), foodDAO.getPopularity(), foodDAO.getPrice(), foodDAO.getImage());
    }

    public User DAOtoUser(UserDAO userDAO){
        return new User(userDAO.getId(), userDAO.getFirstName(), userDAO.getLastName(), userDAO.getPhoneNumber(),
                userDAO.getEmail(), userDAO.getLocation(), userDAO.getCredit(),
                DAOtoCart(userDAO.getShoppingCart()), DAOtoOrderList(userDAO.getOrders()));
    }

    public ShoppingCart DAOtoCart(CartDAO cartDAO){
        return new ShoppingCart(cartDAO.isEmpty(), cartDAO.getRestaurantId(), cartDAO.getRestaurantName(),
                cartDAO.getTotalPayment(), cartDAO.isFoodParty(), cartDAO.getFirstPartyFoodEnteredTime(),
                DAOtoCartItems(cartDAO.getItems()));
    }

    public ArrayList<ShoppingCartItem> DAOtoCartItems(ArrayList<CartItemDAO> cartItemDAOS){
        ArrayList<ShoppingCartItem> items = new ArrayList<>();
        for(CartItemDAO cartItemDAO: cartItemDAOS){
            Food orderedFood = new Food(cartItemDAO.getFoodName(),"",0, cartItemDAO.getPrice(),"");
            items.add(new ShoppingCartItem(orderedFood, cartItemDAO.getNumber(), cartItemDAO.isPartyFood()));
        }
        return items;
    }

    public ArrayList<Order> DAOtoOrderList(ArrayList<OrderDAO> orderDAOS){
        ArrayList<Order> orders = new ArrayList<Order>();
        for(OrderDAO orderDAO: orderDAOS){
            orders.add(new Order(orderDAO.getRestaurantId(), orderDAO.getRestaurantName(), orderDAO.getTotalPayment(), 0,
                    DAOtoOrderItemList(orderDAO.getOrderItems()),orderDAO.getId(), orderDAO.getDeliveryId(), OrderState.valueOf(orderDAO.getState()),
                    orderDAO.getFinalizationTime(), orderDAO.getDeliveringTime()));
        }
        return orders;
    }

    public ArrayList<ShoppingCartItem> DAOtoOrderItemList(ArrayList<OrderItemDAO> orderItemDAOS){
        ArrayList<ShoppingCartItem> orderItems = new ArrayList<>();
        for(OrderItemDAO orderItemDAO: orderItemDAOS){
            Food orderedFood = new Food(orderItemDAO.getFoodName(),"",0, orderItemDAO.getPrice(),"");
            orderItems.add(new ShoppingCartItem(orderedFood, orderItemDAO.getNumber(), false));
        }
        return orderItems;
    }

    public ArrayList<HomeRestaurantDTO> restaurantsToDTO(ArrayList<Restaurant> restaurants){
        ArrayList<HomeRestaurantDTO> restaurantDTOS = new ArrayList<>();
        for(Restaurant restaurant: restaurants){
            restaurantDTOS.add(new HomeRestaurantDTO(restaurant.getName(), restaurant.getId(), restaurant.getLogoUrl()));
        }
        return restaurantDTOS;
    }

    public RestaurantDTO DAOtoRestaurantDTO(RestaurantDAO restaurantDAO){
        return new RestaurantDTO(restaurantDAO.getName(), restaurantDAO.getId(),restaurantDAO.getLocation(),
                restaurantDAO.getMenu(),restaurantDAO.getLogo());
    }

    public ShoppingCartDTO DAOtoCartDTO(CartDAO cartDAO){
        return new ShoppingCartDTO(cartDAO.isEmpty(),cartDAO.getRestaurantId(), cartDAO.getRestaurantName(), cartDAO.getTotalPayment(),
                cartDAO.getItems(), 0);
    }

    public UserDTO DAOtoUserDTO(UserDAO userDAO){
        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();
        for(OrderDAO orderDAO: userDAO.getOrders()){
            orderDTOS.add(DAOtoOrderDTO(orderDAO));
        }
        return new UserDTO(userDAO.getId(),userDAO.getFirstName(),userDAO.getLastName(),userDAO.getPhoneNumber(),
                userDAO.getEmail(), userDAO.getCredit(), orderDTOS);
    }

    public OrderDTO DAOtoOrderDTO(OrderDAO orderDAO){
        return new OrderDTO(orderDAO.getState(),orderDAO.getId(),orderDAO.getRestaurantId(),orderDAO.getRestaurantName(),
                orderDAO.getTotalPayment(),0,orderDAO.getOrderItems());
    }
}
