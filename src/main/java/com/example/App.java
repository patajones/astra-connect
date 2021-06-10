package com.example;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {        
        String username = System.getenv("ASTRA_DB_CLIENT_ID");
        String password = System.getenv("ASTRA_DB_CLIENT_SECRET");        
        Path path = Paths.get("astra-creds.zip");
        System.out.println("ASTRA_DB_CLIENT_ID (length):"+username.length()+" - ASTRA_DB_CLIENT_SECRET (length):"+password.length());
        System.out.println("SecureConnectBundle:"+path.toAbsolutePath().toString());
        Config more = ConfigFactory.load().getConfig("datastax-java-driver.advanced.connection");
        System.out.println("datastax-java-driver.advanced.connection: "+more.toString());
        // Create the CqlSession object:
        try (CqlSession session = CqlSession.builder()
                .withCloudSecureConnectBundle(path)
                .withAuthCredentials(username, password)
                .build()) {
            // Select the release_version from the system.local table:
            ResultSet rs = session.execute("select release_version from system.local");
            Row row = rs.one();
            // Print the results of the CQL query to the console:
            if (row != null) {
                System.out.println(row.getString("release_version"));
            } else {
                System.out.println("An error occurred.");
            }
        }
        System.exit(0);

    }
}
