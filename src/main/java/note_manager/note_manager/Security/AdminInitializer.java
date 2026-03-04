package note_manager.note_manager.Security;

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

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setNombre(adminNombre);
            admin.setApellido(adminApellido);
            admin.setEdad(0);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRol("ADMIN");

            userRepository.save(admin);

            System.out.println("--------------------------------------");
            System.out.println("✅ USUARIO ADMIN CONFIGURADO");
            System.out.println("📧 Email: " + adminEmail);
            System.out.println("--------------------------------------");
        }
    }
}