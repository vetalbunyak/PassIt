package vt.passit.Modules;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE_NAME = "config.properties";

    static {
        try(InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)){
            if(input == null){
                String errorMessage = "Помилка: Конфігураційний файл '" + CONFIG_FILE_NAME + "' не знайдено в classpath!";
                System.err.println(errorMessage);
                throw new RuntimeException(errorMessage);
            }
            properties.load(input);
            System.out.println("Конфігурація успішно завантажена з " + CONFIG_FILE_NAME);
            if (properties.getProperty("db.url") == null ||
                    properties.getProperty("db.user") == null ||
                    properties.getProperty("db.password") == null) {
                String missingPropMessage = "Помилка: Один або кілька необхідних параметрів бази даних відсутні у " + CONFIG_FILE_NAME;
                System.err.println(missingPropMessage);
                throw new RuntimeException(missingPropMessage);
            }

        } catch(IOException e){
            String ioErrorMessage = "Помилка читання конфігураційного файлу '" + CONFIG_FILE_NAME + "': " + e.getMessage();
            System.err.println(ioErrorMessage);
            e.printStackTrace();
            throw new RuntimeException(ioErrorMessage, e);
        }
    }

    private ConfigManager(){

    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Отримує URL бази даних.
     * @return URL бази даних.
     */
    public static String getDbUrl() {
        return getProperty("db.url");
    }

    /**
     * Отримує ім'я користувача бази даних.
     * @return Ім'я користувача бази даних.
     */
    public static String getDbUser() {
        return getProperty("db.user");
    }

    /**
     * Отримує пароль бази даних.
     * @return Пароль бази даних.
     */
    public static String getDbPassword() {
        return getProperty("db.password");
    }

    public static void main(String[] args) {
        System.out.println("Testing ConfigManager:");
        System.out.println("DB URL: " + ConfigManager.getDbUrl());
        System.out.println("DB User: " + ConfigManager.getDbUser());
        System.out.println("DB Password: " + ConfigManager.getDbPassword());
    }
}
