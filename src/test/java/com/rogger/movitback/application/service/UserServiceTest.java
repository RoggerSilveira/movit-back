package com.rogger.movitback.application.service;

import com.rogger.movitback.application.dto.LoginRequest;
import com.rogger.movitback.application.dto.RegisterRequest;
import com.rogger.movitback.domain.model.User;
import com.rogger.movitback.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("Rogger", "rogger@teste.com", "MinhaSenh@123");
    }

    @Test
    void deveRegistrarUsuarioComSucesso() {
        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("senha-criptografada");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.register(registerRequest);

        assertThat(result.getEmail()).isEqualTo("rogger@teste.com");
        assertThat(result.getName()).isEqualTo("Rogger");
        assertThat(result.getPassword()).isEqualTo("senha-criptografada");
        assertThat(result.getProvider()).isEqualTo("LOCAL");

        verify(userRepository).save(any(User.class));
    }

    @Test
    void deveLancarErroQuandoEmailJaExiste() {
        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.register(registerRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email já cadastrado");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deveAutenticarComCredenciaisCorretas() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("rogger@teste.com");
        user.setPassword("senha-criptografada");

        LoginRequest loginRequest = new LoginRequest("rogger@teste.com", "MinhaSenh@123");

        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(true);

        User result = userService.authenticate(loginRequest);

        assertThat(result.getEmail()).isEqualTo("rogger@teste.com");
    }

    @Test
    void deveLancarErroQuandoEmailNaoExisteNoLogin() {
        LoginRequest loginRequest = new LoginRequest("naoexiste@teste.com", "qualquerSenha");

        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.authenticate(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Credenciais inválidas");
    }

    @Test
    void deveLancarErroQuandoSenhaEstaErrada() {
        User user = new User();
        user.setEmail("rogger@teste.com");
        user.setPassword("senha-criptografada");

        LoginRequest loginRequest = new LoginRequest("rogger@teste.com", "senhaErrada");

        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> userService.authenticate(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Credenciais inválidas");
    }
}