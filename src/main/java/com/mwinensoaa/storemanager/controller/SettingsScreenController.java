package com.mwinensoaa.storemanager.controller;

import com.mwinensoaa.storemanager.entities.Settings;
import com.mwinensoaa.storemanager.repositories.SettingsRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import java.util.*;


public class SettingsScreenController implements Initializable {

    @FXML private Pane pane;
    @FXML private Button btnSaveSettings;
    @FXML private Button btnFileChooser;
    @FXML private FontAwesomeIconView lbClose;
    @FXML private ComboBox<String> comboCountry;
    @FXML private TextField tfCompanyName;
    @FXML private TextField tfRegion;
    @FXML private ImageView imgCompanyLogo;
    @FXML private ImageView imgLogo;
    @FXML private TextField tfTelephone;
    @FXML private ComboBox<String> comboLanguage;
    @FXML private ComboBox<String> comboTheme;

    @FXML private Label lbTelephone;
    @FXML private Label lbCountry;
    @FXML private Label lbCompanyName;
    @FXML private Label lbRegion;
    @FXML private Label lbSettings;
    @FXML private Label lbLanguage;
    @FXML private Label lbTheme;
    @FXML private Label lbCompanyLogo;

          private Settings currentSettings;
    private String dialogTitle;
    private String errorHeader;
    private String successHeader;
    private ResourceBundle bundle;

