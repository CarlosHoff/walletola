package br.com.betola.walletola.domain;

import java.util.function.Function;

public enum PasswordType {
    PLAIN(PlainTextPassword::create, PlainTextPassword::restore),
    SHA1(SHA1Password::create, SHA1Password::restore),
    PBDKF2(PBKDF2Password::create, PBKDF2Password::restore);

    private final Function<String, Password> createFn;
    private final Function<String, Password> restoreFn;

    PasswordType(Function<String, Password> createFn, Function<String, Password> restoreFn) {
        this.createFn = createFn;
        this.restoreFn = restoreFn;
    }

    public Password create(final String plainPassword){
        return createFn.apply(plainPassword);
    }

    public Password restore(final String password){
        return restoreFn.apply(password);
    }
}
