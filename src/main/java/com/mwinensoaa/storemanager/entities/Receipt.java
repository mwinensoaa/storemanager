package com.mwinensoaa.storemanager.entities;


import com.mwinensoaa.storemanager.repositories.SettingsRepo;
import com.mwinensoaa.storemanager.repositories.TaxRepo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Receipt {

    @Getter
    @Setter
    private static List<Sale> items = new ArrayList<>();
    @Getter
    @Setter
    private static double totalTaxAmount;
    @Getter
    private static String grandTotal;





    public static void addItem(Sale item) {
        items.add(item);
    }


    public static double getSubTotal() {
        return items.stream()
                .mapToDouble(Sale::getTotalPrice)
                .sum();
    }
    public static void updateItem(String itemName, int quantity){
        for(Sale sale : getItems()){
            if(sale.getProductName().equals(itemName)){
                sale.setQuantity(quantity);
            }
        }
    }
    public static void setGrandTotal(String currency) {
        double total = getSubTotal() + getTotalTaxAmount();
       grandTotal = String.format("%s%.2f", currency, total);
    }

    public static void removeItem(String itemName){
        items.removeIf(item -> item.getProductName().equals(itemName));
    }

    public static List<Tax> getTaxes(){
        return TaxRepo.getInstance().getAllTaxes();
    }

    private static String getReceiptDate(){
        DateTimeFormatter receiptFormat =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.now().format(receiptFormat);
    }

    public static double calculateTax(double amount, double taxPercent) {
        double rate = taxPercent / 100.0;
        double tax = amount * rate;
        return Math.round(tax * 100.0) / 100.0;
    }

    public static void clearItems(){
        items.clear();
    }


    public static String buildReceipt() {

        Settings settings = SettingsRepo.getInstance().getSettings();
        // --- Receipt data (normally this would come from a list)
        List<String> itemList = Receipt.getItems().stream()
                .map(Sale::getProductName)
                .collect(Collectors.toCollection(ArrayList::new));

        getTaxes().forEach(it -> itemList.add(it.getTaxTitle()+"("+it.getPercentage()+"%)"));

        String[] items = itemList.toArray(new String[0]);

        // --- Find longest item name
        int maxItemWidth = 0;
        for (String item : items) {
            maxItemWidth = Math.max(maxItemWidth, item.length());
        }

        // Optional safety cap for receipts
        int ITEM_COL_WIDTH = Math.min(maxItemWidth + 1, 16);

        // Build dynamic format
        String rowFormat = "%-" + ITEM_COL_WIDTH + "s %3d %7.2f%n";
        String headerFormat = "%-" + ITEM_COL_WIDTH + "s %3s %7s%n";

        int receiptWidth = ITEM_COL_WIDTH + 3 + 7 + 2; // columns + spaces
        String divider = "-".repeat(receiptWidth);
        String fDivider = "=".repeat(receiptWidth);

        StringBuilder sb = new StringBuilder();

        // --- Header
        sb.append(center(" ", receiptWidth)).append("\n");
        sb.append(center(settings.getCompanyName(), receiptWidth)).append("\n");
        sb.append(center(settings.getRegion()+"-"+settings.getCountry(), receiptWidth)).append("\n");
        sb.append(center("Tel: "+settings.getTelephone(), receiptWidth)).append("\n");
        sb.append(divider).append("\n");

        // --- Table header
        sb.append(String.format(headerFormat, "Item", "Qty", "Total"));
        sb.append(divider).append("\n");


        for (Sale sale : Receipt.getItems()) {
            sb.append(String.format(
                    rowFormat,
                    fit(sale.getProductName(), ITEM_COL_WIDTH),
                    sale.getQuantity(),
                    sale.getTotalPrice()
            ));
        }

        // --- Footer
        sb.append(divider).append("\n");

        sb.append(String.format(
                "%-" + ITEM_COL_WIDTH + "s %10.2f%n",
                "Sub Total:",
                getSubTotal()
        ));
        sb.append(divider).append("\n");
        sb.append("Taxes").append("\n");

        double totalTaxAmount = 0;

        for(Tax tax : getTaxes() ){
            sb.append(String.format(
                    "%-" + ITEM_COL_WIDTH + "s %10.2f%n",
                    tax.getTaxTitle()+"("+tax.getPercentage()+"%):",
                    calculateTax(getSubTotal(),tax.getPercentage())
            ));
            totalTaxAmount += calculateTax(getSubTotal(),tax.getPercentage());
        }
        setTotalTaxAmount(totalTaxAmount);
        setGrandTotal(settings.getCurrency());

        sb.append(fDivider).append("\n");
        sb.append(String.format(
                "%-" + ITEM_COL_WIDTH + "s %10s%n",
                "GRAND TOTAL:",
                getGrandTotal()
        ));

        sb.append(fDivider).append("\n");
        sb.append(center(getReceiptDate(), receiptWidth)).append("\n");
        sb.append(center("Thank you!", receiptWidth)).append("\n");
        return sb.toString();
    }



    private static String center(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    private static String fit(String text, int max) {
        return text.length() > max
                ? text.substring(0, max - 1) + "…"
                : text;
    }




}
