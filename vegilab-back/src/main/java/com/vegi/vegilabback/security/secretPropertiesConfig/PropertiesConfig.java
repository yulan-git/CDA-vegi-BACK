package com.vegi.vegilabback.security.secretPropertiesConfig;

import org.springframework.stereotype.Component;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesConfig {
    public void writeToSecretProperties(SecretConfig secretConfig) {

        try {
            Properties properties = new Properties();
            properties.setProperty(secretConfig.getRole().toString(), secretConfig.getUsername());

            String filePath = "src/main/resources/secret.properties";
            File file = new File(filePath);
            OutputStream outputStream = new FileOutputStream( file );
            DefaultPropertiesPersister defaultPropertiesPersister = new DefaultPropertiesPersister();
            defaultPropertiesPersister.store(properties, outputStream, "Comment");
        } catch (Exception e ) {
            System.out.println("An error during writing to secret.properties");
            e.printStackTrace();
        }
    }
}
