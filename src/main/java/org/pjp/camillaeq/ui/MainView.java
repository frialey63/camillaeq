package org.pjp.camillaeq.ui;

import java.util.Arrays;

import org.pjp.camillaeq.Application;
import org.pjp.camillaeq.manager.PresetsManager;
import org.pjp.camillaeq.model.BiquadSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

    private static final int NUM_PRESETS = 8;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainView.class);

    private final PresetsManager presetsManager;

    private final Weq8Element weq8 = new Weq8Element();

    private final Button[] presetButton = new Button[NUM_PRESETS];

    private BiquadSettings[] filterSettings;

    /**
     * Construct a new Vaadin view.
     * Build the initial UI state for the user accessing the application.
     * @param presetsManager The manager for the filter presets
     *
     */
    public MainView(PresetsManager presetsManager) {
        this.presetsManager = presetsManager;

        getElement().getStyle().set("background-color", "#202020");
        setHeightFull();

        weq8.addFilterChangedListener(l -> {
            filterSettings = l.getFilterSettings();
            LOGGER.debug("filterChanged.filterSettings = " + Arrays.toString(filterSettings));
            Application.setFilterSettings(filterSettings);
        });

        weq8.setWidthFull();

        HorizontalLayout buttons = new HorizontalLayout(JustifyContentMode.EVENLY);

        for (int i = 0; i < NUM_PRESETS; i++) {
            final int num = i;

            Button preset = presetButton[i] = new Button(String.format("Preset #%d", i));
            preset.getClassNames().add("my-button");
            preset.addClickListener(l -> {
                if (l.isCtrlKey()) {
                    LOGGER.info("save.filterSettings = " + Arrays.toString(filterSettings));
                    presetsManager.save(num, filterSettings);
                    preset.getElement().getStyle().setColor("yellow");
                } else if (l.isAltKey()) {
                    LOGGER.info("clear.filterSettings = " + Arrays.toString(filterSettings));
                    presetsManager.clear(num);
                    preset.getElement().getStyle().setColor("white");
                } else {
                    filterSettings = presetsManager.load(num);
                    LOGGER.info("load.filterSettings = " + Arrays.toString(filterSettings));
                    if (filterSettings != null) {
                        weq8.setFilters(filterSettings);
                    }
                }
            });
            preset.setTooltipText("Ctrl-click to save, click to load, Alt-click to clear");

            buttons.add(preset);
        }

        buttons.setWidthFull();

        add(weq8, buttons);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        // make an initial filter setting here to generate a state event
        weq8.setDefaultFilters();

        for (int i = 0; i < NUM_PRESETS; i++) {
            final int num = i;

            filterSettings = presetsManager.load(num);
            LOGGER.info("init.filterSettings = " + Arrays.toString(filterSettings));
            if (filterSettings != null) {
                presetButton[i].getElement().getStyle().setColor("yellow");
            }
        }
    }

}
