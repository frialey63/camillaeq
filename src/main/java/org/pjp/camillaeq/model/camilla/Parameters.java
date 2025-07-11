package org.pjp.camillaeq.model.camilla;

import java.util.LinkedHashMap;
import java.util.Map;

import org.pjp.camillaeq.model.BiquadSettings;

/**
 * Record which represents the parameters for a Camilla DSP (biquad) filter.
 *
 */
public record Parameters(FilterSubType type, double freq, Double q, Double gain) {

    /**
     * @param biquad The biquad settings from which the filter parameters is constructed
     */
    public Parameters(BiquadSettings biquad) {
        this(FilterSubType.valueOf(biquad.type().name()), biquad.frequency(), biquad.q(), biquad.getGain());
    }

    /**
     * @return A map describing the filter parameters
     */
    Map<String, Object> getMap() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("type", type.toString());
        result.put("freq", freq);
        if (q != null) {
            result.put("q", q);
        }
        if (gain != null) {
            result.put("gain", gain);
        }
        return result;
    }

}
