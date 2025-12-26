package com.mwinensoaa.storemanager.controller;

import com.mwinensoaa.storemanager.entities.Cashier;
import com.mwinensoaa.storemanager.entities.CashierType;
import com.mwinensoaa.storemanager.entities.Product;
import com.mwinensoaa.storemanager.entities.Settings;
import com.mwinensoaa.storemanager.repositories.ProductRepo;
import com.mwinensoaa.storemanager.repositories.SettingsRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

import static javafx.scene.paint.Color.TRANSPARENT;

public class HomeScreenController implements Initializable {

    @FXML private Pane pane;
    @FXML private FontAwesomeIconView lbClose;
    @FXML private FontAwesomeIconView lbMin;
    @FXML private FontAwesomeIconView lbRefresh;
    @FXML private Button btnLogout;
    @FXML private Button btnAddProduct;
    @FXML private Button btnChangePass;
    @FXML private Button btnTransaction;
    @FXML private Button btnNewCashier;
    @FXML private Button btnNewTax;
    @FXML private Button btnTaxesView;
    @FXML private Button btnStatistics;
    @FXML private Button btnSettings;
    @FXML private Button btnProductList;
    @FXML private Button btnRestockProduct;
    @FXML private Button btnDiscount;
    @FXML private Button btnDiscountView;
    @FXML private Button btnDosageView;
    @FXML private Button btnDosage;
    @FXML private Button btnCashiers;
    @FXML private ImageView imgLogo;
    @FXML private ImageView imgAppLogo;
    @FXML private ImageView imgCashier;
    @FXML private TableView<TempProduct> tableView;
    @FXML private TableColumn<TempProduct, String> tcDatePlacedInStock;
    @FXML private TableColumn<TempProduct, String> tcUnitPrice;
    @FXML private TableColumn<TempProduct, String> tcPackPrice;
    @FXML private TableColumn<TempProduct, Integer> tcPackSize;
    @FXML private TableColumn<TempProduct, Integer> tcQuantity;
    @FXML private TableColumn<TempProduct, String> tcProductId;
    @FXML private TableColumn<TempProduct, String> tcProductName;
    @FXML private Label lbTotalItemsToday;
    @FXML private Label lbTotalMoneyToday;
    @FXML private Label lbTotalItemsAllTime;
    @FXML private Label lbTotalMoneyAllTime;
    @FXML private TextField tfSearch;


    @FXML private Label lbTotalInDay;
    @FXML private Label lbTotalAllTime;
    @FXML private Label lbTotalAmountInDay;
    @FXML private Label lbTotalAmountAllTime;




