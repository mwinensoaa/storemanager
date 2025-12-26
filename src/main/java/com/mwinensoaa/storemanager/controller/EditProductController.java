package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Product;
import com.mwinensoaa.storemanager.repositories.ProductRepo;
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
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


public class EditProductController implements Initializable {

    @FXML private Pane pane;
    @FXML private FontAwesomeIconView btnClose;
    @FXML private Button btnFileChooser;
    @FXML private Button btnAddProduct;
    @FXML private DatePicker dpDateInStock;
    @FXML private TextField tfUnitPrice;
    @FXML private TextField tfPackSize;
    @FXML private TextField tfProductName;
    @FXML private TextField tfProductCode;
    @FXML private TextField tfPackPrice;
    @FXML private TextField tfQuantity;
    @FXML private ImageView imgProduct;

    @FXML private Label lbProductId;
    @FXML private Label  lbProductName;
    @FXML private Label  lbDateInStock;
    @FXML private Label  lbUnitPrice;
    @FXML private Label  lbPackSize;
    @FXML private Label  lbPackPrice;
    @FXML private Label  lbQuantity;
    @FXML private Label  lbNewProduct;

            private String dialogTitle;
            private String errorHeader;
            private String successHeader;
            private ResourceBundle bundle;

          private File file;
    ProductRepo productRepo = ProductRepo.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        btnClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        dpDateInStock.setEditable(false);
        tfProductCode.setDisable(true);
        GeneralUtils.initDateFormat(dpDateInStock);
        GeneralUtils.restrictTextFieldToNumbers(tfPackPrice);
        GeneralUtils.restrictTextFieldToNumbers(tfUnitPrice);
        GeneralUtils.restrictTextFieldToNumbers(tfQuantity);
        GeneralUtils.restrictTextFieldToNumbers(tfPackSize);
        initButtonClicks();
    }



    public void initL18N(ResourceBundle bundle){
        this.bundle = bundle;
        dialogTitle = bundle.getString("dialog_title");
        errorHeader = bundle.getString("txt_dialog_error_header");
        successHeader = bundle.getString("txt_dialog_success_header");
        lbProductId.setText(bundle.getString("lbProductId"));
        lbProductName.setText(bundle.getString("lbProductName"));
        lbDateInStock.setText(bundle.getString("lbDateInStock"));
        lbUnitPrice.setText(bundle.getString("lbUnitPrice"));
        lbPackSize.setText(bundle.getString("lbPackSize"));
        lbPackPrice.setText(bundle.getString("lbPackPrice"));
        lbQuantity.setText(bundle.getString("lbQuantity"));
        lbNewProduct.setText(bundle.getString("txtEdit"));
        btnAddProduct.setText(bundle.getString("txtSave"));
        btnFileChooser.setText(bundle.getString("txtChooseImage"));
    }


    public void setProductInfo(Product product, ResourceBundle bundle) {
         if(bundle  != null ){
            initL18N(bundle);
         }
         if(product.getProductId() != null){
             tfProductCode.setText(product.getProductId());
         }
         if(product.getProductName() != null){
             tfProductName.setText(product.getProductName());
         }
         if(product.getDatePlacedInStock() != null){
             dpDateInStock.setValue(GeneralUtils.formatDate(product.getDatePlacedInStock()));
         }
        tfPackPrice.setText(String.valueOf(product.getPackPrice()));
        tfUnitPrice.setText(String.valueOf(product.getUnitPrice()));
        tfPackSize.setText(String.valueOf(product.getPackSize()));
        tfQuantity.setText(String.valueOf(product.getQuantityInStock()));
        tfProductCode.setDisable(true);
        dpDateInStock.setEditable(false);
        File file = getProductImage(product.getProductName());
        if(file != null){
            btnFileChooser.setText(file.getAbsolutePath());
            try {
                imgProduct.setImage(new Image(file.toURI().toURL().toExternalForm()));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private File getProductImage(String productName){
        for(File file : Objects.requireNonNull(GeneralUtils.getProductsImagesDirectory().listFiles())){
             if(file.getName().startsWith(productName)){
                 return file;
             }
        }
        return null;
    }



    private void initButtonClicks(){
        btnFileChooser.setOnAction(event -> configureLogoFileChooser(new FileChooser(), (Stage)pane.getScene().getWindow()));
        btnAddProduct.setOnAction(event -> saveProduct());
    }


    private void saveProduct() {
        if(isValidForm()) {
            String productCode = tfProductCode.getText().trim();
            String productName = tfProductName.getText().trim();
            int productPackSize = Integer.parseInt(tfPackSize.getText().trim());
            double productPackPrice = Double.parseDouble(tfPackPrice.getText().trim());
            double productUnitPrice = Double.parseDouble(tfUnitPrice.getText().trim());
            String datePlaceInStock = GeneralUtils.getPrefDateFormat(dpDateInStock);
            int quantity = Integer.parseInt(tfQuantity.getText().trim());
            Product product = new Product();
            product.setProductName(productName);
            product.setProductId(productCode);
            product.setPackSize(productPackSize);
            product.setUnitPrice(productUnitPrice);
            product.setPackPrice(productPackPrice);
            product.setQuantityInStock(quantity);
            product.setDatePlacedInStock(datePlaceInStock);
            productRepo.createProduct(product);
            if (file != null) {
                renameFile(file, productName);
            }
            file = null;
            GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_product_saved"));
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
                imgProduct.setImage(new Image(file.toURI().toURL().toExternalForm()));
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
        File newFile = new File(GeneralUtils.getProductsImagesDirectory().getPath(), newFileName + fileExtension);
        Path source = Paths.get(oldFile.getAbsolutePath());
        Path destination = Paths.get(newFile.getAbsolutePath());
        try {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void deleteExistingFile(String fileName){
        File[] files = GeneralUtils.getProductsImagesDirectory().listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().startsWith(fileName)) {
                boolean deleted  = file.delete();
            }
        }
    }


    private boolean isValidForm(){
        if(tfProductCode.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_product_code"));
            return false;
        }else if(tfPackPrice.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_pack_price"));
            return false;
        }else if(tfQuantity.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_product_quantity"));
            return false;
        }else if(tfUnitPrice.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_unit_price"));
            return false;
        }else if(tfPackSize.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_pack_size"));
            return false;
        }else if(productRepo.productNameExist(tfProductName.getText().trim())){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_product_name_exist"));
            return false;
        }
        return true;
    }




}
