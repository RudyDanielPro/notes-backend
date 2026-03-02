package note_manager.note_manager.Services;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretString;

    @Value("${jwt.expiration}")
    private long tiempoExpiracion;

    private Key claveSecreta;

    private Key getClaveSecreta(){
        if(claveSecreta==null){
            byte[] keyByte= secretString.getBytes();
            claveSecreta=Keys.hmacShaKeyFor(keyByte);
        }
        return claveSecreta;
    }
     
    public String generarToken(String email, String rol){
       Date ahora=new Date();
       Date fechaExpiracion = new Date(ahora.getTime()+tiempoExpiracion);
       
       return Jwts.builder().setSubject(email).claim("rol", rol).setIssuedAt(ahora).
       setExpiration(fechaExpiracion).signWith(getClaveSecreta()).compact();
    }

    public boolean validarToken(String token){
        try {
        Jwts.parserBuilder().setSigningKey(getClaveSecreta()).build().parseClaimsJws(token);
        return true;  
        } catch (Exception e) {
            return false;
        }
    }

    public String extraerEmail(String token){
        return Jwts.parserBuilder().setSigningKey(getClaveSecreta()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String extraerRol(String token){
        return Jwts.parserBuilder().setSigningKey(getClaveSecreta()).build().parseClaimsJws(token).getBody().get("rol",String.class);
    }
}
