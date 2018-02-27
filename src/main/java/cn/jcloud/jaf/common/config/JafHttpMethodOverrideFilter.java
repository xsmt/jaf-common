package cn.jcloud.jaf.common.config;

import cn.jcloud.jaf.common.util.JafJsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by han on 2017/7/29.
 */
public class JafHttpMethodOverrideFilter extends OncePerRequestFilter {

    public static final String PROXY_PARAM = "$proxy";
    public static final String PROXY_PARAM_ENCODE = "%24proxy";
    public static final String METHOD_PARAM = "$method";
    public static final String BODY_PARAM = "$body";
    public static final String HEADERS_PARAM = "$headers";
    public static final String STATUS = "$status";
    public static final String STATUS_TEXT = "$status_text";
    private static final String defaultCharset = "UTF-8";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JafHttpMethodOverrideFilter() {
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        this.logger.debug("Http override filter start");
        String proxyValue = request.getParameter("$proxy");
        if("POST".equals(request.getMethod()) && "body".equals(proxyValue)) {
            ObjectMapper mapper = JafJsonMapper.getMapper();
            JsonNode proxyNode = mapper.readTree(request.getInputStream());
            JsonNode methodNode = proxyNode.get("$method");
            Assert.notNull(methodNode, "属性 $method 不能为空");
            Object headersNode = proxyNode.get("$headers");
            if(headersNode == null) {
                headersNode = NullNode.getInstance();
            }

            JsonNode bodyNode = proxyNode.get("$body");
            String bodyStr = null;
            if(bodyNode != null) {
                bodyStr = bodyNode.asText();
                if(StringUtils.isEmpty(bodyStr)) {
                    bodyStr = bodyNode.toString();
                }
            }

            JafHttpMethodOverrideFilter.HttpMethodRequestWrapper httpRequestWrapper = new JafHttpMethodOverrideFilter.HttpMethodRequestWrapper(request, methodNode.asText(), (JsonNode)headersNode, bodyStr);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            JafHttpMethodOverrideFilter.HttpServletResponseWrapper httpResponseWrapper = new JafHttpMethodOverrideFilter.HttpServletResponseWrapper(response, os);
            filterChain.doFilter(httpRequestWrapper, httpResponseWrapper);
            this.handleResponse(os, httpResponseWrapper, response, (JsonNode)headersNode);
        } else {
            filterChain.doFilter(request, response);
        }

        this.logger.debug("Http override filter end");
    }

    private void handleResponse(ByteArrayOutputStream os, JafHttpMethodOverrideFilter.HttpServletResponseWrapper httpResponseWrapper, HttpServletResponse response, JsonNode headersObject) throws IOException {
        HashMap map = new HashMap();
        map.put("$headers", headersObject);
        map.put("$status", Integer.valueOf(httpResponseWrapper.getStatus()));
        map.put("$status_text", "OK");
        String body = new String(os.toByteArray(), "UTF-8");
        if(!StringUtils.isEmpty(body)) {
            map.put("$body", body);
        }

        if(response.getContentType() == null) {
            response.setHeader("Content-Type", "application/json");
        }

        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String json = JafJsonMapper.toJson(map);
        response.setContentLength(json.length());
        out.println(json);
        out.flush();
    }

    private static class ServletOutputStreamWrapper extends ServletOutputStream {
        private OutputStream stream;

        public ServletOutputStreamWrapper(OutputStream stream) {
            this.stream = stream;
        }

        public void write(int b) throws IOException {
            this.stream.write(b);
        }

        public void write(byte[] b) throws IOException {
            this.stream.write(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            this.stream.write(b, off, len);
        }

        public boolean isReady() {
            return false;
        }

        public void setWriteListener(WriteListener writeListener) {
        }
    }

    private static class HttpServletResponseWrapper extends javax.servlet.http.HttpServletResponseWrapper implements Serializable {
        private static final long serialVersionUID = -6823255025479924073L;
        private int statusCode = 200;
        private int contentLength;
        private String contentType;
        private final List<String[]> headers = new ArrayList();
        private final List<Cookie> cookies = new ArrayList();
        private ServletOutputStream servletOutputStream;
        private PrintWriter writer;

        public HttpServletResponseWrapper(HttpServletResponse response, OutputStream outputStream) {
            super(response);
            this.servletOutputStream = new JafHttpMethodOverrideFilter.ServletOutputStreamWrapper(outputStream);
        }

        public ServletOutputStream getOutputStream() {
            return this.servletOutputStream;
        }

        public void setStatus(int code) {
            this.statusCode = code;
            super.setStatus(200);
        }

        public void sendError(int i, String string) throws IOException {
            this.statusCode = i;
            super.sendError(i, string);
        }

        public void sendError(int i) throws IOException {
            this.statusCode = i;
            super.sendError(i);
        }

        public void sendRedirect(String string) throws IOException {
            this.statusCode = 302;
            super.sendRedirect(string);
        }

        public void setStatus(int code, String msg) {
            this.statusCode = code;
            super.setStatus(code);
        }

        public int getStatus() {
            return this.statusCode;
        }

