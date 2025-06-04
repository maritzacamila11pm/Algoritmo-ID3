package com.arboles.id3fx;

import java.util.Map;
import java.util.HashMap;

public class NodoID3 {
    private String atributo;
    private String valor;
    private String clasificacion;
    private Map<String, NodoID3> hijos;
    private boolean esHoja;

    public NodoID3() {
        this.hijos = new HashMap<>();
        this.esHoja = false;
    }

    public NodoID3(String clasificacion) {
        this.clasificacion = clasificacion;
        this.esHoja = true;
        this.hijos = new HashMap<>();
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public Map<String, NodoID3> getHijos() {
        return hijos;
    }

    public void agregarHijo(String valorAtributo, NodoID3 hijo) {
        hijos.put(valorAtributo, hijo);
    }

    public boolean esHoja() {
        return esHoja;
    }

    public void setEsHoja(boolean esHoja) {
        this.esHoja = esHoja;
    }
}