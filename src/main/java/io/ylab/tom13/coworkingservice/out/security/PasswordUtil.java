package io.ylab.tom13.coworkingservice.out.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Утилита для хеширования и верификации паролей с использованием алгоритма PBKDF2 с HMAC SHA-256.
 */
public class PasswordUtil {

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String HASH_EXCEPTION_MESSAGE = "Ошибка при шифровании пароля";
    private static final String VERIFY_EXCEPTION_MESSAGE = "Ошибка при верификации пароля: ";

    /**
     * Хеширует пароль с использованием случайной соли.
     *
     * @param password пароль для хеширования
     * @return строка, содержащая хеш и соль в формате "salt:hash"
     * @throws RuntimeException если возникает ошибка при шифровании пароля
     */
    public static String hashPassword(String password) {
        try {
            byte[] salt = getSalt();
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();

            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(salt) + ":" + encoder.encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(HASH_EXCEPTION_MESSAGE, e);
        }
    }

    /**
     * Верифицирует пароль по сохраненному хешу и соли.
     *
     * @param password пароль для верификации
     * @param storedPassword хеш и соль пароля в формате "salt:hash"
     * @return true, если пароль верифицирован, иначе false
     */
    public static boolean verifyPassword(String password, String storedPassword) {
        try {
            String[] parts = storedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);

            byte[] hashToVerify = hashPassword(password.toCharArray(), salt);
            return constantTimeEquals(hash, hashToVerify);
        } catch (Exception e) {
            System.err.println(VERIFY_EXCEPTION_MESSAGE + e.getMessage());
            return false;
        }
    }

    private static byte[] hashPassword(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    private static byte[] getSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}

