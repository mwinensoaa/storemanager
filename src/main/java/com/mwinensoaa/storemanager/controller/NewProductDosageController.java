package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Dosage;
import com.mwinensoaa.storemanager.entities.Product;
import com.mwinensoaa.storemanager.repositories.DosageRepo;
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
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class NewProductDosageController implements Initializable {

    @FXML private Pane pane;
    @FXML private Button btnSave;
    @FXML private ImageView imgLogo;
    @FXML private FontAwesomeIconView lbClose;

    @FXML private Label dialogTitle;
    @FXML private Label lbDosage;
    @FXML private TextField tfSearch;
    @FXML private TextArea taDosage;
    @FXML private ListView<String> listView;
          private String errorHeader;
          private String alertTitle;
          private String successHeader;
          private ResourceBundle bundle;





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        lbClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        List<Product> discounts = ProductRepo.getInstance().getAllProducts();
        discounts.forEach(it -> listView.getItems().add(it.getProductName()));
        btnSave.setDisable(true);
        taDosage.setDisable(true);
        btnSave.setOnAction(event -> createDosage());
        listView.setOnMouseClicked(it -> {
            taDosage.setDisable(false);
            btnSave.setDisable(false);
        });
    }


    public void initI18N(ResourceBundle bundle){
        if (bundle != null) {
            this.bundle = bundle;
            alertTitle =bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
            tfSearch.setPromptText(bundle.getString("tfSearch"));
            btnSave.setText(bundle.getString("txtSave"));
            dialogTitle.setText(bundle.getString("text_product_dosage"));
            lbDosage.setText(bundle.getString("txt_dosage"));
            taDosage.setText(bundle.getString("text_write_dosage"));
        }
    }

    private void createDosage(){
        DosageRepo dosageRepo = DosageRepo.getInstance();
         if(isValidForm()){
             String dosage = taDosage.getText().trim();
             String product = listView.getSelectionModel().getSelectedItem();
             Dosage dos = new Dosage();
             dos.setProductId(product);
             dos.setDosage(dosage);
             dosageRepo.createDosage(dos);
             GeneralUtils.showSuccessAlert(alertTitle,successHeader,bundle.getString("txt_dosage_added"));
         }

    }

    private boolean isValidForm(){
        if(taDosage.getText().isEmpty()){
            GeneralUtils.showErrorAlert(alertTitle,errorHeader,bundle.getString("txt_enter_dosage"));
            return false;
        }
        return true;
    }












}
