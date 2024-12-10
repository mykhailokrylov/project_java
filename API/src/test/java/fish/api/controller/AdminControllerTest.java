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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AdminControllerTest {

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
        String token = "Bearer validToken";
        when(httpRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
        when(jwtService.getRoleFromToken("validToken")).thenReturn("ROLE_ADMIN");

        ResponseEntity<?> response = adminController.unsuspendUser(1L, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User unsuspended", ((Map<String, String>) response.getBody()).get("message"));
    }

    @Test
    public void testGetMyProfile() {
        String token = "Bearer validToken";
        Admin admin = new Admin();
        when(httpRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
        when(jwtService.getRoleFromToken("validToken")).thenReturn("ROLE_ADMIN");
        when(jwtService.getUsernameFromToken("validToken")).thenReturn("admin");
        when(adminService.getAdminByUsername("admin")).thenReturn(Optional.of(admin));

        ResponseEntity<?> response = adminController.getMyProfile(httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }

    @Test
    public void testUpdateMyProfile() {
        String token = "Bearer validToken";
        Admin admin = new Admin();
        when(httpRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
        when(jwtService.getRoleFromToken("validToken")).thenReturn("ROLE_ADMIN");
        when(jwtService.getUsernameFromToken("validToken")).thenReturn("admin");
        when(adminService.updateAdmin(any(String.class), any(Admin.class))).thenReturn(Optional.of(admin));

        ResponseEntity<?> response = adminController.updateMyProfile(admin, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }

    @Test
    public void testGetAllAdmins() {
        String token = "Bearer validToken";
        when(httpRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
        when(jwtService.getRoleFromToken("validToken")).thenReturn("ROLE_ADMIN");

        ResponseEntity<?> response = adminController.getAllAdmins(httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAdminById() {
        String token = "Bearer validToken";
        Admin admin = new Admin();
        when(httpRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(token);
        when(jwtService.getRoleFromToken("validToken")).thenReturn("ROLE_ADMIN");
        when(adminService.getAdminById(1L)).thenReturn(Optional.of(admin));

        ResponseEntity<?> response = adminController.getAdminById(1L, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }
}