package com.mwinensoaa.storemanager.controller;


import com.mwinensoaa.storemanager.entities.Settings;
import com.mwinensoaa.storemanager.network.server.ServerBroadcaster;
import com.mwinensoaa.storemanager.repositories.CashierRepo;
import com.mwinensoaa.storemanager.repositories.SettingsRepo;
import com.mwinensoaa.storemanager.utils.GeneralUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import com.mwinensoaa.storemanager.entities.Cashier;


public class LoginScreenController implements Initializable {

    @FXML private FontAwesomeIconView lbClose;
    @FXML private Button btnSignIn;
    @FXML private AnchorPane root;
    @FXML private PasswordField tfPassword;
    @FXML private ImageView imgAppLogo;
    @FXML private ImageView imgCompanyLogo;
    @FXML private TextField tfUserName;
    @FXML private Label lbUsername;
    @FXML private Label lbPassword;
    @FXML private Label lbSignIn;
          private ResourceBundle bundle;


    private final Stage stage = new Stage();
    private List<Cashier> cashierList;
    private String dialogTitle;
    private String errorHeader;
    private String successHeader;
    private Settings settings;
    private int currentThemeCode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbClose.setOnMouseClicked(event -> System.exit(0));
        GeneralUtils.initControlButtonsAndWindoDrag(root);
        btnSignIn.setOnAction(event -> login());
        settings = SettingsRepo.getInstance().getDefaultLanguage();
        imgAppLogo.setImage(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getClass().getResource("/images/logo4.png")).toExternalForm()))));
        cashierList = CashierRepo.getInstance().getCashiers();;
        cashierList.forEach(it -> {
            System.out.println("Cashier name: "+it.getCashierId()+ " Password: "+it.getCashierPassword());
        });
        setImgCompanyLogo();
        initI18N();


        //new Thread(new ServerBroadcaster()).start();
    }


    private void initI18N(){
        Locale locale = GeneralUtils.getPrefLocale(settings.getLocale());
        Locale.setDefault(locale);
        bundle = ResourceBundle.getBundle("lang", locale);
        if(bundle != null){
            currentThemeCode = settings.getTheme();
            dialogTitle = bundle.getString("dialog_title");
            errorHeader = bundle.getString("txt_dialog_error_header");
            successHeader = bundle.getString("txt_dialog_success_header");
            lbUsername.setText(bundle.getString("lbUsername"));
            lbPassword.setText(bundle.getString("lbPassword"));
            lbSignIn.setText(bundle.getString("txtSignIn"));
            btnSignIn.setText(bundle.getString("txtSignIn"));
        }
    }


    private void setImgCompanyLogo(){
        File file = GeneralUtils.getLogo();
        if(file != null){
            try {
                imgCompanyLogo.setImage(new Image(file.toURI().toURL().toExternalForm()));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private boolean isValidatedField(){
        if (tfUserName.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_enter_username"));
            return false;
        }else if(tfPassword.getText().isEmpty()){
            GeneralUtils.showErrorAlert(dialogTitle,errorHeader,bundle.getString("error_enter_password"));
            return false;
        }
       return true;
    }



    private void login(){
        if(!cashierList.isEmpty()) {
            if (isValidatedField()) {
                String cashierId = tfUserName.getText().trim();
                String password = tfPassword.getText().trim();

                if(isAuthenticated(cashierId, password)) {
                    Cashier cashier = getCashier(cashierId);
                    try {
                        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(LoginScreenController.class.getResource("/fxml/dashboard_screen.fxml")));
                        String css = GeneralUtils.getCssFile("dashboard_screen.css",currentThemeCode);
                        Parent parent = loader.load();
                        HomeScreenController controller = loader.getController();
                        assert cashier != null;
                        controller.setCashierImage(cashier,bundle,settings.getLocale(),settings.getTheme());
                        loadScreen(stage, parent, css);
                    } catch (IOException ignore) {
                    }
                    root.getScene().getWindow().hide();
                } else {
                    GeneralUtils.showErrorAlert(dialogTitle,successHeader,bundle.getString("error_wrong_credentials"));
                }
            }
        }else{
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(LoginScreenController.class.getResource("/fxml/dashboard_screen.fxml")));
                Parent parent = loader.load();
                String css = GeneralUtils.getCssFile("dashboard_screen.css", currentThemeCode);
                HomeScreenController controller = loader.getController();
                controller.initI18N(bundle,settings.getLocale(), settings.getTheme());
                 loadScreen(stage, parent, css);
            } catch (IOException ignore) {
            }
            root.getScene().getWindow().hide();
        }
    }


    private boolean isAuthenticated(String userName, String password) {
        for (Cashier cashier : cashierList) {
            if (cashier.getCashierId().equals(userName) && cashier.getCashierPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }


    private Cashier getCashier(String cashierId) {
        for (Cashier cashier : cashierList) {
            if (cashier.getCashierId().equals(cashierId)) {
                return cashier;
            }
        }
        return null;
    }

    public static double getScreenWidth(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int tempWidth = screenSize.width;
        return (int) (0.8 * tempWidth);
    }

    public static double getScreenHeight(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int height = screenSize.height;
        return (int) (0.7 * height);
    }


    public static void loadScreen(Stage primaryStage, Parent parent, String cssFile){
        if(primaryStage.getScene() == null){
            try{
                Scene scene = new Scene(parent);
                primaryStage.setScene(scene);
                scene.getStylesheets().add(cssFile);
            }catch (Exception ignore){}
            if(primaryStage.getStyle() == StageStyle.DECORATED) {
                primaryStage.initStyle(StageStyle.UNDECORATED);
            }
            primaryStage.setResizable(false);
            primaryStage.setWidth(getScreenWidth());
            primaryStage.setHeight(getScreenHeight());
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(LoginScreenController.class.getResource("/images/logo4.png")).toExternalForm()))));
            primaryStage.show();
        }else if(primaryStage.isIconified()){
            Toolkit.getDefaultToolkit().beep();
            primaryStage.setIconified(false);
        }else {
            Toolkit.getDefaultToolkit().beep();
            primaryStage.toFront();
        }
        primaryStage.setOnHiding(event -> primaryStage.setScene(null));

    }






}
