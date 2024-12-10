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
    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        RegisterRequest request = new RegisterRequest();
        when(authService.register(any(RegisterRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = authController.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testLogin() {
        LoginRequest request = new LoginRequest();
        when(authService.login(any(LoginRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRegisterAdmin() {
        RegisterRequest request = new RegisterRequest();
        when(jwtService.getRoleFromToken(any(String.class))).thenReturn("ROLE_ADMIN");
        when(httpRequest.getHeader(any(String.class))).thenReturn("Bearer token");
        when(authService.registerAdmin(any(RegisterRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = authController.registerAdmin(request, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testLoginAdmin() {
        LoginRequest request = new LoginRequest();
        when(authService.loginAdmin(any(LoginRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = authController.loginAdmin(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
