package note_manager.note_manager.DTO;

import java.util.List;

public class NoteRequest{
    private String titulo;
    private String contenido;
    private List<String> etiquetas;

    public NoteRequest() {
    }

    public NoteRequest(String titulo, String contenido, List<String> etiquetas) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.etiquetas = etiquetas;
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

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    
    
    
}
