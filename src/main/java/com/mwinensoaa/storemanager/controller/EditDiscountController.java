package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Discount;
import com.mwinensoaa.storemanager.repositories.DiscountRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class EditDiscountController implements Initializable {

    @FXML private Pane pane;
    @FXML private Button btnSave;
    @FXML private ImageView imgLogo;
    @FXML private TextField tfProductTitle;
    @FXML private TextField tfDiscount;
    @FXML private FontAwesomeIconView lbClose;
    @FXML private Label lbNewDiscount;
    @FXML private Label lbProduct;
    @FXML private Label lbDiscount;
          private Discount currentDiscount;

        private String dialogTitle;
        private String errorHeader;
        private String successHeader;
        private ResourceBundle bundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        lbClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        GeneralUtils.restrictTextFieldToNumbers(tfDiscount);
        tfProductTitle.setDisable(true);
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
         btnSave.setOnAction(event -> updateDiscount());
    }


    public void initL18N(ResourceBundle bundle){
        if(bundle != null){
            this.bundle = bundle;
            dialogTitle = bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
            lbDiscount.setText(bundle.getString("lbDiscountRate"));
            lbProduct.setText(bundle.getString("lbProduct"));
            lbNewDiscount.setText(bundle.getString("lbDiscount"));
            btnSave.setText(bundle.getString("txtSave"));
        }
    }

    private void updateDiscount(){
        if(isValidForm()){
            double discount = Double.parseDouble(tfDiscount.getText().trim());
            currentDiscount.setDiscount(discount);
            DiscountRepo.getInstance().createDiscount(currentDiscount);
            GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_discount_added"));
        }
    }


    public void setupEdit(Discount discount, ResourceBundle bundle){
        currentDiscount = discount;
        tfProductTitle.setText(discount.getProductTitle());
        tfDiscount.setText(String.valueOf(discount.getDiscount()));
        tfProductTitle.setDisable(true);
        initL18N(bundle);
    }


    private boolean isValidForm(){
        if(tfDiscount.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_discount_percent"));
            return false;
        }
        return true;
    }



}
