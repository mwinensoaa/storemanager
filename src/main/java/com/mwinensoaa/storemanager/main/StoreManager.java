package com.mwinensoaa.storemanager.main;

import com.mwinensoaa.storemanager.controller.LoginScreenController;
import com.mwinensoaa.storemanager.entities.Settings;
import com.mwinensoaa.storemanager.repositories.SettingsRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
//import nickyb.sqleonardo.environment.Application;

import java.util.Locale;
import java.util.Objects;

import static javafx.scene.paint.Color.TRANSPARENT;



public class StoreManager extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    Parent root;
    double xOffset, yOffset;

    @Override
    public void start(Stage primaryStage) {
        Settings settings = SettingsRepo.getInstance().getDefaultLanguage();
        try {
            root = FXMLLoader. load(Objects.requireNonNull(getClass().getResource("/fxml/login_screen.fxml")));
            //root = FXMLLoader. load(Objects.requireNonNull(getClass().getResource("/fxml/dashboard_screen.fxml")));
            Scene scene = new Scene(root);
            String css = GeneralUtils.getCssFile("login_screen.css",settings.getTheme());
            //String css = GeneralUtils.getCssFile("dashboard_screen.css",settings.getTheme());
            scene.getStylesheets().add(css);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();
            scene.setFill(TRANSPARENT);
            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(LoginScreenController.class.getResource("/images/logo4.png")).toExternalForm()))));
    }
}
