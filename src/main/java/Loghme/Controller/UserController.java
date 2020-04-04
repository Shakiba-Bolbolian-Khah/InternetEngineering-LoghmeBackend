//package Loghme.Controller;
//
//import Loghme.Exceptions.Error400;
//import Loghme.Model.CommandHandler;
//import Loghme.Model.User;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.*;
//
//@SuppressWarnings("serial")
//@WebServlet("/user")
//public class UserController extends HttpServlet {
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        User user= CommandHandler.getInstance().getUser();
//        String responsePageName = "/user.jsp";
//        RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//        request.setAttribute("user", user);
//        response.setStatus(HttpServletResponse.SC_OK);
//        requestDispatcher.forward(request, response);
//    }
//
//    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
//            int credit = Integer.parseInt(Objects.requireNonNull(request.getParameter("credit")));
//            if (credit <= 0){
//                throw new Error400("You should enter the credit you want to increase in right format!");
//            }
//            User user = CommandHandler.getInstance().getUser();
//            String msg = CommandHandler.getInstance().increaseCredit(credit);
//            String responsePageName = "/user.jsp";
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//            request.setAttribute("user", user);
//            request.setAttribute("msg", msg);
//            response.setStatus(HttpServletResponse.SC_OK);
//            requestDispatcher.forward(request, response);
//        } catch (NumberFormatException | Error400 e){
//            String responsePageName = "/400Error.jsp";
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//            request.setAttribute("errorMsg", "You should enter the credit you want to increase in right format!");
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            requestDispatcher.forward(request, response);
//        }
//    }
//}
