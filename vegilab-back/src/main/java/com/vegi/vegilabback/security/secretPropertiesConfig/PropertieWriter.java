package com.vegi.vegilabback.security.secretPropertiesConfig;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

public class PropertieWriter {

    public void writeToProperties(SecretConfig secretConfig) {
        FileReader reader = null;
        FileWriter writer = null;

        String filePath = "src/main/resources/secret.properties";
        //byte[] bytesFile = Files.readAllBytes(Paths.get(filePath));

        //File file = new File("application.properties");

        try {
            reader = new FileReader(filePath);
            writer = new FileWriter(filePath);

            Properties p = new Properties();
            p.load(reader);

            p.setProperty(secretConfig.getRole().toString(),secretConfig.getUsername());
            p.store(writer,"write a file");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
