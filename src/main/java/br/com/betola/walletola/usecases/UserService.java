package br.com.betola.walletola.usecases;

import br.com.betola.walletola.domain.User;
import br.com.betola.walletola.domain.entity.UserEntity;
import br.com.betola.walletola.domain.request.CreateUserRequest;
import br.com.betola.walletola.domain.request.RestoreUserRequest;
import br.com.betola.walletola.domain.response.UserResponse;
import br.com.betola.walletola.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(CreateUserRequest request) {
        var userCreated = User.create(request.email(), request.passwordType(), request.password());
        userRepository.save(new UserEntity(userCreated.id(), userCreated.email(), userCreated.password().value()));
        return userCreated;
    }

    public User restore(RestoreUserRequest request) {
        return User.restore(request.id(), request.email(), request.passwordType(), request.password());
    }

    public UserResponse getUserByID(String userID) throws ChangeSetPersister.NotFoundException {
        UserEntity userEntity = userRepository.findById(userID).orElseThrow(ChangeSetPersister.NotFoundException::new);
        return new UserResponse(userEntity.getId(), userEntity.getEmail(), userEntity.getPassword());
    }

    public UserResponse getUserByEmail(String email) throws ChangeSetPersister.NotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(ChangeSetPersister.NotFoundException::new);
        return new UserResponse(userEntity.getId(), userEntity.getEmail(), userEntity.getPassword());
    }
}
