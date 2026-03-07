package note_manager.note_manager.DTO;

public class LoginRequest {
    
    private String identificador;
    private String password;
    
    

    public LoginRequest() {
    }

    public LoginRequest(String identificador, String password) {
        this.identificador = identificador;
        this.password = password;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    

}
