package com.example.biotec.clases;

import java.util.ArrayList;

public class ElementoGanado {
    private String codigo;
    private String codigoMAGAD;
    private String especie;
    private String raza;
    private String imagen;
    private String genero;
    private String fechaNacimiento;
    private String edad ;
    private double peso;
    private double tamanio;
    private String estado;
    private String proposito;
    private String lote;
    private String codMadre;
    private String codPadre;
    private String descripcion;
    private String color;
    private String ficha;
    private ArrayList<ElementoRegistroReproductivo> datosReproductivos;
    private ArrayList<ElementoFoto> fotos;


    public ElementoGanado(){

    }

    public ElementoGanado(String codigo, String especie, String imagen, String raza) {
        this.codigo = codigo;
        this.especie = especie;
        this.raza = raza;
        this.imagen = imagen;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return especie;
    }

    public void setNombre(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getTamanio() {
        return tamanio;
    }

    public void setTamanio(double tamanio) {
        this.tamanio = tamanio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getProposito() {
        return proposito;
    }

    public void setProposito(String proposito) {
        this.proposito = proposito;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getCodMadre() {
        return codMadre;
    }

    public void setCodMadre(String codMadre) {
        this.codMadre = codMadre;
    }

    public String getCodPadre() {
        return codPadre;
    }

    public void setCodPadre(String codPadre) {
        this.codPadre = codPadre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<ElementoRegistroReproductivo> getDatosReproductivos() {
        return datosReproductivos;
    }

    public void setDatosReproductivos(ArrayList<ElementoRegistroReproductivo> datosReproductivos) {
        this.datosReproductivos = datosReproductivos;
    }

    public ArrayList<ElementoFoto> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<ElementoFoto> fotos) {
        this.fotos = fotos;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFicha() {
        return ficha;
    }

    public void setFicha(String ficha) {
        this.ficha = ficha;
    }

    public String getCodigoMAGAD() {
        return codigoMAGAD;
    }

    public void setCodigoMAGAD(String codigoMAGAD) {
        this.codigoMAGAD = codigoMAGAD;
    }
}
