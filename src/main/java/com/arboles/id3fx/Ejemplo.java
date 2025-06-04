package com.arboles.id3fx;

import java.util.Map;
import java.util.HashMap;

public class Ejemplo {
    private Map<String, String> atributos;
    private String clasificacion;

    public Ejemplo() {
        this.atributos = new HashMap<>();
    }

    public Ejemplo(Map<String, String> atributos, String clasificacion) {
        this.atributos = new HashMap<>(atributos);
        this.clasificacion = clasificacion;
    }

    public Map<String, String> getAtributos() {
        return atributos;
    }

    public void setAtributo(String nombre, String valor) {
        atributos.put(nombre, valor);
    }

    public String getAtributo(String nombre) {
        return atributos.get(nombre);
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }
}
