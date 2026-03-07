package note_manager.note_manager.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import note_manager.note_manager.DTO.LoginRequest;
import note_manager.note_manager.DTO.LoginResponse;
import note_manager.note_manager.DTO.RegisterRequest;
import note_manager.note_manager.DTO.UserProfile;
import note_manager.note_manager.Entity.User;
import note_manager.note_manager.Exception.InvalidCredentialsException;
import note_manager.note_manager.Repository.UserRepository;

@Service
public class UserServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServices(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserProfile registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        String encriptacion = passwordEncoder.encode(request.getPassword());
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setEdad(request.getEdad());
        user.setEmail(request.getEmail());
        user.setPassword(encriptacion);
        user.setRol("USER"); 

        User usuarioGuardado = userRepository.save(user);

        return convertToProfile(usuarioGuardado);
    }

    public LoginResponse login(LoginRequest request) {
        User usuario = userRepository.findByUsernameOrEmail(request.getIdentificador(), request.getIdentificador())
                .orElseThrow(() -> new InvalidCredentialsException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new InvalidCredentialsException("Usuario o contraseña incorrectos");
        }

        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());

        return new LoginResponse(token, usuario.getEmail(), usuario.getNombre(), usuario.getRol());
    }

    public List<UserProfile> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToProfile)
                .collect(Collectors.toList());
    }

    public UserProfile getUserById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToProfile(u);
    }

    public UserProfile updateUser(Long id, RegisterRequest request, String nuevoRol) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEdad(request.getEdad());
        usuario.setEmail(request.getEmail());
        
        if (request.getUsername() != null) {
            usuario.setUsername(request.getUsername());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (nuevoRol != null && !nuevoRol.isEmpty()) {
            usuario.setRol(nuevoRol.toUpperCase());
        }

        User guardado = userRepository.save(usuario);
        return convertToProfile(guardado);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }

    private UserProfile convertToProfile(User u) {
        return new UserProfile(
            u.getId(), 
            u.getNombre(), 
            u.getApellido(), 
            u.getEdad(), 
            u.getEmail(), 
            u.getRol()
        );
    }
}