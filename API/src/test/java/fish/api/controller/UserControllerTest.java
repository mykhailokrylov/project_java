package fish.api.controller;

import fish.api.dto.UpdateProfileDTO;
import fish.api.model.User;
import fish.api.service.UserService;
import fish.api.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;
    @Mock
    private HttpServletRequest httpRequest;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnProfileWhenCalledGetMyProfile() {
        User user = mockUserRetrievalWithRole("ROLE_USER");

        ResponseEntity<?> response = userController.getMyProfile(httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void shouldDeleteProfileWhenCalledDeleteMyProfile() {
        mockUserRetrievalWithRole("ROLE_USER");

        ResponseEntity<?> response = userController.deleteMyProfile(httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User profile successfully deleted", response.getBody());
    }

    @Test
    public void shouldReturnUserProfileWhenCalledGetUserProfile() {
        Long userId = 1L;
        User user = new User();
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.getUserProfile(userId, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    private User mockUserRetrievalWithRole(String role) {
        String token = "Bearer validToken";
        User user = new User();
        when(httpRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
        when(jwtService.getUserFromToken(token)).thenReturn(Optional.of(user));
        when(jwtService.getRoleFromToken("validToken")).thenReturn(role);
        return user;
    }
}