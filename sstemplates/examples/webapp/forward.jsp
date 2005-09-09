<%
  request.setAttribute("attr1", "Attribute 1 Value");
  request.getRequestDispatcher("/WEB-INF/examples/forward.sstemplates").forward(request, response);
%>