package br.com.betola.walletola.usecases;

import br.com.betola.walletola.domain.User;
import br.com.betola.walletola.domain.entity.UserEntity;
import br.com.betola.walletola.domain.request.CreateUserRequest;
import br.com.betola.walletola.domain.request.RestoreUserRequest;
import br.com.betola.walletola.domain.response.UserResponse;
import br.com.betola.walletola.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger LOG = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User create(CreateUserRequest request) {
        LOG.info("UserService - method create");
        var userCreated = User.create(request.email(), request.passwordType(), request.password());
        userRepository.save(new UserEntity(userCreated.id(), userCreated.email(), userCreated.password().value()));
        return userCreated;
    }

    public User restore(RestoreUserRequest request) {
        LOG.info("UserService - method restore");
        return User.restore(request.id(), request.email(), request.passwordType(), request.password());
    }

    public UserResponse getUserByID(String userID) throws ChangeSetPersister.NotFoundException {
        LOG.info("UserService - method getUserByID");
        UserEntity userEntity = userRepository.findById(userID).orElseThrow(ChangeSetPersister.NotFoundException::new);
        return new UserResponse(userEntity.getId(), userEntity.getEmail(), userEntity.getPassword());
    }

    public UserResponse getUserByEmail(String email) throws ChangeSetPersister.NotFoundException {
        LOG.info("UserService - method getUserByEmail");
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(ChangeSetPersister.NotFoundException::new);
        return new UserResponse(userEntity.getId(), userEntity.getEmail(), userEntity.getPassword());
    }
}
