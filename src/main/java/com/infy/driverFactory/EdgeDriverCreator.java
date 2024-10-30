package com.infy.driverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EdgeDriverCreator {
    private static final Logger logger = LoggerFactory.getLogger(EdgeDriverCreator.class);

    public WebDriver create() {
        try {
            // Set up the EdgeDriver using WebDriverManager
            WebDriverManager.edgedriver().setup();
            logger.info("EdgeDriver has been set up successfully.");

            // Set EdgeOptions for the driver
            EdgeOptions options = new EdgeOptions();
            // Add any desired options here
            options.addArguments("--start-maximized"); // Example option

            // Create and return the EdgeDriver instance
            WebDriver driver = new EdgeDriver(options);
            logger.info("EdgeDriver instance created successfully.");
            return driver;

        } catch (Exception e) {
            logger.error("Failed to create EdgeDriver: {}", e.getMessage());
            throw new RuntimeException("Could not initialize EdgeDriver", e);
        }
    }
}