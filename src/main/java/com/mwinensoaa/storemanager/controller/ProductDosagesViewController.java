package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Dosage;
import com.mwinensoaa.storemanager.entities.Product;
import com.mwinensoaa.storemanager.repositories.DosageRepo;
import com.mwinensoaa.storemanager.repositories.ProductRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.util.function.Predicate;


public class ProductDosagesViewController implements Initializable {

    @FXML private Pane pane;
    @FXML private ImageView imgLogo;
    @FXML private ListView<String> listView;
    @FXML private TextField tfSearch;
    @FXML private FontAwesomeIconView lbClose;
    @FXML private Label lbDosage;
    @FXML private TextArea taDosage;
    @FXML private Label lbTitle;
    private List<Product> products;
    private ResourceBundle bundle;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        lbClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        products = ProductRepo.getInstance().getAllProducts();
        taDosage.setDisable(true);
        if(!products.isEmpty()){
            for(Product product : products){
                listView.getItems().add(product.getProductName());
            }
        }
        iniListViewSelect();
        enableFiltering();
    }



    public void initI18N(ResourceBundle bundle){
        if(bundle != null){
            this.bundle = bundle;
            lbTitle.setText(bundle.getString("txt_products_dosage"));
            lbDosage.setText(bundle.getString("txt_dosage"));
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

    private void iniListViewSelect(){
        listView.setOnMouseClicked(mouseEvent -> {
            String productName = listView.getSelectionModel().getSelectedItem();
            Dosage dosage = DosageRepo.getInstance().getDosage(productName);
            taDosage.clear();
            try{
                taDosage.setText(dosage.getDosage());
            }catch (Exception ignore){
                taDosage.setText(bundle.getString("txt_no_dosage"));
            }
        });
    }




}
