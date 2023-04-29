package com.huawei.huaweicli.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

import com.google.common.hash.Hashing;
import com.huawei.huaweicli.HuaweiCLI;
import com.huawei.huaweicli.StaticContext;
import com.huawei.huaweicli.utils.RequestHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    @FXML
    private TextField loginField;
    @FXML
    private TextField pswdField;
    @FXML
    private Button loginBtn;
    @FXML
    private Text infoText;

    private void loginAction(MouseEvent event) {
        String protectedPswd = Hashing.sha256()
                .hashString(pswdField.getText(), StandardCharsets.UTF_8)
                .toString();
        String session = RequestHandler.getSession(loginField.getText(), protectedPswd);
        if (session != null) {
            StaticContext.initCreds(loginField.getText(), protectedPswd);
            StaticContext.changeSession(session.substring(0, session.length() - 1));
            infoText.setFill(Color.GREEN);
            infoText.setText("Success!");
            try {
                Scene dataScene = new Scene(FXMLLoader.load(Objects.requireNonNull(HuaweiCLI.class.getResource("data_view.fxml"))));
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(dataScene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        infoText.setFill(Color.RED);
        infoText.setText("Failure!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginBtn.setOnMouseClicked(this::loginAction);
    }
}