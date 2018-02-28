package group33.seg.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {

    private String host;
    private String user;
    private String password;

    public DatabaseConfig(String file) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.host = prop.getProperty("DB_HOST");
        this.user = prop.getProperty("DB_USER");
        this.password = prop.getProperty("DB_PASSWORD");
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
