package dream.team.cetriolo.sprintbootapp.middlewareJava.filter;

import dream.team.cetriolo.sprintbootapp.entity.Usuario;
import dream.team.cetriolo.sprintbootapp.middlewareJava.service.*;
import dream.team.cetriolo.sprintbootapp.service.SecurityService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.core.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

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

        StringBuilder sb = new StringBuilder();

        // TODO: usar essa parte na Sprint 3, ao criptografar tudo...
        Usuario usuario = securityService.buscarUsuarioPorEmail(req.getUserPrincipal().getName());
  
        // Pega os Headers
        sb.append("{ \"header\": ");
        sb.append("{");
        req.getHeaderNames().asIterator().
                forEachRemaining(key -> {
                    String texto = "\"";
                    texto += key;
                    texto += "\":";
                    texto += "\"";
                    texto += req.getHeader(key);
                    texto += "\",";
                    sb.append(texto);
                });

        // Pega outras informações
        sb.append("\"method\": " + "\"" + req.getMethod() + "\",");
        sb.append("\"query string\": " + "\"" + req.getQueryString() +"\",");
        sb.append("\"date\": " + "\"" + System.currentTimeMillis() + "\"},");
        sb.append("\"system_data\":{\"");
        sb.append("id_usuario_logado\": " + usuario.getId()+"}");
        StringBuilder body = new StringBuilder();
        // Pega o body
        cachedReq.getReader().lines().forEach(linha -> body.append(linha));
        if(!body.toString().isEmpty()){
            sb.append(", \"body\": ");
            sb.append(body);
        }

        sb.append("}");
        System.out.println(sb.toString());

        new Thread(() -> messageSender.send(sb.toString())).start();

        filterChain.doFilter(cachedReq, servletResponse);
    }

}
