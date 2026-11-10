/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation.view;

/**
 *
 * @author alfie
 */

import domain.authentication.UserAccount;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.Year;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainMenu extends JPanel {

    private final UserAccount userAccount;

    private final JTextField searchField;
    private final JButton searchButton;
    private final JList<String> searchResultList;
    private final DefaultListModel<String> searchResultModel;

    private final JComboBox<String> monthCombo;
    private final JComboBox<String> yearCombo;
    private final JButton computeButton;

    private final DefaultListModel<String> resultModel;
    private final JList<String> resultList;

    private final JButton employeeRecordsButton;
    private final JButton addAdminButton;
    private final JButton logoutButton;
    private final JButton exitButton;

    public MainMenu(UserAccount userAccount) {
        this.userAccount = userAccount;

        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel title = new JLabel("MotorPH Payroll System");
        title.setFont(title.getFont().deriveFont(24f));

        JLabel welcome = new JLabel(
                "Welcome, " + userAccount.getUsername()
                + " | Role: " + userAccount.getRole()
        );

        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.WEST);
        header.add(welcome, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Employee search
        searchField = new JTextField();
        searchButton = new JButton("Search");

        JPanel searchInput = new JPanel(new BorderLayout(5, 5));
        searchInput.add(searchField, BorderLayout.CENTER);
        searchInput.add(searchButton, BorderLayout.EAST);

        searchResultModel = new DefaultListModel<>();
        searchResultList = new JList<>(searchResultModel);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBorder(
                BorderFactory.createTitledBorder("Search Employee")
        );
        searchPanel.add(searchInput, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchResultList), BorderLayout.CENTER);

        add(searchPanel, BorderLayout.CENTER);

        // Payroll controls
        monthCombo = new JComboBox<>(new String[]{
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        });

        int currentYear = Year.now().getValue();
        String[] years = new String[5];

        for (int i = 0; i < years.length; i++) {
            years[i] = String.valueOf(currentYear - 2 + i);
        }

        yearCombo = new JComboBox<>(years);
        yearCombo.setSelectedItem(String.valueOf(currentYear));

        computeButton = new JButton("Compute Salary");
        computeButton.setEnabled(false);

        JPanel payrollControls = new JPanel(new GridLayout(3, 1, 5, 5));
        payrollControls.setBorder(
                BorderFactory.createTitledBorder("Payroll Computation")
        );

        payrollControls.add(monthCombo);
        payrollControls.add(yearCombo);
        payrollControls.add(computeButton);

        // Payroll results
        resultModel = new DefaultListModel<>();
        resultList = new JList<>(resultModel);

        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(
                BorderFactory.createTitledBorder("Salary Computation")
        );
        resultsPanel.setPreferredSize(new Dimension(250, 180));
        resultsPanel.add(new JScrollPane(resultList), BorderLayout.CENTER);

        // Navigation buttons
        employeeRecordsButton = new JButton("Employee Records");
        addAdminButton = new JButton("Add Admin");
        logoutButton = new JButton("Logout");
        exitButton = new JButton("Exit");

        JPanel actionsPanel = new JPanel(
                new GridLayout(4, 1, 5, 5)
        );

        actionsPanel.add(employeeRecordsButton);
        actionsPanel.add(addAdminButton);
        actionsPanel.add(logoutButton);
        actionsPanel.add(exitButton);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setPreferredSize(new Dimension(270, 0));
        rightPanel.add(payrollControls, BorderLayout.NORTH);
        rightPanel.add(resultsPanel, BorderLayout.CENTER);
        rightPanel.add(actionsPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JList<String> getSearchResultList() {
        return searchResultList;
    }

    public DefaultListModel<String> getSearchResultModel() {
        return searchResultModel;
    }

    public JComboBox<String> getMonthCombo() {
        return monthCombo;
    }

    public JComboBox<String> getYearCombo() {
        return yearCombo;
    }

    public JButton getComputeButton() {
        return computeButton;
    }

    public DefaultListModel<String> getResultModel() {
        return resultModel;
    }

    public JButton getEmployeeRecordsButton() {
        return employeeRecordsButton;
    }

    public JButton getAddAdminButton() {
        return addAdminButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}

