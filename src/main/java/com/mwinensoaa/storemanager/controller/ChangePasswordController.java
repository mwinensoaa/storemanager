package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Cashier;
import com.mwinensoaa.storemanager.repositories.CashierRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class ChangePasswordController implements Initializable {

    @FXML private Pane pane;
    @FXML private Button btnChangePass;
    @FXML private ImageView imgLogo;
    @FXML private PasswordField tfOldPass;
    @FXML private PasswordField tfNewPass;
    @FXML private PasswordField tfConfirmPass;
    @FXML private FontAwesomeIconView lbClose;
          private Cashier currentCashier;

    @FXML private Label lbConfirmPass;
    @FXML private Label lbNewPass;
    @FXML private Label lbCurrentPass;
    @FXML private Label lbChangePass;

    private String dialogTitle;
    private String errorHeader;
    private String successHeader;
    private ResourceBundle bundle;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        lbClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        btnChangePass.setOnAction(event -> updatePassword());
    }


    public void initI18N(ResourceBundle bundle){
        if(bundle != null){
            this.bundle = bundle;
            dialogTitle = bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
            lbConfirmPass.setText(bundle.getString("lbConfirmPass"));
            lbNewPass.setText(bundle.getString("lbNewPass"));
            lbCurrentPass.setText(bundle.getString("lbCurrentPass"));
            lbChangePass.setText(bundle.getString("lbChangePass"));
            btnChangePass.setText(bundle.getString("txtSave"));
        }
    }

    public void setupUpdate(Cashier cashier,ResourceBundle bundle){
        if(cashier != null){
            currentCashier = cashier;
        }
        if(bundle != null){
            initI18N(bundle);
        }
    }

    private boolean isValidForm(){
        if(tfNewPass.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_enter_password"));
            return false;
        }else if(tfConfirmPass.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_reenter_password"));
            return false;
        }else if(!tfNewPass.getText().trim().equals(tfConfirmPass.getText().trim())){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("txtPasswordMismatch"));
            return false;
        }else if(!tfOldPass.getText().trim().equals(currentCashier.getCashierPassword())){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("txtWrongOldPassword"));
            return false;
        }
        return true;
    }


    private void updatePassword(){
        if(isValidForm()){
            String password = tfNewPass.getText().trim();
            currentCashier.setCashierPassword(password);
            CashierRepo.getInstance().createCashier(currentCashier);
            GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_password_updated"));
        }
    }







}
