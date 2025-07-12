package org.pjp.camillaeq.yaml;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pjp.camillaeq.model.BiquadSettings;
import org.pjp.camillaeq.model.BiquadType;
import org.pjp.camillaeq.model.camilla.Config;
import org.pjp.camillaeq.model.camilla.Filter;
import org.pjp.camillaeq.model.camilla.PipelineStep;
import org.pjp.camillaeq.model.camilla.PipelineStepType;
import org.pjp.camillaeq.ws.CamillaAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * This class encapsulates the logic for reconfiguration of the CamillaDSP to follow
 * revised biquad filter node settings applied to the Weq8 equaliser.
 *
 */
public class Reconfigure {

    private static final Logger LOGGER = LoggerFactory.getLogger(Reconfigure.class);

    /**
     * Get a new CamillaDSP configuration to match a specified array of biquad filter node settings.
     * @param filterSettings The array of biquad filter node settings
     * @return New CamillaDSP configuration
     */
    public Config getFilterConfig(BiquadSettings[] filterSettings) {
        List<Filter> filters = new ArrayList<>();
        List<PipelineStep> pipelineSteps = new ArrayList<>();

        for (int i = 0; i < filterSettings.length; i++) {
            BiquadSettings biquad = filterSettings[i];

            if (biquad.type() != BiquadType.NOOP) {
                String name = String.format("Filter %d", i);

                Filter filter = new Filter(name, biquad);
                filters.add(filter);

                PipelineStep step = new PipelineStep(PipelineStepType.FILTER, PipelineStep.ALL_CHANNELS, name, "~", biquad.bypass());
                pipelineSteps.add(step);
            }
        }

        return new Config(filters, pipelineSteps);
    }

    /**
     * Applies a new configuration by uploading it to the CamillaDSP.
     * @param newConfig The new configuration for CamillaDSP
     */
    public void reconfigure(Config newConfig) {
        String configStr = downloadConfig();

        Map<String, Object> config = parseConfig(configStr);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> pipeline = (List<Map<String, Object>>) config.get(Config.PIPELINE);

        // replace filters and pipeline in the config
        config.put(Config.FILTERS, newConfig.getFilters());
        config.put(Config.PIPELINE, newConfig.getPipeline(pipeline));

        configStr = toString(config);

        String res = uploadConfig(configStr);
        LOGGER.debug(res);
    }

    private String uploadConfig(String configStr) {
        return CamillaAccess.query(CamillaAccess.SET_CONFIG, configStr);
    }

    private String toString(Map<String, Object> config) {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);

        StringWriter stringWriter = new StringWriter();
        yaml.dump(config, stringWriter);

        String result = stringWriter.toString();
        result = result.replaceAll("null", "~");
        result = result.replaceAll("\n", "\\\\n");
        return result;
    }

    private Map<String, Object> parseConfig(String configStr) {
        Yaml yaml = new Yaml();
        return yaml.load(configStr);
    }

    private String downloadConfig() {
        String result = CamillaAccess.queryForValue(CamillaAccess.GET_CONFIG);
        result = result.replaceAll("\\\\n", System.lineSeparator());
        return result;
    }

}
