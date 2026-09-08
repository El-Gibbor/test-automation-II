package com.xyzbank.listeners;

import com.xyzbank.utils.ConfigReader;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Writes target/allure-results/environment.properties once the suite finishes, so the Allure
 * report header shows what it actually ran against (browser, OS, base URL) without anyone
 * having to open the CI logs to find out.
 */
public class AllureEnvironmentWriter implements ISuiteListener {

    @Override
    public void onFinish(ISuite suite) {
        Properties env = new Properties();
        env.setProperty("Browser", ConfigReader.browser());
        env.setProperty("Headless", String.valueOf(ConfigReader.headless()));
        env.setProperty("Base.URL", ConfigReader.baseUrl());
        env.setProperty("OS", System.getProperty("os.name"));
        env.setProperty("Java.Version", System.getProperty("java.version"));

        Path resultsDir = Path.of("target", "allure-results");
        try {
            Files.createDirectories(resultsDir);
            try (OutputStream out = Files.newOutputStream(resultsDir.resolve("environment.properties"))) {
                env.store(out, "Generated automatically after suite execution");
            }
        } catch (IOException e) {
            // reporting metadata only - never fail the build over this
            System.err.println("Could not write Allure environment.properties: " + e.getMessage());
        }
    }
}
