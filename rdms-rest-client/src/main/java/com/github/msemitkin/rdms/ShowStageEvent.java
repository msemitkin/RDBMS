package com.github.msemitkin.rdms;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

abstract class ShowStageEvent extends ApplicationEvent {

    protected ShowStageEvent(Stage source) {
        super(source);
    }

    @Override
    public Stage getSource() {
        return (Stage) source;
    }
}
