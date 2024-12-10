package fish.api.controller;

import fish.api.model.Admin;
import fish.api.service.AdminService;
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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AdminControllerTest {
    private static final String VALID_TOKEN = "Bearer validToken";

    @Mock
    private AdminService adminService;
    @Mock
    private JwtService jwtService;
    @Mock
    private HttpServletRequest httpRequest;
    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private void configureAuthorizationMocks(String token) {
        when(httpRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
        when(jwtService.getRoleFromToken("validToken")).thenReturn("ROLE_ADMIN");
    }

    @Test
    public void testCreateDefaultAdmin() {
        when(adminService.adminCount()).thenReturn(0L);
        when(adminService.createAdmin(any(Admin.class))).thenReturn(new Admin());
        ResponseEntity<?> response = adminController.createDefaultAdmin();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Default admin created", ((Map<String, String>) response.getBody()).get("message"));
    }

    @Test
    public void testUnsuspendUser() {
        configureAuthorizationMocks(VALID_TOKEN);
        ResponseEntity<?> response = adminController.unsuspendUser(1L, httpRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User unsuspended", ((Map<String, String>) response.getBody()).get("message"));
    }

    @Test
    public void testGetMyProfile() {
        Admin expectedAdmin = new Admin();
        configureAuthorizationMocks(VALID_TOKEN);
        when(jwtService.getUsernameFromToken("validToken")).thenReturn("admin");
        when(adminService.getAdminByUsername("admin")).thenReturn(Optional.of(expectedAdmin));
        ResponseEntity<?> response = adminController.getMyProfile(httpRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAdmin, response.getBody());
    }

    @Test
    public void testUpdateMyProfile() {
        Admin expectedAdmin = new Admin();
        configureAuthorizationMocks(VALID_TOKEN);
        when(jwtService.getUsernameFromToken("validToken")).thenReturn("admin");
        when(adminService.updateAdmin(any(String.class), any(Admin.class))).thenReturn(Optional.of(expectedAdmin));
        ResponseEntity<?> response = adminController.updateMyProfile(expectedAdmin, httpRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAdmin, response.getBody());
    }

    @Test
    public void testGetAllAdmins() {
        configureAuthorizationMocks(VALID_TOKEN);
        ResponseEntity<?> response = adminController.getAllAdmins(httpRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAdminById() {
        Admin expectedAdmin = new Admin();
        configureAuthorizationMocks(VALID_TOKEN);
        when(adminService.getAdminById(1L)).thenReturn(Optional.of(expectedAdmin));
        ResponseEntity<?> response = adminController.getAdminById(1L, httpRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAdmin, response.getBody());
    }
}