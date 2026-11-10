/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation.view;

/**
 *
 * @author alfie
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {

    private static final Color BACKGROUND = new Color(11, 19, 43);
    private static final Color ACCENT = new Color(249, 115, 22);
    private static final Color TEXT = new Color(255, 255, 255);
    private static final Color MUTED = new Color(180, 190, 210);

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JLabel messageLabel;

    public LoginPanel() {

        setLayout(new GridBagLayout());
        setBackground(BACKGROUND);

        JPanel loginCard = new JPanel(new GridBagLayout());
        loginCard.setBackground(new Color(20, 31, 57));
        loginCard.setBorder(new EmptyBorder(40, 45, 40, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel logoLabel = new JLabel("MotorPH");
        logoLabel.setForeground(ACCENT);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 0;
        loginCard.add(logoLabel, gbc);

        JLabel subtitleLabel = new JLabel("PAYROLL MANAGEMENT SYSTEM");
        subtitleLabel.setForeground(MUTED);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 0);
        loginCard.add(subtitleLabel, gbc);

        JLabel usernameLabel = createLabel("Username");

        gbc.gridy = 2;
        gbc.insets = new Insets(8, 0, 5, 0);
        loginCard.add(usernameLabel, gbc);

        usernameField = new JTextField(22);
        styleTextField(usernameField);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 12, 0);
        loginCard.add(usernameField, gbc);

        JLabel passwordLabel = createLabel("Password");

        gbc.gridy = 4;
        gbc.insets = new Insets(8, 0, 5, 0);
        loginCard.add(passwordLabel, gbc);

        passwordField = new JPasswordField(22);
        styleTextField(passwordField);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 20, 0);
        loginCard.add(passwordField, gbc);

        loginButton = new JButton("SIGN IN");
        loginButton.setBackground(ACCENT);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(280, 42));

        gbc.gridy = 6;
        gbc.insets = new Insets(8, 0, 8, 0);
        loginCard.add(loginButton, gbc);

        messageLabel = new JLabel(" ");
        messageLabel.setForeground(new Color(255, 110, 110));
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 7;
        gbc.insets = new Insets(5, 0, 0, 0);
        loginCard.add(messageLabel, gbc);

        add(loginCard);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBackground(new Color(240, 243, 248));
        field.setForeground(BACKGROUND);
        field.setCaretColor(BACKGROUND);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 85, 110)),
                new EmptyBorder(10, 12, 10, 12)
        ));
        field.setPreferredSize(new Dimension(280, 42));
    }

    // Controller integration methods

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void setLoginEnabled(boolean enabled) {
        loginButton.setEnabled(enabled);
    }

    public void clearPassword() {
        passwordField.setText("");
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }

    public void clearMessage() {
        messageLabel.setText(" ");
    }
}

