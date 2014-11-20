package mydietplan.cnunezba.com.mydietplan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cnune_000 on 16/11/2014.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Comida {

    @JsonProperty("id_comida")
    private Integer id_comida;
    @JsonProperty("id_dieta")
    private Integer id_dieta;
    @JsonProperty("hora")
    private String hora;
    @JsonProperty("id_alimento")
    private Integer id_alimento;
    @JsonProperty("cantidad")
    private Integer cantidad;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("tipo")
    private String tipo;
    @JsonProperty("prot")
    private Float prot;
    @JsonProperty("hdc")
    private Float hdc;
    @JsonProperty("grasa")
    private Float grasa;

    public Integer getId_comida() {
        return id_comida;
    }

    public void setId_comida(Integer id_comida) {
        this.id_comida = id_comida;
    }

    public Integer getId_dieta() {
        return id_dieta;
    }

    public void setId_dieta(Integer id_dieta) {
        this.id_dieta = id_dieta;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getId_alimento() {
        return id_alimento;
    }

    public void setId_alimento(Integer id_alimento) {
        this.id_alimento = id_alimento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Float getProt() {
        return prot;
    }

    public void setProt(Float prot) {
        this.prot = prot;
    }

    public Float getHdc() {
        return hdc;
    }

    public void setHdc(Float hdc) {
        this.hdc = hdc;
    }

    public Float getGrasa() {
        return grasa;
    }

    public void setGrasa(Float grasa) {
        this.grasa = grasa;
    }
}

