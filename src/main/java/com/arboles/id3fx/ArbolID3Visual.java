package com.arboles.id3fx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

public class ArbolID3Visual {
    private NodoID3 raiz;
    private Canvas canvas;
    private final double nodeWidth;
    private final double nodeHeight;
    private final double verticalSeparation;
    private final double horizontalSeparation;
    private List<Ejemplo> ejemplos;
    private Set<String> atributos;
    private String atributoClase;

    public ArbolID3Visual(double nodeWidth, double nodeHeight, double verticalSeparation, double horizontalSeparation) {
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
        this.verticalSeparation = verticalSeparation;
        this.horizontalSeparation = horizontalSeparation;
        this.ejemplos = new ArrayList<>();
        this.atributos = new HashSet<>();
        this.raiz = null;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setDatos(List<Ejemplo> ejemplos, Set<String> atributos, String atributoClase) {
        this.ejemplos = new ArrayList<>(ejemplos);
        this.atributos = new HashSet<>(atributos);
        this.atributoClase = atributoClase;
    }

    public void construirArbol() {
        Set<String> atributosDisponibles = new HashSet<>(atributos);
        atributosDisponibles.remove(atributoClase);
        raiz = construirArbolRecursivo(ejemplos, atributosDisponibles);
    }

    private NodoID3 construirArbolRecursivo(List<Ejemplo> ejemplos, Set<String> atributosDisponibles) {
        // Caso base: todos los ejemplos tienen la misma clasificación
        Set<String> clasificaciones = new HashSet<>();
        for (Ejemplo ejemplo : ejemplos) {
            clasificaciones.add(ejemplo.getClasificacion());
        }

        if (clasificaciones.size() == 1) {
            return new NodoID3(clasificaciones.iterator().next());
        }

        // Caso base: no hay más atributos disponibles
        if (atributosDisponibles.isEmpty()) {
            String clasificacionMasFrecuente = obtenerClasificacionMasFrecuente(ejemplos);
            return new NodoID3(clasificacionMasFrecuente);
        }

        // Seleccionar el mejor atributo usando ganancia de información
        String mejorAtributo = seleccionarMejorAtributo(ejemplos, atributosDisponibles);
        NodoID3 nodo = new NodoID3();
        nodo.setAtributo(mejorAtributo);

        // Obtener valores únicos del mejor atributo
        Set<String> valoresAtributo = obtenerValoresAtributo(ejemplos, mejorAtributo);

        // Crear subárboles para cada valor del atributo
        for (String valor : valoresAtributo) {
            List<Ejemplo> subEjemplos = filtrarEjemplosPorValor(ejemplos, mejorAtributo, valor);

            Set<String> nuevosAtributos = new HashSet<>(atributosDisponibles);
            nuevosAtributos.remove(mejorAtributo);

            NodoID3 hijo = construirArbolRecursivo(subEjemplos, nuevosAtributos);
            hijo.setValor(valor);
            nodo.agregarHijo(valor, hijo);
        }

        return nodo;
    }

    private String seleccionarMejorAtributo(List<Ejemplo> ejemplos, Set<String> atributos) {
        double entropiaTotal = calcularEntropia(ejemplos);
        double mejorGanancia = -1;
        String mejorAtributo = null;

        for (String atributo : atributos) {
            double ganancia = calcularGananciaInformacion(ejemplos, atributo, entropiaTotal);
            if (ganancia > mejorGanancia) {
                mejorGanancia = ganancia;
                mejorAtributo = atributo;
            }
        }

        return mejorAtributo;
    }

    private double calcularEntropia(List<Ejemplo> ejemplos) {
        Map<String, Integer> conteos = new HashMap<>();

        for (Ejemplo ejemplo : ejemplos) {
            String clasificacion = ejemplo.getClasificacion();
            conteos.put(clasificacion, conteos.getOrDefault(clasificacion, 0) + 1);
        }

        double entropia = 0.0;
        int total = ejemplos.size();

        for (int conteo : conteos.values()) {
            if (conteo > 0) {
                double probabilidad = (double) conteo / total;
                entropia -= probabilidad * Math.log(probabilidad) / Math.log(2);
            }
        }

        return entropia;
    }

    private double calcularGananciaInformacion(List<Ejemplo> ejemplos, String atributo, double entropiaTotal) {
        Set<String> valores = obtenerValoresAtributo(ejemplos, atributo);
        double entropiaPonderada = 0.0;

        for (String valor : valores) {
            List<Ejemplo> subEjemplos = filtrarEjemplosPorValor(ejemplos, atributo, valor);
            double peso = (double) subEjemplos.size() / ejemplos.size();
            entropiaPonderada += peso * calcularEntropia(subEjemplos);
        }

        return entropiaTotal - entropiaPonderada;
    }

    private Set<String> obtenerValoresAtributo(List<Ejemplo> ejemplos, String atributo) {
        Set<String> valores = new HashSet<>();
        for (Ejemplo ejemplo : ejemplos) {
            valores.add(ejemplo.getAtributo(atributo));
        }
        return valores;
    }

    private List<Ejemplo> filtrarEjemplosPorValor(List<Ejemplo> ejemplos, String atributo, String valor) {
        List<Ejemplo> filtrados = new ArrayList<>();
        for (Ejemplo ejemplo : ejemplos) {
            if (valor.equals(ejemplo.getAtributo(atributo))) {
                filtrados.add(ejemplo);
            }
        }
        return filtrados;
    }

    private String obtenerClasificacionMasFrecuente(List<Ejemplo> ejemplos) {
        Map<String, Integer> conteos = new HashMap<>();

        for (Ejemplo ejemplo : ejemplos) {
            String clasificacion = ejemplo.getClasificacion();
            conteos.put(clasificacion, conteos.getOrDefault(clasificacion, 0) + 1);
        }

        String masFrecuente = null;
        int maxConteo = 0;

        for (Map.Entry<String, Integer> entry : conteos.entrySet()) {
            if (entry.getValue() > maxConteo) {
                maxConteo = entry.getValue();
                masFrecuente = entry.getKey();
            }
        }

        return masFrecuente;
    }

    public void dibujarArbol() {
        if (canvas != null && raiz != null) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setFont(new Font("Arial", 10));

            // Calcular posición inicial
            double x = canvas.getWidth() / 2;
            double y = 50;

            dibujarNodo(raiz, x, y, canvas.getWidth() / 4, gc, 0);
            mostrarInformacionArbol(gc);
        }
    }

