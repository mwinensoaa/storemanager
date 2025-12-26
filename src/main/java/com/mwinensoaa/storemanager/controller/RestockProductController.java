package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Product;
import com.mwinensoaa.storemanager.entities.Restock;
import com.mwinensoaa.storemanager.repositories.ProductRepo;
import com.mwinensoaa.storemanager.repositories.RestockRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


public class RestockProductController implements Initializable {

    @FXML private Pane pane;
    @FXML private Button btnRestock;
    @FXML private DatePicker dpDateStocked;
    @FXML private ComboBox<String> comboProductName;
    @FXML private ImageView imgLogo;
    @FXML private TextField tfQuantity;
    @FXML private FontAwesomeIconView lbClose;

    @FXML private Label lbProduct;
    @FXML private Label lbQuantity;
    @FXML private Label lbDateInStock;
    @FXML private Label lbRestock;

          private List<Product> products;
        private String dialogTitle;
        private String errorHeader;
        private String successHeader;
        private ResourceBundle bundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        lbClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        GeneralUtils.initDateFormat(dpDateStocked);
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        products = ProductRepo.getInstance().getAllProducts();
        iniEvents();
    }


    public void initL18N(ResourceBundle bundle){
        if(bundle != null){
            this.bundle = bundle;
            dialogTitle = bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
            lbProduct.setText(bundle.getString("lbProduct"));
            lbDateInStock.setText(bundle.getString("lbDateInStock"));
            lbQuantity.setText(bundle.getString("lbQuantity"));
            lbRestock.setText(bundle.getString("lbRestock"));
            btnRestock.setText(bundle.getString("lbRestock"));
            comboProductName.setPromptText(bundle.getString("txtSelect"));
        }
    }


    private void iniEvents(){
        products.forEach(it -> comboProductName.getItems().add(it.getProductName()));
        btnRestock.setOnAction(event -> restockProduct());
    }

     private void restockProduct(){
        if(isValidForm()){
            String dateRestocked = GeneralUtils.getPrefDateFormat(dpDateStocked);
            int quantity = Integer.parseInt(tfQuantity.getText().trim());
            String productName = comboProductName.getValue();
            Product product = getSelectedProduct(productName);
            Restock restock = new Restock();
            restock.setRestockDate(dateRestocked);
            restock.setProductName(productName);
            restock.setQuantity(quantity);
            restock.setProductId(product.getProductId());
            RestockRepo.getInstance().createRestock(restock);
            product.setQuantityInStock(product.getQuantityInStock()+quantity);
            ProductRepo.getInstance().createProduct(product);
            GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_restocked"));
        }
     }

     private Product getSelectedProduct(String productName){
        for(Product product : products){
            if(productName.equals(product.getProductName()))
                return product;
        }
        return new Product();
     }

     private boolean isValidForm(){
        if(comboProductName.getValue() == null){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_restock_product"));
            return false;
        }else if(tfQuantity.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_quantity_restocking"));
            return false;
        }else if(dpDateStocked.getValue() == null){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_select_date"));
            return false;
        }
        return true;
     }


}
