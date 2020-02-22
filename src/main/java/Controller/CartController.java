package Controller;

import Exceptions.Error404;
import Model.CommandHandler;
import Model.Restaurant;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


@SuppressWarnings("serial")
@WebServlet("/cart")
public class CartController extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String responsePageName;
        try {
            Map<String, Integer> cart = CommandHandler.getInstance().getCart();
            responsePageName = "cart.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
            request.setAttribute("cart", cart);
            String restaurantName = CommandHandler.getInstance().getCartRestaurant();
            request.setAttribute("restaurantName", restaurantName);
            response.setStatus(HttpServletResponse.SC_OK);
            requestDispatcher.forward(request, response);
        } catch (Error404 error404) {
            responsePageName = "404Error.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
            request.setAttribute("errorMsg", error404.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            requestDispatcher.forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String responsePageName;
        try {
            Map<String, Integer> cart = CommandHandler.getInstance().getCart();
            responsePageName = "cart.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
            request.setAttribute("cart", cart);
            String restaurantName = CommandHandler.getInstance().getCartRestaurant();
            request.setAttribute("restaurantName", restaurantName);
            response.setStatus(HttpServletResponse.SC_OK);
            requestDispatcher.forward(request, response);
        } catch (Error404 error404) {
            responsePageName = "404Error.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
            request.setAttribute("errorMsg", error404.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            requestDispatcher.forward(request, response);
        }
    }
}
