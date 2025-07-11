package org.pjp.camillaeq.model.camilla;

/**
 * Enum which represents the type of a Camilla DSP pipeline step.
 *
 */
public enum PipelineStepType {

    /**
     * The Processor pipeline step.
     */
    PROCESSOR("Processor"),

    /**
     * The Filter pipeline step.
     */
    FILTER("Filter"),

    /**
     * The Mixer pipeline step.
     */
    MIXER("Mixer");

    private String name;

    private PipelineStepType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


}
