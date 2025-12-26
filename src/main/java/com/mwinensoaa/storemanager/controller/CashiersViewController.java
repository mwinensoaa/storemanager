package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Cashier;
import com.mwinensoaa.storemanager.entities.CashierType;
import com.mwinensoaa.storemanager.repositories.CashierRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class CashiersViewController implements Initializable {

    @FXML private Pane pane;
    @FXML private Button btnRemoveCashier;
    @FXML private Button btnEditCashier;
    @FXML private ImageView imgLogo;
    @FXML private ListView<String> listView;
    @FXML private TextField tfSearch;
    @FXML private FontAwesomeIconView lbClose;
    @FXML private Label lbCashierList;
          private ResourceBundle bundle;

    private final Stage editCashiersInfoStage = new Stage();
    private String dialogTitle;
    private String errorHeader;
    private String successHeader;
    private int currentThemeCode;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        lbClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        btnRemoveCashier.setOnAction(event -> removeCashier());
        btnEditCashier.setOnAction(event -> editCashierInfo());
        List<Cashier> cashiers = CashierRepo.getInstance().getCashiers();
        if(!cashiers.isEmpty()){
            for(Cashier cashier : cashiers){
                listView.getItems().add(cashier.getCashierName()+ " ("+cashier.getCashierId()+")");
            }
        }
        enableFiltering();
    }



    public void verifyPrivileges(Cashier cashier, ResourceBundle bundle, int themeCode){
          this.currentThemeCode = themeCode;
        if(cashier != null && cashier.getCashierType().equals(CashierType.Cashier)){
            btnRemoveCashier.setDisable(true);
            //btnEditCashier.setDisable(true);
        }
        if(bundle != null){
            this.bundle = bundle;
            dialogTitle = bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
            lbCashierList.setText(bundle.getString("lbCashierList"));
        }
    }

    private void enableFiltering() {
        ObservableList<String> data = listView.getItems();
        FilteredList<String> filteredList = new FilteredList<>(data, e ->true);
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate((Predicate<? super String>) staff ->{
            if (newValue == null || newValue.isEmpty()){
                return true;
            }
            String lowerCase = newValue.toLowerCase();
            if (tfSearch.getText().toLowerCase().contains(lowerCase)){
                filteredList.setPredicate(s -> s.toLowerCase().contains(lowerCase));
            }
            return false;
        }));
        listView.setItems(filteredList);
    }


    private void editCashierInfo() {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            new Thread(()->{
                String cashierId = selectedItem.split("\\(")[1].replace("(","").replace(")","");
                try {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/edit_cashier_screen.fxml")));
                    Parent parent  = loader.load();
                    EditCashierController controller = loader.getController();
                    Cashier cashier = CashierRepo.getInstance().getCashier(cashierId);
                    controller.setupCashierInfo(cashier, bundle);
                    String css = GeneralUtils.getCssFile("cashier_screen.css",currentThemeCode);
                    Platform.runLater(()->GeneralUtils.loadScreen(editCashiersInfoStage, parent, css));
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
            }).start();
        }else {
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_select_cashier"));
        }
    }



    private void removeCashier() {
       String selectedItem = listView.getSelectionModel().getSelectedItem();
       if(selectedItem != null){
           String id = selectedItem.split("\\(")[1].replace("(","").replace(")","");
           showConfirmationDialog(id);
           deletePhoto(id);
           listView.getItems().remove(selectedItem);
       }else{
           GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_select_cashier"));
       }

    }

  private void showConfirmationDialog(String id){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle(bundle.getString("txt_confirmation"));
      alert.setContentText(bundle.getString("txt_sure_delete_cashier"));
      alert.initStyle(StageStyle.UNDECORATED);
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && result.get() == ButtonType.OK){
          CashierRepo.getInstance().deleteCashier(id);
          GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_cashier_deleted"));
      }
  }



  private void deletePhoto(String id){
        for(File file : Objects.requireNonNull(GeneralUtils.getCashiersImagesDirectory().listFiles())){
            if(file.getName().startsWith(id)){
                file.delete();
            }
        }
  }





}
