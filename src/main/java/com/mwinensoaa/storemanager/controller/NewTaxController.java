package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Tax;
import com.mwinensoaa.storemanager.repositories.TaxRepo;
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


public class NewTaxController implements Initializable {

    @FXML private Pane pane;
    @FXML private Button btnAddTax;
    @FXML private ImageView imgLogo;
    @FXML private TextField tfTaxName;
    @FXML private TextField tfTaxPercentage;
    @FXML private FontAwesomeIconView lbClose;
    @FXML private Label lbTax;
    @FXML private Label lbTaxRate;
    @FXML private Label lbTaxTitle;

    private String dialogTitle;
    private String errorHeader;
    private String successHeader;
    private ResourceBundle bundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        GeneralUtils.restrictTextFieldToNumbers(tfTaxPercentage);
        lbClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
       btnAddTax.setOnAction(event -> saveTax());
    }


    public void initL18N(ResourceBundle bundle){
        if(bundle != null){
            this.bundle = bundle;
            dialogTitle = bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
           lbTax.setText(bundle.getString("lbNewTax"));
           lbTaxTitle.setText(bundle.getString("lbTaxTitle"));
           lbTaxRate.setText(bundle.getString("lbTaxRate"));
           btnAddTax.setText(bundle.getString("txtSave"));

        }
    }


    private void saveTax(){
        if(isValidForm()){
            String taxTitle = GeneralUtils.capitalize(tfTaxName.getText().trim());
            double taxPercentage = Double.parseDouble(tfTaxPercentage.getText().trim());
            Tax tax = new Tax();
            tax.setTaxTitle(taxTitle);
            tax.setPercentage(taxPercentage);
            TaxRepo.getInstance().createTax(tax);
            GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_tax_updated"));
        }
    }


    private boolean isValidForm(){
        if(tfTaxName.getText().isEmpty()){
            GeneralUtils.showSuccessAlert(dialogTitle,errorHeader,bundle.getString("error_tax_title"));
            return false;
        }else if(tfTaxPercentage.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_tax_rate"));
            return false;
        }else if(TaxRepo.getInstance().taxTitleExist(tfTaxName.getText().trim())){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_tax_exists"));
            return false;
        }
        return true;
    }




}
