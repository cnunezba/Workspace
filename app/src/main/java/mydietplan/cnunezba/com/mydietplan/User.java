package mydietplan.cnunezba.com.mydietplan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by cnunezba on 11/11/2014.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
@SuppressWarnings("serial")
public class User implements Serializable {

    @JsonProperty("id_usuario")
    private String id_usuario;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("pass")
    private String pass;
    @JsonProperty("email")
    private String email;
    @JsonProperty("altura")
    private String altura;
    @JsonProperty("peso")
    private String peso;
    @JsonProperty("imagen")
    private String imagen;
    @JsonProperty("perfil")
    private String perfil;
    @JsonProperty("peso_magro")
    private String peso_magro;
    @JsonProperty("session_id")
    private String session_id;

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getPeso_magro() {
        return peso_magro;
    }

    public void setPeso_magro(String peso_magro) {
        this.peso_magro = peso_magro;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
