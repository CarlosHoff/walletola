package br.com.betola.walletola.domain;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public record SHA1Password(String value) implements Password{

    public static SHA1Password create(final String plainPass){
        return new SHA1Password(hash(plainPass));
    }

    public static SHA1Password restore(final String password){
        return new SHA1Password(password);
    }

    private static String hash(String plainPass) {
        return Hashing.sha1().hashString(plainPass, StandardCharsets.UTF_8).toString();
    }

    @Override
    public boolean validate(String password) {
        return value().equals(hash(password));
    }
}
