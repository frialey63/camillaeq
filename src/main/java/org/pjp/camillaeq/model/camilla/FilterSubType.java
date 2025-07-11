package org.pjp.camillaeq.model.camilla;

/**
 * Enum which represents the sub-type of a Camilla DSP (biquad) filter.
 *
 * All first-order filters have a 20 dB/decade roll-off. The same roll-off can also be specified as 6 dB/octave.
 * An octave is a term borrowed from music and represents a doubling of frequency.
 * (It is so called because the frequency span in a doubling of frequency is divided into the eight notes of a musical scale.)
 *
 * Higher-order filters have a steeper roll-off. For second-order filters it is 40 dB/decade (or 12 dB/octave) and for third-order filters it is 60 dB/decade (or 18 dB/octave).
 * Each successive order adds a further 20 dB/decade (or 6 dB/octave) to the roll-off.
 */
public enum FilterSubType {
    /**
     * Highpass
     * Second order high/lowpass filters (12dB/oct)
     * Defined by cutoff frequency freq and Q-value q.
     */
    HIGH_PASS("Highpass"),

    /**
     * Lowpass
     * Second order high/lowpass filters (12dB/oct)
     * Defined by cutoff frequency freq and Q-value q.
     */
    LOW_PASS("Lowpass"),

    /**
     * Peaking
     * A parametric peaking filter with selectable gain gain at a given frequency freq with a bandwidth given either by the Q-value q or bandwidth in octaves bandwidth.
     * Note that bandwidth and Q-value are inversely related, a small bandwidth corresponds to a large Q-value etc.
     * Use positive gain values to boost, and negative values to attenuate.
     */
    PEAKING("Peaking"),

    /**
     * Highshelf & Lowshelf
     * High / Low uniformly affects the high / low frequencies respectively while leaving the low / high part unaffected. In between there is a slope of variable steepness.
     * Parameters:
     *     freq is the center frequency of the sloping section.
     *     gain gives the gain of the filter
     *     slope is the steepness in dB/octave. Values up to around +-12 are usable.
     *     q is the Q-value and can be used instead of slope to define the steepness of the filter. Only one of q and slope can be given.
     */
    HIGH_SHELF("Highshelf"),

    /**
     * Highshelf & Lowshelf
     * High / Low uniformly affects the high / low frequencies respectively while leaving the low / high part unaffected. In between there is a slope of variable steepness.
     * Parameters:
     *     freq is the center frequency of the sloping section.
     *     gain gives the gain of the filter
     *     slope is the steepness in dB/octave. Values up to around +-12 are usable.
     *     q is the Q-value and can be used instead of slope to define the steepness of the filter. Only one of q and slope can be given.
     */
    LOW_SHELF("Lowshelf"),

    /**
     * Notch
     * A notch filter to attenuate a given frequency freq with a bandwidth given either by the Q-value q or bandwidth in octaves bandwidth.
     * The notch filter is similar to a Peaking filter configured with a large negative gain.
     */
    NOTCH("Notch");

    private String name;

    private FilterSubType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @param str The string assumed to contain the name of a filter sub-type
     * @return The filter sub-type parsed from the string or null
     */
    public static FilterSubType parse(String str) {
        return switch(str) {
        case "Highpass" -> HIGH_PASS;
        case "Lowpass" -> LOW_PASS;
        case "Peaking" -> PEAKING;
        case "Highshelf" -> HIGH_SHELF;
        case "Lowshelf" -> LOW_SHELF;
        case "Notch" -> NOTCH;
        default -> null;
        };
    }

}
