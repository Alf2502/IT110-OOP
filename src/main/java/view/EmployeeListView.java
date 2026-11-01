/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author alfie
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class EmployeeListView extends JDialog {

    private final JTable employeeTable;
    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JTextField searchField;

    private final JButton editEmployeeButton;
    private final JButton viewDetailsButton;
    private final JButton deleteEmployeeButton;
    private final JButton newEmployeeButton;

    private final JFrame parentFrame;
    private final EmployeeFileHandler employeeHandler;

    private boolean loggedIn;

    public EmployeeListView(JFrame parent, boolean isLoggedIn) {
        super(parent, "Employee List", true);

        this.parentFrame = parent;
        this.loggedIn = isLoggedIn;

        // Reuse one handler throughout this window.
        this.employeeHandler = new EmployeeFileHandler();

        setSize(860, 660);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Table setup.
        String[] columnNames = {
            "Employee Number",
            "Last Name",
            "First Name",
            "SSS Number",
            "PhilHealth Number",
            "TIN",
            "Pag-IBIG Number"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);

        sorter = new TableRowSorter<>(tableModel);
        employeeTable.setRowSorter(sorter);

        add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        // Buttons.
        viewDetailsButton = new JButton("View Details");
        editEmployeeButton = new JButton("Edit Employee");
        deleteEmployeeButton = new JButton("Delete Employee");
        newEmployeeButton = new JButton("Add New Employee");

        viewDetailsButton.addActionListener(
                e -> showEmployeeDetails());

        editEmployeeButton.addActionListener(
                e -> editSelectedEmployee());

        deleteEmployeeButton.addActionListener(
                e -> deleteSelectedEmployee());

        newEmployeeButton.addActionListener(e -> {
            NewEmployeeForm form = new NewEmployeeForm(
                    parentFrame,
                    employeeHandler,
                    this::loadEmployeeData);

            form.setVisible(true);
        });

        JPanel bottomPanel =
                new JPanel(new FlowLayout(FlowLayout.RIGHT));

        bottomPanel.add(viewDetailsButton);
        bottomPanel.add(editEmployeeButton);
        bottomPanel.add(newEmployeeButton);
        bottomPanel.add(deleteEmployeeButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Search and refresh.
        JPanel topPanel = new JPanel(
                new BorderLayout(5, 5));

        searchField = new JTextField();
        JButton refreshButton = new JButton("Refresh");

        topPanel.add(new JLabel("Search:"),
                BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(topPanel, BorderLayout.NORTH);

        searchField.getDocument().addDocumentListener(
                new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String text = searchField.getText().trim();

                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(
                            RowFilter.regexFilter(
                                    "(?i)" + Pattern.quote(text)));
                }
            }
        });

        refreshButton.addActionListener(
                e -> loadEmployeeData());

        employeeTable.getSelectionModel()
                .addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        updateButtonStates();
                    }
                });

        // Initial state.
        loadEmployeeData();
        updateButtonStates();
    }

    // Enable or disable protected actions.
    public void setProtectedButtonsEnabled(boolean enable) {
        this.loggedIn = enable;
        updateButtonStates();
    }

    private void updateButtonStates() {
        boolean selected =
                employeeTable.getSelectedRow() != -1;

        viewDetailsButton.setEnabled(
                loggedIn && selected);

        editEmployeeButton.setEnabled(
                loggedIn && selected);

        deleteEmployeeButton.setEnabled(
                loggedIn && selected);

        newEmployeeButton.setEnabled(loggedIn);
    }

    // Display the selected employee's details.
    private void showEmployeeDetails() {
        Employee employee = getSelectedEmployee();

        if (employee == null) {
            return;
        }

        AttendanceFileHandler attendanceHandler =
                new AttendanceFileHandler();

        EmployeeDetailView detailsView =
                new EmployeeDetailView(
                        parentFrame,
                        employee,
                        attendanceHandler);

        detailsView.setVisible(true);
    }

    // Open the existing employee-editing form.
    private void editSelectedEmployee() {
        Employee employee = getSelectedEmployee();

        if (employee == null) {
            return;
        }

        EditEmployeeForm form = new EditEmployeeForm(
                parentFrame,
                employee,
                employeeHandler,
                this::loadEmployeeData);

        form.setVisible(true);
    }

    // Delete the selected employee.
    private void deleteSelectedEmployee() {
        Employee employee = getSelectedEmployee();

        if (employee == null) {
            return;
        }

        String employeeNumber =
                employee.getEmployeeNumber();

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete Employee #"
                        + employeeNumber + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean success =
                    employeeHandler.deleteEmployeeByNumber(
                            employeeNumber);

            if (success) {
                JOptionPane.showMessageDialog(
                        this, "Employee deleted successfully.");

                loadEmployeeData();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Employee could not be deleted.",
                        "Delete Error",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (RuntimeException e) {
            showFileError("deleting the employee", e);
        }
    }

    // Retrieve the selected employee using the model row.
    private Employee getSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();

        if (selectedRow == -1) {
            return null;
        }

        int modelRow =
                employeeTable.convertRowIndexToModel(
                        selectedRow);

        String employeeNumber =
                (String) tableModel.getValueAt(modelRow, 0);

        try {
            return employeeHandler.readEmployees().stream()
                    .filter(employee ->
                            employeeNumber.equals(
                                    employee.getEmployeeNumber()))
                    .findFirst()
                    .orElseGet(() -> {
                        JOptionPane.showMessageDialog(
                                this,
                                "Employee record not found.",
                                "Record Not Found",
                                JOptionPane.WARNING_MESSAGE);
                        return null;
                    });

        } catch (RuntimeException e) {
            showFileError("loading employee details", e);
            return null;
        }
    }

    // Reload employee data from the CSV file.
    private void loadEmployeeData() {
        try {
            List<Employee> employees =
                    employeeHandler.readEmployees();

            tableModel.setRowCount(0);

            for (Employee employee : employees) {
                tableModel.addRow(new Object[]{
                    employee.getEmployeeNumber(),
                    employee.getLastName(),
                    employee.getFirstName(),
                    employee.getSssNumber(),
                    employee.getPhilHealthNumber(),
                    employee.getTin(),
                    employee.getPagIbigNumber()
                });
            }

            updateButtonStates();

        } catch (RuntimeException e) {
            showFileError("loading employee records", e);
        }
    }

    private void showFileError(
            String operation, RuntimeException exception) {

        JOptionPane.showMessageDialog(
                this,
                "An error occurred while " + operation + ".\n"
                        + exception.getMessage(),
                "Employee File Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
