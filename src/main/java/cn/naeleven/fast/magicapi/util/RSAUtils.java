package cn.naeleven.fast.magicapi.util;

import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

/**
 * RSAUtils 加解密
 */
public class RSAUtils {
    private static final String KEY_ALGORITHM = "RSA" ;
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA" ;

    private static final String BEGIN_ENCRYPTED_PRIVATE_KEY = "-----BEGIN ENCRYPTED PRIVATE KEY-----" ;
    private static final String END_ENCRYPTED_PRIVATE_KEY = "-----END ENCRYPTED PRIVATE KEY-----" ;
    private static final String BEGIN_RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----" ;
    private static final String END_RSA_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----" ;

    private static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----" ;
    private static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----" ;
    private static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----" ;
    private static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----" ;

    private static final String CHARSET_NAME = "UTF-8" ;
    private static final String LINE_SPLIT = "\n" ;

    private static final List<String> SKIP_STR = Lists.newArrayList(
            BEGIN_PRIVATE_KEY, END_PRIVATE_KEY, BEGIN_PUBLIC_KEY, END_PUBLIC_KEY,
            BEGIN_ENCRYPTED_PRIVATE_KEY, END_ENCRYPTED_PRIVATE_KEY, BEGIN_RSA_PRIVATE_KEY, END_RSA_PRIVATE_KEY
    );

    private static String getPermKey(File permFile) throws IOException {
        StringBuilder strKeyPEM = new StringBuilder(2048);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(permFile),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (SKIP_STR.contains(line)) {
                    continue;
                }
                strKeyPEM.append(line).append(LINE_SPLIT);
            }
        }
        return strKeyPEM.toString();
    }

    private static String getPermKey(String permBase64) throws IOException {
        String perm = Base64Utils.decodeContentToStr(permBase64);
        if (StringUtils.isEmpty(perm)) {
            throw new IOException("Perm content is empty");
        }
        StringBuilder strKeyPEM = new StringBuilder(2048);
        try (BufferedReader br = new BufferedReader(new StringReader(perm))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (SKIP_STR.contains(line)) {
                    continue;
                }
                strKeyPEM.append(line).append(LINE_SPLIT);
            }
        }
        return strKeyPEM.toString();
    }


    public static RSAPrivateKey getPrivateKey(File rsaPrivatePermFile) throws IOException, GeneralSecurityException {
        String privateKeyPEM = getPermKey(rsaPrivatePermFile);
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        return (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(encoded));
    }

    public static RSAPrivateKey getPrivateKey(String rsaPrivateKeyBase64) throws IOException, GeneralSecurityException {
        String privateKeyPEM = getPermKey(rsaPrivateKeyBase64);
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);
        return (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(encoded));
    }

    public static RSAPublicKey getPublicKey(File rsaPublicPermFile) throws IOException, GeneralSecurityException {
        String publicKeyPEM = getPermKey(rsaPublicPermFile);
        byte[] encoded = Base64.decodeBase64(publicKeyPEM);
        return (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(encoded));
    }

    public static RSAPublicKey getPublicKey(String rsaPublicKeyBase64) throws IOException, GeneralSecurityException {
        byte[] encoded = Base64.decodeBase64(getPermKey(rsaPublicKeyBase64));
        return (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(encoded));
    }

    public static String sign(PrivateKey privateKey,
                              String message) throws NoSuchAlgorithmException, InvalidKeyException,
            SignatureException, UnsupportedEncodingException {
        Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);
        sign.initSign(privateKey);
        sign.update(message.getBytes(CHARSET_NAME));
        return new String(Base64.encodeBase64(sign.sign()), CHARSET_NAME);
    }

    public static boolean verify(PublicKey publicKey, String message,
                                 String signature) throws SignatureException, NoSuchAlgorithmException,
            UnsupportedEncodingException, InvalidKeyException {
        Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);
        sign.initVerify(publicKey);
        sign.update(message.getBytes(CHARSET_NAME));
        return sign.verify(Base64.decodeBase64(signature.getBytes(CHARSET_NAME)));
    }

    public static String encrypt(String rawText, PublicKey publicKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64String(cipher.doFinal(rawText.getBytes(CHARSET_NAME)));
    }

    public static String decrypt(String cipherText,
                                 PrivateKey privateKey) throws IOException, GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.decodeBase64(cipherText)), CHARSET_NAME);
    }
}
