package dream.team.cetriolo.sprintbootapp.middlewareJava.filter;

import dream.team.cetriolo.sprintbootapp.entity.Usuario;
import dream.team.cetriolo.sprintbootapp.middlewareJava.cipher.AES.AESKeyGenerator;
import dream.team.cetriolo.sprintbootapp.middlewareJava.cipher.AES.AESUtils;
import dream.team.cetriolo.sprintbootapp.middlewareJava.cipher.RSA.RSAUtils;
import dream.team.cetriolo.sprintbootapp.middlewareJava.serviceDw.*;
import dream.team.cetriolo.sprintbootapp.service.SecurityService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.core.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Base64;

import org.json.JSONObject;

@Component
@Order(1)
public class DwFilter extends GenericFilterBean {

    @Autowired
    private MessageSender messageSender;
    
    @Autowired
    private SecurityService securityService;
    
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRQoCWbSU3pEhYSKHpQmpUyv73aUZpVdHzxhLVsbCc2JQh/g4aiWOO4mvTusOvrBeCrHECzJ2nKe+AiKd04UowbvcO4qNTvS3xzm6Xr1YnDhIDXCbh6+yMdZ60j6XU6wLSM/+AKzbivkn0CB9BLu0g/1JZB/ITnP00fHL/oiSD/QIDAQAB";

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

        filterChain.doFilter(cachedReq, responseCopier);
        json.put("response", new String(responseCopier.getCopy()));
        System.out.println("\n\n"+json.toString()+ "\n");

        try {
            SecretKey key = AESKeyGenerator.generateKey(128);
            byte[] aesKey = key.getEncoded();
            IvParameterSpec ivParameterSpec = AESUtils.generateIv();
            String algorithm = "AES/CBC/PKCS5Padding";
            String messageCipheredAES = AESUtils.encrypt(algorithm, json.toString(), key, ivParameterSpec);
            String cipheredAESKey = Base64.getEncoder().encodeToString(RSAUtils.encrypt(Base64.getEncoder().encodeToString(aesKey), publicKey));
            JSONObject mensagemEnviar = new JSONObject();
            mensagemEnviar.put("chaveAESCriptografadaRSA", cipheredAESKey);
            mensagemEnviar.put("mensagemCriptografadaAES", messageCipheredAES);
            new Thread(() -> messageSender.send(mensagemEnviar.toString())).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        // new Thread(() -> messageSender.send(new String(responseCopier.getCopy()))).start();
    }

}