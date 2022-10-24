package com.github.msemitkin.rdms;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RdmsJavaFxApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new ShowDatabasesEvent(stage));
    }

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        applicationContext = new SpringApplicationBuilder(RdmsDesktopApplication.class).run(args);
    }

    @Override
    public void stop() {
        applicationContext.stop();
        Platform.exit();
    }
}
