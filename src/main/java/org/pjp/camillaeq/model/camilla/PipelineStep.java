package org.pjp.camillaeq.model.camilla;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Record which represents a pipeline step of the Camilla DSP.
 *
 */
public record PipelineStep(PipelineStepType type, List<Integer> channels, String name, String description, Boolean bypassed) {

    /**
     * Constant for specifying channels as ALL, i.e. channels 0 and 1 (assuming stereo)
     */
    public static final List<Integer> ALL_CHANNELS = List.of(0, 1);

    /**
     * @return A map describing the pipeline step
     */
    Map<String, Object> getMap() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("type", type.toString());
        if (channels != null) {
            result.put("channels", channels);
            result.put("names", Collections.singletonList(name));
        } else {
            result.put("name", name);
        }
        result.put("description", description);
        result.put("bypassed", bypassed);
        return result;
    }

}
