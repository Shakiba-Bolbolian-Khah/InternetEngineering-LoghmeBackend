//package Loghme.Controller;
//
//import Loghme.Exceptions.*;
//import Loghme.Model.CommandHandler;
//import Loghme.Model.Location;
//import Loghme.Model.Restaurant;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//import java.io.IOException;
//
//@SuppressWarnings("serial")
//@WebServlet("/restaurant/*")
//public class RestaurantController  extends HttpServlet {
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            String restaurantId = request.getPathInfo().replace("/", "");
//            String responsePageName = "/restaurant.jsp";
//            Restaurant restaurant= CommandHandler.getInstance().getRestaurant(restaurantId);
//            Location userLocation = CommandHandler.getInstance().getUser().getLocation();
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//            request.setAttribute("restaurant", restaurant);
//            request.setAttribute("userLocation", userLocation);
//            response.setStatus(HttpServletResponse.SC_OK);
//            requestDispatcher.forward(request, response);
//        } catch (Error404 error404) {
//            String responsePageName = "/404Error.jsp";
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//            request.setAttribute("errorMsg", error404.getMessage());
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            requestDispatcher.forward(request, response);
//        } catch (Error403 error403){
//            String responsePageName = "/403Error.jsp";
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//            request.setAttribute("errorMsg", error403.getMessage());
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            requestDispatcher.forward(request, response);
//        }
//    }
//
//    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//        try {
//            String foodName = request.getParameter("foodName");
//            String restaurantId = request.getPathInfo().replace("/", "");
//            String msg = CommandHandler.getInstance().addToCart(restaurantId, foodName);
//            String responsePageName = "/restaurant.jsp";
//            Restaurant restaurant= CommandHandler.getInstance().getRestaurant(restaurantId);
//            Location userLocation = CommandHandler.getInstance().getUser().getLocation();
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//            request.setAttribute("restaurant", restaurant);
//            request.setAttribute("userLocation", userLocation);
//            request.setAttribute("msg", msg);
//            response.setStatus(HttpServletResponse.SC_OK);
//            requestDispatcher.forward(request, response);
//        } catch (Error404 error404) {
//            String responsePageName = "/404Error.jsp";
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//            request.setAttribute("errorMsg", error404.getMessage());
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            requestDispatcher.forward(request, response);
//        } catch (Error403 error403) {
//            String responsePageName = "/403Error.jsp";
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//            request.setAttribute("errorMsg", error403.getMessage());
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            requestDispatcher.forward(request, response);
//        }
//    }
//}