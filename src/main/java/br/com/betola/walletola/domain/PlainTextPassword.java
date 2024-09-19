package br.com.betola.walletola.domain;

public record PlainTextPassword(String value) implements Password{

    public static PlainTextPassword create(final String plainPass){
        return new PlainTextPassword(plainPass);
    }

    public static PlainTextPassword restore(final String password){
        return new PlainTextPassword(password);
    }
    @Override
    public boolean validate(String password) {
        return value().equals(password);
    }
}
