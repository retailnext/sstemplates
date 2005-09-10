<%
  request.setAttribute("attr1", "Attribute 1 Value");
  request.getRequestDispatcher("/WEB-INF/examples/forward.sst").forward(request, response);
%>