   private final Stage loginStage = new Stage();
   private final Stage transactionStage = new Stage();
   private final Stage addCashierStage = new Stage();
   private final Stage newTaxStage = new Stage();
   private final Stage taxesViewStage = new Stage();
   private final Stage settingsStage = new Stage();
   private final Stage changePassStage = new Stage();
   private final Stage addProductStage = new Stage();
   private final Stage statisticsStage = new Stage();
   private final Stage cashiersViewStage = new Stage();
   private final Stage productViewStage = new Stage();
   private final Stage restockProductStage = new Stage();
   private final Stage discountViewStage = new Stage();
   private final Stage discountStage = new Stage();
   private final Stage productDosage = new Stage();
   private final Stage productDosageView = new Stage();
   private Cashier currentCashier;
   private ResourceBundle bundle;
   private int currentLocaleCode;
   private int currentThemeCode;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initImageView();
        initLabels();
        Tooltip.install(btnTransaction, new Tooltip("This is something I am trying to see"));
        initButtonEvents();
        initLabels();
        lbRefresh.setOnMouseClicked(event -> {
            setupTableView();
            initLabels();
        });
        initTableColumns();
        setupTableView();
    }


    public void setCashierImage(Cashier cashier, ResourceBundle bundle, int languageCode, int themeCode){
        currentCashier = cashier;
        if(cashier.getCashierType().equals(CashierType.Cashier)){
            //disableButtons();
        }
        File file = getCashierImage(cashier.getCashierId());
        if(file != null){
            try {
                imgCashier.setImage(new Image(file.toURI().toURL().toExternalForm()));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        initI18N(bundle, languageCode, themeCode);
    }


    public void initI18N(ResourceBundle bundle,int localeCode, int themeCode){
        if(bundle != null){
            currentLocaleCode = localeCode;
            currentThemeCode = themeCode;
            setNodesLabels(bundle, themeCode);
            startUpdateSettingsThread();
        }
    }


    private void setNodesLabels(ResourceBundle bundle, int themeCode){
        this.bundle = bundle;
        btnLogout.setText(bundle.getString("btnLogout"));
        btnAddProduct.setText(bundle.getString("btnAddProduct"));
        btnChangePass.setText(bundle.getString("btnChangePass"));
        btnTransaction.setText(bundle.getString("btnTransaction"));
        btnNewCashier.setText(bundle.getString("btnNewCashier"));
        btnTaxesView.setText(bundle.getString("btnTaxesView"));
        btnStatistics.setText(bundle.getString("btnStatistics"));
        btnSettings.setText(bundle.getString("btnSettings"));
        btnRestockProduct.setText(bundle.getString("btnRestockProduct"));
        btnDiscount.setText(bundle.getString("btnDiscount"));
        btnDiscountView.setText(bundle.getString("btnDiscountView"));
        btnCashiers.setText(bundle.getString("btnCashiers"));
        btnNewTax.setText(bundle.getString("btnNewTax"));
        btnProductList.setText(bundle.getString("btnProductList"));

        tcDatePlacedInStock.setText(bundle.getString("tcDatePlacedInStock"));
        tcUnitPrice.setText(bundle.getString("tcUnitPrice"));
        tcPackPrice.setText(bundle.getString("tcPackPrice"));
        tcPackSize.setText(bundle.getString("tcPackSize"));
        tcQuantity.setText(bundle.getString("tcQuantity"));
        tcProductId.setText(bundle.getString("tcProductId"));
        tcProductName.setText(bundle.getString("tcProductName"));
        tfSearch.setPromptText(bundle.getString("tfSearch"));

        lbTotalAllTime.setText(bundle.getString("lbTotalAllTime"));
        lbTotalInDay.setText(bundle.getString("lbTotalInDay"));
        lbTotalAmountAllTime.setText(bundle.getString("lbTotalAmountAllTime"));
        lbTotalAmountInDay.setText(bundle.getString("lbTotalAmountInDay"));


        btnDosage.setText(bundle.getString("btnProductDosage"));
        btnDosageView.setText(bundle.getString("btnProductDosageView"));

    }


    private void startUpdateSettingsThread(){

       new Thread(()->{
           while(true){
               Settings settings = SettingsRepo.getInstance().getDefaultLanguage();
               if(settings.getLocale() != currentLocaleCode){
                   Locale locale = GeneralUtils.getPrefLocale(settings.getLocale());
                   Locale.setDefault(locale);
                   bundle = ResourceBundle.getBundle("lang", locale);
                   Platform.runLater(()->setNodesLabels(bundle, currentThemeCode));
                   currentLocaleCode = settings.getLocale();

               }
               if(settings.getTheme() != currentThemeCode){
                   Platform.runLater(()->{
                       if(pane.getScene() != null){
                           pane.getScene().getStylesheets().clear();
                           pane.getScene().getStylesheets().add(GeneralUtils.getCssFile("dashboard_screen.css",settings.getTheme()));
                           currentThemeCode = settings.getTheme();
                       }
                   });
               }
               setupTableView();
               try {
                   Thread.sleep(2000);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
       }).start();

    }


    private void disableButtons(){
        btnStatistics.setDisable(true);
        btnSettings.setDisable(true);
        btnNewTax.setDisable(true);
        btnNewCashier.setDisable(true);
        btnAddProduct.setDisable(true);
        btnRestockProduct.setDisable(true);
        btnDiscount.setDisable(true);
    }



    private void initTableColumns(){
        tcProductName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty);
        tcQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityInStockProperty.asObject());
        tcProductId.setCellValueFactory(cellData -> cellData.getValue().productIdProperty);
        tcPackPrice.setCellValueFactory(cellData -> cellData.getValue().packPriceProperty);
        tcPackSize.setCellValueFactory(cellData -> cellData.getValue().packSizeProperty.asObject());
        tcDatePlacedInStock.setCellValueFactory(cellData -> cellData.getValue().datePlaceInStockProperty);
        tcUnitPrice.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty);
    }

    private void initLabels(){
        Settings settings = SettingsRepo.getInstance().getSettings();
        if (settings != null){
            String currencyCode = settings.getCurrency();
            lbTotalMoneyToday.setText(currencyCode+ " 0.00");
            lbTotalItemsAllTime.setText("0");
            lbTotalItemsToday.setText("0");
            lbTotalMoneyAllTime.setText(currencyCode+ " 0.00");
        }
    }



    private void initImageView(){
        lbClose.setOnMouseClicked(event -> System.exit(0));
        imgAppLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        lbMin.setOnMouseClicked(event -> {
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.setIconified(true);
        });
    }



    private File getCashierImage(String cashierId){
        for(File file : Objects.requireNonNull(GeneralUtils.getCashiersImagesDirectory().listFiles())){
            int dotIndex = file.getName().lastIndexOf('.');
            String fileName = file.getName().substring(0, dotIndex);
            if(cashierId.equals(fileName)){
                return file;
            }
        }
        return null;
    }


    private void setupTableView(){
        List<Product> products = ProductRepo.getInstance().getAllProducts();
        ObservableList<TempProduct> data = FXCollections.observableArrayList();
        if(!products.isEmpty()){
            for(Product product : products){
                data.add(new TempProduct(product));
            }
        }
        FilteredList<TempProduct> filteredList = new FilteredList<>(data, e ->true);
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate((Predicate<? super TempProduct>) product ->{
            if (newValue == null || newValue.isEmpty()){
                return true;
            }
            String lowerCase = newValue.toLowerCase();
            if (product.getProductNameProperty().toLowerCase().contains(lowerCase)){
                return true;
            }else if(String.valueOf(product.productIdPropertyProperty()).toLowerCase().contains(lowerCase)){
                return true;
            }else if(product.getDatePlaceInStockProperty().toLowerCase().contains(lowerCase)) {
                return true;
            }else if(String.valueOf(product.getPackPriceProperty()).toLowerCase().contains(lowerCase)) {
                return true;
            }else if(String.valueOf(product.getPackSizeProperty()).toLowerCase().contains(lowerCase)) {
                return true;
            }else if(String.valueOf(product.getUnitPriceProperty()).toLowerCase().contains(lowerCase)){
                return true;
            }else return String.valueOf(product.getPackPriceProperty()).toLowerCase().contains(lowerCase);
        }));
        SortedList<TempProduct> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }




    private void initButtonEvents(){
        btnLogout.setOnAction(event -> logout());
        btnTransaction.setOnAction(event -> loadTransactionScreen());
        btnAddProduct.setOnAction(event -> loadNewProductScreen());
        btnSettings.setOnAction(event -> loadSettingsScreen());
        btnTaxesView.setOnAction(event -> loadTaxesViewScreen());
        btnNewTax.setOnAction(event -> loadNewTaxScreen());
        btnNewCashier.setOnAction(event -> loadNewCashierScreen());
        btnChangePass.setOnAction(event -> loadChangePasswordScreen());
        btnStatistics.setOnAction(event -> loadStatisticsScreen());
        btnCashiers.setOnAction(event -> loadCashiersViewScreen());
        btnProductList.setOnAction(event -> loadProductListViewScreen());
        btnRestockProduct.setOnAction(event -> loadRestockProductScreen());
        btnDiscount.setOnAction(event -> loadNewDiscountScreen());
        btnDiscountView.setOnAction(event -> loadDiscountViewScreen());
        btnDosage.setOnAction(event -> loadProductDosageScreen());
        btnDosageView.setOnAction(event -> loadProductDosageViewScreen());
    }


    private void loadProductListViewScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/products_view_screen.fxml")));
            Parent parent = loader.load();
            ProductsViewController controller = loader.getController();
            controller.verifyPrivileges(currentCashier, bundle, currentThemeCode);
            String css = GeneralUtils.getCssFile("product_view_screen.css",currentThemeCode);
            GeneralUtils.loadScreen(productViewStage, parent, css);
        } catch (Exception ignore){
          ignore.printStackTrace();
        }
    }

    private void loadProductDosageScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/product_dosage_screen.fxml")));
            Parent parent = loader.load();
            NewProductDosageController controller = loader.getController();
            controller.initI18N(bundle);
            String css = GeneralUtils.getCssFile("product_dosage_screen.css",currentThemeCode);
            GeneralUtils.loadScreen(productDosage, parent, css);
        } catch (Exception ignore){
            ignore.printStackTrace();
        }
    }

    private void loadProductDosageViewScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/product_dosage_view_screen.fxml")));
            Parent parent = loader.load();
            ProductDosagesViewController controller = loader.getController();
            controller.initI18N(bundle);
            String css = GeneralUtils.getCssFile("product_dosage_view_screen.css",currentThemeCode);
            GeneralUtils.loadScreen(productDosageView, parent, css);
        } catch (Exception ignore){

        }
    }


    private void logout(){
        try {
            Parent root =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login_screen.fxml")));
            Scene scene = new Scene(root);
            String css = GeneralUtils.getCssFile("login_screen.css",currentThemeCode);
            scene.getStylesheets().add(css);
            loginStage.setScene(scene);
            scene.setFill(TRANSPARENT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loginStage.initStyle(StageStyle.TRANSPARENT);
        loginStage.show();
        pane.getScene().getWindow().hide();

    }

    private void loadNewProductScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/new_product_screen.fxml")));
            Parent parent = loader.load();
            NewProductController controller = loader.getController();
            controller.initI18N(bundle);
            String css = GeneralUtils.getCssFile("product_screen.css",currentThemeCode);
            GeneralUtils.loadScreen(addProductStage, parent, css);
        } catch (Exception ignore) {

        }
    }


    private void loadRestockProductScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/restock_product_screen.fxml")));
            String css = GeneralUtils.getCssFile("restock_screen.css",currentThemeCode);
            Parent parent = loader.load();
            RestockProductController controller = loader.getController();
            controller.initL18N(bundle);
            GeneralUtils.loadScreen(restockProductStage, parent, css);
        } catch (Exception ignore) {

        }
    }

    private void loadStatisticsScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/statistics_screen.fxml")));
            Parent parent = loader.load();
            StatisticsController controller = loader.getController();
            controller.initL18N(bundle, currentCashier);
            String css = GeneralUtils.getCssFile("statistics_screen.css",currentThemeCode);
            GeneralUtils.loadScreen(statisticsStage, parent, css);
        } catch (Exception ignore) {

        }
    }

    private void loadNewTaxScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/new_tax_screen.fxml")));
            Parent parent = loader.load();
            NewTaxController controller = loader.getController();
            controller.initL18N(bundle);
            String css = GeneralUtils.getCssFile("tax_screen.css",currentThemeCode);
            GeneralUtils.loadScreen(newTaxStage, parent, css);
        } catch (Exception ignore) {

        }
    }


    private void loadTaxesViewScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/taxes_view_screen.fxml")));
            String css = GeneralUtils.getCssFile("taxes_view_screen.css",currentThemeCode);
            Parent parent = loader.load();
            TaxesViewController controller = loader.getController();
             controller.verifyPrivileges(currentCashier, bundle, currentThemeCode);
            GeneralUtils.loadScreen(taxesViewStage, parent, css);
        } catch (Exception ignore) {

        }
    }

    private void loadDiscountViewScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/discount_view_screen.fxml")));
            String css = GeneralUtils.getCssFile("discount_view_screen.css",currentThemeCode);
            Parent parent = loader.load();
            DiscountViewController controller = loader.getController();
            controller.verifyPrivileges(currentCashier,bundle, currentThemeCode);
            GeneralUtils.loadScreen(discountViewStage, parent, css);
        } catch (Exception ignore) {
        }
    }


    private void loadNewDiscountScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/new_discount_screen.fxml")));
            String css = GeneralUtils.getCssFile("discount_screen.css",currentThemeCode);
            Parent parent = loader.load();
            NewDiscountController controller = loader.getController();
            controller.initL18N(bundle);
            GeneralUtils.loadScreen(discountStage, parent, css);
        } catch (Exception ignore) {
        }
    }

    private void loadCashiersViewScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/cashiers_view_screen.fxml")));
            Parent parent = loader.load();
            CashiersViewController controller = loader.getController();
            controller.verifyPrivileges(currentCashier, bundle,currentThemeCode);
            String css = GeneralUtils.getCssFile("cashier_view_screen.css",currentThemeCode);
            GeneralUtils.loadScreen(cashiersViewStage, parent, css);
        } catch (Exception ignore) {

        }

    }

    private void loadChangePasswordScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/change_password_screen.fxml")));
            Parent parent = loader.load();
            ChangePasswordController controller = loader.getController();
            controller.setupUpdate(currentCashier, bundle);
            String css = GeneralUtils.getCssFile("change_pass_screen.css",currentThemeCode);
            GeneralUtils.loadScreen(changePassStage, parent, css);
        } catch (Exception ignore) {

        }
    }

    private void loadSettingsScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/settings_screen.fxml")));
            String css = GeneralUtils.getCssFile("settings_screen.css",currentThemeCode);
            Parent parent = loader.load();
            SettingsScreenController controller = loader.getController();
            controller.initI18N(bundle);
            GeneralUtils.loadScreen(settingsStage, parent, css);
        } catch (Exception ignore) {

        }
    }

   private void loadTransactionScreen() {
           try {
               FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/transaction_screen.fxml")));
               String css = GeneralUtils.getCssFile("transaction_screen.css",currentThemeCode);
               Parent parent = loader.load();
               TransactionController controller = loader.getController();
               controller.initL18N(bundle, currentCashier);
               GeneralUtils.loadScreen(transactionStage, parent, css);
           } catch (Exception ignore) {

           }
   }


   private void loadNewCashierScreen() {
           try{
               FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HomeScreenController.class.getResource("/fxml/new_cashier_screen.fxml")));
               Parent parent = loader.load();
               NewCashierController controller = loader.getController();
               controller.initL18N(bundle);
               String css = GeneralUtils.getCssFile("cashier_screen.css",currentThemeCode);
               GeneralUtils.loadScreen(addCashierStage, parent, css);
           } catch (Exception ignore) {
           }
   }



    private static class TempProduct{
        private final StringProperty productIdProperty;
        private final StringProperty productNameProperty;
        private final IntegerProperty packSizeProperty;
        private final StringProperty packPriceProperty;
        private final StringProperty unitPriceProperty;
        private final StringProperty datePlaceInStockProperty;
        private final IntegerProperty quantityInStockProperty;

        private TempProduct(Product product) {
            productIdProperty = new SimpleStringProperty(product.getProductId());
            productNameProperty = new SimpleStringProperty(product.getProductName());
            packPriceProperty = new SimpleStringProperty(String.format("%.2f",product.getPackPrice()));
            packSizeProperty = new SimpleIntegerProperty(product.getPackSize());
            unitPriceProperty = new SimpleStringProperty(String.format("%.2f",product.getUnitPrice()));
            datePlaceInStockProperty = new SimpleStringProperty(product.getDatePlacedInStock());
            quantityInStockProperty = new SimpleIntegerProperty(product.getQuantityInStock());
        }

        public String getProductIdProperty() {
            return productIdProperty.get();
        }

        public StringProperty productIdPropertyProperty() {
            return productIdProperty;
        }

        public String getProductNameProperty() {
            return productNameProperty.get();
        }

        public StringProperty productNamePropertyProperty() {
            return productNameProperty;
        }

        public int getPackSizeProperty() {
            return packSizeProperty.get();
        }

        public IntegerProperty packSizePropertyProperty() {
            return packSizeProperty;
        }

        public String getPackPriceProperty() {
            return packPriceProperty.get();
        }

        public StringProperty packPricePropertyProperty() {
            return packPriceProperty;
        }

        public String getUnitPriceProperty() {
            return unitPriceProperty.get();
        }

        public StringProperty unitPricePropertyProperty() {
            return unitPriceProperty;
        }

        public String getDatePlaceInStockProperty() {
            return datePlaceInStockProperty.get();
        }

        public StringProperty datePlaceInStockPropertyProperty() {
            return datePlaceInStockProperty;
        }

        public int getQuantityInStockProperty() {
            return quantityInStockProperty.get();
        }

        public IntegerProperty quantityInStockPropertyProperty() {
            return quantityInStockProperty;
        }


    }







}
