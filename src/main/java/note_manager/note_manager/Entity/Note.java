package note_manager.note_manager.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @Column(name = "Título", nullable = false)
    private String titulo;

    @Column(name = "Contenido", nullable = false, length = 100000)
    private String contenido;

    @Column(name = "Fecha", nullable = false)
    private LocalDateTime fecha;

    @ElementCollection
    @CollectionTable(name = "note_etiquetas", joinColumns = @JoinColumn(name = "note_id"))
    @Column(name = "Etiquetas", nullable = false)
    private List<String> etiquetas = new ArrayList<>();

    public Note(User usuario, String titulo, String contenido, LocalDateTime fecha, List<String> etiquetas) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
        this.etiquetas = etiquetas;
    }

    public Note() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
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

    public void addEtiqueta(String etiqueta) {
        this.etiquetas.add(etiqueta);
    }

    public void removeEtiqueta(String etiqueta) {
        this.etiquetas.remove(etiqueta);
    }
}
