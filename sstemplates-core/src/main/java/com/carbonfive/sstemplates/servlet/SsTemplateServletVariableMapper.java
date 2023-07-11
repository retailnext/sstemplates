package com.carbonfive.sstemplates.servlet;

import com.carbonfive.sstemplates.SsTemplateVariableMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SsTemplateServletVariableMapper extends SsTemplateVariableMapper {
    private final Map<String, Map<String, Object>> implicitObjects = new HashMap<>();
    private final HttpServletRequest request;
    private final ServletContext servletContext;

    public SsTemplateServletVariableMapper(HttpServletRequest request, ServletContext servletContext, ExpressionFactory expressionFactory) {
        super(expressionFactory);
        this.request = request;
        this.servletContext = servletContext;
    }

    @Override
    public ValueExpression resolveVariable(String name)
    {
        if ("requestScope".equals(name)) {
            Map<String, Object> requestScope = getRequestScope();
            return getExpressionFactory().createValueExpression(requestScope, requestScope.getClass());
        }

        if ("sessionScope".equals(name)) {
            Map<String, Object> sessionScope = getSessionScope();
            return getExpressionFactory().createValueExpression(sessionScope, sessionScope.getClass());
        }


        if ("applicationScope".equals(name)) {
            Map<String, Object> applicationScope = getApplicationScope();
            return getExpressionFactory().createValueExpression(applicationScope, applicationScope.getClass());
        }

        if ("param".equals(name)) {
            Map<String, Object> param = getParam();
            return getExpressionFactory().createValueExpression(param, param.getClass());
        }

        if ("paramValues".equals(name)) {
            Map<String, Object> paramValues = getParamValues();
            return getExpressionFactory().createValueExpression(paramValues, paramValues.getClass());
        }

        if ("header".equals(name)) {
            Map<String, Object> header = getHeader();
            return getExpressionFactory().createValueExpression(header, header.getClass());
        }

        if ("headerValues".equals(name)) {
            Map<String, Object> headerValues = getHeaderValues();
            return getExpressionFactory().createValueExpression(headerValues, headerValues.getClass());
        }

        if ("cookie".equals(name)) {
            Map<String, Object> cookie = getCookie();
            return getExpressionFactory().createValueExpression(cookie, cookie.getClass());
        }

        if ("initParam".equals(name)) {
            Map<String, Object> initParam = getInitParam();
            return getExpressionFactory().createValueExpression(initParam, initParam.getClass());
        }

        // otherwise, try to find the name in page, request, session, then application scope
        if (getRequest().getAttribute(name) != null) {
            Object value = getRequest().getAttribute(name);
            return getExpressionFactory().createValueExpression(value, value.getClass());
        }


        if ((getRequest().getSession(false) != null) && (getRequest().getSession(false).getAttribute(name) != null)) {
            Object value = getRequest().getSession().getAttribute(name);
            return getExpressionFactory().createValueExpression(value, value.getClass());
        }

        if (getServletContext().getAttribute(name) != null) {
            Object value = getServletContext().getAttribute(name);
            return getExpressionFactory().createValueExpression(value, value.getClass());
        }

        return super.resolveVariable(name);
    }

    private Map<String, Object> getRequestScope() {
        Map<String, Object> map = implicitObjects.get("requestScope");
        if (map == null) {
            map = new HashMap<>();
            for (Enumeration<String> e = getRequest().getAttributeNames(); e.hasMoreElements();) {
                String s = e.nextElement();
                map.put(s, getRequest().getAttribute(s));
            }
            implicitObjects.put("requestScope", map);
        }
        return map;
    }

    private Map<String, Object> getSessionScope() {
        Map<String, Object> map = implicitObjects.get("sessionScope");
        if ( map == null ) {
            map = new HashMap<>();
            if ( getRequest().getSession(false) != null ) {
                for (Enumeration<String> e = getRequest().getSession().getAttributeNames(); e.hasMoreElements();) {
                    String s = e.nextElement();
                    map.put( s, getRequest().getSession().getAttribute(s) );
                }
            }
            implicitObjects.put( "sessionScope", map );
        }
        return map;
    }

    private Map<String, Object> getApplicationScope() {
        Map<String, Object> map = implicitObjects.get("applicationScope");
        if ( map == null ) {
            map = new HashMap<>();
            for (Enumeration<String> e = getServletContext().getAttributeNames(); e.hasMoreElements();) {
                String s = e.nextElement();
                map.put( s, getServletContext().getAttribute(s) );
            }
            implicitObjects.put( "applicationScope", map );
        }
        return map;
    }

    private Map<String, Object> getParam() {
        Map<String, Object> map = implicitObjects.get("param");
        if ( map == null ) {
            map = new HashMap<>();
            for (Enumeration<String> e = getRequest().getParameterNames(); e.hasMoreElements();) {
                String s = e.nextElement();
                map.put( s, getRequest().getParameter(s) );
            }
            implicitObjects.put( "param", map );
        }
        return map;
    }

    private Map<String, Object> getParamValues() {
        Map<String, Object> map = implicitObjects.get("paramValues");
        if ( map == null ) {
            map = new HashMap<>();
            for (Enumeration<String> e = getRequest().getParameterNames(); e.hasMoreElements();) {
                String s = e.nextElement();
                map.put( s, getRequest().getParameterValues(s) );
            }
            implicitObjects.put( "paramValues", map );
        }
        return map;
    }

    private Map<String, Object> getHeader() {
        Map<String, Object> map = implicitObjects.get("header");
        if ( map == null ) {
            map = new HashMap<>();
            for (Enumeration<String> e = getRequest().getHeaderNames(); e.hasMoreElements();) {
                String s = e.nextElement();
                map.put( s, getRequest().getHeader(s) );
            }
            implicitObjects.put( "header", map );
        }
        return map;
    }

    private Map<String, Object> getHeaderValues() {
        Map<String, Object> map = implicitObjects.get("headerValues");
        if ( map == null ) {
            map = new HashMap<>();
            for (Enumeration<String> e = getRequest().getHeaderNames(); e.hasMoreElements();) {
                String s = e.nextElement();
                map.put( s, getRequest().getHeaders(s) );
            }
            implicitObjects.put( "headerValues", map );
        }
        return map;
    }

    private Map<String, Object> getCookie() {
        Map<String, Object> map = implicitObjects.get("cookies");
        if ( map == null ) {
            map = new HashMap<>();
            Cookie[] cookies = getRequest().getCookies();
            for (int i=0; i < cookies.length; i++) {
                if ( ! map.containsKey(cookies[i].getName()) )
                    map.put( cookies[i].getName(), cookies[i] );
            }
            implicitObjects.put( "cookies", map );
        }
        return map;
    }

    private Map<String, Object> getInitParam() {
        Map<String, Object> map = implicitObjects.get("initParam");
        if ( map == null ) {
            map = new HashMap<String, Object>();
            for (Enumeration<String> e = getServletContext().getInitParameterNames(); e.hasMoreElements();) {
                String s = e.nextElement();
                map.put( s, getServletContext().getInitParameter(s) );
            }
            implicitObjects.put( "initParam", map );
        }
        return map;
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }

    public ServletContext getServletContext()
    {
        return servletContext;
    }

}
