package note_manager.note_manager.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import note_manager.note_manager.Entity.Note;


public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
    
    List<Note> findByUsuarioIdAndFechaBetweenOrderByFechaDesc(Long usuarioId,LocalDateTime fechaInicio,LocalDateTime fechaFin);
    
    Optional<Note> findByIdAndUsuarioId(Long id, Long usuarioId);    
   
    List<Note> findByUsuarioIdAndEtiquetasContaining(Long usuarioId, String etiqueta);
    
    List<Note> findTop5ByUsuarioIdOrderByFechaDesc(Long usuarioId);
    
    List<Note> findByUsuarioIdAndTituloContainingIgnoreCase(Long usuarioId, String titulo);
}
