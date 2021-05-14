package dream.team.cetriolo.sprintbootapp.middlewareJava.cipher.AES;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.json.JSONObject;

import dream.team.cetriolo.sprintbootapp.middlewareJava.cipher.RSA.RSAUtils;

public class AESKeyGenerator {
    
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRQoCWbSU3pEhYSKHpQmpUyv73aUZpVdHzxhLVsbCc2JQh/g4aiWOO4mvTusOvrBeCrHECzJ2nKe+AiKd04UowbvcO4qNTvS3xzm6Xr1YnDhIDXCbh6+yMdZ60j6XU6wLSM/+AKzbivkn0CB9BLu0g/1JZB/ITnP00fHL/oiSD/QIDAQAB";

    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        SecretKey key = generateKey(128);
        byte[] aesKey = key.getEncoded();
        // System.out.println(Base64.getEncoder().encodeToString(aesKey));
        
        
        IvParameterSpec ivParameterSpec = AESUtils.generateIv();
        String algorithm = "AES/CBC/PKCS5Padding";
        try {
            String messageCipheredAES = AESUtils.encrypt(algorithm, "pepino", key, ivParameterSpec);
            System.out.println("Mensagem criptografada AES\n" + messageCipheredAES);
            String cipheredAESKey = Base64.getEncoder().encodeToString(RSAUtils.encrypt(Base64.getEncoder().encodeToString(aesKey), publicKey));
            System.out.println("Chave AES criptografada\n" + cipheredAESKey);
            JSONObject jsonCipheredAESKey = new JSONObject();
            jsonCipheredAESKey.put("fodase", messageCipheredAES);
            jsonCipheredAESKey.put("fodase2", cipheredAESKey);
            System.out.println("Json\n" + jsonCipheredAESKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
