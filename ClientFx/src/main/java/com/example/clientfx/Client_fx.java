package com.example.clientfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Client_fx extends Application {
    private static Client_fxController controleur;
    @Override
    public void start(Stage stage) throws IOException {
           // FXMLLoader fxmlLoader = new FXMLLoader(Client_fx.class.getResource("Client_fx.fxml"));
            //Scene scene = new Scene(fxmlLoader.load(), 900, 600);
            stage.setTitle("Inscription UdeM");
            VBox root = new VBox();
            root.setSpacing(10);
            root.setPadding(new Insets(10));

            Scene scene = new Scene(root, 900, 600);
            stage.setScene(scene);
            stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}