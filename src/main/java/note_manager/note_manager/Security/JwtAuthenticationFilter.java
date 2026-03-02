package note_manager.note_manager.Security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import note_manager.note_manager.Entity.User;
import note_manager.note_manager.Repository.UserRepository;
import note_manager.note_manager.Services.JwtUtil;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. Obtener el header "Authorization"
        String authHeader = request.getHeader("Authorization");
        
        // 2. Si no viene o no empieza con "Bearer ", continuar sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 3. Extraer el token (quitar "Bearer ")
        String token = authHeader.substring(7);
        
        // 4. Validar token con jwtUtil
        if (!jwtUtil.validarToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 5. Si es válido, extraer email
        String email = jwtUtil.extraerEmail(token);
        
        // 6. Buscar usuario en BD por email
        User usuario = userRepository.findByEmail(email)
            .orElse(null);
        
        // 7. Si existe, crear autenticación y ponerla en SecurityContext
        if (usuario != null) {
            UsernamePasswordAuthenticationToken auth = 
                new UsernamePasswordAuthenticationToken(
                    usuario, 
                    null, 
                    Collections.emptyList()
                );
            
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        
        // 8. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}