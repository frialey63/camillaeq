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

    private static boolean ok(String str) {
        return str.contains("\"Ok\"");
    }

    public static String uploadConfig(String configStr) {
        configStr = configStr.replaceAll("null", "~");
        configStr = configStr.replaceAll("\n", "\\\\n");
        return CamillaAccess.query(CamillaAccess.SET_CONFIG, configStr);
    }

    public static String downloadConfig() {
        String result = CamillaAccess.queryForValue(CamillaAccess.GET_CONFIG);
        result = result.replaceAll("\\\\n", System.lineSeparator());
        return result;
    }

    private static String toString(Map<String, Object> config) {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);

        StringWriter stringWriter = new StringWriter();
        yaml.dump(config, stringWriter);

        return stringWriter.toString();
    }

    private static Map<String, Object> parseConfig(String configStr) {
        Yaml yaml = new Yaml();
        return yaml.load(configStr);
    }

    private final BiquadSettings[] filterSettings;

    private Config newConfig;

    /**
     * @param filterSettings The array of biquad filter node settings
     */
    public Reconfigure(BiquadSettings[] filterSettings) {
        super();
        this.filterSettings = filterSettings;
    }

    /**
     * Get a new CamillaDSP configuration to match a specified array of biquad filter node settings.
     * @param stepPerFilter If true then use a separate pipeline step for each filter
     */
    public void getFilterConfig(boolean stepPerFilter) {
        List<Filter> filters = new ArrayList<>();
        List<PipelineStep> pipelineSteps = new ArrayList<>();

        List<String> filterNames = new ArrayList<>();

        for (int i = 0; i < filterSettings.length; i++) {
            BiquadSettings biquad = filterSettings[i];

            if (biquad.type() != BiquadType.NOOP) {
                String name = String.format("Filter %d", i);

                filters.add(new Filter(name, biquad));

                if (stepPerFilter) {
                    pipelineSteps.add(new PipelineStep(PipelineStepType.FILTER, PipelineStep.ALL_CHANNELS, name, "~", biquad.bypass()));
                } else {
                    if (!biquad.bypass()) {
                        filterNames.add(name);
                    }
                }
            }
        }

        if (!stepPerFilter) {
            pipelineSteps.add(new PipelineStep(PipelineStepType.FILTER, PipelineStep.ALL_CHANNELS, filterNames, "~", false));
        }

        newConfig = new Config(filters, pipelineSteps);
    }

    /**
     * Applies a new configuration by uploading it to the CamillaDSP.
     */
    public boolean reconfigure() {
        String configStr = downloadConfig();

        Map<String, Object> config = parseConfig(configStr);

        if (config != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> pipeline = (List<Map<String, Object>>) config.get(Config.PIPELINE);

            // replace filters and pipeline in the config
            config.put(Config.FILTERS, newConfig.getFilters());
            config.put(Config.PIPELINE, newConfig.getPipeline(pipeline));

            configStr = toString(config);

            String res = uploadConfig(configStr);
            LOGGER.debug(res);

            return ok(res);
        } else {
            LOGGER.warn("No config received from CamillaDSP, check DAC is switched-on and apply config.");
        }

        return false;
    }

}
