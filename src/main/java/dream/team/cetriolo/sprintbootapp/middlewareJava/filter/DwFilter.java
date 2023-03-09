package dream.team.cetriolo.sprintbootapp.middlewareJava.filter;

import dream.team.cetriolo.sprintbootapp.entity.Usuario;
import dream.team.cetriolo.sprintbootapp.middlewareJava.cipher.AES.AESKeyGenerator;
import dream.team.cetriolo.sprintbootapp.middlewareJava.cipher.AES.AESUtils;
import dream.team.cetriolo.sprintbootapp.middlewareJava.cipher.RSA.RSAUtils;
import dream.team.cetriolo.sprintbootapp.middlewareJava.serviceDw.*;
import dream.team.cetriolo.sprintbootapp.service.SecurityService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.core.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Base64;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;

@Component
@Order(1)
public class DwFilter extends GenericFilterBean {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private SecurityService securityService;

    private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCC2LAlHV2BkVZo49hdjMMyrcZapQlzIyB35cXgDuo+eZRZAsHYkH2vEj/RLddJVrKiLa71BSytdPPpncXWek/hR7PXgirm40qpx49L4vfKipCLtVaMLb8p4sjxu09E3HR9tZiU+00TiCSYlRJBeZtPAvzztmnvdCcKpQGEURNeNQIDAQAB";

    @Override
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            CachedBodyHttpServletRequest cachedReq = new CachedBodyHttpServletRequest(req);

            HttpServletResponse res = (HttpServletResponse) servletResponse;
            HttpServletResponseCopier responseCopier = new HttpServletResponseCopier(res);

            if (cachedReq.getServletPath().equals("/login")) {
                filterChain.doFilter(cachedReq, responseCopier);
                return;
            }

            JSONObject json = new JSONObject();

            JSONObject headerJson = new JSONObject();
            req.getHeaderNames().asIterator().forEachRemaining(key -> {
                headerJson.put(key, req.getHeader(key));
            });

            headerJson.put("method", req.getMethod());
            headerJson.put("query-string", req.getQueryString());
            headerJson.put("date", System.currentTimeMillis());
            headerJson.put("path", cachedReq.getServletPath());

            Usuario usu = securityService.buscarUsuarioPorEmail(req.getUserPrincipal().getName());

            JSONObject systemData = new JSONObject();
            systemData.put("id_usuario_logado", usu.getId());

            StringBuilder body = new StringBuilder();
            cachedReq.getReader().lines().forEach(linha -> body.append(linha));

            JSONObject bodyJson = new JSONObject(body.toString());

            json.put("header", headerJson);
            json.put("system_data", systemData);
            json.put("body", bodyJson);

            filterChain.doFilter(cachedReq, responseCopier);
            json.put("response", new JSONObject(new String(responseCopier.getCopy())));

            SecretKey key = AESKeyGenerator.generateKey(256);
            byte[] aesKey = key.getEncoded();
            IvParameterSpec ivParameterSpec = AESUtils.generateIv();
            String algorithm = "AES/CBC/PKCS5Padding";
            String messageCipheredAES = AESUtils.encrypt(algorithm, json.toString(), key, ivParameterSpec);
            String cipheredAESKey = Base64.getEncoder()
                    .encodeToString(RSAUtils.encrypt(Hex.encodeHexString(aesKey), publicKey));
            JSONObject mensagemEnviar = new JSONObject();
            mensagemEnviar.put("chaveAESCriptografadaRSA", cipheredAESKey);
            mensagemEnviar.put("mensagemCriptografadaAES", messageCipheredAES);
            mensagemEnviar.put("iv", Hex.encodeHexString(ivParameterSpec.getIV()));
            new Thread(() -> messageSender.send(mensagemEnviar.toString())).start();
        } catch (Exception e) {
            logger.error("An error occurred while processing the request", e);

            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter out = httpResponse.getWriter();
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("message", "An error occurred while processing the request");
            out.print(errorResponse.toString());
            out.flush();
        }
    }
}