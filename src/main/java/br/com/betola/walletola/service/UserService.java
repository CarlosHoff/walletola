package br.com.betola.walletola.service;

import br.com.betola.walletola.domain.User;
import br.com.betola.walletola.domain.request.UserRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User create(UserRequest request) {

        return User.create(request.email(), request.passwordType(), request.password());
    }
}
