package org.pjp.camillaeq.model.camilla;

/**
 * Enum which represents the type of a Camilla DSP filter.
 *
 */
public enum FilterType {

    /**
     * The BIQUAD filter.
     */
    BIQUAD("Biquad");

    private String name;

    private FilterType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


}
