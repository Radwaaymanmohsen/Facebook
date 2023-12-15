package mytry;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class AccountManager {

    private String userEmail;

    public AccountManager(String userEmail) {
        this.userEmail = userEmail;
    }

    public static List<String> readAccountsFromFile() {
        List<String> accountsList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                accountsList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accountsList;
    }

    public static void updateAccountsFile(List<String> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt", false))) {
            for (String account : accounts) {
                writer.write(account);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static boolean isAccountExists(String email, List<String> accounts) {
        for (String account : accounts) {
            String[] parts = account.split(":");
            if (parts.length >= 1 && parts[0].equals(email)) {
                return true;
            }
        }
        return false;
    }

    public static void addAccount(String email, String password, String name, String birthDate, String gender,
            List<String> accounts) {
        accounts.add(email + ":" + password + ":" + name + ":" + birthDate + ":" + gender);
    }

    public static boolean isAccountValid(String email, String password, List<String> accounts) {
        for (String account : accounts) {
            String[] parts = account.split(":");
            if (parts.length >= 2 && parts[0].equals(email) && parts[1].equals(password)) {
                return true;
            }
        }
        return false;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
