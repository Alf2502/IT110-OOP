/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;

/**
 *
 * @author alfie
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import model.Employee;
import util.AttendanceFileHandler;
import util.EmployeeFileHandler;
import util.SalaryCalculator;

public class MainMenu extends JFrame {

    // View components and state
    private EmployeeListView employeeListView;
    private boolean isLoggedIn = false;
    private JPanel mainPanel;

    private final JButton loginButton;
    private final JButton empRecBtn;
    private final JButton addAdminBtn;
    private final JButton exitBtn;
    private final JTextField searchField;
    private final JButton searchButton;
    private final JButton computeButton;
    private final JComboBox<String> monthCombo;
    private final JComboBox<String> yearCombo;

    private final DefaultListModel<String> listModel;
    private final DefaultListModel<String> resultModel;
    private final JList<String> searchResultList;
    private final JList<String> resultList;

    // Model data and dependencies
    private List<Employee> allEmployees;

    private final EmployeeFileHandler employeeHandler;
    private final AttendanceFileHandler attendanceHandler;
    private final SalaryCalculator salaryCalculator;

    public MainMenu() {

        // Step 1: Authenticate the user.
        showLoginDialog();

        if (!isLoggedIn) {
            System.exit(0);
            throw new IllegalStateException("Login was cancelled.");
        }

        // Step 2: Configure the main window.
        setTitle("MotorPH Payroll System - Main Menu");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Step 3: Initialize dependencies.
        employeeHandler = new EmployeeFileHandler();
        attendanceHandler = new AttendanceFileHandler();
        salaryCalculator = new SalaryCalculator(attendanceHandler);

        // Load employee records.
        allEmployees = employeeHandler.readEmployees();

        // Step 4: Initialize search components.
        searchField = new JTextField();
        searchButton = new JButton("Search");

        listModel = new DefaultListModel<>();
        searchResultList = new JList<>(listModel);

        JScrollPane scrollPane =
                new JScrollPane(searchResultList);

        // Step 5: Initialize salary computation components.
        computeButton = new JButton("Compute Salary");
        computeButton.setEnabled(false);

        resultModel = new DefaultListModel<>();
        resultList = new JList<>(resultModel);
        resultList.setBorder(
                BorderFactory.createTitledBorder(
                        "Salary Computation"));

        monthCombo = new JComboBox<>(new String[]{
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
        });

        yearCombo = new JComboBox<>(new String[]{
            "2023", "2024", "2025"
        });

        // Step 6: Register event listeners.
        searchButton.addActionListener(
                e -> performSearch());

        searchField.addActionListener(
                e -> performSearch());

        searchResultList.addListSelectionListener(e -> {
            computeButton.setEnabled(
                    !searchResultList.isSelectionEmpty()
                    && !isNoResultsSelected());
        });

        computeButton.addActionListener(
                e -> computeSelectedEmployeeSalary());

        // Step 7: Build the search panel.
        JPanel topSearchPanel =
                new JPanel(new BorderLayout(5, 5));

        topSearchPanel.add(
                searchField, BorderLayout.CENTER);

        topSearchPanel.add(
                searchButton, BorderLayout.EAST);

        JPanel searchResultPanel =
                new JPanel(new BorderLayout(5, 5));

        searchResultPanel.add(
                scrollPane, BorderLayout.CENTER);

        JPanel searchPanel =
                new JPanel(new BorderLayout(10, 10));

        searchPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Search Employee"));

        searchPanel.add(
                topSearchPanel, BorderLayout.NORTH);

        searchPanel.add(
                searchResultPanel, BorderLayout.CENTER);

        // Step 8: Initialize navigation buttons.
        empRecBtn = new JButton("Employee Records");
        addAdminBtn = new JButton("   ");
        loginButton = new JButton("Logout");
        exitBtn = new JButton("Exit");

        // Step 9: Build the upper-right panel.
        JPanel topRightPanel = new JPanel();

        topRightPanel.setLayout(
                new BoxLayout(
                        topRightPanel, BoxLayout.Y_AXIS));

        for (JComponent component : new JComponent[]{
                empRecBtn, computeButton, monthCombo, yearCombo}) {

            component.setMaximumSize(
                    new Dimension(200, 30));

            component.setAlignmentX(
                    Component.CENTER_ALIGNMENT);

            topRightPanel.add(
                    Box.createVerticalStrut(10));

            topRightPanel.add(component);
        }

        // Step 10: Build the salary results panel.
        JPanel centerRightPanel = new JPanel();

        centerRightPanel.setLayout(
                new BoxLayout(
                        centerRightPanel, BoxLayout.Y_AXIS));

        resultList.setMaximumSize(
                new Dimension(250, 180));

        resultList.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        centerRightPanel.add(
                Box.createVerticalStrut(10));

        centerRightPanel.add(
                new JScrollPane(resultList));

        // Step 11: Build the bottom-right panel.
        JPanel bottomRightPanel = new JPanel();

        bottomRightPanel.setLayout(
                new BoxLayout(
                        bottomRightPanel, BoxLayout.Y_AXIS));

        for (JButton button : new JButton[]{
                addAdminBtn, loginButton, exitBtn}) {

            button.setMaximumSize(
                    new Dimension(200, 30));

            button.setAlignmentX(
                    Component.CENTER_ALIGNMENT);

            bottomRightPanel.add(
                    Box.createVerticalStrut(10));

            bottomRightPanel.add(button);
        }

        // Step 12: Assemble the right panel.
        JPanel rightPanel = new JPanel(
                new BorderLayout());

        rightPanel.add(
                topRightPanel, BorderLayout.NORTH);

        rightPanel.add(
                centerRightPanel, BorderLayout.CENTER);

        rightPanel.add(
                bottomRightPanel, BorderLayout.SOUTH);

        // Step 13: Assemble the main window.
        mainPanel = new JPanel(
                new BorderLayout(10, 10));

        mainPanel.add(
                rightPanel, BorderLayout.EAST);

        mainPanel.add(
                searchPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        // Step 14: Employee records window.
        empRecBtn.addActionListener(e -> {
            if (employeeListView == null
                    || !employeeListView.isDisplayable()) {

                employeeListView =
                        new EmployeeListView(
                                this, isLoggedIn);
            }

            employeeListView.setVisible(true);
        });

        // Step 15: Admin window.
        addAdminBtn.setEnabled(isLoggedIn);

        addAdminBtn.addActionListener(
                e -> new NewAdminForm(this).setVisible(true));

        // Step 16: Logout and exit.
        loginButton.addActionListener(
                e -> handleLogout());

        exitBtn.addActionListener(
                e -> System.exit(0));
    }

    // Searches employees using the employee number.
    private void performSearch() {

        String query = searchField.getText()
                .trim()
                .toLowerCase();

        listModel.clear();
        computeButton.setEnabled(false);
        resultModel.clear();

        if (query.isEmpty()) {
            return;
        }

        allEmployees.stream()
                .filter(employee ->
                        employee.getEmployeeNumber() != null
                        && employee.getEmployeeNumber()
                                .toLowerCase()
                                .contains(query))
                .forEach(employee ->
                        listModel.addElement(
                                employee.getEmployeeNumber()
                                + " - "
                                + employee.getFirstName()
                                + " "
                                + employee.getLastName()));

        if (listModel.isEmpty()) {
            listModel.addElement("No results found.");
        }
    }

    // Checks whether the placeholder is selected.
    private boolean isNoResultsSelected() {
        String selected = searchResultList.getSelectedValue();

        return selected == null
                || selected.equals("No results found.");
    }

    // Calculates salary for the selected employee.
    private void computeSelectedEmployeeSalary() {

        resultModel.clear();

        String selectedEmployee =
                searchResultList.getSelectedValue();

        if (selectedEmployee == null
                || selectedEmployee.equals("No results found.")) {
            return;
        }

        String[] selectedParts =
                selectedEmployee.split(" - ", 2);

        if (selectedParts.length < 2) {
            return;
        }

        String employeeNumber = selectedParts[0].trim();

        String month = (String) monthCombo.getSelectedItem();

        String monthTwoDigit = String.format(
                "%02d", monthCombo.getSelectedIndex() + 1);

        String year = (String) yearCombo.getSelectedItem();

        if (month == null || year == null) {
            return;
        }

        Employee employee = allEmployees.stream()
                .filter(item ->
                        employeeNumber.equals(
                                item.getEmployeeNumber()))
                .findFirst()
                .orElse(null);

        if (employee == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Employee record not found.",
                    "Search Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double totalHours =
                    salaryCalculator.calculateMonthlyHours(
                            employeeNumber,
                            year,
                            monthTwoDigit);

            double salary =
                    salaryCalculator.calculateSalary(
                            employee, totalHours);

            double fixedCompensation = salary > 0
                    ? salaryCalculator
                            .calculateTotalWithAllowances(employee)
                    : 0.0;

            double totalPay = salary + fixedCompensation;

            resultModel.addElement(
                    "Employee #" + employeeNumber);

            resultModel.addElement(
                    "Month: " + month + " " + year);

            resultModel.addElement(
                    String.format(
                            "Total Hours: %.2f", totalHours));

            resultModel.addElement(
                    String.format(
                            "Hourly Salary: ₱%,.2f", salary));

            resultModel.addElement(
                    String.format(
                            "Fixed Allowances: ₱%,.2f",
                            fixedCompensation));

            resultModel.addElement(
                    String.format(
                            "TOTAL: ₱%,.2f", totalPay));

            resultModel.addElement(
                    "------------------------------");

        } catch (IllegalArgumentException
                | IllegalStateException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to calculate salary:\n"
                    + exception.getMessage(),
                    "Payroll Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Displays the login dialog.
    private void showLoginDialog() {

        JDialog loginDialog =
                new JDialog(this, "Login", true);

        loginDialog.setSize(300, 200);
        loginDialog.setLocationRelativeTo(null);

        LoginPanel loginPanel = new LoginPanel(() -> {
            isLoggedIn = true;
            loginDialog.dispose();
        });

        loginDialog.add(loginPanel);
        loginDialog.setVisible(true);
    }

    // Handles logout and subsequent login.
    private void handleLogout() {

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        isLoggedIn = false;
        loginButton.setText("Login");
        addAdminBtn.setEnabled(false);

        if (employeeListView != null
                && employeeListView.isDisplayable()) {

            employeeListView
                    .setProtectedButtonsEnabled(false);
        }

        showLoginDialog();

        if (!isLoggedIn) {
            dispose();
            System.exit(0);
        } else {
            loginButton.setText("Logout");
            enableProtectedFeatures(true);
        }
    }

    // Enables or disables protected features.
    private void enableProtectedFeatures(boolean enable) {

        addAdminBtn.setEnabled(enable);

        if (employeeListView != null
                && employeeListView.isDisplayable()) {

            employeeListView
                    .setProtectedButtonsEnabled(enable);
        }
    }
}