    private void dibujarNodo(NodoID3 nodo, double x, double y, double offsetX, GraphicsContext gc, int nivel) {
        if (nodo == null) return;

        // Dibujar conexiones a hijos
        double childY = y + verticalSeparation;
        int numHijos = nodo.getHijos().size();

        if (numHijos > 0) {
            double startX = x - (numHijos - 1) * horizontalSeparation / 2;
            int i = 0;

            for (Map.Entry<String, NodoID3> entry : nodo.getHijos().entrySet()) {
                double childX = startX + i * horizontalSeparation;

                // Dibujar línea
                gc.setStroke(Color.BLACK);
                gc.strokeLine(x, y + nodeHeight/2, childX, childY - nodeHeight/2);

                // Dibujar etiqueta de la arista
                gc.setFill(Color.BLUE);
                gc.fillText(entry.getKey(), (x + childX) / 2 - 10, (y + childY) / 2);

                // Dibujar hijo recursivamente
                dibujarNodo(entry.getValue(), childX, childY, offsetX / 2, gc, nivel + 1);
                i++;
            }
        }

        // Dibujar el nodo
        gc.setFill(nodo.esHoja() ? Color.LIGHTGREEN : Color.LIGHTBLUE);
        gc.fillRoundRect(x - nodeWidth/2, y - nodeHeight/2, nodeWidth, nodeHeight, 10, 10);
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(x - nodeWidth/2, y - nodeHeight/2, nodeWidth, nodeHeight, 10, 10);

        // Dibujar texto del nodo
        gc.setFill(Color.BLACK);
        String texto = nodo.esHoja() ? nodo.getClasificacion() : nodo.getAtributo();
        gc.fillText(texto, x - texto.length() * 3, y + 3);
    }

    private void mostrarInformacionArbol(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 12));

        int altura = calcularAltura(raiz);
        int numNodos = contarNodos(raiz);
        int numHojas = contarHojas(raiz);

        gc.fillText("Altura del árbol: " + altura, 20, 20);
        gc.fillText("Número de nodos: " + numNodos, 20, 40);
        gc.fillText("Número de hojas: " + numHojas, 20, 60);
        gc.fillText("Algoritmo: ID3 (Árbol de Decisión)", 20, 80);
    }

    private int calcularAltura(NodoID3 nodo) {
        if (nodo == null || nodo.esHoja()) {
            return 1;
        }

        int maxAltura = 0;
        for (NodoID3 hijo : nodo.getHijos().values()) {
            maxAltura = Math.max(maxAltura, calcularAltura(hijo));
        }

        return maxAltura + 1;
    }

    private int contarNodos(NodoID3 nodo) {
        if (nodo == null) return 0;

        int contador = 1;
        for (NodoID3 hijo : nodo.getHijos().values()) {
            contador += contarNodos(hijo);
        }

        return contador;
    }

    private int contarHojas(NodoID3 nodo) {
        if (nodo == null) return 0;
        if (nodo.esHoja()) return 1;

        int contador = 0;
        for (NodoID3 hijo : nodo.getHijos().values()) {
            contador += contarHojas(hijo);
        }

        return contador;
    }

    public String clasificar(Map<String, String> ejemplo) {
        return clasificarRecursivo(raiz, ejemplo);
    }

    private String clasificarRecursivo(NodoID3 nodo, Map<String, String> ejemplo) {
        if (nodo == null) return "Desconocido";

        if (nodo.esHoja()) {
            return nodo.getClasificacion();
        }

        String valorAtributo = ejemplo.get(nodo.getAtributo());
        NodoID3 hijo = nodo.getHijos().get(valorAtributo);

        if (hijo == null) {
            return "Desconocido";
        }

        return clasificarRecursivo(hijo, ejemplo);
    }

    public List<String> obtenerRecorridoPreOrden() {
        List<String> resultado = new ArrayList<>();
        recorridoPreOrden(raiz, resultado);
        return resultado;
    }

    private void recorridoPreOrden(NodoID3 nodo, List<String> resultado) {
        if (nodo != null) {
            resultado.add(nodo.esHoja() ? nodo.getClasificacion() : nodo.getAtributo());
            for (NodoID3 hijo : nodo.getHijos().values()) {
                recorridoPreOrden(hijo, resultado);
            }
        }
    }

    public NodoID3 getRaiz() {
        return raiz;
    }
}
