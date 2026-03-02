package note_manager.note_manager.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class NoteResponse {
    private Long id;
    private String titulo;
    private String contenido;
    private LocalDateTime fecha;
    private List<String> etiquetas;
    private Long usuarioId;

    public NoteResponse() {
    }

    public NoteResponse(Long id, String titulo, String contenido, LocalDateTime fecha, List<String> etiquetas,Long usuarioId) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
        this.etiquetas = etiquetas;
        this.usuarioId = usuarioId;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    
}
