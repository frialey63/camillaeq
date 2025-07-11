package org.pjp.camillaeq.model.camilla;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Record which stores the Camilla DSP configuration relevant to the EQ, the list of filters and the pipeline.
 *
 */
public record Config(List<Filter> filters, List<PipelineStep> pipeline) {

    public static final String PIPELINE = "pipeline";

    public static final String FILTERS = "filters";

    private static final String FILTER = PipelineStepType.FILTER.toString();

    /**
     * @return The filters as a map keyed by the name of each filter
     */
    public Map<String, Object> getFilters() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        filters.forEach(f -> {
            result.put(f.name(), f.getMap());
        });

        return result;
    }

    /**
     * @return The pipeline as a list of pipeline steps
     */
    public List<Map<String, Object>> getPipeline() {
        List<Map<String, Object>> result = new ArrayList<>();

        pipeline.forEach(ps -> {
            result.add(ps.getMap());
        });

        return result;
    }

    /**
     * @param oldPipeline The old pipeline
     * @return  The pipeline as a list of pipeline steps based on the old pipeline with its filter steps removed and the steps from this configuration appended
     */
    public List<Map<String, Object>> getPipeline(List<Map<String, Object>> oldPipeline) {
        List<Map<String, Object>> result = oldPipeline.stream().filter(ps -> !FILTER.equals(((Map<String, Object>) ps).get("type"))).collect(Collectors.toList());
        result.addAll(getPipeline());
        return result;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put(FILTERS, getFilters());
        result.put(PIPELINE, getPipeline());

        return result;
    }

}
