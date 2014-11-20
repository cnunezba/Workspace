package mydietplan.cnunezba.com.mydietplan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cnune_000 on 15/11/2014.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Dieta {

    @JsonProperty("id_dieta")
    private Integer id_dieta;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("id_usuario")
    private Integer id_usuario;
    @JsonProperty("principal")
    private Integer principal;





    @JsonProperty("prueba_sesion")
    private String prueba_sesion;

    public String getPrueba_sesion() {
        return prueba_sesion;
    }

    public void setPrueba_sesion(String prueba_sesion) {
        this.prueba_sesion = prueba_sesion;
    }









    public Integer getId_dieta() {
        return id_dieta;
    }

    public void setId_dieta(Integer id_dieta) {
        this.id_dieta = id_dieta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Integer getPrincipal() {
        return principal;
    }

    public void setPrincipal(Integer principal) {
        this.principal = principal;
    }
}
