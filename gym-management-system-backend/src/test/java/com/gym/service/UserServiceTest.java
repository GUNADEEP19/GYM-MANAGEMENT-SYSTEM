package com.gym.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.LoginUserRequest;
import com.gym.dto.RegisterUserRequest;
import com.gym.dto.UserResponse;
import com.gym.dto.UserType;
import com.gym.model.Member;
import com.gym.model.Trainer;
import com.gym.model.User;
import com.gym.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUserThrowsWhenUserTypeMissing() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("Alex");
        request.setEmail("alex@gym.com");
        request.setPhone("1111111111");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.registerUser(request));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("400 BAD_REQUEST \"userType is required\"", exception.getMessage());
    }

    @Test
    void registerUserThrowsWhenEmailAlreadyExists() {
        RegisterUserRequest request = buildRegisterRequest(UserType.MEMBER);
        Member existing = new Member();
        existing.setEmail(request.getEmail());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existing));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.registerUser(request));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("409 CONFLICT \"Email already registered\"", exception.getMessage());
    }

    @Test
    void registerUserCreatesTrainerAndMapsResponse() {
        RegisterUserRequest request = buildRegisterRequest(UserType.TRAINER);
        Trainer savedTrainer = new Trainer();
        savedTrainer.setUserId("trainer-1");
        savedTrainer.setName(request.getName());
        savedTrainer.setEmail(request.getEmail());
        savedTrainer.setPhone(request.getPhone());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedTrainer);

        UserResponse response = userService.registerUser(request);

        verify(userRepository).save(any(User.class));
        assertEquals("trainer-1", response.getUserId());
        assertEquals("Ari", response.getName());
        assertEquals("ari@gym.com", response.getEmail());
        assertEquals("9876543210", response.getPhone());
        assertEquals("Trainer", response.getRole());
    }

    @Test
    void loginUserReturnsMappedResponseWhenCredentialsAreValid() {
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("member@gym.com");
        request.setPhone("9999999999");

        Member member = new Member();
        member.setUserId("member-1");
        member.setName("Jordan");
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());

        when(userRepository.findByEmailAndPhone(request.getEmail(), request.getPhone()))
                .thenReturn(Optional.of(member));

        UserResponse response = userService.loginUser(request);

        assertEquals("member-1", response.getUserId());
        assertEquals("Jordan", response.getName());
        assertEquals("member@gym.com", response.getEmail());
        assertEquals("9999999999", response.getPhone());
        assertEquals("Member", response.getRole());
    }

    @Test
    void loginUserThrowsUnauthorizedWhenCredentialsAreInvalid() {
        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("unknown@gym.com");
        request.setPhone("0000000000");

        when(userRepository.findByEmailAndPhone(request.getEmail(), request.getPhone()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.loginUser(request));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("401 UNAUTHORIZED \"Invalid credentials\"", exception.getMessage());
    }

    private RegisterUserRequest buildRegisterRequest(UserType userType) {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("Ari");
        request.setEmail("ari@gym.com");
        request.setPhone("9876543210");
        request.setUserType(userType);
        return request;
    }
}
