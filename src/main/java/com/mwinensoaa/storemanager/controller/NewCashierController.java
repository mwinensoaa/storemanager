package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Cashier;
import com.mwinensoaa.storemanager.entities.CashierType;
import com.mwinensoaa.storemanager.entities.LastIdStore;
import com.mwinensoaa.storemanager.repositories.CashierRepo;
import com.mwinensoaa.storemanager.repositories.LastIdStoreRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;


public class NewCashierController implements Initializable {

    @FXML private Pane pane;
    @FXML private Button btnAddCashier;
    @FXML private FontAwesomeIconView btnClose;
    @FXML private Button btnFileChooser;
    @FXML private DatePicker dpDateEmployed;
    @FXML private DatePicker dpDob;
    @FXML private ComboBox<String> privilegesCombo;
    @FXML private TextField tfTelephone;
    @FXML private TextField tfCashierID;
    @FXML private PasswordField tfConfirmPassword;
    @FXML private PasswordField tfPassword;
    @FXML private TextField tfCashierName;
    @FXML private ImageView imgCashier;
    @FXML private Label lbCashierId;
    @FXML private Label lbFullName;
    @FXML private Label lbDateEmployed;
    @FXML private Label lbDateOfBirth;
    @FXML private Label lbTelephone;
    @FXML private Label lbPassword;
    @FXML private Label lbPrivileges;
    @FXML private Label lbNewCashier;
    @FXML private Label lbConfirmPass;
    @FXML private Button btnGenerate;

