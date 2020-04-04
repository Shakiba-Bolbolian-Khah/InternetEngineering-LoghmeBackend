//package Loghme.Controller;
//
//import Loghme.Exceptions.Error403;
//import Loghme.Exceptions.Error404;
//import Loghme.Model.CommandHandler;
//import Loghme.Model.PartyFood;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//
//@SuppressWarnings("serial")
//@WebServlet("/foodparty")
//public class FoodPartyController extends HttpServlet {
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        String responsePageName = "/foodparty.jsp";
//        RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//        ArrayList<PartyFood> partyFoods = CommandHandler.getInstance().getFoodParty();
//        request.setAttribute("partyFoods", partyFoods);
//        response.setStatus(HttpServletResponse.SC_OK);
//        requestDispatcher.forward(request, response);
//    }
//
//    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//        try {
//            String foodInfo = request.getParameter("foodInfo");
//            String[] foodData = foodInfo.split("\\*");
//            String msg = CommandHandler.getInstance().addPartyFoodToCart(foodData[1], foodData[0]);
//            String responsePageName = "/foodparty.jsp";
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(responsePageName);
//            ArrayList<PartyFood> partyFoods = CommandHandler.getInstance().getFoodParty();
//            request.setAttribute("partyFoods", partyFoods);
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