        public void setContentLength(int length) {
            this.contentLength = length;
            super.setContentLength(length);
        }

        public int getContentLength() {
            return this.contentLength;
        }

        public void setContentType(String type) {
            this.contentType = type;
            super.setContentType(type);
        }

        public String getContentType() {
            return this.contentType;
        }

        public PrintWriter getWriter() throws IOException {
            if(this.writer == null) {
                this.writer = new PrintWriter(new OutputStreamWriter(this.servletOutputStream, this.getCharacterEncoding()), true);
            }

            return this.writer;
        }

        public void addHeader(String name, String value) {
            String[] header = new String[]{name, value};
            this.headers.add(header);
            super.addHeader(name, value);
        }

        public void setHeader(String name, String value) {
            this.addHeader(name, value);
        }

        public Collection<String[]> getHeaders() {
            return this.headers;
        }

        public void addCookie(Cookie cookie) {
            this.cookies.add(cookie);
            super.addCookie(cookie);
        }

        public Collection<Cookie> getCookies() {
            return this.cookies;
        }

        public void flushBuffer() throws IOException {
            this.flush();
            super.flushBuffer();
        }

        public void reset() {
            super.reset();
            this.cookies.clear();
            this.headers.clear();
            this.statusCode = 200;
            this.contentType = null;
            this.contentLength = 0;
        }

        public void resetBuffer() {
            super.resetBuffer();
        }

        public void flush() throws IOException {
            if(this.writer != null) {
                this.writer.flush();
            }

            this.servletOutputStream.flush();
        }

        public String encodeRedirectUrl(String s) {
            return super.encodeRedirectURL(s);
        }

        public String encodeUrl(String s) {
            return super.encodeURL(s);
        }
    }

    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
        private final HttpServletRequest request;
        private final String method;
        private final JsonNode headersNode;
        private String body;

        public HttpMethodRequestWrapper(HttpServletRequest request, String method, JsonNode headersNode, String bodyNode) {
            super(request);
            this.request = request;
            this.method = method.toUpperCase();
            if(headersNode == null) {
                headersNode = NullNode.getInstance();
            }

            this.headersNode = (JsonNode)headersNode;
            if(bodyNode != null) {
                this.body = bodyNode;
            }

        }

        private String tryGetString(JsonNode node, String name) {
            JsonNode jsonNode = node.get(name);
            return jsonNode != null?jsonNode.asText():null;
        }

        private long tryGetLong(JsonNode node, String name) {
            JsonNode jsonNode = node.get(name);
            return jsonNode != null?jsonNode.asLong():0L;
        }

        private int tryGetInt(JsonNode node, String name) {
            JsonNode jsonNode = node.get(name);
            return jsonNode != null?jsonNode.asInt():0;
        }

        public String getQueryString() {
            StringBuilder reqQueryStr = new StringBuilder();
            String queryStr = this.request.getQueryString();
            if(!StringUtils.isEmpty(queryStr)) {
                if(queryStr.contains("&")) {
                    String[] paramString = queryStr.split("&");
                    String[] arr$ = paramString;
                    int len$ = paramString.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        String string = arr$[i$];
                        if(!string.contains("$proxy") && !string.contains("%24proxy")) {
                            reqQueryStr.append(string).append("&");
                        }
                    }

                    queryStr = reqQueryStr.toString();
                    if(queryStr.lastIndexOf("&") > -1) {
                        queryStr = queryStr.substring(0, queryStr.length() - 1);
                    }
                } else if(!queryStr.contains("$proxy") || !queryStr.contains("%24proxy")) {
                    queryStr = "";
                }
            }

            return queryStr;
        }

        public long getDateHeader(String name) {
            return this.tryGetLong(this.headersNode, name);
        }

        public String getContentType() {
            return this.tryGetString(this.headersNode, "Content-Type");
        }

        public String getHeader(String name) {
            String value = this.tryGetString(this.headersNode, name);
            if(null == value && name.equals("Host")) {
                value = this.request.getHeader("Host");
            }

            return value;
        }

        public Enumeration<String> getHeaders(String name) {
            String value = this.getHeader(name);
            return value != null ? Collections.enumeration(Collections.singletonList(value)):
                    Collections.<String>emptyEnumeration();
        }

        public Enumeration<String> getHeaderNames() {
            return IteratorUtils.asEnumeration(this.headersNode.fieldNames());
        }

        public String getAuthType() {
            return super.getAuthType();
        }

        public String getCharacterEncoding() {
            return super.getCharacterEncoding();
        }

        public int getContentLength() {
            return super.getContentLength();
        }

        public int getIntHeader(String name) {
            return this.tryGetInt(this.headersNode, name);
        }

        public String getMethod() {
            return this.method;
        }

        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = this.body == null?new ByteArrayInputStream(new byte[0]):new ByteArrayInputStream(this.body.getBytes("UTF-8"));
            return new ServletInputStream() {
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }

                public boolean isFinished() {
                    return false;
                }

                public boolean isReady() {
                    return false;
                }

                public void setReadListener(ReadListener readListener) {
                }
            };
        }

        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }
    }
}
