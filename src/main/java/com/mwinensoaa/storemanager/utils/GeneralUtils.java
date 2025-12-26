package com.mwinensoaa.storemanager.utils;

import com.mwinensoaa.storemanager.controller.LoginScreenController;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class GeneralUtils {
    private static double xOffset;
    private static double yOffset;
    private static int x = 0;
    private static int y = 0;

    public static void loadScreen(Stage primaryStage, Parent parent, String cssFile){
        if(primaryStage.getScene() == null){
            try{
                Scene scene = new Scene(parent);
                primaryStage.setScene(scene);
                scene.getStylesheets().add(cssFile);
            }catch (Exception ignore){}
            if(primaryStage.getStyle() == StageStyle.DECORATED) {
               primaryStage.initStyle(StageStyle.UNDECORATED);
            }
            primaryStage.setResizable(false);
            primaryStage.show();
        }else if(primaryStage.isIconified()){
            Toolkit.getDefaultToolkit().beep();
            primaryStage.setIconified(false);
        }else {
            Toolkit.getDefaultToolkit().beep();
            primaryStage.toFront();
        }
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(LoginScreenController.class.getResource("/images/logo4.png")).toExternalForm()))));
        primaryStage.setOnHiding(event -> primaryStage.setScene(null));
    }


    public static void initControlButtonsAndWindoDrag(Node node){
        node.setOnMousePressed(event -> {
            xOffset = node.getScene().getWindow().getX() - event.getScreenX();
            yOffset = node.getScene().getWindow().getY() - event.getScreenY();
        });
        node.setOnMouseDragged(event -> {
            node.getScene().getWindow().setX(event.getScreenX() + xOffset);
            node.getScene().getWindow().setY(event.getScreenY() + yOffset);
        });

    }

    public static void showErrorAlert(String title, String header, String message){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void showSuccessAlert(String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText(message);
        alert.showAndWait();
    }

//    public static void showSuccessAlert( String message){
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Results Dialog");
//        alert.setHeaderText("Success");
//        alert.initStyle(StageStyle.UNDECORATED);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//    public static void showErrorAlert( String message){
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Results Dialog");
//        alert.setHeaderText("Success");
//        alert.initStyle(StageStyle.UNDECORATED);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }


    public static File getReportFile(String reportName){
        //"app" + File.separator +
        return  new File(System.getProperty("user.dir") +File.separator + "reports"+ File.separator+ reportName);
        //return  new File(System.getProperty("user.dir")+File.separator + "app" +File.separator + "reports"+ File.separator+ reportName);
    }

    public static File getLogo(){
        //File.separator + "app" +

        return Objects.requireNonNull(new File(System.getProperty("user.dir")+ File.separator + "Images").listFiles())[0];
        //return Objects.requireNonNull(new File(System.getProperty("user.dir") +  File.separator +"app" + File.separator + "Images").listFiles())[0];
    }

    public static String getCssFile(String fileName,int themeCode){
         if(themeCode == 1){
             fileName = "dark_"+fileName;
         }else if(themeCode == 2){
             fileName = "material_"+fileName;
         }else if(themeCode == 3){
             fileName = "flat_"+fileName;
         }else if(themeCode == 0){
             fileName = "default_"+fileName;
         }
        //"app" + File.separator +
        return  new File(System.getProperty("user.dir") + File.separator + "css"+ File.separator + fileName).toURI().toString();
        //return  new File(System.getProperty("user.dir") +File.separator +"app" + File.separator + "css"+ File.separator + fileName).toURI().toString();
    }


    public static String getCssFile(String fileName){
        //"app" + File.separator +
        return  new File(System.getProperty("user.dir") + File.separator + "css"+ File.separator + fileName).toURI().toString();
        //return  new File(System.getProperty("user.dir") +File.separator +"app" + File.separator + "css"+ File.separator + fileName).toURI().toString();
    }



    public static File getOpenCVNativeLib(){
        //+ File.separator + "app"
        return new File(System.getProperty("user.dir")   + File.separator + "opencv");
        //return new File(System.getProperty("user.dir")  + File.separator +"app" + File.separator + "opencv");
    }

    public static LocalDate formatDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date, formatter);
    }

    public static String getPrefDateFormat(DatePicker date){
        LocalDate d = date.getValue();
        return (d.getDayOfMonth()<10 ? "0"+d.getDayOfMonth() : d.getDayOfMonth())+
                "-"+(d.getMonthValue()<10? "0"+ d.getMonthValue():d.getMonthValue())+"-"+d.getYear();
    }


    public static void initDateFormat(DatePicker date){

        date.setConverter(new StringConverter<LocalDate>() {
            final String pattern = "dd/MM/yyyy";
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            {
                date.setPromptText("");
            }
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }



    public static void restrictTextFieldToNumbers(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^-?\\d*(\\.\\d*)?$")) {
                textField.setText(oldValue);
            }
        });
    }


    public static File getImageDirectory(){
        //+ File.separator + "app"
        return new File(System.getProperty("user.dir")   + File.separator + "Images");
        //return new File(System.getProperty("user.dir")  + File.separator +"app" + File.separator + "Images");
    }
    public static File getCashiersImagesDirectory(){
        //+ File.separator + "app"
        return new File(System.getProperty("user.dir")   + File.separator + "CashierImages");
        //return new File(System.getProperty("user.dir")  + File.separator +"app" + File.separator + "CashierImages");
    }

    public static File getProductsImagesDirectory(){
        //+ File.separator + "app"
        return new File(System.getProperty("user.dir")   + File.separator + "ProductsImages");
        //return new File(System.getProperty("user.dir")  + File.separator +"app" + File.separator + "CashierImages");
    }


    public static String capitalize(String str){
        return Arrays.stream(str.split("\\s+"))
                .map(t -> t.substring(0, 1).toUpperCase() + t.substring(1))
                .collect(Collectors.joining(" "));
    }


    public static Locale getPrefLocale(int selectedIndex){
        if(selectedIndex == 1){
            return new Locale("es", "ES");
        }else if(selectedIndex == 2){
            return new Locale("fr","FR");
        }
        return new Locale("en","US");
    }





}
