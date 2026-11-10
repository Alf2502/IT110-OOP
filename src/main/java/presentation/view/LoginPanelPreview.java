/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation.view;

/**
 *
 * @author alfie
 */

// Delete this class upon completion. -Alfie

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class LoginPanelPreview {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("MotorPH Payroll System");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new LoginPanel());
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setMinimumSize(new java.awt.Dimension(700, 500));
            frame.setVisible(true);

        });
    }
}

