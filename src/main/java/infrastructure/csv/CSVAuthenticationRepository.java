/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.csv;

/**
 *
 * @author alfie
 */

import config.ApplicationConfig;
import domain.authentication.UserAccount;
import repository.AuthenticationRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CSVAuthenticationRepository
        implements AuthenticationRepository {

    private static final String HEADER =
            "Username,Password Hash,Role,Active";

    private final Path filePath;

    public CSVAuthenticationRepository(ApplicationConfig config) {
        Objects.requireNonNull(config, "Configuration cannot be null.");
        this.filePath = config.getAdminFilePath();
    }

    @Override
    public Optional<UserAccount> findByUsername(
            String username) throws IOException {

        validateText(username, "Username");

        return readAll().stream()
                .filter(account -> account.getUsername()
                        .equalsIgnoreCase(username.trim()))
                .findFirst();
    }

    @Override
    public boolean existsByUsername(
            String username) throws IOException {

        return findByUsername(username).isPresent();
    }

    @Override
    public void save(UserAccount account) throws IOException {

        validateAccount(account);

        if (existsByUsername(account.getUsername())) {
            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }

        List<UserAccount> accounts = readAll();
        accounts.add(account);

        writeAll(accounts);
    }

    @Override
    public void update(UserAccount account) throws IOException {

        validateAccount(account);

        List<UserAccount> accounts = readAll();
        boolean found = false;

        for (int i = 0; i < accounts.size(); i++) {

            if (accounts.get(i).getUsername()
                    .equalsIgnoreCase(account.getUsername())) {

                accounts.set(i, account);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException(
                    "User account not found."
            );
        }

        writeAll(accounts);
    }

    @Override
    public void updatePassword(
            String username,
            String newPasswordHash) throws IOException {

        validateText(newPasswordHash, "Password hash");

        UserAccount existing = findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User account not found."
                ));

        existing.updatePasswordHash(newPasswordHash);

        update(existing);
    }

    private List<UserAccount> readAll() throws IOException {

        List<UserAccount> accounts = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return accounts;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String header = reader.readLine();

            if (header == null) {
                return accounts;
            }

            validateHeader(header);

            String line;

            while ((line = reader.readLine()) != null) {

                if (!line.isBlank()) {
                    accounts.add(toUserAccount(parseCSV(line)));
                }
            }
        }

        return accounts;
    }

    private void writeAll(List<UserAccount> accounts)
            throws IOException {

        Path parent = filePath.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath, StandardCharsets.UTF_8)) {

            writer.write(HEADER);
            writer.newLine();

            for (UserAccount account : accounts) {

                List<String> fields = List.of(
                        account.getUsername(),
                        account.getPasswordHash(),
                        account.getRole(),
                        String.valueOf(account.isActive())
                );

                writer.write(fields.stream()
                        .map(this::escapeCSV)
                        .reduce((a, b) -> a + "," + b)
                        .orElse(""));

                writer.newLine();
            }
        }
    }

    private UserAccount toUserAccount(List<String> fields) {

        if (fields.size() != 4) {
            throw new IllegalArgumentException(
                    "Invalid authentication CSV row. "
                    + "Expected 4 columns, found " + fields.size()
            );
        }

        String activeValue = fields.get(3).trim();

        if (!activeValue.equalsIgnoreCase("true")
                && !activeValue.equalsIgnoreCase("false")) {

            throw new IllegalArgumentException(
                    "Invalid account active status: " + activeValue
            );
        }

        return new UserAccount(
                fields.get(0),
                fields.get(1),
                fields.get(2),
                Boolean.parseBoolean(activeValue)
        );
    }

    private List<String> parseCSV(String line) {

        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char current = line.charAt(i);

            if (current == '"') {

                if (insideQuotes
                        && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {

                    field.append('"');
                    i++;

                } else {
                    insideQuotes = !insideQuotes;
                }

            } else if (current == ',' && !insideQuotes) {

                fields.add(field.toString());
                field.setLength(0);

            } else {
                field.append(current);
            }
        }

        if (insideQuotes) {
            throw new IllegalArgumentException(
                    "Invalid CSV row: unclosed quotation mark."
            );
        }

        fields.add(field.toString());

        return fields;
    }

    private String escapeCSV(String value) {

        if (value.contains(",")
                || value.contains("\"")
                || value.contains("\n")
                || value.contains("\r")) {

            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }

    private void validateHeader(String header) throws IOException {

        String normalized = header.replace("\uFEFF", "").trim();

        if (!HEADER.equals(normalized)) {
            throw new IOException(
                    "Authentication CSV header is invalid."
            );
        }
    }

    private void validateAccount(UserAccount account) {

        Objects.requireNonNull(account, "Account cannot be null.");

        validateText(account.getUsername(), "Username");
        validateText(account.getPasswordHash(), "Password hash");
        validateText(account.getRole(), "Role");
    }

    private void validateText(String value, String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty."
            );
        }
    }
}

