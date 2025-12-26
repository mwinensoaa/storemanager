package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Discount;
import com.mwinensoaa.storemanager.entities.Product;
import com.mwinensoaa.storemanager.repositories.DiscountRepo;
import com.mwinensoaa.storemanager.repositories.ProductRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class NewDiscountController implements Initializable {

    @FXML private Pane pane;
    @FXML private Button btnSave;
    @FXML private ImageView imgLogo;
    @FXML private MenuButton productsMenuButton;
    @FXML private RadioButton radioSpecificProduct;
    @FXML private RadioButton radioMinPurchase;
    @FXML private TextField tfTotalDiscountProduct;
    @FXML private TextField tfTotalDiscountMin;
    @FXML private TextField tfMinPurchase;
    @FXML private FontAwesomeIconView lbClose;

    @FXML private Label lbProduct;
    @FXML private Label lbDiscount;
    @FXML private Label lbDiscountRate;
    @FXML private Label lbMinPurchase;
    @FXML private Label lbDiscountRate2;
          private String dialogTitle;
          private String errorHeader;
          private String successHeader;
          private ResourceBundle bundle;





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        GeneralUtils.restrictTextFieldToNumbers(tfTotalDiscountMin);
        GeneralUtils.restrictTextFieldToNumbers(tfTotalDiscountProduct);
        GeneralUtils.restrictTextFieldToNumbers(tfMinPurchase);
        lbClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        productsMenuButton.getItems().clear();
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        List<Product> discounts = ProductRepo.getInstance().getAllProducts();
        discounts.forEach(this::setupProductMenuButtonItems);
        disableMinPurchaseFields();
        disableSpecificProductFields();
        initRadioButtonEvent();
        btnSave.setDisable(true);
        btnSave.setOnAction(event -> createDiscount());
    }


    public void initL18N(ResourceBundle bundle){
        if (bundle != null) {
            this.bundle = bundle;
            dialogTitle = bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
            lbProduct.setText(bundle.getString("lbProduct"));
            lbDiscountRate.setText(bundle.getString("lbDiscountRate"));
            lbDiscountRate2.setText(bundle.getString("lbDiscountRate"));
            lbMinPurchase.setText(bundle.getString("lbMinPurchase"));
            radioSpecificProduct.setText(bundle.getString("txtProductDiscount"));
            radioMinPurchase.setText(bundle.getString("txtMinPurchaseDiscount"));
            lbDiscount.setText(bundle.getString("lbDiscount"));
            btnSave.setText(bundle.getString("txtSave"));
            productsMenuButton.setText(bundle.getString("txtSelect"));
        }
    }

    private void createDiscount(){
        DiscountRepo discountRepo = DiscountRepo.getInstance();
        if(radioMinPurchase.isSelected() && isValidMinPurchaseDiscount()){
            String minAmount = bundle.getString("txt_min_purchase_amount")+" "+tfMinPurchase.getText().trim();
            if(discountRepo.isDiscountExists(minAmount)){
              //GeneralUtils.showErrorAlert("This discount already exist");
            }else {
                double discount = Double.parseDouble(tfTotalDiscountMin.getText().trim());
                discountRepo.createDiscount(new Discount(minAmount, discount));
                GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_discount_added"));
            }
        }else if(radioSpecificProduct.isSelected() && isValidSpecificProductDiscount()){
            double discount = Double.parseDouble(tfTotalDiscountProduct.getText().trim());
            getSelectedProduct().forEach(it ->{
               discountRepo.createDiscount(new Discount(it, discount));
            });
            GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_discount_added"));
        }
    }



    private boolean isValidMinPurchaseDiscount(){
        if(tfMinPurchase.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_minimum_purchase"));
            return false;
        }else if(tfTotalDiscountMin.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_discount_percent"));
            return false;
        }
        return true;
    }


    private boolean isValidSpecificProductDiscount(){
        if(getSelectedProduct().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_discount_product"));
            return false;
        }else if(tfTotalDiscountProduct.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_discount_percent"));
            return false;
        }
        return true;
    }


    private void initRadioButtonEvent(){
        radioMinPurchase.setOnAction(event -> {
            radioSpecificProduct.setSelected(false);
            disableSpecificProductFields();
             enableMinPurchaseFields();
            if(!productsMenuButton.getItems().isEmpty()){
                uncheckSubjectsBoxes();
            }
            btnSave.setDisable(false);
        });
        radioSpecificProduct.setOnAction(event -> {
            radioMinPurchase.setSelected(false);
            disableMinPurchaseFields();
            enableSpecificProductFields();
            btnSave.setDisable(false);
        });
    }


    private void setupProductMenuButtonItems(Product product){
        CheckBox checkBox = new CheckBox(product.getProductName());
        CustomMenuItem menuItem = new CustomMenuItem(checkBox);
        checkBox.setOnAction(event1 -> {
            if(checkBox.isSelected()){
                if(productsMenuButton.getText().equals(bundle.getString("txtSelect"))){
                    productsMenuButton.setText("");
                    productsMenuButton.setText(checkBox.getText());
                }else{
                    productsMenuButton.setText(productsMenuButton.getText() + ","+checkBox.getText());
                }
            }else{
                String text = productsMenuButton.getText();
                if(text.startsWith(checkBox.getText()+",")){
                    productsMenuButton.setText(text.replace((checkBox.getText()+","),""));
                }else if(text.equals(checkBox.getText())){
                    productsMenuButton.setText(text.replace((checkBox.getText()),bundle.getString("txtSelect")));
                }else{
                    productsMenuButton.setText(text.replace((","+checkBox.getText()),""));
                }
            }
        });
        menuItem.setHideOnClick(false);
        productsMenuButton.getItems().add(menuItem);
    }

    private void disableMinPurchaseFields(){
        tfMinPurchase.setText("");
        tfMinPurchase.setDisable(true);
        tfTotalDiscountMin.setDisable(true);
        tfTotalDiscountMin.setText("");
    }

    private void disableSpecificProductFields(){
        tfTotalDiscountProduct.setText("");
        tfTotalDiscountProduct.setDisable(true);
        productsMenuButton.setDisable(true);
    }

    private void enableMinPurchaseFields(){
        tfMinPurchase.setText("");
        tfMinPurchase.setDisable(false);
        tfTotalDiscountMin.setDisable(false);
        tfTotalDiscountMin.setText("");
    }

    private void enableSpecificProductFields(){
        tfTotalDiscountProduct.setText("");
        tfTotalDiscountProduct.setDisable(false);
        productsMenuButton.setDisable(false);
    }


    private void uncheckSubjectsBoxes() {
        for (MenuItem item : productsMenuButton.getItems()) {
            CustomMenuItem menuItem = (CustomMenuItem) item;
            CheckBox checkBox = (CheckBox) menuItem.getContent();
            if (checkBox.isSelected()) {
                checkBox.setSelected(false);
            }
        }
        productsMenuButton.setText(productsMenuButton.getText().replaceAll(productsMenuButton.getText(), bundle.getString("txtSelect")));
    }


    private ArrayList<String> getSelectedProduct() {
        ArrayList<String> subjects = new ArrayList<>();
        for (javafx.scene.control.MenuItem item : productsMenuButton.getItems()) {
            CustomMenuItem menuItem = (CustomMenuItem) item;
            CheckBox checkBox = (CheckBox) menuItem.getContent();
            if (checkBox.isSelected()) {
                subjects.add(checkBox.getText());
            }
        }
        return subjects;
    }



}
