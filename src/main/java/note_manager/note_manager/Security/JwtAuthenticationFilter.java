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
        
        String path = request.getRequestURI();
        System.out.println("🔍 Filtro procesando: " + path);
        
        if (path.startsWith("/api/auth/")) {
            System.out.println("➡️ Ruta pública, saltando filtro");
            filterChain.doFilter(request, response);
            return;
        }
        
        String authHeader = request.getHeader("Authorization");
        System.out.println("🔍 Authorization header: " + (authHeader != null ? "presente" : "ausente"));
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("🔍 Token extraído: " + token.substring(0, Math.min(20, token.length())) + "...");
            
            try {
                if (jwtUtil.validarToken(token)) {
                    System.out.println("✅ Token válido");
                    String email = jwtUtil.extraerEmail(token);
                    System.out.println("📧 Email: " + email);
                    
                    User usuario = userRepository.findByEmail(email).orElse(null);
                    System.out.println("👤 Usuario en BD: " + (usuario != null ? usuario.getEmail() : "NO ENCONTRADO"));
                    
                    if (usuario != null) {
                        UsernamePasswordAuthenticationToken auth = 
                            new UsernamePasswordAuthenticationToken(
                                usuario, 
                                null, 
                                Collections.emptyList()
                            );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        System.out.println("✅ Autenticación GUARDADA en SecurityContext");
                        System.out.println("🔍 Verificación: " + SecurityContextHolder.getContext().getAuthentication());
                    }
                } else {
                    System.out.println("❌ Token inválido");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        } else {
            System.out.println("➡️ No hay token Bearer, continuando sin autenticar");
        }
        
        System.out.println("➡️ Continuando cadena de filtros");
        filterChain.doFilter(request, response);
    }
}