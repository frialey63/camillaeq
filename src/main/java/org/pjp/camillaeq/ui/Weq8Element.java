package org.pjp.camillaeq.ui;

import org.pjp.camillaeq.model.BiquadSettings;
import org.pjp.camillaeq.model.BiquadType;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.shared.Registration;

import elemental.json.impl.JreJsonArray;
import elemental.json.impl.JreJsonObject;

/**
 * This class is the Vaadin Flow wrapper for the Weq8 Lit template.
 *
 */
@Tag("weq8-element")
@JsModule("custom/weq8-element.js")
public class Weq8Element extends Component implements HasSize, HasTheme {

    private static final long serialVersionUID = 1L;

    /**
     * The number of filters, fixed by the Weq8 component.
     */
    public static final int NUM_FILTERS = 8;

    /**
     * No operation filter in Weq8.
     */
    public static final BiquadSettings NOOP_FILTER = new BiquadSettings(BiquadType.NOOP, 30, 0.70, 0, false);

    /**
     * Default settings for filter 0 in Weq8.
     */
    public static final BiquadSettings DEFAULT_FILTER_0 = new BiquadSettings(BiquadType.LOW_SHELF, 30, 0.70, 0, false);

    /**
     * Default settings for filter 1 in Weq8.
     */
    public static final BiquadSettings DEFAULT_FILTER_1 = new BiquadSettings(BiquadType.PEAKING, 200, 0.70, 0, false);

    /**
     * Default settings for filter 2 in Weq8.
     */
    public static final BiquadSettings DEFAULT_FILTER_2 = new BiquadSettings(BiquadType.PEAKING, 1000, 0.70, 0, false);

    /**
     * Default settings for filter 3 in Weq8.
     */
    public static final BiquadSettings DEFAULT_FILTER_3 = new BiquadSettings(BiquadType.HIGH_SHELF, 5000, 0.70, 0, false);

    /**
     * This class is the event which is emitted by the Weq8 component when a filter is changed.
     * @param <T> The generic type
     */
    @DomEvent("filter-changed")
    public static class FilterChangedEvent<T extends Weq8Element> extends ComponentEvent<Weq8Element> {

        private static final long serialVersionUID = 1L;

        private final BiquadSettings[] filterSettings;

        /**
         * When a change occurs to a filter in the Weq8 component an instance of this class is created and the biquad filter settings are extracted from the event data and stored
         *
         * @param source The source element for the event
         * @param fromClient True if from client
         * @param detailsArray The data for the event in the form of a JSON array
         */
        public FilterChangedEvent(Weq8Element source, boolean fromClient, @EventData("event.detail") JreJsonArray detailsArray) {
            super(source, fromClient);

            BiquadSettings[] tempSettings = new BiquadSettings[detailsArray.length()];

            for (int i = 0; i < tempSettings.length; i++) {
                JreJsonObject v = (JreJsonObject) detailsArray.get(i);
                tempSettings[i] = new BiquadSettings(BiquadType.parse(v.getString("type")), v.getNumber("frequency"), v.getNumber("Q"), v.getNumber("gain"), v.getBoolean("bypass"));
            }

            filterSettings = tempSettings;
        }

        /**
         * @return The biquad filter settings passed in this event which have been converted from JSON to the Java model
         */
        public BiquadSettings[] getFilterSettings() {
            return filterSettings;
        }
    }

    /**
     * Add a listener for the FilterChangedEvent to the Weq8 component.
     * @param listener The listener for the FilterChangedEvent
     * @return the Registration which can be used to later unregister the listener
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Registration addFilterChangedListener(ComponentEventListener<FilterChangedEvent<Weq8Element>> listener) {
        return addListener(FilterChangedEvent.class, (ComponentEventListener) listener);
    }

    /**
     * Remove a listener for the FilterChangedEvent from the Weq8 component.
     * @param registration the Registration of the listener to remove
     */
    public void removeFilterChangedListener(Registration registration) {
        registration.remove();
    }

    /**
     * Set the biquad filter nodes to default settings.
     */
    public void setDefaultFilters() {
        for (int i = 0; i < Weq8Element.NUM_FILTERS; i++) {
            setFilter(i, Weq8Element.NOOP_FILTER);
        }

        setFilter(0, Weq8Element.DEFAULT_FILTER_0);
        setFilter(1, Weq8Element.DEFAULT_FILTER_1);
        setFilter(2, Weq8Element.DEFAULT_FILTER_2);
        setFilter(3, Weq8Element.DEFAULT_FILTER_3);
    }

    /**
     * Sets array of biquad filter nodes on the Weq8 component.
     * @param settings The settings array for the biquad filters
     */
    public void setFilters(BiquadSettings[] settings) {
        for (int i = 0; i < settings.length; i++) {
            setFilter(i, settings[i]);
        }
    }

    /**
     * Sets an indexed biquad filter node on the Weq8 component.
     * @param index The index of the biquad filter
     * @param settings The settings for the biquad filter
     */
    public void setFilter(int index, BiquadSettings settings) {
        setFilterType(index, settings.type());
        toggleBypass(index, settings.bypass());
        setFilterFrequency(index, settings.frequency());
        setFilterQ(index, settings.q());
        setFilterGain(index, settings.gain());
    }

    private void setFilterType(int index, BiquadType type) {
        getElement().callJsFunction("setFilterType", index, type.toString());
    }

    private void toggleBypass(int index, boolean bypass) {
        getElement().callJsFunction("toggleBypass", index, bypass);
    }

    private void setFilterFrequency(int index, double frequency) {
        getElement().callJsFunction("setFilterFrequency", index, frequency);
    }

    private void setFilterQ(int index, double q) {
        getElement().callJsFunction("setFilterQ", index, q);
    }

    private void setFilterGain(int index, double gain) {
        getElement().callJsFunction("setFilterGain", index, gain);
    }
}