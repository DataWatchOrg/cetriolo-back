package dream.team.cetriolo.sprintbootapp.middlewareJava.filter;

import dream.team.cetriolo.sprintbootapp.middlewareJava.serviceDw.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import org.json.JSONObject;

@Component
@Order(1)
public class DwFilter extends GenericFilterBean {

    @Autowired
    private MessageSender messageSender;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        CachedBodyHttpServletRequest cachedReq = new CachedBodyHttpServletRequest(req);

        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier(res);

        JSONObject json = new JSONObject();

        // TODO: usar essa parte na Sprint 3, ao criptografar tudo...

        // Pega os Headers
        JSONObject headerJson = new JSONObject();
        req.getHeaderNames().asIterator().forEachRemaining(key -> {
            headerJson.put(key, req.getHeader(key));
        });

        // Pega outras informações
        headerJson.put("method", req.getMethod());
        headerJson.put("query-string", req.getQueryString());
        headerJson.put("date", System.currentTimeMillis());

        // Pega o body
        StringBuilder body = new StringBuilder();
        cachedReq.getReader().lines().forEach(linha -> body.append(linha));

        JSONObject bodyJson = new JSONObject(body.toString());

        json.put("header", headerJson);
        json.put("body", bodyJson);
        System.out.println(json.toString());

        new Thread(() -> messageSender.send(json.toString())).start();

        filterChain.doFilter(cachedReq, responseCopier);

        new Thread(() -> messageSender.send(new String(responseCopier.getCopy()))).start();
    }

}
