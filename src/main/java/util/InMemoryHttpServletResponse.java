package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * HTTP response implementation storing all the data in memory without actually writing anywhere.
 */
@SuppressWarnings("unchecked")
public class InMemoryHttpServletResponse implements HttpServletResponse {
    private final Collection<Cookie> cookies = new ArrayList<>();
    private final Map<String, Set<Object>> headers = new HashMap<>();
    private final Set<String> errors = new HashSet<>();
    private final Set<String> messages = new HashSet<>();
    private String redirect;
    private int code;
    private String encoding = "UTF-8";
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private boolean committed;
    private Locale locale = Locale.getDefault();

    public void addCookie(Cookie cookie) { cookies.add(cookie); }

    public boolean containsHeader(String name) { return headers.containsKey(name); }

    public String encodeURL(String url) { return url; }

    public String encodeRedirectURL(String url) { return url; }

    public String encodeUrl(String url) { return url; }
    
    public String encodeRedirectUrl(String url) { return url; }

    public void sendError(int sc, String msg) throws IOException {
        code = sc;
        errors.add(msg);
    }

    public void sendError(int sc) throws IOException {
        code = sc;
    }

    public void sendRedirect(String location) throws IOException {
        redirect = location;
    }

    public void doSetHeader(String name, Object value) {
        headers.put(name, new HashSet() {{ add(value); }});
    }

    public void doAddHeader(String name, Object value) {
        if (!headers.containsKey(name)) {
            doSetHeader(name, value);
        } else {
            headers.get(name).add(value);
        }
    }

    public void setDateHeader(String name, long date) {
        doSetHeader(name, date);
    }

    public void addDateHeader(String name, long date) {
        doAddHeader(name, date);
    }

    public void setHeader(String name, String value) {
        doSetHeader(name, value);
    }

    public void addHeader(String name, String value) {
        doAddHeader(name, value);
    }

    public void setIntHeader(String name, int value) {
        doSetHeader(name, value);
    }

    public void addIntHeader(String name, int value) {
        doAddHeader(name, value);
    }

    public void setStatus(int sc) { code = sc; }

    public void setStatus(int sc, String sm) {
        code = sc;
        messages.add(sm);
    }

    public int getStatus() { return code; }

    public String getHeader(String name) {
        if (headers.containsKey(name)) {
            return headers.get(name).iterator().next().toString();
        }
        return null;
    }

    public Collection<String> getHeaders(String name) {
        if (headers.containsKey(name)) {
            return headers.get(name).stream().map((i) -> i.toString()).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
    
    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    public String getCharacterEncoding() { return encoding; }

    public String getContentType() {
        return getHeader("Content-Type");
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            private WriteListener listener;
            @Override
            public boolean isReady() {
                return true;
            }
            @Override
            public void setWriteListener(WriteListener writeListener) {
                listener = writeListener;
                try {
                    writeListener.onWritePossible();
                } catch (Exception e) {}
            }
            @Override
            public void write(int b) throws IOException {
                out.write(b);
            }
            @Override
            public void flush() {
                committed = true;
            }
        };
    }

    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding()));
    }

    public void setCharacterEncoding(String charset) { this.encoding = charset; }

    public void setContentLength(int len) { doSetHeader("Content-Length", len); }

    public void setContentLengthLong(long len) { doSetHeader("Content-Length", len); }

    public void setContentType(String type) { doSetHeader("Content-Type", type); }

    public void setBufferSize(int size) { out = new ByteArrayOutputStream(size); }

    public int getBufferSize() { return out.size(); }

    public void flushBuffer() throws IOException {}

    public void resetBuffer() { committed = false; out.reset(); }

    public boolean isCommitted() { return committed; }

    public void reset() { resetBuffer(); }

    public void setLocale(Locale loc) { this.locale = loc; }

    public Locale getLocale() { return locale; }

    public String toString() {
        try {
            return out.toString(getCharacterEncoding());
        } catch(Exception e) { return null; }
    }

}
