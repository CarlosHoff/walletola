package br.com.betola.walletola.domain;

import com.google.common.io.BaseEncoding;
import org.apache.log4j.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static java.lang.StringTemplate.STR;

public record PBKDF2Password(String value, String salt) implements Password {

    @Override
    public String value() {
        return STR."\{value}\{salt}";
    }

    public static PBKDF2Password create(final String plainPass) {
        var salt = generateSalt();
        try {
            return new PBKDF2Password(hash(plainPass, salt), salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateSalt() {
        var bytes = new byte[16];
        var random = new SecureRandom();
        random.nextBytes(bytes);
        return BaseEncoding.base16().encode(bytes);
    }

    public static PBKDF2Password restore(final String password) {
        var allParts = password.split("\\$\\$");
        var pass = allParts[0];
        var salt = allParts[1];
        return new PBKDF2Password(pass, salt);
    }

    public static String hash(String plainPass, final String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            PBEKeySpec spec = new PBEKeySpec(plainPass.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), 100, 512);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);  // Base64
        } catch (Throwable t) {
            Logger.getRootLogger();
            throw new RuntimeException(t);
        }
    }

    @Override
    public boolean validate(String password) {
        try {
            return value().equals(hash(password, salt));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
