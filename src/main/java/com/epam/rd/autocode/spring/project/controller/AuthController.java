package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.aop.LoggableSecurityEvent;
import com.epam.rd.autocode.spring.project.dto.ClientRegistrationDTO;
import com.epam.rd.autocode.spring.project.dto.PasswordResetDTO;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.security.JwtUtils;
import com.epam.rd.autocode.spring.project.service.RegistrationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.access-token-expiration-seconds}")
    private long accessTokenExpirationSeconds;

    @Value("${jwt.refresh-token-expiration-seconds}")
    private long refreshTokenExpirationSeconds;

    private final EmployeeRepository employeeRepo;
    private final ClientRepository clientRepo;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationService registrationService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(EmployeeRepository employeeRepo,
                          ClientRepository clientRepo,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils,
                          RegistrationService registrationService) {
        this.employeeRepo = employeeRepo;
        this.clientRepo = clientRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "authentification/login";
    }

    @PostMapping("/login")
    @LoggableSecurityEvent(value = "Спроба входу користувача", level = Level.INFO)
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam String redirect,
                        HttpServletResponse response,
                        Model model) {

        var employeeOpt = employeeRepo.findByEmail(email);
        if (employeeOpt.isPresent()) {
            var emp = employeeOpt.get();
            if (passwordEncoder.matches(password, emp.getPassword())) {
                String accessToken = jwtUtils.generateAccessToken(emp.getId().toString(), emp.getRole().name(), emp.getEmail());
                String refreshToken = jwtUtils.generateRefreshToken(emp.getId().toString(), emp.getRole().name(), emp.getEmail());
                setCookies(response, accessToken, refreshToken);
                logger.info("[SECURITY EVENT] Успішний вхід для співробітника: {}", email);

                if (!redirect.isEmpty()) {
                    return "redirect:" + redirect;
                }
                return "redirect:/books";
            }
            logger.warn("[SECURITY EVENT] Невдала спроба входу (неправильний пароль) для співробітника: {}", email);
            model.addAttribute("error", "Invalid password");
            return "authentification/login";
        }

        var clientOpt = clientRepo.findByEmail(email);
        if (clientOpt.isPresent()) {
            var cli = clientOpt.get();
            if (passwordEncoder.matches(password, cli.getPassword())) {
                String accessToken = jwtUtils.generateAccessToken(cli.getId().toString(), cli.getRole().name(), cli.getEmail());
                String refreshToken = jwtUtils.generateRefreshToken(cli.getId().toString(), cli.getRole().name(), cli.getEmail());
                setCookies(response, accessToken, refreshToken);
                logger.info("[SECURITY EVENT] Успішний вхід для клієнта: {}", email);

                if (!redirect.isEmpty()) {
                    return "redirect:" + redirect;
                }
                return "redirect:/books";
            }
            logger.warn("[SECURITY EVENT] Невдала спроба входу (неправильний пароль) для клієнта: {}", email);
            model.addAttribute("error", "Invalid password");
            return "authentification/login";
        }

        model.addAttribute("error", "User not found");
        return "authentification/login";
    }

    @PostMapping("/refresh")
    @ResponseBody
    @LoggableSecurityEvent("Спроба оновлення токена доступу")
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return ResponseEntity.status(401).body("No cookies found");

        for (Cookie cookie : cookies) {
            if ("RefreshJWT".equals(cookie.getName())) {
                String refreshToken = cookie.getValue();
                if (jwtUtils.validateToken(refreshToken)) {
                    String role = jwtUtils.getRole(refreshToken);
                    String id = jwtUtils.getSubject(refreshToken);
                    String email = jwtUtils.getEmail(refreshToken);

                    String newAccessToken = jwtUtils.generateAccessToken(id, role, email);
                    Cookie jwtCookie = new Cookie("JWT", newAccessToken);
                    jwtCookie.setHttpOnly(true);
                    jwtCookie.setPath("/");
                    jwtCookie.setMaxAge((int) accessTokenExpirationSeconds);
                    response.addCookie(jwtCookie);

                    return ResponseEntity.ok("Access token refreshed");
                }
            }
        }
        return ResponseEntity.status(401).body("Invalid or expired refresh token");
    }

    @PostMapping("/logout")
    @LoggableSecurityEvent("Вихід користувача з системи")
    public String logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("JWT", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        Cookie refreshCookie = new Cookie("RefreshJWT", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        return "redirect:/books";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "authentification/forgot-password-form";
    }

    @PostMapping("/forgot-password")
    @LoggableSecurityEvent("Запит на відновлення паролю")
    public String processForgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        Optional<Client> clientOpt = clientRepo.findByEmail(email);

        if (clientOpt.isPresent()) {
            redirectAttributes.addAttribute("email", email);
            return "redirect:/auth/reset-password";
        } else {
            redirectAttributes.addFlashAttribute("error", "User with this email was not found.");
            return "redirect:/auth/forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String email, Model model) {
        if (!model.containsAttribute("passwordDto")) {
            PasswordResetDTO dto = new PasswordResetDTO();
            dto.setToken(email);
            model.addAttribute("passwordDto", dto);
        }
        return "authentification/reset-password-form";
    }

    @PostMapping("/reset-password")
    @LoggableSecurityEvent(value = "Завершення процедури скидання паролю", level = Level.WARN)
    public String processResetPassword(@Valid @ModelAttribute("passwordDto") PasswordResetDTO passwordDto,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("passwordDto", passwordDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordDto", bindingResult);
            return "redirect:/auth/reset-password?email=" + passwordDto.getToken();
        }

        Client client = clientRepo.findByEmail(passwordDto.getToken())
                .orElseThrow(() -> new RuntimeException("Client not found - something went wrong"));

        client.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        clientRepo.save(client);

        return "redirect:/auth/login?reset_success";
    }

    @GetMapping("/client")
    public String showClientRegistrationForm(Model model) {
        model.addAttribute("registrationDto", new ClientRegistrationDTO());
        model.addAttribute("formTitle", "Create a Client Account");
        model.addAttribute("formAction", "/register/client");
        return "authentification/registration-form";
    }

    @PostMapping("/client")
    public String processClientRegistration(@Valid @ModelAttribute("registrationDto") ClientRegistrationDTO registrationDto,
                                            BindingResult bindingResult,
                                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "authentification/registration-form";
        }

        registrationService.registerClient(registrationDto);

        redirectAttributes.addFlashAttribute("registration_success", "Registration successful! Please log in.");
        return "redirect:/auth/login";
    }

    private void setCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie jwtCookie = new Cookie("JWT", accessToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) accessTokenExpirationSeconds);

        Cookie refreshCookie = new Cookie("RefreshJWT", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge((int) refreshTokenExpirationSeconds);

        response.addCookie(jwtCookie);
        response.addCookie(refreshCookie);
    }
}




