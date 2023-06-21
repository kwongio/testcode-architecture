package com.example.demo.mock;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.stub.UserStub;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {

    private Long id = 1L;
    private final List<User> data = new ArrayList<>();

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return data.stream().filter(user -> user.getEmail().equals(email) && user.getStatus().equals(userStatus)).findAny();
    }

    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return data.stream().filter(user -> user.getId().equals(id) && user.getStatus().equals(userStatus)).findAny();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId() == 0L) {
            User newUser = User.builder()
                    .id(id++)
                    .email(UserStub.EMAIL)
                    .nickname(UserStub.NICKNAME)
                    .address(UserStub.ADDRESS)
                    .status(UserStatus.PENDING)
                    .certificationCode(UserStub.CERTIFICATION_CODE)
                    .lastLoginAt(0L)
                    .build();
            data.add(newUser);
            return newUser;
        }
        data.removeIf(u -> u.getId().equals(user.getId()));
        data.add(user);
        return user;
}

    @Override
    public Optional<User> findById(long id) {
        return data.stream().filter(user -> user.getId().equals(id)).findAny();
    }

    @Override
    public User getById(long id) {
        return data.stream().filter(user -> user.getId().equals(id)).findAny().orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }
}
