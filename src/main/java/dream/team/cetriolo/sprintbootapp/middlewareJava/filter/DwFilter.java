package dream.team.cetriolo.sprintbootapp.middlewareJava.filter;

import dream.team.cetriolo.sprintbootapp.entity.Usuario;
import dream.team.cetriolo.sprintbootapp.middlewareJava.encryptionSha3.HashMaker;
import dream.team.cetriolo.sprintbootapp.middlewareJava.serviceDw.*;
import dream.team.cetriolo.sprintbootapp.service.SecurityService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.core.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
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
    
    @Autowired
    private SecurityService securityService;
    
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
    	
    	
    	
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        CachedBodyHttpServletRequest cachedReq = new CachedBodyHttpServletRequest(req);

        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier(res);
        
        if (cachedReq.getServletPath().equals("/login")) {
        	filterChain.doFilter(cachedReq, responseCopier);
            return;
        }

        JSONObject json = new JSONObject();

        // Pega os Headers
        JSONObject headerJson = new JSONObject();
        req.getHeaderNames().asIterator().forEachRemaining(key -> {
            headerJson.put(key, req.getHeader(key));
        });

        // Pega outras informações
        headerJson.put("method", req.getMethod());
        headerJson.put("query-string", req.getQueryString());
        headerJson.put("date", System.currentTimeMillis());
        headerJson.put("path", cachedReq.getServletPath());

        Object usuarioLogado = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
     	Usuario usu = securityService.buscarUsuarioPorEmail(req.getUserPrincipal().getName());
        
        JSONObject systemData = new JSONObject();
        systemData.put("id_usuario_logado", usu.getId());
        
        // Pega o body
        StringBuilder body = new StringBuilder();
        cachedReq.getReader().lines().forEach(linha -> body.append(linha));

        JSONObject bodyJson = new JSONObject(body.toString());

        json.put("header", headerJson);
        json.put("system-data", systemData);
        json.put("body", bodyJson);
        System.out.println(json.toString());

        filterChain.doFilter(cachedReq, responseCopier);

        json.put("response", new JSONObject(new String(responseCopier.getCopy())));

        // Cria hash da resposta e inclui no json
//        String hash_integridade = HashMaker.makeHash(json.response.toString());
//        json.put("hash_integridade", hash_integridade);

        new Thread(() -> messageSender.send(json.toString())).start();
    }

}