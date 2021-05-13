package dream.team.cetriolo.sprintbootapp.middlewareJava.cipher.AES;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AESKeyGenerator {
    
    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        SecretKey key = generateKey(128);
        byte[] aesKey = key.getEncoded();
        System.out.println(Base64.getEncoder().encodeToString(aesKey));
    }
}
