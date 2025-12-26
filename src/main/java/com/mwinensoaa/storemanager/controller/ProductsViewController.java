package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Cashier;
import com.mwinensoaa.storemanager.entities.CashierType;
import com.mwinensoaa.storemanager.entities.Dosage;
import com.mwinensoaa.storemanager.entities.Product;
import com.mwinensoaa.storemanager.repositories.DosageRepo;
import com.mwinensoaa.storemanager.repositories.ProductRepo;
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


public class ProductsViewController implements Initializable {

    @FXML private Pane pane;
    @FXML private Button btnRemoveProduct;
    @FXML private Button btnEditProduct;
    @FXML private ImageView imgLogo;
    @FXML private ListView<String> listView;
    @FXML private TextField tfSearch;
    @FXML private FontAwesomeIconView lbClose;
    @FXML private Label lbProductList;
          private List<Product> products;
          private ResourceBundle bundle;
    private final Stage editProductStage = new Stage();
    private String dialogTitle;
    private String errorHeader;
    private String successHeader;
    private int currentThemeCode;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        lbClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        btnRemoveProduct.setOnAction(event -> removeProduct());
        btnEditProduct.setOnAction(event -> editProductInfo());
         products = ProductRepo.getInstance().getAllProducts();
        if(!products.isEmpty()){
            for(Product product : products){
                listView.getItems().add(product.getProductName());
            }
        }
        enableFiltering();
    }


    private void disableButtons(){
        btnEditProduct.setDisable(true);
        btnRemoveProduct.setDisable(true);
    }


    public void verifyPrivileges(Cashier cashier, ResourceBundle bundle, int themeCode){
        this.currentThemeCode = themeCode;
        if(cashier != null && cashier.getCashierType().equals(CashierType.Cashier)){
            //disableButtons();
        }
        if(bundle != null){
            this.bundle = bundle;
            dialogTitle = bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
           lbProductList.setText(bundle.getString("lbProductList"));
           tfSearch.setPromptText(bundle.getString("tfSearch"));
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

    private void editProductInfo() {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            new Thread(()->{
                try {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/edit_product_screen.fxml")));
                    Parent parent  = loader.load();
                    EditProductController controller = loader.getController();
                    String productId = getProductId(selectedItem);
                    Product product = ProductRepo.getInstance().getProduct(productId);
                    controller.setProductInfo(product,bundle);
                    String css = GeneralUtils.getCssFile("product_screen.css",currentThemeCode);
                    Platform.runLater(()->GeneralUtils.loadScreen(editProductStage, parent, css));
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
            }).start();
        }else {
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_select_product"));
        }
    }

    private String getProductId(String productName){
        for(Product product : products){
            if(productName.equals(product.getProductName())){
                return product.getProductId();
            }
        }
        return "";
    }

    private void removeProduct() {
       String selectedItem = listView.getSelectionModel().getSelectedItem();
       if(selectedItem != null){
           String productId = getProductId(selectedItem);
           showConfirmationDialog(productId);
           deletePhoto(productId);

           listView.getItems().remove(selectedItem);
           listView.refresh();
       }else{
           GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_select_product"));
       }

    }

  private void showConfirmationDialog(String productId){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle(bundle.getString("txt_confirmation"));
      alert.setContentText(bundle.getString("txt_sure_delete_product"));
      alert.initStyle(StageStyle.UNDECORATED);
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && result.get() == ButtonType.OK){
          ProductRepo.getInstance().deleteProduct(productId);
          DosageRepo.getInstance().deleteDosage(productId);
          GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_product_deleted"));
      }
  }


  private void deletePhoto(String id){
        for(File file : Objects.requireNonNull(GeneralUtils.getProductsImagesDirectory().listFiles())){
            if(file.getName().startsWith(id)){
                file.delete();
            }
        }
  }





}
