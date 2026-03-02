package note_manager.note_manager.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import note_manager.note_manager.DTO.NoteRequest;
import note_manager.note_manager.DTO.NoteResponse;
import note_manager.note_manager.Entity.Note;
import note_manager.note_manager.Entity.User;
import note_manager.note_manager.Repository.NoteRepository;
import note_manager.note_manager.Repository.UserRepository;

@Service
public class NoteService {
    
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    
    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }
    
    
    public NoteResponse createNote(NoteRequest request, Long usuarioId) {
        User usuario = userRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Note note = new Note(
            usuario,
            request.getTitulo(),
            request.getContenido(),
            LocalDateTime.now(),
            request.getEtiquetas()
        );
        
       
        Note savedNote = noteRepository.save(note);
        
        return convertToResponse(savedNote);
    }
    
    public List<NoteResponse> getUserNotes(Long usuarioId) {
        List<Note> notas = noteRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
        
        return notas.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    public NoteResponse getNoteById(Long noteId, Long usuarioId) {
        Note note = noteRepository.findByIdAndUsuarioId(noteId, usuarioId)
            .orElseThrow(() -> new RuntimeException("Nota no encontrada o no pertenece al usuario"));
        
        return convertToResponse(note);
    }
    
    public NoteResponse updateNote(Long noteId, NoteRequest request, Long usuarioId) {
        Note note = noteRepository.findByIdAndUsuarioId(noteId, usuarioId)
            .orElseThrow(() -> new RuntimeException("Nota no encontrada o no pertenece al usuario"));
        
        note.setTitulo(request.getTitulo());
        note.setContenido(request.getContenido());
        note.setEtiquetas(request.getEtiquetas());
        
        Note updatedNote = noteRepository.save(note);
        
        return convertToResponse(updatedNote);
    }

    public void deleteNote(Long noteId, Long usuarioId) {
        Note note = noteRepository.findByIdAndUsuarioId(noteId, usuarioId)
            .orElseThrow(() -> new RuntimeException("Nota no encontrada o no pertenece al usuario"));
        
        noteRepository.delete(note);
    }
    
    public List<NoteResponse> getNotesByEtiqueta(Long usuarioId, String etiqueta) {
        List<Note> notas = noteRepository.findByUsuarioIdAndEtiquetasContaining(usuarioId, etiqueta);
        
        return notas.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    public List<NoteResponse> searchNotesByTitulo(Long usuarioId, String titulo) {
        List<Note> notas = noteRepository.findByUsuarioIdAndTituloContainingIgnoreCase(usuarioId, titulo);
        
        return notas.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    public List<NoteResponse> getLast5Notes(Long usuarioId) {
        List<Note> notas = noteRepository.findTop5ByUsuarioIdOrderByFechaDesc(usuarioId);
        
        return notas.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    private NoteResponse convertToResponse(Note note) {
        return new NoteResponse(
            note.getId(),
            note.getTitulo(),
            note.getContenido(),
            note.getFecha(),
            note.getEtiquetas(),
            note.getUsuario().getId()
        );
    }
}