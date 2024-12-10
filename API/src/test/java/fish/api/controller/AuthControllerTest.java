package fish.api.controller;

import fish.api.dto.LoginRequest;
import fish.api.dto.RegisterRequest;
import fish.api.service.AuthService;
import fish.api.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    private static final String BEARER_TOKEN = "Bearer token";

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUpMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        when(authService.register(any(RegisterRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = authController.register(request);

        assertResponseStatus(response, HttpStatus.OK);
    }

    @Test
    public void shouldLoginUserSuccessfully() {
        LoginRequest request = new LoginRequest();
        when(authService.login(any(LoginRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = authController.login(request);

        assertResponseStatus(response, HttpStatus.OK);
    }

    @Test
    public void shouldRegisterAdminSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        when(jwtService.getRoleFromToken(any(String.class))).thenReturn("ROLE_ADMIN");
        when(httpRequest.getHeader(any(String.class))).thenReturn(BEARER_TOKEN);
        when(authService.registerAdmin(any(RegisterRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = authController.registerAdmin(request, httpRequest);

        assertResponseStatus(response, HttpStatus.OK);
    }

    @Test
    public void shouldLoginAdminSuccessfully() {
        LoginRequest request = new LoginRequest();
        when(authService.loginAdmin(any(LoginRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = authController.loginAdmin(request);

        assertResponseStatus(response, HttpStatus.OK);
    }

    private void assertResponseStatus(ResponseEntity<?> response, HttpStatus expectedStatus) {
        assertEquals(expectedStatus, response.getStatusCode());
    }
}