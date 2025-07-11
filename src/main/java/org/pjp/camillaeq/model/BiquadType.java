package org.pjp.camillaeq.model;

/**
 * Enum which represents the type of a BIQUAD filter node in (WEQ8) Web Audio.
 *
 * The types, frequencies, Qs, and gains are as documented for the standard BiquadFilterNode.
 * The filter types suffixed with 12 are singular BiquadFilterNodes and the types suffixed with 24 are two BiquadFilterNodes in series.
 *
 */
public enum BiquadType {

    /**
     * highpass
     * Standard second-order resonant highpass filter with 12dB/octave rolloff. Frequencies below the cutoff are attenuated; frequencies above it pass through.
     * Freq The cutoff frequency.
     * Q    Indicates how peaked the frequency is around the cutoff. The greater the value, the greater the peak.
     * Gain Not used
     */
    HIGH_PASS("highpass12"),

    /**
     * lowpass
     * Standard second-order resonant lowpass filter with 12dB/octave rolloff. Frequencies below the cutoff pass through; frequencies above it are attenuated.
     * Freq The cutoff frequency.
     * Q    Indicates how peaked the frequency is around the cutoff. The greater the value is, the greater is the peak.
     * Gain Not used
     */
    LOW_PASS("lowpass12"),

    /**
     * peaking
     * Frequencies inside the range get a boost or an attenuation; frequencies outside it are unchanged.
     * Freq The middle of the frequency range getting a boost or an attenuation.
     * Q    Controls the width of the frequency band. The greater the Q value, the smaller the frequency band.
     * Gain The boost, in dB, to be applied; if negative, it will be an attenuation.
     */
    PEAKING("peaking12"),

    /**
     * highshelf
     * Standard second-order highshelf filter. Frequencies higher than the frequency get a boost or an attenuation; frequencies lower than it are unchanged.
     * Freq The lower limit of the frequencies getting a boost or an attenuation.
     * Q    Not used
     * Gain The boost, in dB, to be applied; if negative, it will be an attenuation.
     */
    HIGH_SHELF("highshelf12"),

    /**
     * lowshelf
     * Standard second-order lowshelf filter. Frequencies lower than the frequency get a boost, or an attenuation; frequencies over it are unchanged.
     * Freq The upper limit of the frequencies getting a boost or an attenuation.
     * Q    Not used
     * Gain The boost, in dB, to be applied; if negative, it will be an attenuation.
     *
     */
    LOW_SHELF("lowshelf12"),

    /**
     * notch
     * Standard notch filter, also called a band-stop or band-rejection filter. It is the opposite of a bandpass filter: frequencies outside the give range of frequencies pass through; frequencies inside it are attenuated.
     * Freq The center of the range of frequencies.
     * Q    Controls the width of the frequency band. The greater the Q value, the smaller the frequency band.
     * Gain Not used
     */
    NOTCH("notch12");

    private String name;

    private BiquadType(String name) {
        this.name = name;
    }

    /**
     * @return True if Q is applicable for this biquad type
     */
    public boolean hasQ() {
        return switch(this) {
        case HIGH_SHELF -> false;
        case LOW_SHELF -> false;
        default -> true;
        };
    }

    /**
     * @return True if Gain is applicable for this biquad type
     */
    public boolean hasGain() {
        return switch(this) {
        case HIGH_PASS -> false;
        case LOW_PASS -> false;
        case NOTCH -> false;
        default -> true;
        };
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @param str The string assumed to contain the name of a biquad type
     * @return The biquad type parsed from the string or null
     */
    public static BiquadType parse(String str) {
        return switch(str) {
        case "highpass12" -> HIGH_PASS;
        case "lowpass12" -> LOW_PASS;
        case "peaking12" -> PEAKING;
        case "highshelf12" -> HIGH_SHELF;
        case "lowshelf12" -> LOW_SHELF;
        case "notch12" -> NOTCH;
        default -> null;
        };
    }
}
