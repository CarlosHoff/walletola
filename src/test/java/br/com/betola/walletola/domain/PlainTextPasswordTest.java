package br.com.betola.walletola.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlainTextPasswordTest {

    @Test
    public void deveCriarUmPassword(){

        final var expectedPass = "123456";

        var password = new PlainTextPassword(expectedPass);

        assertTrue(password.validate(expectedPass));
    }

}