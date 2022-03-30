package com.example.biotec.clases;

public class ElementoRegistroReproductivo {
    private String id;
    private String codigo_ganado;
    private String fecha_monta;
    private String fecha;
    private String fecha_parto_real;
    private String observacion;
    private int num_servicios;
    private int num_partos;
    private String genero_cria;
    private String codigo_cria;
    private String estado;
    private String inseminacion;
    private String codPareja;
    private String fecha_celo;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getGeneroCria() {
        return genero_cria;
    }

    public void setGeneroCria(String genero) {
        this.genero_cria = genero;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo_ganado() {
        return codigo_ganado;
    }

    public void setCodigo_ganado(String codigo_ganado) {
        this.codigo_ganado = codigo_ganado;
    }

    public String getFecha_monta() {
        return fecha_monta;
    }

    public void setFecha_monta(String fecha_monta) {
        this.fecha_monta = fecha_monta;
    }

    public String getFecha_parto_real() {
        return fecha_parto_real;
    }

    public void setFecha_parto_real(String fecha_parto_real) {
        this.fecha_parto_real = fecha_parto_real;
    }

    public int getNum_servicios() {
        return num_servicios;
    }

    public void setNum_servicios(int num_servicios) {
        this.num_servicios = num_servicios;
    }

    public int getNum_partos() {
        return num_partos;
    }

    public void setNum_partos(int num_partos) {
        this.num_partos = num_partos;
    }

    public String getGenero_cria() {
        return genero_cria;
    }

    public void setGenero_cria(String genero_cria) {
        this.genero_cria = genero_cria;
    }

    public String getCodigo_cria() {
        return codigo_cria;
    }

    public void setCodigo_cria(String codigo_cria) {
        this.codigo_cria = codigo_cria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getInseminacion() {
        return inseminacion;
    }

    public void setInseminacion(String inseminacion) {
        this.inseminacion = inseminacion;
    }

    public String getCodPareja() {
        return codPareja;
    }

    public void setCodPareja(String codPareja) {
        this.codPareja = codPareja;
    }

    public String getFecha_celo() {
        return fecha_celo;
    }

    public void setFecha_celo(String fecha_celo) {
        this.fecha_celo = fecha_celo;
    }
}
