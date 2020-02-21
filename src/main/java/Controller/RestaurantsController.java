package Controller;
import Exceptions.Error404;
import Exceptions.ErrorHandler;
import Model.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("serial")
@WebServlet("/restaurants")
public class RestaurantsController extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String responsePageName;
        try {
            ArrayList<Restaurant> restaurants = CommandHandler.getInstance().getRestaurants();
            responsePageName = "restaurants.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
            request.setAttribute("restaurants", restaurants);
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
