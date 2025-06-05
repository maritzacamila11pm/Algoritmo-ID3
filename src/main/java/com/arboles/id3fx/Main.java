package com.arboles.id3fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

import java.util.*;

public class Main extends Application {

    private TableView<Ejemplo> tableViewDatos;

    @Override
    public void start(Stage primaryStage) {
        int canvasWidth = 800;
        int canvasHeight = 600;
        double nodeWidth = 80;
        double nodeHeight = 40;
        double verticalSeparation = 100;
        double horizontalSeparation = 120;

        ArbolID3Visual arbolID3 = new ArbolID3Visual(nodeWidth, nodeHeight, verticalSeparation, horizontalSeparation);
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        arbolID3.setCanvas(canvas);

        // Datos ejemplo
        List<Ejemplo> ejemplos = crearDatosEjemplo();
        Set<String> atributos = new HashSet<>(Arrays.asList("InterésPrincipal", "PreferenciaTrabajo", "HabilidadDestacada", "NivelEstudios"));
        String atributoClase = "Carrera";

        arbolID3.setDatos(ejemplos, atributos, atributoClase);

        // Label de bienvenida arriba
        Label labelBienvenida = new Label("Bienvenido al Recomendador de Carreras");
        labelBienvenida.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10;");

        Label labelInformacion = new Label("MARITZA CAMILA PONGO MAMANI");

        // TableView para mostrar datos (sin cuadro y con color)
        tableViewDatos = new TableView<>();
        tableViewDatos.setPrefWidth(450);
        tableViewDatos.setPrefHeight(500);

        // Columnas
        TableColumn<Ejemplo, String> colInteres = new TableColumn<>("Interés Principal");
        colInteres.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAtributos().get("InterésPrincipal")));
        colInteres.setPrefWidth(90);

        TableColumn<Ejemplo, String> colTrabajo = new TableColumn<>("Preferencia Trabajo");
        colTrabajo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAtributos().get("PreferenciaTrabajo")));
        colTrabajo.setPrefWidth(90);

        TableColumn<Ejemplo, String> colHabilidad = new TableColumn<>("Habilidad Destacada");
        colHabilidad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAtributos().get("HabilidadDestacada")));
        colHabilidad.setPrefWidth(90);

        TableColumn<Ejemplo, String> colNivel = new TableColumn<>("Nivel Estudios");
        colNivel.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAtributos().get("NivelEstudios")));
        colNivel.setPrefWidth(70);

        TableColumn<Ejemplo, String> colCarrera = new TableColumn<>("Carrera");
        colCarrera.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClasificacion()));
        colCarrera.setPrefWidth(100);

        tableViewDatos.getColumns().addAll(colInteres, colTrabajo, colHabilidad, colNivel, colCarrera);

        // Estilo tabla con colores
        tableViewDatos.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ffffff, #d0e8ff);" +  // Fondo azul claro degradado
                        "-fx-table-cell-border-color: transparent;" +
                        "-fx-border-color: transparent;"
        );

        // Botón Mostrar Datos
        Button btnMostrarCuadro = new Button("Mostrar Datos");
        btnMostrarCuadro.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold;");
        btnMostrarCuadro.setOnAction(e -> {
            tableViewDatos.getItems().setAll(ejemplos);
        });

        Button btnConstruir = new Button("Construir Árbol");
        btnConstruir.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnConstruir.setOnAction(e -> {
            arbolID3.construirArbol();
            arbolID3.dibujarArbol();
            labelInformacion.setText("Árbol construido: Sugerencias de carrera basadas en tus preferencias.");
        });

        Button btnSalir = new Button("Salir");
        btnSalir.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSalir.setOnAction(e -> Platform.exit());

        HBox botones = new HBox(15, btnConstruir, btnMostrarCuadro, btnSalir);
        botones.setAlignment(Pos.CENTER);
        botones.setPadding(new Insets(10));

        VBox infoBox = new VBox(10, labelInformacion);
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-background-color: #f0f0f0;");

        // Layout principal
        HBox centro = new HBox(10, canvas, tableViewDatos);
        centro.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(new VBox(labelBienvenida, botones));
        root.setCenter(centro);
        root.setBottom(infoBox);

        Scene scene = new Scene(root, canvasWidth + 480, canvasHeight + 130);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Árbol ID3 - Recomendador de Carreras Profesionales");
        primaryStage.setTitle("Algoritmo ID3 - Maritza Camila Pongo Mamani");

        primaryStage.show();
    }

    private List<Ejemplo> crearDatosEjemplo() {
        List<Ejemplo> ejemplos = new ArrayList<>();

        ejemplos.add(crearEjemplo("Tecnología", "Oficina", "Cálculo", "Alto", "Ingeniería"));
        ejemplos.add(crearEjemplo("Salud", "Laboratorio", "Precisión", "Alto", "Medicina"));
        ejemplos.add(crearEjemplo("Arte", "Creativo", "Creatividad", "Medio", "Diseño Gráfico"));
        ejemplos.add(crearEjemplo("Negocios", "Oficina", "Comunicación", "Alto", "Administración"));
        ejemplos.add(crearEjemplo("Salud", "Campo", "Comunicación", "Medio", "Psicología"));
        ejemplos.add(crearEjemplo("Tecnología", "Campo", "Cálculo", "Medio", "Ingeniería"));
        ejemplos.add(crearEjemplo("Arte", "Campo", "Creatividad", "Básico", "Diseño Gráfico"));
        ejemplos.add(crearEjemplo("Negocios", "Campo", "Comunicación", "Medio", "Agronomía"));
        ejemplos.add(crearEjemplo("Salud", "Laboratorio", "Precisión", "Medio", "Biotecnología"));
        ejemplos.add(crearEjemplo("Tecnología", "Oficina", "Cálculo", "Medio", "Sistemas"));

        return ejemplos;
    }

    private Ejemplo crearEjemplo(String interes, String trabajo, String habilidad, String nivel, String carrera) {
        Map<String, String> atributos = new HashMap<>();
        atributos.put("InterésPrincipal", interes);
        atributos.put("PreferenciaTrabajo", trabajo);
        atributos.put("HabilidadDestacada", habilidad);
        atributos.put("NivelEstudios", nivel);

        return new Ejemplo(atributos, carrera);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
