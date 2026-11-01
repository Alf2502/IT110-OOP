/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.SwingUtilities;
import util.FilePathManager;
import view.MainMenu;

/**
 *
 * @author alfie
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            FilePathManager.initialize("C:/Users/Alfie/Documents/NetBeansProjects/MotorPHCP2");
        
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
    }
}
