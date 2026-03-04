package note_manager.note_manager.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import note_manager.note_manager.DTO.RegisterRequest;
import note_manager.note_manager.DTO.UserProfile;
import note_manager.note_manager.Services.UserServices;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin("*")
public class AdminController {

    private final UserServices userServices;

    public AdminController(UserServices userServices) {
        this.userServices = userServices;
    }

    // Obtener todos los usuarios
    @GetMapping
    public List<UserProfile> listarUsuarios() {
        return userServices.getAllUsers();
    }

    // Obtener un usuario específico por ID
    @GetMapping("/{id}")
    public UserProfile obtenerUsuario(@PathVariable Long id) {
        return userServices.getUserById(id);
    }

    // Actualizar usuario (y opcionalmente su rol)
    @PutMapping("/{id}")
    public UserProfile actualizarUsuario(@PathVariable Long id, 
                                         @RequestBody RegisterRequest request, 
                                         @RequestParam(required = false) String rol) {
        return userServices.updateUser(id, request, rol);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        userServices.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}