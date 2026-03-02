package note_manager.note_manager.Services;

import java.util.Optional;

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

    String encriptacion=passwordEncoder.encode(request.getPassword());
    User user =new User(request.getNombre(), request.getApellido(), request.getEdad(), request.getEmail(), encriptacion);

    user.setRol("User"); 

    User usuarioGuardado=userRepository.save(user);

    UserProfile profile = new UserProfile(usuarioGuardado.getId(), usuarioGuardado.getNombre(), 
    usuarioGuardado.getApellido(), usuarioGuardado.getEdad(), usuarioGuardado.getEmail(), usuarioGuardado.getRol());

    return profile;

}

public LoginResponse login(LoginRequest request){
        Optional<User> usuarioOpt = userRepository.findByEmail(request.getEmail());
        
        if(usuarioOpt.isEmpty()){
            throw new RuntimeException("Email o contraseña incorrectos");
        }
        
        User usuario = usuarioOpt.get();
        
        if(!passwordEncoder.matches(request.getPassword(), usuario.getPassword())){
            throw new RuntimeException("Email o contraseña incorrectos");
        }
        
        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setEmail(usuario.getEmail());
        response.setNombre(usuario.getNombre());
        response.setRol(usuario.getRol());
        
        return response;
    }




}
