package br.com.betola.walletola.domain;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTypeTest {

    @Test
    public void generatePasswordPlain(){

        final var expectedPassword = "123456";

        var actualPassword = PasswordType.PLAIN.create(expectedPassword);

        assertEquals(expectedPassword, actualPassword.value());
    }

    @Test
    public void generatePasswordSHA1(){

        final var expectedPassword = SHA1Password.create("123456");

        var actualPassword = PasswordType.SHA1.create("123456");

        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testHash_Success() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String plainPassword = "MySecretPassword";
        String salt = "randomSalt123";

        String hashedPassword = PBKDF2Password.hash(plainPassword, salt);

        assertNotNull(hashedPassword, "O hash não deveria ser nulo");
        assertFalse(hashedPassword.isEmpty(), "O hash não deveria ser vazio");

        assertEquals(88, hashedPassword.length(), "O hash Base64 deve ter 88 caracteres.");
    }

    @Test
    public void testHash_WithDifferentSalts() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String plainPassword = "MySecretPassword";
        String salt1 = "randomSalt123";
        String salt2 = "differentSalt456";

        String hash1 = PBKDF2Password.hash(plainPassword, salt1);
        String hash2 = PBKDF2Password.hash(plainPassword, salt2);

        assertNotEquals(hash1, hash2, "Hashes com salts diferentes não devem ser iguais.");
    }

    @Test
    public void testHash_WithSameSaltAndPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String plainPassword = "MySecretPassword";
        String salt = "randomSalt123";

        String hash1 = PBKDF2Password.hash(plainPassword, salt);
        String hash2 = PBKDF2Password.hash(plainPassword, salt);

        assertEquals(hash1, hash2, "Hashes com o mesmo salt e senha devem ser iguais.");
    }

    @Test
    public void testHash_InvalidInput() {
        String plainPassword = null;
        String salt = "randomSalt123";

        assertThrows(RuntimeException.class, () -> {
            PBKDF2Password.hash(plainPassword, salt);
        });
    }

}