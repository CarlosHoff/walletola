package br.com.betola.walletola.domain.request;

import br.com.betola.walletola.domain.PasswordType;

public record RestoreUserRequest(String id, String email, PasswordType passwordType, String password) {
}
