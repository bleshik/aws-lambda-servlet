package com.github.bleshik.example;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PingServlet extends HttpServlet {
   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      response.setContentType("text/html");
      response.setStatus(200);
      PrintWriter out = response.getWriter();
      out.print("OK");
      out.close();
   }
}
