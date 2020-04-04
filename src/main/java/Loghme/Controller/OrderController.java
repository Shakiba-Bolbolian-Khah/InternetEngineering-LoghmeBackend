//package Loghme.Controller;
//
//import Loghme.Exceptions.Error404;
//import Loghme.Model.CommandHandler;
//import Loghme.Model.Order;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Objects;
//
//@SuppressWarnings("serial")
//@WebServlet("/order/*")
//public class OrderController extends HttpServlet {
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        try {
//            String id = request.getPathInfo().replace("/", "");
//            int orderId = Integer.parseInt(Objects.requireNonNull(id));
//            Order order = CommandHandler.getInstance().getOrder(orderId);
//            String responsePageName = "/order.jsp";
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//            request.setAttribute("order", order);
//            response.setStatus(HttpServletResponse.SC_OK);
//            requestDispatcher.forward(request, response);
//        } catch (Error404 error404) {
//            String responsePageName = "/404Error.jsp";
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//            request.setAttribute("errorMsg", error404.getMessage());
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            requestDispatcher.forward(request, response);
//        }
//    }
//}