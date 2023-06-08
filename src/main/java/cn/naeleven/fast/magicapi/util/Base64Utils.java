package cn.naeleven.fast.magicapi.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
public class Base64Utils {
    /**
     * BASE64 解码字符，返回解码后的字符
     *
     * @param content
     * @return
     */
    public static String decodeContentToStr(String content) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        try {
            return new String(Base64.decodeBase64(content), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("Decode content fail", e);
            return null;
        }
    }

    /**
     * BASE64 编码字符，返回编码后的字符
     *
     * @param content
     * @return
     */
    public static String encodeContentToStr(String content) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        try {
            return Base64.encodeBase64String(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.warn("Encode content fail", e);
            return null;
        }
    }

    /**
     * BASE64 编码字节流，返回编码后的字符
     *
     * @param byteContent
     * @return
     */
    public static String encodeContentToStr(byte[] byteContent) {
        if (byteContent == null || byteContent.length == 0) {
            return null;
        }
        try {
            return Base64.encodeBase64String(byteContent);
        } catch (Exception e) {
            log.warn("Encode content fail", e);
            return null;
        }
    }

    /**
     * BASE64 解码字符，返回解码后的字节流
     *
     * @param content
     * @return
     */
    public static byte[] decodeContentToByte(String content) {
        if (StringUtils.isEmpty(content)) {
            return new byte[0];
        }
        try {
            return Base64.decodeBase64(content);
        } catch (Exception e) {
            log.warn("Decode content fail", e);
            throw e;
        }
    }

    public static void main(String[] args) {
        String data = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCuKb5Yr32vtXK2ZccdzMEV0bWyClR+oNR8a/Nx6o+ucf8Gi14xU8T51fR5ej97Fc+oSNHzpW6RZgsyAXI2eXmtX9NrtF0zYcWxuqU54qylo6YWaEYcetMgA9ySoDyVLTjZBCQh4008UZINRV3z9ya7++RbYLFxxF/H31DAT2NoFMG1ieykVctDOpU83w32K5GFB3Jxx4CT1/rOkrRzCx1RcKZ1RnbquA0ksaFZLcDUk/Gl4KqYxG0zytOcLD9f5PsQInsM17+rKyVUaQxmS6cuSvZQegzCJhA1FsNk6XkNEIG/knhY2+P5xn0C9c9IUVpFdboxg/OG/AaN6XyXZfWPAgMBAAECggEAT6L4zbaZAxI1/N83GBI7LA3zRhDhJ/HEl4wJCkrWHk8z7Lsne+ixBghmPHHAuzhy9qEtl3pKv6NodRoiLnSHRdauZu5XO5p2elk9GeXjSe41kMNnY7hp95asckNg0FmYR+tT1kcIVw95eCYC2v5lMMnyVjI1kS7ZGRys4PKsIreupRr6Hi0adiykabVFS/Lkphmvp1OlxLea1nJBhKXn0pjKS07QaT5uSgxem7QZyfDvTepYrsxbf6XQtPeinHyNmTZbx5NIx46gPNgVcSyhkSXV3xHlkQH39Lio4ZbAYgE8XEIF3sDJ66G/jL68DyA0SgAmWTb2dnrHMHDIi0qb8QKBgQD91EvnJvghLwZ9leOiM55Mc9TPhgz6Hjirm8tiyEWTjVVrxlzrq9PUYfUi0HX2VHgGZEbCe4UGDLTeIyae4n+//ivyeVSHL8dukuw/Swe3xqFmaSFGqEkSHBGlOB2xnSme1NnBM89yFNXhPPoH9AI02/OCTjH+clfpGLAoTQ1kWQKBgQCvpwkLyv3lGShKNIVRUX7nhAscfgdiJCWn+VYi/YwM0wZ4qPCjBQQOUPnGMsoCKhyb3ISfJBk9cnBC1DrTevfH0nt77Mz3jNAuxMPh//QhljmvtYcNi3Fc7uhZbwxLQxcJ5KT2qW3/Q6xMnLptrLCkCg6W59hata/C+uAMn/OMJwKBgQC6s4XlDz/hwKIoi7C05PzhxS1aO4KeEs6iQ2T1UtQCdSnIrU45ttiH1kmmAulRd84U/XsioBI3Ye9DmV1V7soCoWUX4/CtwxIRHhaRc5HItlyPZHKqcyvUGztLaJxGwZAUmQFWX9KNWg/7rYtHgE6DLOnPBc82FkrdVGB2ND7OeQKBgHBOTfxaR0cpdJctSWgawRbpV+lZswLOtbzWMHqivd6dNfADpAe290boAyrz4jDMGAqXTbwqanDdMGWqKAp6/t2l+And95b3U0vzxoJDEiY/VJbBa53/X/XZUnmMBMJCHQLYS5jh71BcRledhZ0F4tKfE4Dsr2of7Eav/HxV8HPVAoGBAMPqAHtFDDVyKt63IHdHI5fPVJThMLnFHeOt3UvqdUbsKbL1Diy7EqSgjG+JjYRO8QhjLDCwtjfa+LvlrQJ1nDG0fJj3pLFxod6OGOcMHV72fyI9XSlvuXys0cJ9AvBsFdFf8l/3B8xSk102zYexkFIzWk6ngbMYMaZl5iluH1YI" ;
        System.out.println(Base64.encodeBase64String(data.getBytes(StandardCharsets.UTF_8)));
    }
}
