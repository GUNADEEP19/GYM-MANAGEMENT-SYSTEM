package com.gym.backend;

import com.gym.dto.LoginUserRequest;
import com.gym.dto.RegisterUserRequest;
import com.gym.dto.UserType;
import com.gym.model.Member;
import com.gym.model.User;
import com.gym.repository.UserRepository;
import com.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GymManagementSystemBackendApplicationTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void registerUserCreatesMember() {
		RegisterUserRequest request = new RegisterUserRequest();
		request.setName("Deepak");
		request.setEmail("deepak@example.com");
		request.setPhone("9876543210");
		request.setUserType(UserType.MEMBER);

		when(userRepository.findByEmail("deepak@example.com")).thenReturn(Optional.empty());
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
			Member member = (Member) invocation.getArgument(0);
			member.setUserId("u-123");
			return member;
		});

		var response = userService.registerUser(request);

		assertEquals("u-123", response.getUserId());
		assertEquals("Member", response.getRole());
		assertEquals("deepak@example.com", response.getEmail());
	}

	@Test
	void loginUserReturnsExistingUser() {
		LoginUserRequest request = new LoginUserRequest();
		request.setEmail("deepak@example.com");
		request.setPhone("9876543210");

		Member member = new Member();
		member.setUserId("u-123");
		member.setName("Deepak");
		member.setEmail("deepak@example.com");
		member.setPhone("9876543210");

		when(userRepository.findByEmailAndPhone("deepak@example.com", "9876543210"))
				.thenReturn(Optional.of(member));

		var response = userService.loginUser(request);

		assertEquals("deepak@example.com", response.getEmail());
		assertEquals("Member", response.getRole());
	}

	@Test
	void loginUserThrowsOnInvalidCredentials() {
		LoginUserRequest request = new LoginUserRequest();
		request.setEmail("wrong@example.com");
		request.setPhone("0000");

		when(userRepository.findByEmailAndPhone("wrong@example.com", "0000"))
				.thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> userService.loginUser(request));
	}
}
