package br.com.betola.walletola.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SHA1PasswordTest {

    @Test
    public void noMatchInPassword(){

        var password = SHA1Password.create("123456");

        assertNotEquals("123456", password.value());
    }

    @Test
    public void matchInPassword(){

        var password = SHA1Password.create("123456");

        assertTrue(password.validate("123456"));
    }

}