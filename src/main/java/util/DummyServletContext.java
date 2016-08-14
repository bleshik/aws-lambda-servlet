package util;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;

@SuppressWarnings("deprecation")
public class DummyServletContext implements ServletContext {
    public String getContextPath() { return null; }
    public ServletContext getContext(String uripath) { return null; }
    public int getMajorVersion() { return 3; }
    public int getMinorVersion() { return 0; }
    public int getEffectiveMajorVersion() { return 3; }
    public int getEffectiveMinorVersion() { return 0; }
    public String getMimeType(String file) { return null; }
    public Set<String> getResourcePaths(String path) { return Collections.emptySet(); }
    public URL getResource(String path) throws MalformedURLException { return null; }
    public InputStream getResourceAsStream(String path) { return null; }
    public RequestDispatcher getRequestDispatcher(String path) { return null; }
    public RequestDispatcher getNamedDispatcher(String name) { return null; }
    public Servlet getServlet(String name) throws ServletException { return null; }
    public Enumeration<Servlet> getServlets() { return Collections.enumeration(Collections.emptySet()); }
    public Enumeration<String> getServletNames() { return Collections.enumeration(Collections.emptySet()); }
    public void log(String msg) { }
    public void log(Exception exception, String msg) { }
    public void log(String message, Throwable throwable) { }
    public String getRealPath(String path) { return null; }
    public String getServerInfo() { return null; }
    public String getInitParameter(String name) { return null; }
    public Enumeration<String> getInitParameterNames() { return Collections.enumeration(Collections.emptySet()); }
    public boolean setInitParameter(String name, String value) { return false; }
    public Object getAttribute(String name) { return null; }
    public Enumeration<String> getAttributeNames() { return Collections.enumeration(Collections.emptySet()); }
    public void setAttribute(String name, Object object) { }
    public void removeAttribute(String name) { }
    public String getServletContextName() { return null; }
    public ServletRegistration.Dynamic addServlet(String servletName, String className) { return null; }
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) { return null; }
    public ServletRegistration.Dynamic addServlet(String servletName, Class <? extends Servlet> servletClass) { return null; }
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException { return null; }
    public ServletRegistration getServletRegistration(String servletName) { return null; }
    public Map<String, ? extends ServletRegistration> getServletRegistrations() { return null; }
    public FilterRegistration.Dynamic addFilter(String filterName, String className) { return null; }
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) { return null; }
    public FilterRegistration.Dynamic addFilter(String filterName, Class <? extends Filter> filterClass) { return null; }
    public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException { return null; }
    public FilterRegistration getFilterRegistration(String filterName) { return null; }
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() { return null; }
    public SessionCookieConfig getSessionCookieConfig() { return null; }
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) { }
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() { return null; }
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() { return null; }
    public void addListener(String className) { }
    public <T extends EventListener> void addListener(T t) { }
    public void addListener(Class <? extends EventListener> listenerClass) { }
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException { return null; }
    public JspConfigDescriptor getJspConfigDescriptor() { return null; }
    public ClassLoader getClassLoader() { return getClass().getClassLoader(); }
    public void declareRoles(String... roleNames) { }
    public String getVirtualServerName() { return null; }
}
