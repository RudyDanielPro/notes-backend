package note_manager.note_manager.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import note_manager.note_manager.DTO.LoginRequest;
import note_manager.note_manager.DTO.LoginResponse;
import note_manager.note_manager.DTO.RegisterRequest;
import note_manager.note_manager.DTO.UserProfile;
import note_manager.note_manager.Entity.User;
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

    public UserProfile registerUser(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("El email ya esta registrado");
        }

        String encriptacion = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getNombre(), request.getApellido(), request.getEdad(), request.getEmail(), encriptacion);

        user.setRol("USER"); // <-- IMPORTANTE: Cambiado a mayúsculas para evitar problemas con Spring Security

        User usuarioGuardado = userRepository.save(user);

        return new UserProfile(usuarioGuardado.getId(), usuarioGuardado.getNombre(), 
            usuarioGuardado.getApellido(), usuarioGuardado.getEdad(), usuarioGuardado.getEmail(), usuarioGuardado.getRol());
    }

    public LoginResponse login(LoginRequest request){
        Optional<User> usuarioOpt = userRepository.findByEmail(request.getEmail());
        
        if(usuarioOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), usuarioOpt.get().getPassword())){
            throw new RuntimeException("Email o contraseña incorrectos");
        }
        
        User usuario = usuarioOpt.get();
        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());
        
        return new LoginResponse(token, usuario.getEmail(), usuario.getNombre(), usuario.getRol());
    }

    // ==========================================
    // MÉTODOS CRUD PARA EL ADMINISTRADOR
    // ==========================================

    public List<UserProfile> getAllUsers() {
        return userRepository.findAll().stream()
            .map(u -> new UserProfile(u.getId(), u.getNombre(), u.getApellido(), u.getEdad(), u.getEmail(), u.getRol()))
            .collect(Collectors.toList());
    }

    public UserProfile getUserById(Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new UserProfile(u.getId(), u.getNombre(), u.getApellido(), u.getEdad(), u.getEmail(), u.getRol());
    }

    public UserProfile updateUser(Long id, RegisterRequest request, String nuevoRol) {
        User usuario = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEdad(request.getEdad());
        usuario.setEmail(request.getEmail());
        
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (nuevoRol != null && !nuevoRol.isEmpty()) {
            usuario.setRol(nuevoRol.toUpperCase());
        }

        User guardado = userRepository.save(usuario);
        return new UserProfile(guardado.getId(), guardado.getNombre(), guardado.getApellido(), guardado.getEdad(), guardado.getEmail(), guardado.getRol());
    }

    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }
}