    private File file;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GeneralUtils.initControlButtonsAndWindoDrag(pane);
        lbClose.setOnMouseClicked(event -> pane.getScene().getWindow().hide());
        imgLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        comboCountry.getItems().addAll(getCountries());
        initButtonClicks();
        comboLanguage.getItems().clear();
        comboTheme.getItems().clear();
        currentSettings = SettingsRepo.getInstance().getSettings();
    }


    public void initI18N(ResourceBundle bundle){
        if(bundle != null){
            setNodeLabels(bundle);
            setCurrentSettings(currentSettings);
        }
    }

    private void setNodeLabels(ResourceBundle bundle){
        this.bundle = bundle;
        dialogTitle = bundle.getString("dialog_title");
        errorHeader = bundle.getString("txt_dialog_error_header");
        successHeader = bundle.getString("txt_dialog_success_header");
        lbTelephone.setText(bundle.getString("lbTelephone"));
        lbCountry.setText(bundle.getString("lbCountry"));
        lbRegion.setText(bundle.getString("lbRegion"));
        lbSettings.setText(bundle.getString("lbSettings"));
        lbCompanyName.setText(bundle.getString("lbCompanyName"));
        btnSaveSettings.setText(bundle.getString("txtSave"));
        btnFileChooser.setText(bundle.getString("lbCompanyLogo"));
        lbCompanyLogo.setText(bundle.getString("lbCompanyLogo"));
        lbLanguage.setText(bundle.getString("lbLanguage"));
        lbTheme.setText(bundle.getString("lbTheme"));
        comboLanguage.getItems().clear();
        comboLanguage.setPromptText(bundle.getString("txtSelect"));
        comboTheme.getItems().clear();
        comboTheme.setPromptText(bundle.getString("txtSelect"));
        comboLanguage.getItems().addAll(
                bundle.getString("txt_english"),
                bundle.getString("txt_spanish"),
                bundle.getString("txt_french")
        );
        comboTheme.getItems().addAll(
                bundle.getString("txt_default_theme"),
                bundle.getString("txt_dark_theme"),
                bundle.getString("txt_material_theme"),
                bundle.getString("txt_flat_theme")
        );
    }


     private void setCurrentSettings(Settings currentSettings){
         if (currentSettings != null){
             if(!currentSettings.getCompanyName().isEmpty()){
                 tfCompanyName.setText(currentSettings.getCompanyName());
             }
             if(!currentSettings.getTelephone().isEmpty()){
                 tfTelephone.setText(currentSettings.getTelephone());
             }
             if(currentSettings.getRegion() != null){
                 tfRegion.setText(currentSettings.getRegion());
             }
             if(!currentSettings.getCountry().isEmpty()){
                 comboCountry.setValue(currentSettings.getCountry());
             }
             comboLanguage.setValue(comboLanguage.getItems().get(currentSettings.getLocale()));
             comboTheme.setValue(comboTheme.getItems().get(currentSettings.getTheme()));
             File file = GeneralUtils.getLogo();
             if(file != null){
                 btnFileChooser.setText(file.getAbsolutePath());
                 try {
                     imgCompanyLogo.setImage(new Image(file.toURI().toURL().toExternalForm()));
                     btnFileChooser.setText(file.getAbsolutePath());
                 } catch (MalformedURLException e) {
                     throw new RuntimeException(e);
                 }
             }
         }
     }

    private void initButtonClicks(){
        btnFileChooser.setOnAction(event -> configureLogoFileChooser(new FileChooser(), (Stage)pane.getScene().getWindow()));
        btnSaveSettings.setOnAction(event -> saveInfo());
    }

    private void saveInfo() {
        if(comboCountry.getValue() == null){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_select_country"));
        }else if(tfCompanyName.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_company_name"));
        }else if(tfTelephone.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_company_telephone"));
        }else {
            int locale = Math.max(comboLanguage.getSelectionModel().getSelectedIndex(), 0);
            int theme = Math.max(comboTheme.getSelectionModel().getSelectedIndex(),0);
            String country = comboCountry.getValue().trim();
            String currency = getCurrencyByCountryName(country);
            String telephone = tfTelephone.getText().trim();
            String companyName = GeneralUtils.capitalize(tfCompanyName.getText().trim());
            String regionText = tfRegion.getText().trim();
            String region = !regionText.isEmpty() ? GeneralUtils.capitalize(tfRegion.getText().trim()) : "";
            Settings settings = new Settings();
            settings.setRegion(region);
            settings.setCompanyName(companyName);
            settings.setCountry(country);
            settings.setCurrency(currency);
            settings.setTelephone(telephone);
            settings.setLocale(locale);
            settings.setTheme(theme);
            if(currentSettings != null && !currentSettings.getCompanyName().equals(companyName)){
                SettingsRepo.getInstance().deleteSettings(currentSettings.getCompanyName());
                SettingsRepo.getInstance().createSettings(settings);
            }else{
                SettingsRepo.getInstance().createSettings(settings);
            }
            if(file != null){
                renameFile(file, "company_logo");
            }
            updateLocate(locale,theme);
            GeneralUtils.showSuccessAlert(dialogTitle,successHeader,bundle.getString("success_settings_save"));
        }

    }


    private void updateLocate(int localeCode, int themeCode){
        Locale locale = GeneralUtils.getPrefLocale(localeCode);
        bundle = ResourceBundle.getBundle("lang",locale);
        Locale.setDefault(locale);
        setNodeLabels(bundle);
        pane.getScene().getStylesheets().clear();
        pane.getScene().getStylesheets().add(GeneralUtils.getCssFile("settings_screen.css",themeCode));
        comboLanguage.setValue(comboLanguage.getItems().get(localeCode));
        comboTheme.setValue(comboTheme.getItems().get(themeCode));
    }



    public static List<String> getCountries() {
        Set<String> set = new HashSet<>();
        // Get the default locale of the computer
        Locale[] countries = Locale.getAvailableLocales();
        for(Locale country: countries){
            if (!country.getDisplayCountry().isEmpty()){
                set.add(country.getDisplayCountry());
            }
        }
        List<String> sortedCountries = new ArrayList<>(set);
        Collections.sort(sortedCountries);
        return sortedCountries;
    }



    public static String getCurrencyByCountryName(String countryName) {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            if (countryName.equalsIgnoreCase(locale.getDisplayCountry())) {
                Currency currency = Currency.getInstance(locale);
                return  currency.getSymbol();
            }
        }
        return "";
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
                imgCompanyLogo.setImage(new Image(file.toURI().toURL().toExternalForm()));
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
        File newFile = new File(GeneralUtils.getImageDirectory().getPath(), newFileName + fileExtension);
        Path source = Paths.get(oldFile.getAbsolutePath());
        Path destination = Paths.get(newFile.getAbsolutePath());
        try {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteExistingFile(String fileName){
        File[] files = GeneralUtils.getImageDirectory().listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().startsWith(fileName)) {
                boolean deleted  = file.delete();
            }
        }
    }







}
