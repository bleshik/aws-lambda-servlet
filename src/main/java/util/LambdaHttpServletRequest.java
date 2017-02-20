package util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.ReadListener;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

/**
 * Wraps the "pass-through" AWS Lambda parameters into an HTTP request.
 */
@SuppressWarnings("unchecked")
public class LambdaHttpServletRequest implements HttpServletRequest {

    private Map<String, Object> input;
    private Map<String, Object> context;
    private Map<String, String> identity;
    private Map<String, String> querystring;
    private Map<String, String> header;
    private String body;
    private final Optional<? extends SessionManager> sm;

    public LambdaHttpServletRequest(Map<String, Object> input) {
        this(input, Optional.empty());
    }

    public LambdaHttpServletRequest(Map<String, Object> input, Optional<? extends SessionManager> sm) {
        this.sm = sm;
        this.input = input;
        this.context = (Map<String, Object>) input.get("requestContext");
        if (this.context == null) {
            this.context = new HashMap<>();
        }
        this.identity = (Map<String, String>) context.get("identity");
        if (this.identity == null) {
            this.identity = new HashMap<>();
        }
        this.querystring = (Map<String, String>) input.get("queryStringParameters");
        if (this.querystring == null) {
            this.querystring = new HashMap<>();
        }
        this.header = (Map<String, String>) input.get("headers");
        if (this.header == null) {
            this.header = new HashMap<>();
        }
        this.body = (String) input.get("body");
        if (this.body == null) {
            this.body = "";
        }
    }

    public String getMethod() { return (String) input.get("httpMethod"); }

    public String getPathInfo() {
        return (String) input.get("path");
    }

    public String getPathTranslated() {
        return getPathInfo();
    }

    public String getContextPath() {
        return "";
    }

    public String getQueryString() {
        return querystring.entrySet()
            .stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));
    }

    public String getRemoteUser() { return null; }

    public boolean isUserInRole(String role) { return false; }

    public java.security.Principal getUserPrincipal() { return null; }

    public String getRequestedSessionId() { return null; }

    public String getRequestURI() { return getPathInfo(); }

    public StringBuffer getRequestURL() { return new StringBuffer("http://localhost:80/" + getPathInfo()); }

    public String getServletPath() { return ""; }

    public HttpSession getSession(boolean create) {
        return sm.map(m -> m.getSession(this, create)).orElse(null);
    }

    public HttpSession getSession() { return getSession(true); }

    public String changeSessionId() { return null; }

    public boolean isRequestedSessionIdValid() { return false; }

    public boolean isRequestedSessionIdFromCookie() { return false; }

    public boolean isRequestedSessionIdFromURL() { return false; }

    public boolean isRequestedSessionIdFromUrl() { return isRequestedSessionIdFromURL(); }

    public boolean authenticate(HttpServletResponse response) throws IOException,ServletException {
        return false;
    }

    public void login(String username, String password) throws ServletException { }

    public void logout() throws ServletException { }

    public Collection<Part> getParts() throws IOException, ServletException {
        return Collections.emptySet();
    }

    public Part getPart(String name) throws IOException, ServletException {
        return null;
    }

    public <T extends HttpUpgradeHandler> T  upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return null;
    }

    public String getAuthType() { return null; }

    public Cookie[] getCookies() {
        if (!header.containsKey("Cookie")) {
            return new Cookie[0];
        }
        try {
            List<Cookie> cookies = Arrays.asList(header.get("Cookie").split("; ")).stream().map(c -> {
                return new Cookie(c.substring(0, c.indexOf("=")), c.substring(c.indexOf("=") + 1));
            }).collect(Collectors.toList());
            return cookies.toArray(new Cookie[cookies.size()]);
        } catch (Exception e) {
            return new Cookie[0];
        }
    }

    public long getDateHeader(String name) {
        return System.currentTimeMillis();
    }

    public String getHeader(String name) {
        return header.get(name);
    }

    public Enumeration<String> getHeaders(String name) {
        return Collections.enumeration(Collections.singleton(getHeader(name)));
    }

    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(header.keySet());
    }

    public int getIntHeader(String name) {
        return Integer.parseInt(getHeader(name));
    }

    public String getRemoteAddr() {
        return identity.get("sourceIp");
    }

    public int getContentLength() {
        return body.length();
    }

    public String getContentType() {
        return header.get("Content-Type");
    }

    public String getCharacterEncoding() {
        return "UTF-8";
    }

    public Locale getLocale() {
        return Locale.getDefault();
    }

    public String getScheme() {
        return "http";
    }

    public int getServerPort() {
        return 80;
    }

    public String getServerName() {
        return "lambda";
    }

    public DispatcherType getDispatcherType() {
        return DispatcherType.REQUEST;
    }

    public AsyncContext getAsyncContext() { throw new IllegalStateException(); }

    public AsyncContext startAsync() throws IllegalStateException { throw new IllegalStateException(); }

    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        throw new IllegalStateException();
    }

    public boolean isAsyncStarted() { return false; }

    public boolean isAsyncSupported() { return false; }

    public ServletContext getServletContext() { return null; }

    public int getLocalPort() { return 80; }

    public String getLocalAddr() { return "127.0.0.1"; }

    public String getLocalName() { return "lo0"; }

    public int getRemotePort() { return 0; }

    public String getRealPath(String path) { return ""; }

    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    public boolean isSecure() { return false; }

    public Enumeration<Locale> getLocales() {
        return Collections.enumeration(Collections.singleton(getLocale()));
    }

    public Object getAttribute(String name) { return null; }
    
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(Collections.emptySet());
    }

    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {}

    public long getContentLengthLong() { return getContentLength(); }

    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {

            private final InputStream bodyStream = new ByteArrayInputStream(body.getBytes());
            private int next;
            private ReadListener listener;
            private boolean finished;

            public int read() throws IOException {
                if (finished) {
                    return -1;
                }
                int toReturn;
                if (next != -1) {
                    toReturn = next;
                    next = -1;
                }
                toReturn = bodyStream.read();
                if (toReturn == -1) {
                    finished = true;
                    if (listener != null) {
                        listener.onAllDataRead();
                    }
                }
                return toReturn;
            }

            public boolean isFinished() {
                try {
                    return finished || next == -1 && (next = bodyStream.read()) == -1;
                } catch (IOException e) {
                    return true;
                }
            }

            public boolean isReady() { return !isFinished(); }

            public void setReadListener(ReadListener readListener) {
                listener = readListener;
                try {
                    readListener.onDataAvailable();
                } catch (IOException e) {}
            }
        };
    }
     
    public String getParameter(String name) {
        return querystring.get(name);
    }

    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(querystring.keySet());
    }

    public String[] getParameterValues(String name) {
        return new String[] { getParameter(name) };
    }
 
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> result = new HashMap<>();
        for (String name : querystring.keySet()) {
            result.put(name, getParameterValues(name));
        }
        return result;
    }

    public String getProtocol() { return "HTTP/1.1"; }

    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new StringReader(body));
    }

    public String getRemoteHost() { return getRemoteAddr(); }

    public void setAttribute(String name, Object o) {}

    public void removeAttribute(String name) {}

}
