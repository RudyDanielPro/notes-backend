package note_manager.note_manager.Controller;

import org.springframework.web.bind.annotation.RestController;

import note_manager.note_manager.DTO.NoteRequest;
import note_manager.note_manager.DTO.NoteResponse;
import note_manager.note_manager.Entity.User;
import note_manager.note_manager.Services.NoteService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/notes")
    public NoteResponse registrarNota(@RequestBody NoteRequest request) {
        User usuario = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return noteService.createNote(request, usuario.getId());
    }

    @GetMapping("/notes")
    public List<NoteResponse> listarNotas(@RequestParam Long id) {
        return noteService.getUserNotes(id);
    }

    @GetMapping("/notes/{noteId}")
    public NoteResponse obtenerNota(@PathVariable Long noteId, @RequestParam Long id) {
        return noteService.getNoteById(noteId, id);
    }

    @PutMapping("/notes/{noteId}")
    public NoteResponse actualizarNota(@PathVariable Long noteId,
            @RequestBody NoteRequest request,
            @RequestParam Long id) {
        return noteService.updateNote(noteId, request, id);
    }

    @DeleteMapping("/notes/{noteId}")
    public ResponseEntity<Void> eliminarNota(@PathVariable Long noteId, @RequestParam Long id) {
        noteService.deleteNote(noteId, id);
        return ResponseEntity.noContent().build();
    }

}
