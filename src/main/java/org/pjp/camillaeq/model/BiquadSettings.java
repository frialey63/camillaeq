package org.pjp.camillaeq.model;

import java.io.Serializable;

/**
 * Record which represents settings of a BIQUAD filter node in (WEQ8) Web Audio.
 *
 */
public record BiquadSettings(BiquadType type, double frequency, double q, double gain, boolean bypass) implements Serializable {
    /**
     * @return The Q or null if not applicable for this type of biquad
     */
    public Double getQ() {
        return type.hasQ() ? q : null;
    }

    /**
     * @return The Gain or null if not applicable for this type of biquad
     */
    public Double getGain() {
        return type.hasGain() ? gain : null;
    }
}