package Controller;

import Exceptions.Error400;
import Exceptions.Error403;
import Exceptions.Error404;
import Model.CommandHandler;
import Model.Order;
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
            responsePageName = "/cart.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
            request.setAttribute("cart", cart);
            String restaurantName = CommandHandler.getInstance().getCartRestaurant();
            request.setAttribute("restaurantName", restaurantName);
            response.setStatus(HttpServletResponse.SC_OK);
            requestDispatcher.forward(request, response);
        } catch (Error404 error404) {
            responsePageName = "/404Error.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
            request.setAttribute("errorMsg", error404.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            requestDispatcher.forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String responsePageName;
        try {
            Order order = CommandHandler.getInstance().finalizeOrder();
            responsePageName = "/order.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
            request.setAttribute("order", order);
            request.setAttribute("msg","Your order finalized successfully!");
            response.setStatus(HttpServletResponse.SC_OK);
            requestDispatcher.forward(request, response);
        } catch (Error404 error404) {
            responsePageName = "/404Error.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
            request.setAttribute("errorMsg", error404.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            requestDispatcher.forward(request, response);
        } catch (Error400 error400) {
            responsePageName = "/400Error.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
            request.setAttribute("errorMsg", error400.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            requestDispatcher.forward(request, response);
        } catch (Error403 error403) {
            responsePageName = "/403Error.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
            request.setAttribute("errorMsg", error403.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            requestDispatcher.forward(request, response);
        }
    }
}
