package com.mwinensoaa.storemanager.controller;

import com.mwinensoaa.storemanager.entities.Cashier;
import com.mwinensoaa.storemanager.entities.Sale;
import com.mwinensoaa.storemanager.repositories.CashierRepo;
import com.mwinensoaa.storemanager.repositories.SaleRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

public class StatisticsController implements Initializable {
    @FXML private Pane pane;
    @FXML private Label dialogTitle;
    @FXML private FontAwesomeIconView lbClose;
    @FXML private ImageView imgLogo;
    @FXML private ListView<String> listView;
    @FXML private TextField tfSearch;
    @FXML private TextField tfSearchTable;
    @FXML private FontAwesomeIconView faRefresh;

    @FXML private TableColumn<InnerSale, String> tcDate;
    @FXML private TableColumn<InnerSale, String> tcQuantity;
    @FXML private TableColumn<InnerSale, String> tcCashier;
    @FXML private TableColumn<InnerSale, String> tcItemName;
    @FXML private TableColumn<InnerSale, String> tcTotal;
    @FXML private TableView<InnerSale> tableView;

    private List<Cashier> cashiers;
    private Cashier currentCashier;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        lbClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
         cashiers = CashierRepo.getInstance().getCashiers();
         cashiers.forEach(it -> listView.getItems().add(it.getCashierName()) );
         faRefresh.setOnMouseClicked(mouseEvent -> refreshCashierList());

         enableFiltering();
         iniTableColumns();
         enableTableFiltering();
    }




    public void enableTableFiltering() {

        // Load master data
        ObservableList<InnerSale> masterData = FXCollections.observableArrayList();
        SaleRepo.getInstance().getSales().forEach(it -> masterData.add(new InnerSale(it)));


        // Wrap the ObservableList in a FilteredList
        FilteredList<InnerSale> filteredData = new FilteredList<>(masterData, sale -> true);

        // Listen to text changes in the search field
        tfSearchTable.textProperty().addListener((obs, oldValue, newValue) -> {
            String filterText = newValue == null ? "" : newValue.toLowerCase().trim();

            filteredData.setPredicate(sale -> {
                // No filter → show all
                if (filterText.isEmpty()) {
                    return true;
                }
                // Match against Sale fields
                return String.valueOf(sale.getItemNameProperty()).contains(filterText)
                        || sale.getCashierProperty().toLowerCase().contains(filterText)
                        || String.valueOf(sale.getTotalAmountProperty()).toLowerCase().contains(filterText)
                        || String.valueOf(sale.getQuantityProperty()).toLowerCase().contains(filterText)
                        || sale.getDateProperty().toLowerCase().contains(filterText);
            });
        });

        // Wrap in SortedList to preserve TableView sorting
        SortedList<InnerSale> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // Set data to table
        tableView.setItems(sortedData);
    }

    private void iniTableColumns() {
        tcItemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        tcQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tcTotal.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty());
        tcCashier.setCellValueFactory(cellData -> cellData.getValue().cashierProperty());
        tcDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
    }

    private void refreshCashierList(){
        if(!cashiers.isEmpty()){
            if(!listView.getItems().isEmpty()){
                listView.getItems().clear();
                cashiers.forEach(it -> listView.getItems().add(it.getCashierName()));
            }else{
                cashiers.forEach(it -> listView.getItems().add(it.getCashierName()));
            }
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



    public void initL18N(ResourceBundle bundle, Cashier cashier){
        if(bundle != null){
//            this.bundle = bundle;
              dialogTitle.setText(bundle.getString("lbStats")); ;
//            errorHeader = bundle.getString("txt_dialog_error_header");
//            successHeader = bundle.getString("txt_dialog_success_header");
//            btnAddItem.setText(bundle.getString("lbAdd"));
//            btnProcessTransaction.setText(bundle.getString("processTransaction"));
//            lbTotalAmount.setText(bundle.getString("lbPrice"));
//            tfQuantity.setPromptText(bundle.getString("lbQuantity"));
            tfSearch.setPromptText(bundle.getString("tfSearch"));
            tcItemName.setText(bundle.getString("lbProduct"));
            tcQuantity.setText(bundle.getString("lbQuantity"));
            tcCashier.setText(bundle.getString("lbCashier"));
            tcDate.setText(bundle.getString("lbDate"));
            tcTotal.setText(bundle.getString("lbPrice"));
        }
        if(cashier != null){
            currentCashier = cashier;
        }
    }



    private static class InnerSale{
        private final StringProperty itemNameProperty;
        private final StringProperty quantityProperty;
        private final StringProperty totalAmountProperty;
        private final StringProperty dateProperty;

        private final StringProperty cashierProperty;

        private InnerSale(Sale sale) {
            dateProperty = new SimpleStringProperty(sale.getSaleDate());
            cashierProperty = new SimpleStringProperty(sale.getCashierName());
            itemNameProperty = new SimpleStringProperty(sale.getProductName());
            quantityProperty = new SimpleStringProperty(String.valueOf(sale.getQuantity()));
            totalAmountProperty = new SimpleStringProperty(String.valueOf(sale.getTotalPrice()));
        }

        public String getItemNameProperty() {
            return itemNameProperty.get();
        }
        public String getDateProperty() {return dateProperty.get();}
        public String getCashierProperty() {return cashierProperty.get();}

        public StringProperty itemNameProperty() {
            return itemNameProperty;
        }
        public StringProperty dateProperty() {return dateProperty;}
        public StringProperty cashierProperty() {return cashierProperty;}

        public String getQuantityProperty() {return quantityProperty.get();}
        public StringProperty quantityProperty() {return quantityProperty;}

        public String getTotalAmountProperty() {return totalAmountProperty.get();}
        public StringProperty totalAmountProperty() {return totalAmountProperty;}

    }

}
