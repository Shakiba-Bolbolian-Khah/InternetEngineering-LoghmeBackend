/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2020-03-01 08:47:09 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import Model.*;

public final class order_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html lang=\"en\">\r\n");
      out.write("<head>\r\n");
      out.write("    <meta charset=\"UTF-8\">\r\n");
      out.write("    <title>Order</title>\r\n");
      out.write("    <style>\r\n");
      out.write("        li, div, form {\r\n");
      out.write("        \tpadding: 5px\r\n");
      out.write("        }\r\n");
      out.write("    </style>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("    ");
 Order order = (Order) request.getAttribute("order");
    
      out.write("\r\n");
      out.write("    ");
 if(Integer.toString(order.getId()) != null){
      out.write("\r\n");
      out.write("    <div>Order ID: ");
      out.print(order.getId());
      out.write("</div>\r\n");
      out.write("    ");
}
      out.write("\r\n");
      out.write("    <div>");
      out.print(order.getRestaurantName());
      out.write("</div>\r\n");
      out.write("    <ul>\r\n");
      out.write("    ");
for( ShoppingCartItem item : order.getItems()){
      out.write("\r\n");
      out.write("        <li>");
      out.print(item.getFood().getName());
      out.write(':');
      out.write('‌');
      out.write(' ');
      out.print(item.getNumber());
      out.write("</li>\r\n");
      out.write("    ");
}
      out.write("\r\n");
      out.write("    </ul>\r\n");
      out.write("    ");
 String state = order.getStateString(); 
      out.write("\r\n");
      out.write("    <div>\r\n");
      out.write("        status : ");
      out.print(state);
      out.write("\r\n");
      out.write("        ");
 if(state.equals("Delivering")){
      out.write("\r\n");
      out.write("        <div>remained time : 10 min 12 sec</div>\r\n");
      out.write("        ");
}
      out.write("\r\n");
      out.write("    </div>\r\n");
      out.write("    ");
 String msg = (String) request.getAttribute("msg");
        if(msg != null){ 
      out.write("\r\n");
      out.write("        <h4>");
      out.print(msg);
      out.write("</h4>\r\n");
      out.write("    ");
}
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
