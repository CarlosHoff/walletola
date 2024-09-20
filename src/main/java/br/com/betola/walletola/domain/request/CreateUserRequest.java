package br.com.betola.walletola.domain.request;

import br.com.betola.walletola.domain.PasswordType;

public record CreateUserRequest(String email, PasswordType passwordType, String password) {

    public CreateUserRequest(String email, String password) {
        this(email, PasswordType.PBDKF2, password);
    }
}
