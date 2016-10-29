package util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface SessionManager {
    HttpSession getSession(HttpServletRequest request);
    HttpSession getSession(HttpServletRequest request, boolean create);
}
