package Loghme.Domain.Logic;

import Loghme.DataSource.*;

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
                new Location(restaurantDAO.getX(), restaurantDAO.getY()), menu, restaurantDAO.getLogo());
    }

    public Food DAOtoFood(FoodDAO foodDAO){
        return new Food(foodDAO.getName(), foodDAO.getDescription(), foodDAO.getPopularity(), foodDAO.getPrice(), foodDAO.getImage());
    }

    public ArrayList<PartyFood> DAOtoPartyFoodList(ArrayList<PartyFoodDAO> partyFoodDAOS){
        ArrayList<PartyFood> partyFoods = new ArrayList<>();
        for(PartyFoodDAO partyFoodDAO: partyFoodDAOS){
            partyFoods.add(new PartyFood(partyFoodDAO.getName(), partyFoodDAO.getDescription(), partyFoodDAO.getPopularity(),
                    partyFoodDAO.getPrice(), partyFoodDAO.getImage(), partyFoodDAO.getRestaurantId(), partyFoodDAO.getRestaurantName(), partyFoodDAO.getCount(), partyFoodDAO.getOldPrice()));
        }
        return partyFoods;
    }

    public FoodParty DAOtoFoodParty(FoodPartyDAO foodPartyDAO){
        return new FoodParty(foodPartyDAO.getEnteredTime(), DAOtoPartyFoodList(foodPartyDAO.getPartyFoodDAOS()));
    }

    public User DAOtoUser(UserDAO userDAO){
        return new User(userDAO.getId(), userDAO.getFirstName(), userDAO.getLastName(), userDAO.getPhoneNumber(),
                userDAO.getEmail(), userDAO.getCredit(), userDAO.getCart(), DAOtoOrderList(userDAO.getOrders()));
    }

    public ArrayList<Order> DAOtoOrderList(ArrayList<OrderDAO> orderDAOS){
        ArrayList<Order> orders = new ArrayList<Order>();
        for(OrderDAO orderDAO: orderDAOS){
            orders.add(new Order(orderDAO.getRestaurantId(), orderDAO.getRestaurantName(), orderDAO.getTotalPayment(), orderDAO.isFoodParty(),
                    DAOtoOrderItemList(orderDAO.getOrderItems()),orderDAO.getId(), OrderState.valueOf(orderDAO.getState())));
        }
        return orders;
    }

    public ArrayList<ShoppingCartItem> DAOtoOrderItemList(ArrayList<OrderItemDAO> orderItemDAOS){
        ArrayList<ShoppingCartItem> orderItems = new ArrayList<>();
        for(OrderItemDAO orderItemDAO: orderItemDAOS){
            Food orderedFood = new Food(orderItemDAO.getFoodName(),"",0,orderItemDAO.getPrice(),"");
            orderItems.add(new ShoppingCartItem(orderedFood, orderItemDAO.getNumber(), false));
        }
        return orderItems;
    }
}
