package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Cashier;
import com.mwinensoaa.storemanager.entities.CashierType;
import com.mwinensoaa.storemanager.entities.Tax;
import com.mwinensoaa.storemanager.repositories.TaxRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class TaxesViewController implements Initializable {

    @FXML private Pane pane;
    @FXML private Button btnRemoveTax;
    @FXML private Button btnEditTax;
    @FXML private ImageView imgLogo;
    @FXML private ListView<String> listView;
    @FXML private TextField tfSearch;
    @FXML private FontAwesomeIconView lbClose;
    @FXML private Label lbTaxes;

    private String dialogTitle;
    private String errorHeader;
    private String successHeader;
    private ResourceBundle bundle;
    private int currentThemeCode;

    private final Stage editTaxStage = new Stage();
          private List<Tax> taxes;
          private ObservableList<String> observableList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        enableFiltering();
        lbClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        taxes = TaxRepo.getInstance().getAllTaxes();
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        btnEditTax.setOnAction(event -> editTaxInfo());
        ObservableList<String> observableList = FXCollections.observableArrayList();
        taxes.forEach(it -> observableList.add(it.getTaxTitle()));
        listView.setItems(observableList);
        btnRemoveTax.setOnAction(event -> deleteTax());
    }

    private void deleteTax() {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        TaxRepo.getInstance().deleteTax(selectedItem);
        listView.getItems().remove(selectedItem);
    }


    public void verifyPrivileges(Cashier cashier, ResourceBundle bundle, int themeCode){
        this.currentThemeCode = themeCode;
        if(cashier != null && cashier.getCashierType().equals(CashierType.Cashier)){
            //btnRemoveTax.setDisable(true);
            //btnEditTax.setDisable(true);
        }
        if(bundle != null){
            this.bundle = bundle;
            dialogTitle = bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
            tfSearch.setPromptText(bundle.getString("tfSearch"));
            lbTaxes.setText(bundle.getString("lbTaxes"));
            this.bundle = bundle;
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

    private void editTaxInfo() {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            new Thread(()->{
                String cashierId = selectedItem.split("\\(")[0].trim();
                try {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/edit_tax_screen.fxml")));
                    Parent parent  = loader.load();
                    EditTaxController controller = loader.getController();
                    Tax tax = getSelectedTax(selectedItem);
                    controller.setupEdit(tax, bundle);
                    String css = GeneralUtils.getCssFile("tax_screen.css", currentThemeCode);
                    Platform.runLater(()->GeneralUtils.loadScreen(editTaxStage, parent, css));
                } catch (Exception ignore) {

                }
            }).start();
        }else {
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_select_tax"));
        }
    }


    private Tax getSelectedTax(String selectedTax){
        for(Tax tax : taxes){
            if(selectedTax.equals(tax.getTaxTitle()))
                return tax;
        }
        return new Tax();
    }



}
