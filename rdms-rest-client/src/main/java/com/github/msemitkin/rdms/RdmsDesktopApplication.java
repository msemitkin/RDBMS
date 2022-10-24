package com.github.msemitkin.rdms;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RdmsDesktopApplication {
    public static void main(String[] args) {
        Application.launch(RdmsJavaFxApplication.class, args);
    }
}
