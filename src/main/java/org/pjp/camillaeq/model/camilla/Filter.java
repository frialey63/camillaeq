package org.pjp.camillaeq.model.camilla;

import java.util.LinkedHashMap;
import java.util.Map;

import org.pjp.camillaeq.model.BiquadSettings;

/**
 * Record which represents a filter of the Camilla DSP.
 *
 */
public record Filter(String name, FilterType type, String description, Parameters parameters) {

    /**
     * @param name The filter name
     * @param biquad The biquad settings from which the filter is constructed
     */
    public Filter(String name, BiquadSettings biquad) {
        this(name, FilterType.BIQUAD, "~", new Parameters(biquad));
    }

    /**
     * @return A map describing the filter
     */
    Map<String, Object> getMap() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("type", type.toString());
        result.put("description", description);
        result.put("parameters", parameters.getMap());
        return result;
    }

}
