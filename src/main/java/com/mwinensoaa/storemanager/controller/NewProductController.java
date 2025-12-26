
package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Product;
import com.mwinensoaa.storemanager.entities.LastIdStore;
import com.mwinensoaa.storemanager.repositories.LastIdStoreRepo;
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
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;


public class NewProductController implements Initializable {

    @FXML private Pane pane;
    @FXML private FontAwesomeIconView btnClose;
    @FXML private Button btnFileChooser;
    @FXML private Button btnAddProduct;
    @FXML private Button btnGenerate;
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

    private File file;
    ProductRepo productRepo = ProductRepo.getInstance();

    private LastIdStore lastLastIdStore;
    private String dialogTitle;
    private String errorHeader;
    private String successHeader;
    private ResourceBundle bundle;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        btnClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        lastLastIdStore = LastIdStoreRepo.getInstance().getId("products");
        tfProductCode.setDisable(true);
        dpDateInStock.setChronology(java.time.chrono.IsoChronology.INSTANCE);
        //initDateFormat(dpDateInStock);
         initButtonClicks();
        //GeneralUtils.initDateFormat(dpDateInStock);
//        GeneralUtils.restrictTextFieldToNumbers(tfPackPrice);
//        GeneralUtils.restrictTextFieldToNumbers(tfQuantity);
//        GeneralUtils.restrictTextFieldToNumbers(tfUnitPrice);
//        GeneralUtils.restrictTextFieldToNumbers(tfPackSize);
    }

    public static void initDateFormat(DatePicker date){
        date.setPromptText(date.getChronology().dateNow().toString());
        date.setConverter(new StringConverter<LocalDate>() {
            final String pattern = "dd/MM/yyyy";
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern,Locale.US);
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.US);

            {
                date.setPromptText("");
            }
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, formatter);
                } else {
                    return null;
                }
            }
        });

    }



    public void initI18N(ResourceBundle bundle){
        if(bundle != null){
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
            btnGenerate.setText(bundle.getString("btnGenerate"));
            lbNewProduct.setText(bundle.getString("lbNewProduct"));
            btnAddProduct.setText(bundle.getString("txtSave"));
            btnFileChooser.setText(bundle.getString("txtChooseImage"));
        }
    }

    private void initButtonClicks(){
        btnFileChooser.setOnAction(event -> configureLogoFileChooser(new FileChooser(), (Stage)pane.getScene().getWindow()));
        btnAddProduct.setOnAction(event -> saveProduct());
        btnGenerate.setOnAction(event -> generateId());
    }


    private void generateId(){
        String nextProductId = getNextId(lastLastIdStore);
        tfProductCode.setText(nextProductId);
    }

    private void saveProduct() {
         if(isValidForm()) {
              String productCode = tfProductCode.getText().trim();
              String productName = GeneralUtils.capitalize(tfProductName.getText().trim());
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
                updateNextId(productCode);
                 if (file != null) {
                     renameFile(file, productName);
                 }
                 file = null;
                 GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_product_saved"));
         }
    }

    private void updateNextId(String value){
        if(lastLastIdStore == null){
            LastIdStore lastIdStore = new LastIdStore("products", value);
            LastIdStoreRepo.getInstance().updateLastId(lastIdStore);
        }else{
            lastLastIdStore.setMValue(value);
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


    public static String getNextId(LastIdStore lastIdStore){
        String nextId;
        if(lastIdStore == null){
            nextId = "001";
        }else{
            int newLastId = Integer.parseInt(lastIdStore.getMValue())+1;
            nextId = String.format("%03d",newLastId);
        }
        return nextId;
    }



}
