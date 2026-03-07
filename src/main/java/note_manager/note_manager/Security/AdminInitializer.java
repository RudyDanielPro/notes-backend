package note_manager.note_manager.Security;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import note_manager.note_manager.Entity.User;
import note_manager.note_manager.Repository.UserRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.user.email}")
    private String adminEmail;

    @Value("${admin.user.password}")
    private String adminPassword;

    @Value("${admin.user.nombre}")
    private String adminNombre;

    @Value("${admin.user.apellido}")
    private String adminApellido;

    @Value("${admin.user.role}")
    private String adminRole;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<User> adminOpt = userRepository.findByEmail(adminEmail);

        User admin = adminOpt.orElse(new User());
        admin.setNombre(adminNombre);
        admin.setApellido(adminApellido);
        admin.setEdad(0);
        admin.setEmail(adminEmail);
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRol(adminRole != null ? adminRole : "ADMIN");

        userRepository.save(admin);
    }
}