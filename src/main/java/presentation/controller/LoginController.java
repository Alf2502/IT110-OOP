/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation.controller;

/**
 *
 * @author alfie
 */

import domain.authentication.UserAccount;
import java.io.IOException;
import presentation.view.LoginPanel;
import service.AuthenticationService;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class LoginController {

    private final LoginPanel loginPanel;
    private final AuthenticationService authenticationService;
    private final Consumer<UserAccount> onLoginSuccess;

    public LoginController(
            LoginPanel loginPanel,
            AuthenticationService authenticationService,
            Consumer<UserAccount> onLoginSuccess) {

        this.loginPanel = Objects.requireNonNull(loginPanel);
        this.authenticationService =
                Objects.requireNonNull(authenticationService);
        this.onLoginSuccess = Objects.requireNonNull(onLoginSuccess);

        initializeController();
    }

    private void initializeController() {
        loginPanel.addLoginListener(event -> {
            try {
                handleLogin();
            } catch (IOException ex) {
                System.getLogger(LoginController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    }

    private void handleLogin() throws IOException {

        loginPanel.clearMessage();

        String username = loginPanel.getUsername();
        char[] password = loginPanel.getPassword();

        if (username.isBlank() || password.length == 0) {
            loginPanel.showMessage(
                    "Please enter your username and password."
            );
            java.util.Arrays.fill(password, '\0');
            loginPanel.clearPassword();
            return;
        }

        loginPanel.setLoginEnabled(false);

        try {
            Optional<UserAccount> authenticatedUser =
                    authenticationService.authenticate(
                            username,
                            password
                    );

            if (authenticatedUser.isPresent()) {

                loginPanel.clearPassword();
                loginPanel.clearMessage();

                onLoginSuccess.accept(authenticatedUser.get());

            } else {

                loginPanel.showMessage(
                        "Invalid username or password."
                );

                loginPanel.clearPassword();
            }

        } catch (IOException exception) {

            loginPanel.showMessage(
            "Unable to access authentication data."
                 );

            loginPanel.clearPassword();

        } catch (RuntimeException exception) {

            loginPanel.showMessage(
                    "Unable to authenticate. Please try again."
            );

            loginPanel.clearPassword();

        } finally {

            java.util.Arrays.fill(password, '\0');
            loginPanel.setLoginEnabled(true);
        }
    }
}

