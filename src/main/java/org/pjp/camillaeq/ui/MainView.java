package org.pjp.camillaeq.ui;

import java.util.Arrays;

import org.pjp.camillaeq.Application;
import org.pjp.camillaeq.model.BiquadSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

/**
 * The main CamillaEq (Vaadin) view class.
 */
@Route
public class MainView extends VerticalLayout implements AfterNavigationObserver {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainView.class);

    private Weq8Element weq8 = new Weq8Element();

    /**
     * Construct a new Vaadin view.
     * Build the initial UI state for the user accessing the application.
     *
     */
    public MainView() {
        weq8.addFilterChangedListener(l -> {
            BiquadSettings[] filterSettings = l.getFilterSettings();

            LOGGER.debug(Arrays.toString(l.getFilterSettings()));

            Application.setFilterSettings(filterSettings);
        });

        weq8.setWidthFull();

        add(weq8);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

    }
}
