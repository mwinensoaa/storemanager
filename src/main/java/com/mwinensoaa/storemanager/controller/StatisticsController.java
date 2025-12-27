package com.mwinensoaa.storemanager.controller;

import com.mwinensoaa.storemanager.entities.Cashier;
import com.mwinensoaa.storemanager.entities.Sale;
import com.mwinensoaa.storemanager.repositories.CashierRepo;
import com.mwinensoaa.storemanager.repositories.ProductRepo;
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
import javafx.scene.chart.*;
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
    @FXML private FontAwesomeIconView lbClose;
    @FXML private ImageView imgLogo;
    @FXML private ListView<String> listView;
    @FXML private TextField tfSearch;
    @FXML private TextField tfSearchTable;
    @FXML private Button btnPrintReport;
    @FXML private FontAwesomeIconView faRefresh;

    @FXML private Label dialogTitle;

    @FXML private NumberAxis lbQuantitySold;
    @FXML private NumberAxis lbSalesAmount;
    @FXML private CategoryAxis lbSalesProduct;
    @FXML private CategoryAxis lbSalesDate;

    @FXML private Label lbSalesStatus;
    @FXML private Label lbCashier;
    @FXML private Label lbTopSellingProducts;
    @FXML private Label lbLowStockAlert;
    @FXML private Label lbExpiringSoon;
    @FXML private Label lbTopProducts;
    @FXML private Label lbSales;
    @FXML private Label lbSalesTrend;


    @FXML private TableColumn<InnerSale, String> tcDate;
    @FXML private TableColumn<InnerSale, String> tcQuantity;
    @FXML private TableColumn<InnerSale, String> tcCashier;
    @FXML private TableColumn<InnerSale, String> tcItemName;
    @FXML private TableColumn<InnerSale, String> tcTotal;
    @FXML private TableView<InnerSale> tableView;

    @FXML private TableView<InnerLowStock> tableLowStockAlert;
    @FXML private TableColumn<InnerLowStock, String> tcLowStockProductName;
    @FXML private TableColumn<InnerLowStock, String> tcLowStock;

    @FXML private TableView<InnerExpiring> tableExpiringSoon;
    @FXML private TableColumn<InnerExpiring, String> tcExpiringProductName;
    @FXML private TableColumn<InnerExpiring, String> tcExpiringDate;

    @FXML private TableView<InnerTopSeller> tableTopProducts;
    @FXML private TableColumn<InnerTopSeller, String> tcTopProductName;
    @FXML private TableColumn<InnerTopSeller, String> tcTopProductQuantity;
    @FXML private TableColumn<InnerTopSeller, String> tcTopProductRevenue;

    @FXML private LineChart<String, Number> salesLineChart;
    @FXML private BarChart<String, Number> topProductsBarChart;
    @FXML private PieChart paymentPieChart, inventoryPieChart;

    

    private List<Cashier> cashiers;
    private Cashier currentCashier;
    private ResourceBundle bundle;


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
         initTableColumns();
         enableTableFiltering();

         loadInventoryStatus();
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

    private void initTableColumns() {
        tcItemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        tcQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tcTotal.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty());
        tcCashier.setCellValueFactory(cellData -> cellData.getValue().cashierProperty());
        tcDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
    }

    private void initInnerTopSellerTableColumns() {
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
            this.bundle = bundle;
            dialogTitle.setText(bundle.getString("lbStats"));
            btnPrintReport.setText(bundle.getString("lbPrintReport"));
            lbSales.setText(bundle.getString("lbSales"));
            lbCashier.setText(bundle.getString("lbCashier"));
            lbTopProducts.setText(bundle.getString("lbTopProducts"));
            lbLowStockAlert.setText(bundle.getString("lbLowStock"));
            lbExpiringSoon.setText(bundle.getString("lbExpiringSoon"));
            lbTopSellingProducts.setText(bundle.getString("lbTopSellingProducts"));
            lbSalesStatus.setText(bundle.getString("lbSalesStatus"));
            lbSalesTrend.setText(bundle.getString("lbSalesTrend"));

            lbSalesProduct.setLabel(bundle.getString("lbProduct"));
            lbSalesAmount.setLabel(bundle.getString("lbSalesAmount"));
            lbQuantitySold.setLabel(bundle.getString("lbQuantitySold"));
            lbSalesDate.setLabel(bundle.getString("lbDate"));

            tfSearch.setPromptText(bundle.getString("tfSearch"));
            tcItemName.setText(bundle.getString("lbProduct"));
            tcQuantity.setText(bundle.getString("lbQuantity"));
            tcCashier.setText(bundle.getString("lbCashier"));
            tcDate.setText(bundle.getString("lbDate"));
            tcTotal.setText(bundle.getString("lbPrice"));

            tcTopProductName.setText(bundle.getString("lbProduct"));
            tcTopProductQuantity.setText(bundle.getString("lbQuantity"));
            tcTopProductRevenue.setText(bundle.getString("lbRevenue"));

            tcExpiringProductName.setText(bundle.getString("lbProduct"));
            tcExpiringDate.setText(bundle.getString("lbExpiring"));

            tcLowStock.setText(bundle.getString("lbStock"));
            tcLowStockProductName.setText(bundle.getString("lbProduct"));

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

    private static class InnerTopSeller{
        private final StringProperty itemNameProperty;
        private final StringProperty quantityProperty;
        private final StringProperty totalRevenueProperty;

        private InnerTopSeller(Sale sale) {
            itemNameProperty = new SimpleStringProperty(sale.getProductName());
            quantityProperty = new SimpleStringProperty(String.valueOf(sale.getQuantity()));
            totalRevenueProperty = new SimpleStringProperty(String.valueOf(sale.getTotalPrice()));
        }
        public String getItemNameProperty() {
            return itemNameProperty.get();
        }
        public StringProperty itemNameProperty() {
            return itemNameProperty;
        }
        public String getQuantityProperty() {return quantityProperty.get();}
        public StringProperty quantityProperty() {return quantityProperty;}
        public String getTotalRevenueProperty() {return totalRevenueProperty.get();}
        public StringProperty totalAmountProperty() {return totalRevenueProperty;}
    }

    private static class InnerLowStock{
        private final StringProperty itemNameProperty;
        private final StringProperty stockProperty;


        private InnerLowStock(Sale sale) {
            itemNameProperty = new SimpleStringProperty(sale.getProductName());
            stockProperty = new SimpleStringProperty(String.valueOf(sale.getQuantity()));
        }
        public String getItemNameProperty() {
            return itemNameProperty.get();
        }
        public StringProperty itemNameProperty() {
            return itemNameProperty;
        }
        public String getStockProperty() {return stockProperty.get();}
        public StringProperty quantityProperty() {return stockProperty;}

    }
    private static class InnerExpiring{
        private final StringProperty itemNameProperty;
        private final StringProperty expiryDateProperty;


        private InnerExpiring(Sale sale) {
            itemNameProperty = new SimpleStringProperty(sale.getProductName());
            expiryDateProperty = new SimpleStringProperty(String.valueOf(sale.getQuantity()));
        }

        public String getItemNameProperty() {return itemNameProperty.get();}
        public StringProperty itemNameProperty() {return itemNameProperty;}
        public String getExpiryDateProperty() {return expiryDateProperty.get();}
        public StringProperty quantityProperty() {return expiryDateProperty;}
    }




    private void loadSalesTrend() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        StatsDAO.getDailySales().forEach(d ->
//                series.getData().add(new XYChart.Data<>(d.getDate(), d.getAmount()))
//        );
        salesLineChart.getData().add(series);
    }

    private void loadTopProducts() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        StatsDAO.getTopProducts().forEach(p ->
//                series.getData().add(new XYChart.Data<>(p.getName(), p.getQty()))
//        );
        topProductsBarChart.getData().add(series);
    }

    private void loadInventoryStatus() {
        ProductRepo productRepo = ProductRepo.getInstance();
        inventoryPieChart.getData().addAll(
                new PieChart.Data("In Stock", productRepo.getProductsCount(100)),
                new PieChart.Data("Low Stock", productRepo.getProductsCount(10)),
                new PieChart.Data("Out of Stock", productRepo.getProductsCount(0))
        );
    }

}
