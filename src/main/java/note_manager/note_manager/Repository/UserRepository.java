package note_manager.note_manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import note_manager.note_manager.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    List<User> findByNombre(String nombre);

    List<User> findByApellido(String apellido);

    List<User> findByNombreAndApellido(String nombre, String apellido);

    List<User> findByRol(String rol);

    boolean existsByEmail(String email);

}
