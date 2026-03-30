package com.gym.backend;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.gym.dto.LoginUserRequest;
import com.gym.dto.RegisterUserRequest;
import com.gym.dto.UserResponse;
import com.gym.dto.UserType;
import com.gym.model.Member;

import com.gym.repository.UserRepository;
import com.gym.security.JwtService;
import com.gym.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class GymManagementSystemBackendApplicationTests {

	@Mock
	private UserRepository userRepository;

	@Mock
	private JwtService jwtService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@Test
	void registerUserCreatesMember() {
		RegisterUserRequest request = new RegisterUserRequest();
		request.setName("Deepak");
		request.setEmail("deepak@example.com");
		request.setPhone("9876543210");
		request.setUserType(UserType.MEMBER);
		request.setPassword("pw");

		when(userRepository.findByEmail("deepak@example.com")).thenReturn(Optional.empty());
		when(userRepository.save(any())).thenAnswer(invocation -> {
			Member member = invocation.getArgument(0);
			member.setUserId("u-123");
			return member;
		});

		UserResponse response = userService.registerUser(request);

		assertEquals("u-123", response.getUserId());
		assertEquals("Member", response.getRole());
		assertEquals("deepak@example.com", response.getEmail());
	}

	@Test
	void loginUserReturnsExistingUser() {
		LoginUserRequest request = new LoginUserRequest();
		request.setEmail("deepak@example.com");
		request.setPassword("pw");

		Member member = new Member();
		member.setUserId("u-123");
		member.setName("Deepak");
		member.setEmail("deepak@example.com");
		member.setPhone("9876543210");
		member.setPasswordHash("hash");

		when(userRepository.findByEmail("deepak@example.com"))
				.thenReturn(Optional.of(member));
		when(passwordEncoder.matches("pw", "hash")).thenReturn(true);
		when(jwtService.generateToken("deepak@example.com")).thenReturn("jwt-token");

		UserResponse response = userService.loginUser(request);

		assertEquals("deepak@example.com", response.getEmail());
		assertEquals("Member", response.getRole());
	}

	@Test
	void loginUserThrowsOnInvalidCredentials() {
		LoginUserRequest request = new LoginUserRequest();
		request.setEmail("wrong@example.com");
		request.setPassword("bad");

		when(userRepository.findByEmail("wrong@example.com"))
				.thenReturn(Optional.empty());

		ResponseStatusException exception =
				assertThrows(ResponseStatusException.class, () -> userService.loginUser(request));
		assertEquals("401 UNAUTHORIZED \"Invalid credentials\"", exception.getMessage());
	}
}
