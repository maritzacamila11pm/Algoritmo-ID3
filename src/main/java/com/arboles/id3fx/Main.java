package com.arboles.id3fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        int canvasWidth = 1200;
        int canvasHeight = 700;
        double nodeWidth = 80;
        double nodeHeight = 40;
        double verticalSeparation = 100;
        double horizontalSeparation = 120;

        ArbolID3Visual arbolID3 = new ArbolID3Visual(nodeWidth, nodeHeight, verticalSeparation, horizontalSeparation);

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        arbolID3.setCanvas(canvas);

        // Crear datos de ejemplo (jugar tenis)
        List<Ejemplo> ejemplos = crearDatosEjemplo();
        Set<String> atributos = new HashSet<>(Arrays.asList("Clima", "Temperatura", "Humedad", "Viento", "Jugar"));
        String atributoClase = "Jugar";

        arbolID3.setDatos(ejemplos, atributos, atributoClase);

        // Labels para mostrar información
        Label labelRecorrido = new Label();
        Label labelInformacion = new Label("Presiona 'Construir Árbol' para generar el árbol de decisión ID3");

        // Botón para construir el árbol
        Button btnConstruir = new Button("Construir Árbol");
        btnConstruir.setOnAction(e -> {
            arbolID3.construirArbol();
            arbolID3.dibujarArbol();
            labelRecorrido.setText("Recorrido PreOrden: " + arbolID3.obtenerRecorridoPreOrden());
            labelInformacion.setText("Árbol ID3 construido - Ejemplo: Decisión para jugar tenis");
        });

        // Botón para clasificar un ejemplo
        Button btnClasificar = new Button("Clasificar Ejemplo");
        btnClasificar.setOnAction(e -> {
            Map<String, String> ejemploTest = new HashMap<>();
            ejemploTest.put("Clima", "Soleado");
            ejemploTest.put("Temperatura", "Caliente");
            ejemploTest.put("Humedad", "Alta");
            ejemploTest.put("Viento", "Débil");

            String resultado = arbolID3.clasificar(ejemploTest);
            labelInformacion.setText("Clasificación para [Soleado, Caliente, Alta, Débil]: " + resultado);
        });

        HBox botones = new HBox(10, btnConstruir, btnClasificar);
        VBox infoBox = new VBox(5, labelInformacion, labelRecorrido);

        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setTop(botones);
        root.setBottom(infoBox);

        Scene scene = new Scene(root, canvasWidth, canvasHeight + 100);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Algoritmo ID3 - Árbol de Decisión Visual");
        primaryStage.show();
    }

    private List<Ejemplo> crearDatosEjemplo() {
        List<Ejemplo> ejemplos = new ArrayList<>();

        // Datos clásicos del problema "Jugar Tenis"
        ejemplos.add(crearEjemplo("Soleado", "Caliente", "Alta", "Débil", "No"));
        ejemplos.add(crearEjemplo("Soleado", "Caliente", "Alta", "Fuerte", "No"));
        ejemplos.add(crearEjemplo("Nublado", "Caliente", "Alta", "Débil", "Sí"));
        ejemplos.add(crearEjemplo("Lluvioso", "Templado", "Alta", "Débil", "Sí"));
        ejemplos.add(crearEjemplo("Lluvioso", "Frío", "Normal", "Débil", "Sí"));
        ejemplos.add(crearEjemplo("Lluvioso", "Frío", "Normal", "Fuerte", "No"));
        ejemplos.add(crearEjemplo("Nublado", "Frío", "Normal", "Fuerte", "Sí"));
        ejemplos.add(crearEjemplo("Soleado", "Templado", "Alta", "Débil", "No"));
        ejemplos.add(crearEjemplo("Soleado", "Frío", "Normal", "Débil", "Sí"));
        ejemplos.add(crearEjemplo("Lluvioso", "Templado", "Normal", "Débil", "Sí"));
        ejemplos.add(crearEjemplo("Soleado", "Templado", "Normal", "Fuerte", "Sí"));
        ejemplos.add(crearEjemplo("Nublado", "Templado", "Alta", "Fuerte", "Sí"));
        ejemplos.add(crearEjemplo("Nublado", "Caliente", "Normal", "Débil", "Sí"));
        ejemplos.add(crearEjemplo("Lluvioso", "Templado", "Alta", "Fuerte", "No"));

        return ejemplos;
    }

    private Ejemplo crearEjemplo(String clima, String temperatura, String humedad, String viento, String jugar) {
        Map<String, String> atributos = new HashMap<>();
        atributos.put("Clima", clima);
        atributos.put("Temperatura", temperatura);
        atributos.put("Humedad", humedad);
        atributos.put("Viento", viento);

        return new Ejemplo(atributos, jugar);
    }

    public static void main(String[] args) {
        launch(args);
    }
}