    private File file;
    private String dialogTitle;
    private String errorHeader;
    private String successHeader;
    private ResourceBundle bundle;
    private LastIdStore lastLastIdStore;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        tfCashierID.setDisable(true);
        btnClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        lastLastIdStore = LastIdStoreRepo.getInstance().getId("cashiers");
//        GeneralUtils.initDateFormat(dpDateEmployed);
//        GeneralUtils.initDateFormat(dpDob);
        initButtonClicks();
    }

    public void initL18N(ResourceBundle bundle) {
        if (bundle != null) {
            this.bundle = bundle;
            privilegesCombo.getItems().clear();
            String admin = bundle.getString("txtAdministrator");
            String cashier = bundle.getString("txtCashier");
            privilegesCombo.getItems().addAll(cashier,admin);
            dialogTitle = bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
            lbCashierId.setText(bundle.getString("lbCashierId"));
            lbFullName.setText(bundle.getString("lbFullName"));
            lbDateEmployed.setText(bundle.getString("lbDateEmployed"));
            lbDateOfBirth.setText(bundle.getString("lbDateOfBirth"));
            lbTelephone.setText(bundle.getString("lbTelephone"));
            lbPassword.setText(bundle.getString("lbPassword"));
            lbConfirmPass.setText(bundle.getString("lbConfirmPass"));
            lbPrivileges.setText(bundle.getString("lbPrivileges"));
            lbNewCashier.setText(bundle.getString("lbNewCashier"));
            btnAddCashier.setText(bundle.getString("txtSave"));
            btnFileChooser.setText(bundle.getString("txtChooseImage"));
            privilegesCombo.setPromptText(bundle.getString("txtSelect"));
            btnGenerate.setText(bundle.getString("btnGenerate"));
        }
    }

    private void initButtonClicks() {
        btnFileChooser.setOnAction(event -> configureLogoFileChooser(new FileChooser(), (Stage) pane.getScene().getWindow()));
        btnAddCashier.setOnAction(event -> createCashier());
        btnGenerate.setOnAction(event -> generateId());
    }

    private boolean isValidForm() {
        if (tfCashierID.getText().isEmpty()) {
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_cashier_id"));
            return false;
        }
        if (tfCashierName.getText().isEmpty()) {
            GeneralUtils.showErrorAlert(dialogTitle, errorHeader, bundle.getString("error_cashier_name"));
            return false;
        } else if (tfTelephone.getText().isEmpty()) {
            GeneralUtils.showErrorAlert(dialogTitle, errorHeader, bundle.getString("error_cashier_telephone"));
            return false;
        } else if (tfPassword.getText().isEmpty()) {
            GeneralUtils.showErrorAlert(dialogTitle, errorHeader, bundle.getString("error_cashier_password "));
            return false;
        } else if (tfConfirmPassword.getText().isEmpty()) {
            GeneralUtils.showErrorAlert(dialogTitle, errorHeader, bundle.getString("error_reenter_password"));
            return false;
        } else if (dpDateEmployed.getValue() == null) {
            GeneralUtils.showErrorAlert(dialogTitle, errorHeader, bundle.getString("error_employment_date"));
            return false;
        } else if (dpDob.getValue() == null) {
            GeneralUtils.showErrorAlert(dialogTitle, errorHeader, bundle.getString("error_date_of_birth"));
            return false;
        } else if (privilegesCombo.getValue() == null) {
            GeneralUtils.showErrorAlert(dialogTitle, errorHeader, bundle.getString("error_cashier_privilege"));
            return false;
        }
        return true;
    }



     private void createCashier(){
        if(isValidForm()){
            String cashierId = tfCashierID.getText().trim();
            String selectedType = privilegesCombo.getValue();
            String cashierName = GeneralUtils.capitalize(tfCashierName.getText().trim());
            String telephone  = tfTelephone.getText().trim();
            String password = tfPassword.getText().trim();
            String dateEmployed = GeneralUtils.getPrefDateFormat(dpDateEmployed);
            String dateOfBirth = GeneralUtils.getPrefDateFormat(dpDob);
            CashierType cashierType  = selectedType.equals(privilegesCombo.getItems().get(0)) ? CashierType.Cashier : CashierType.Administrator;
            Cashier cashier = new Cashier();
            cashier.setCashierId(cashierId);
            cashier.setCashierName(cashierName);
            cashier.setCashierTelephone(telephone);
            cashier.setCashierType(cashierType);
            cashier.setCashierDob(dateOfBirth);
            cashier.setCashierPassword(password);
            cashier.setCashierEmploymentDate(dateEmployed);
            CashierRepo.getInstance().createCashier(cashier);
            updateNextId(cashierId);
            if(file != null){

                renameFile(file, cashierId);
            }
            file = null;
            GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_cashier_saved"));
        }
     }

    private void updateNextId(String lastId){
        if(lastLastIdStore == null){
            LastIdStore lastIdStore = new LastIdStore("cashiers", lastId);
            LastIdStoreRepo.getInstance().updateLastId(lastIdStore);
        }else{
            lastLastIdStore.setMValue(lastId);
            LastIdStoreRepo.getInstance().updateLastId(lastLastIdStore);
        }
    }

    private  void configureLogoFileChooser(final FileChooser fileChooser, final Stage stage) {
        fileChooser.setTitle("Choose image");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home")+"/Desktop")
        );
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png","*.jpeg")
        );
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                imgCashier.setImage(new Image(file.toURI().toURL().toExternalForm()));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            btnFileChooser.setText(file.getAbsolutePath());
        }
    }


    public static void renameFile(File file, String newFileName) {
        File oldFile = new File(file.getAbsolutePath());
        String fileExtension = "";
        int lastDotIndex = oldFile.getName().lastIndexOf(".");
        if (lastDotIndex > 0) {
            fileExtension = oldFile.getName().substring(lastDotIndex);
        }
        deleteExistingFile(newFileName);
        File newFile = new File(GeneralUtils.getCashiersImagesDirectory().getPath(), newFileName + fileExtension);
        Path source = Paths.get(oldFile.getAbsolutePath());
        Path destination = Paths.get(newFile.getAbsolutePath());
        try {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteExistingFile(String fileName){
        File[] files = GeneralUtils.getCashiersImagesDirectory().listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().startsWith(fileName)) {
                boolean deleted  = file.delete();
            }
        }
    }


    private void generateId(){
        String nextProductId = getNextId(lastLastIdStore);
        tfCashierID.setText(nextProductId);
    }

    public static String getNextId(LastIdStore lastIdStore){
        String nextId;
        if(lastIdStore == null){
            nextId = "00001";
        }else{
            int newLastId = Integer.parseInt(lastIdStore.getMValue())+1;
            nextId = String.format("%05d",newLastId);
        }
        return nextId;
    }

}
