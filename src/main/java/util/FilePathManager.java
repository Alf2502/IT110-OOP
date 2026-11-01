/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author alfie
 */

/*
 *  Encapsulation: "baseDirectory" and "instance" are private.
 *  Singleton pattern: initialized() and retrieved through getInstance().
 *  Access Modifiers: The private constructor prevents other classes from creating manager instances directly.
 *                    public methods provide controlled access.
 */

import java.awt.Component;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JFileChooser;

public final class FilePathManager {

    private static FilePathManager instance;

    private final Path baseDirectory;

    // Private constructor prevents external instantiation.
    private FilePathManager(String baseDirectory) {
        if (baseDirectory == null || baseDirectory.isBlank()) {
            throw new IllegalArgumentException(
                    "Base directory cannot be empty.");
        }

        this.baseDirectory = Path.of(baseDirectory)
                .toAbsolutePath()
                .normalize();
    }

    // Initialize the Singleton once.
    public static synchronized FilePathManager initialize(
            String baseDirectory) {

        if (instance != null) {
            throw new IllegalStateException(
                    "FilePathManager is already initialized.");
        }

        instance = new FilePathManager(baseDirectory);
        return instance;
    }

    public static synchronized FilePathManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    "FilePathManager has not been initialized yet.");
        }

        return instance;
    }

    public String getAttendanceFilePath() {
        return baseDirectory.resolve(
                "MotorPH Attendance Record.csv").toString();
    }

    public String getEmployeeFilePath() {
        return baseDirectory.resolve(
                "MotorPH Employee Details.csv").toString();
    }

    public String getAdminFilePath() {
        return baseDirectory.resolve("admin.csv").toString();
    }

    public boolean fileExists(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return false;
        }

        return Files.isRegularFile(Path.of(filePath));
    }

    public String chooseFile(Component parent) {
        JFileChooser chooser = new JFileChooser(
                baseDirectory.toFile());

        int result = chooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile()
                    .getAbsolutePath();
        }

        return null;
    }
}
