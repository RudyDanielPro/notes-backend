package note_manager.note_manager.Controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import note_manager.note_manager.DTO.LoginRequest;
import note_manager.note_manager.DTO.LoginResponse;
import note_manager.note_manager.DTO.RegisterRequest;
import note_manager.note_manager.DTO.UserProfile;
import note_manager.note_manager.Entity.User;
import note_manager.note_manager.Services.UserServices;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class AuthController  {

    private final UserServices userServices;

    AuthController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/auth/register")
    public UserProfile registrarUsuario(@RequestBody RegisterRequest request) {
     return userServices.registerUser(request);
    }

    @PostMapping("/auth/login")
    public LoginResponse postMethodName(@RequestBody LoginRequest request) {
        return userServices.login(request);
    }       

    @GetMapping("/auth/me")
    public UserProfile getCurrentUser() {
        // CORRECCIÓN: Extracción segura del usuario
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof User) {
            User usuario = (User) principal;
            return new UserProfile(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEdad(),
                usuario.getEmail(),
                usuario.getRol()
            );
        } else {
            throw new RuntimeException("No se encontró un usuario autenticado válido. Principal es: " + principal.getClass().getSimpleName());
        }
    }
}