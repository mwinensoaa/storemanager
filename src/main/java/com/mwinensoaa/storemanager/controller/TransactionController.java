package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Cashier;
import com.mwinensoaa.storemanager.entities.Product;
import com.mwinensoaa.storemanager.entities.Receipt;
import com.mwinensoaa.storemanager.entities.Sale;
import com.mwinensoaa.storemanager.repositories.ProductRepo;
import com.mwinensoaa.storemanager.repositories.SaleRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;
import java.util.function.Predicate;


public class TransactionController implements Initializable {

    @FXML private Pane anchor;
    @FXML private FontAwesomeIconView btnClose;
    @FXML private ImageView imgProduct;
    @FXML private TextField tfQuantity;
    @FXML private TextField tfSearch;
    @FXML private TextArea taReceipt;
    @FXML private ListView<String> listView;
    @FXML private Button btnAddItem;
    @FXML private Button btnProcessTransaction;
    @FXML private Button btnRemoveItem;
    @FXML private TableColumn<Transaction, String> tcProductName;
    @FXML private TableColumn<Transaction, String> tcQuantity;
    @FXML private TableColumn<Transaction, String> tcTotal;
    @FXML private TableView<Transaction> tableView;
    @FXML private Label lbGrandTotal;
    @FXML private Label lbTotalAmount;

    private List<Product> products;
    private String dialogTitle;
    private String errorHeader;
    private String successHeader;
    private ResourceBundle bundle;
    private Cashier currentCashier;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GeneralUtils.initControlButtonsAndWindoDrag(anchor);
        btnClose.setOnMouseClicked(event -> anchor.getScene().getWindow().hide());
        taReceipt.setEditable(false);
        taReceipt.setWrapText(false);
        taReceipt.setFocusTraversable(false);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        btnAddItem.setOnAction(event -> addItem());
        btnRemoveItem.setOnAction(event -> removeItem());
        btnProcessTransaction.setOnAction(event -> processTransaction());
        products = ProductRepo.getInstance().getAllProducts();
        lbGrandTotal.setText("0.00");
        products.forEach(it -> listView.getItems().add(it.getProductName()));
        iniTableColumns();
        enableFiltering();

    }

   public void initL18N(ResourceBundle bundle, Cashier cashier){
        if(bundle != null){
            this.bundle = bundle;
            dialogTitle = bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
            btnAddItem.setText(bundle.getString("lbAdd"));
            btnProcessTransaction.setText(bundle.getString("processTransaction"));
            lbTotalAmount.setText(bundle.getString("lbTotal"));
            tfQuantity.setPromptText(bundle.getString("lbQuantity"));
            tfSearch.setPromptText(bundle.getString("tfSearch"));
            tcProductName.setText(bundle.getString("lbProduct"));
            tcQuantity.setText(bundle.getString("lbQuantity"));
            tcTotal.setText(bundle.getString("lbPrice"));

        }
        if(cashier != null){
            currentCashier = cashier;

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

    private void processTransaction() {

       printReceipt(taReceipt);
        for(Sale sale : Receipt.getItems()){
            SaleRepo.getInstance().createSale(sale);
            ProductRepo.getInstance().minusProduct(sale.getProductName(),sale.getQuantity());

        }
       Receipt.clearReceipt();
    }

    private void removeItem() {
      Transaction transaction = tableView.getSelectionModel().getSelectedItem();
      tableView.getItems().remove(transaction);
      Receipt.removeItem(transaction.getProductNameProperty());
      taReceipt.setText("");
      taReceipt.setText(Receipt.buildReceipt());
      lbGrandTotal.setText(Receipt.getGrandTotal());
      tableView.refresh();
    }



    private void addItem() {
        if(isValidForm()){
            ObservableList<String> selectedItems =
                    listView.getSelectionModel().getSelectedItems();
            for(String selectedItem : selectedItems){
                String text = tfQuantity.getText();
                int quantity = text.isEmpty() ? 1 : Integer.parseInt(text);
                Product product = getProduct(selectedItem);
                double totalAmount = product.getUnitPrice() * quantity;
                String totalAmountString = String.format("%.2f", totalAmount);
                Sale sale = new Sale(selectedItem,quantity,(quantity * product.getUnitPrice()),currentCashier.getCashierName());
                if(isPresent(selectedItem)){
                 tableView.getItems().removeIf(item -> item.getProductNameProperty().equals(selectedItem));
                 Receipt.removeItem(selectedItem);
                }
                Receipt.addItem(sale);
                tableView.getItems().add(new Transaction(selectedItem,String.valueOf(quantity),totalAmountString));
            }
           taReceipt.setText(Receipt.buildReceipt());
           lbGrandTotal.setText(Receipt.getGrandTotal());
        }
    }

    private boolean isPresent(String itemName){
        for(Transaction item : tableView.getItems()){
            if(item.productNameProperty.getValue().equals(itemName)){
                return true;
            }
        }
        return false;
    }

    private Product getProduct(String productName){
        for(Product product : products){
            if(productName.equals(product.getProductName()))
                return product;
        }
        return new Product();
    }

    private void iniTableColumns() {
        tcProductName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        tcQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tcTotal.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty());
    }


   private boolean isValidForm(){
        if(listView.getSelectionModel().getSelectedItem() == null){
          GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_select_product"));
          return false;
        }
        return true;
   }



    public static void printReceipt(TextArea textArea) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob == null) {
            System.out.println("No printer available.");
            return;
        }
        boolean proceed = printerJob.showPrintDialog(textArea.getScene().getWindow());
        if (proceed) {
            Text printableText = new Text(textArea.getText());
            printableText.setFont(textArea.getFont());
            boolean success = printerJob.printPage(printableText);
            if (success) {
                printerJob.endJob();
            }
        }
    }


    private record Transaction(StringProperty productNameProperty, StringProperty quantityProperty, StringProperty totalAmountProperty) {

            private Transaction(String productNameProperty, String quantityProperty, String totalAmountProperty) {
                this(new SimpleStringProperty(productNameProperty),
                        new SimpleStringProperty(quantityProperty),
                        new SimpleStringProperty(totalAmountProperty));
            }

            public String getProductNameProperty() {
                return productNameProperty.get();
            }

            public String getQuantityProperty() {
                return quantityProperty.get();
            }
            public void setQuantityProperty(String quantityProperty) {
                this.quantityProperty.set(quantityProperty);
            }
            public String getTotalAmountProperty() {
                return totalAmountProperty.get();
            }

        }



}
