package com.epam.rd.autocode.spring.project.controllers;

import com.epam.rd.autocode.spring.project.controller.AuthController;
import com.epam.rd.autocode.spring.project.dto.ClientRegistrationDTO;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.model.enums.Role;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.security.JwtUtils;
import com.epam.rd.autocode.spring.project.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepo;
    @MockBean
    private ClientRepository clientRepo;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private RegistrationService registrationService;

    private Employee testEmployee;
    private Client testClient;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setEmail("employee@example.com");
        testEmployee.setPassword("encodedPassword");
        testEmployee.setRole(Role.ROLE_EMPLOYEE);

        testClient = new Client();
        testClient.setId(2L);
        testClient.setEmail("client@example.com");
        testClient.setPassword("encodedPassword");
        testClient.setRole(Role.ROLE_CLIENT);
    }

    @Test
    void showLoginForm_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("authentification/login"));
    }

    @Test
    void login_withValidEmployeeCredentialsAndRedirect_shouldRedirectToUrl() throws Exception {
        given(employeeRepo.findByEmail("employee@example.com")).willReturn(Optional.of(testEmployee));
        given(passwordEncoder.matches("password123", "encodedPassword")).willReturn(true);
        given(jwtUtils.generateAccessToken(any(), any(), any())).willReturn("access_token");
        given(jwtUtils.generateRefreshToken(any(), any(), any())).willReturn("refresh_token");

        mockMvc.perform(post("/auth/login")
                        .param("email", "employee@example.com")
                        .param("password", "password123")
                        .param("redirect", "/some/path"))
                .andExpect(status().is3xxRedirection())
                .andExpect(cookie().exists("JWT"))
                .andExpect(cookie().exists("RefreshJWT"))
                .andExpect(redirectedUrl("/some/path"));
    }

    @Test
    void login_withValidEmployeeAndNoRedirect_shouldRedirectToBooks() throws Exception {
        given(employeeRepo.findByEmail("employee@example.com")).willReturn(Optional.of(testEmployee));
        given(passwordEncoder.matches("password123", "encodedPassword")).willReturn(true);

        mockMvc.perform(post("/auth/login")
                        .param("email", "employee@example.com")
                        .param("password", "password123")
                        .param("redirect", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void login_withInvalidEmployeePassword_shouldReturnLoginViewWithError() throws Exception {
        given(employeeRepo.findByEmail("employee@example.com")).willReturn(Optional.of(testEmployee));
        given(passwordEncoder.matches("wrongpassword", "encodedPassword")).willReturn(false);

        mockMvc.perform(post("/auth/login")
                        .param("email", "employee@example.com")
                        .param("password", "wrongpassword")
                        .param("redirect", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("authentification/login"))
                .andExpect(model().attribute("error", "Invalid password"));
    }

    @Test
    void login_withValidClientCredentials_shouldRedirect() throws Exception {
        given(employeeRepo.findByEmail("client@example.com")).willReturn(Optional.empty());
        given(clientRepo.findByEmail("client@example.com")).willReturn(Optional.of(testClient));
        given(passwordEncoder.matches("password123", "encodedPassword")).willReturn(true);

        mockMvc.perform(post("/auth/login")
                        .param("email", "client@example.com")
                        .param("password", "password123")
                        .param("redirect", "/client/path"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/client/path"));
    }

    @Test
    void login_withInvalidClientPassword_shouldReturnLoginViewWithError() throws Exception {
        given(employeeRepo.findByEmail("client@example.com")).willReturn(Optional.empty());
        given(clientRepo.findByEmail("client@example.com")).willReturn(Optional.of(testClient));
        given(passwordEncoder.matches("wrongpassword", "encodedPassword")).willReturn(false);

        mockMvc.perform(post("/auth/login")
                        .param("email", "client@example.com")
                        .param("password", "wrongpassword")
                        .param("redirect", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("authentification/login"))
                .andExpect(model().attribute("error", "Invalid password"));
    }

    @Test
    void login_withNonExistentUser_shouldReturnLoginViewWithError() throws Exception {
        given(employeeRepo.findByEmail(anyString())).willReturn(Optional.empty());
        given(clientRepo.findByEmail(anyString())).willReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .param("email", "nouser@example.com")
                        .param("password", "password123")
                        .param("redirect", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("authentification/login"))
                .andExpect(model().attribute("error", "User not found"));
    }


    @Test
    void refreshToken_withValidToken_shouldReturnOkAndSetCookie() throws Exception {
        Cookie refreshTokenCookie = new Cookie("RefreshJWT", "valid_refresh_token");
        given(jwtUtils.validateToken("valid_refresh_token")).willReturn(true);
        given(jwtUtils.getRole("valid_refresh_token")).willReturn("ROLE_CLIENT");
        given(jwtUtils.getSubject("valid_refresh_token")).willReturn("2");
        given(jwtUtils.getEmail("valid_refresh_token")).willReturn("client@example.com");
        given(jwtUtils.generateAccessToken("2", "ROLE_CLIENT", "client@example.com")).willReturn("new_access_token");

        mockMvc.perform(post("/auth/refresh").cookie(refreshTokenCookie))
                .andExpect(status().isOk())
                .andExpect(content().string("Access token refreshed"))
                .andExpect(cookie().exists("JWT"))
                .andExpect(cookie().value("JWT", "new_access_token"));
    }

    @Test
    void refreshToken_withInvalidToken_shouldReturnUnauthorized() throws Exception {
        Cookie refreshTokenCookie = new Cookie("RefreshJWT", "invalid_token");
        given(jwtUtils.validateToken("invalid_token")).willReturn(false);

        mockMvc.perform(post("/auth/refresh").cookie(refreshTokenCookie))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid or expired refresh token"));
    }

    @Test
    void refreshToken_withNoCookies_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/auth/refresh"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("No cookies found"));
    }

    @Test
    void refreshToken_withNoRefreshCookie_shouldReturnUnauthorized() throws Exception {
        Cookie otherCookie = new Cookie("SomeOtherCookie", "value");

        mockMvc.perform(post("/auth/refresh").cookie(otherCookie))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid or expired refresh token"));
    }

    @Test
    void logout_shouldExpireCookiesAndRedirect() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(cookie().maxAge("JWT", 0))
                .andExpect(cookie().maxAge("RefreshJWT", 0))
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void showForgotPasswordForm_shouldReturnFormView() throws Exception {
        mockMvc.perform(get("/auth/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("authentification/forgot-password-form"));
    }

    @Test
    void processForgotPassword_withExistingUser_shouldRedirectToResetPassword() throws Exception {
        given(clientRepo.findByEmail("client@example.com")).willReturn(Optional.of(testClient));

        mockMvc.perform(post("/auth/forgot-password").param("email", "client@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/reset-password?email=client%40example.com"));
    }

    @Test
    void processForgotPassword_withNonExistingUser_shouldRedirectBackWithError() throws Exception {
        given(clientRepo.findByEmail(anyString())).willReturn(Optional.empty());

        mockMvc.perform(post("/auth/forgot-password").param("email", "nouser@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "User with this email was not found."))
                .andExpect(redirectedUrl("/auth/forgot-password"));
    }

    @Test
    void showResetPasswordForm_whenAccessedDirectly_shouldAddModelAttributes() throws Exception {
        mockMvc.perform(get("/auth/reset-password").param("email", "client@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("authentification/reset-password-form"))
                .andExpect(model().attributeExists("passwordDto"))
                .andExpect(model().attribute("passwordDto", hasProperty("token", is("client@example.com"))));
    }

    @Test
    void processResetPassword_withValidData_shouldUpdatePasswordAndRedirectToLogin() throws Exception {
        given(clientRepo.findByEmail("client@example.com")).willReturn(Optional.of(testClient));
        given(passwordEncoder.encode("newPassword123")).willReturn("newEncodedPassword");

        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("token", "client@example.com")
                        .param("password", "newPassword123")
                        .param("confirmPassword", "newPassword123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?reset_success"));

        verify(clientRepo).save(any(Client.class));
    }

    @Test
    void processResetPassword_withNonExistentClient_shouldThrowException() {
        given(clientRepo.findByEmail("nouser@example.com")).willReturn(Optional.empty());

        ServletException thrown = assertThrows(
                ServletException.class,
                () -> {
                    mockMvc.perform(post("/auth/reset-password")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("token", "nouser@example.com")
                            .param("password", "newPassword123")
                            .param("confirmPassword", "newPassword123")
                            .with(csrf()));
                },
                "Expected mockMvc.perform() to throw NestedServletException, but it didn't"
        );

        Throwable cause = thrown.getCause();

        assertThat(cause).isInstanceOf(RuntimeException.class);

        assertThat(cause.getMessage()).isEqualTo("Client not found - something went wrong");
    }

    @Test
    void showClientRegistrationForm_shouldReturnRegistrationView() throws Exception {
        mockMvc.perform(get("/auth/client"))
                .andExpect(status().isOk())
                .andExpect(view().name("authentification/registration-form"))
                .andExpect(model().attributeExists("registrationDto"))
                .andExpect(model().attribute("formTitle", "Create a Client Account"))
                .andExpect(model().attribute("formAction", "/register/client"));
    }

    @Test
    void processClientRegistration_withValidData_shouldRegisterAndRedirectToLogin() throws Exception {
        mockMvc.perform(post("/auth/client")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Test Client")
                        .param("email", "newclient@example.com")
                        .param("password", "Password123")
                        .param("confirmPassword", "Password123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("registration_success", "Registration successful! Please log in."))
                .andExpect(redirectedUrl("/auth/login"));

        verify(registrationService).registerClient(any(ClientRegistrationDTO.class));
    }

    @Test
    void processClientRegistration_withBindingErrors_shouldReturnFormView() throws Exception {
        mockMvc.perform(post("/auth/client")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("fullName", "")
                        .param("email", "not-an-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("authentification/registration-form"))
                .andExpect(model().hasErrors());

        verify(registrationService, never()).registerClient(any(ClientRegistrationDTO.class));
    }

    @Test
    void processResetPassword_withBindingErrors_shouldRedirectBackWithErrors() throws Exception {
        String userEmail = "user@example.com";

        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("token", userEmail)
                        .param("password", "short")
                        .param("confirmPassword", "short")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("passwordDto"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.passwordDto"))
                .andExpect(redirectedUrl("/auth/reset-password?email=" + userEmail));

        verify(clientRepo, never()).save(any(Client.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void login_whenClientLogsInWithNonEmptyRedirect_shouldRedirectToSpecifiedUrl() throws Exception {
        when(employeeRepo.findByEmail("client@example.com")).thenReturn(Optional.empty());

        when(clientRepo.findByEmail("client@example.com")).thenReturn(Optional.of(testClient));

        when(passwordEncoder.matches("correct-password", testClient.getPassword())).thenReturn(true);

        when(jwtUtils.generateAccessToken(any(), any(), any())).thenReturn("dummy-access-token");
        when(jwtUtils.generateRefreshToken(any(), any(), any())).thenReturn("dummy-refresh-token");

        mockMvc.perform(post("/login").with(csrf())
                        .param("email", "client@example.com")
                        .param("password", "correct-password")
                        .param("redirect", "/my/client/page")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void login_whenClientLogsInWithEmptyRedirect_shouldRedirectToBooks() throws Exception {
        when(employeeRepo.findByEmail("client@example.com")).thenReturn(Optional.empty());
        when(clientRepo.findByEmail("client@example.com")).thenReturn(Optional.of(testClient));
        when(passwordEncoder.matches("correct-password", testClient.getPassword())).thenReturn(true);
        when(jwtUtils.generateAccessToken(any(), any(), any())).thenReturn("dummy-access-token");
        when(jwtUtils.generateRefreshToken(any(), any(), any())).thenReturn("dummy-refresh-token");

        mockMvc.perform(post("/login").with(csrf())
                        .param("email", "client@example.com")
                        .param("password", "correct-password")
                        .param("redirect", "")
                )
                .andExpect(status().isNotFound());
    }
}