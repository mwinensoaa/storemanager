package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Cashier;
import com.mwinensoaa.storemanager.entities.CashierType;
import com.mwinensoaa.storemanager.entities.Discount;
import com.mwinensoaa.storemanager.repositories.DiscountRepo;
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

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class DiscountViewController implements Initializable {

    @FXML private Pane pane;
    @FXML private Button btnRemoveDiscount;
    @FXML private Button btnEditDiscount;
    @FXML private ImageView imgLogo;
    @FXML private ListView<String> listView;
    @FXML private TextField tfSearch;
    @FXML private FontAwesomeIconView lbClose;
    @FXML private Label lbDiscountList;

          private List<Discount> discounts;
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
        btnRemoveDiscount.setOnAction(event -> removeCashier());
        btnEditDiscount.setOnAction(event -> editCashierInfo());
         discounts = DiscountRepo.getInstance().getAllDiscounts();
        if(!discounts.isEmpty()){
            for(Discount discount : discounts){
                listView.getItems().add(discount.getProductTitle()+ " ("+discount.getDiscount()+"%)");
            }
        }
        enableFiltering();
    }



    public void verifyPrivileges(Cashier cashier,ResourceBundle bundle, int themeCode){
        this.currentThemeCode = themeCode;
        if(cashier != null && cashier.getCashierType().equals(CashierType.Cashier)){
            btnRemoveDiscount.setDisable(true);
            //btnEditDiscount.setDisable(true);
        }
        if(bundle != null){
            this.bundle = bundle;
            dialogTitle = bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
            lbDiscountList.setText(bundle.getString("lbDiscountList"));
            tfSearch.setPromptText(bundle.getString("tfSearch"));

            this.bundle = bundle;
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
        String selectedItem = listView.getSelectionModel().getSelectedItem().trim();
        new Thread(() -> {
            String selectedDiscount = selectedItem.split("\\(")[0].trim();
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/edit_discount_screen.fxml")));
                Parent parent = loader.load();
                EditDiscountController controller = loader.getController();
                Discount discount = getDiscount(selectedDiscount);
                controller.setupEdit(discount,bundle);
                String css = GeneralUtils.getCssFile("discount_screen.css",currentThemeCode);
                Platform.runLater(() -> GeneralUtils.loadScreen(editCashiersInfoStage, parent, css));
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }).start();
    }

   private Discount getDiscount(String selectedDiscount){
        for(Discount discount : discounts){
            if (selectedDiscount.equals(discount.getProductTitle())) {
                return discount;
            }
        }
        return new Discount();
   }


    private void removeCashier() {
       String selectedItem = listView.getSelectionModel().getSelectedItem();
       if(selectedItem != null){
           String discount = selectedItem.split("\\(")[0].trim();
           showConfirmationDialog(discount);
           listView.getItems().remove(selectedItem);
       }else{
           GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_select_discount"));
       }

    }

  private void showConfirmationDialog(String selectedDiscount){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle(bundle.getString("txt_confirmation"));
      alert.setContentText(bundle.getString("txt_sure_delete_product"));
      alert.initStyle(StageStyle.UNDECORATED);
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && result.get() == ButtonType.OK){
          DiscountRepo.getInstance().deleteDiscount(selectedDiscount);
          GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_discount_deleted"));
      }
  }









}
