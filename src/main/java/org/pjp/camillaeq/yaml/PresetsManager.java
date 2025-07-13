package org.pjp.camillaeq.yaml;

import java.util.HashMap;
import java.util.Map;

import org.pjp.camillaeq.model.BiquadSettings;
import org.springframework.stereotype.Component;

/**
 * This component manages the biquad filter node presets.
 *
 */
@Component
public class PresetsManager {

    private Map<Integer, BiquadSettings[]> map = new HashMap<>();

    /**
     * Save filter preset.
     * @param presetNumber The number of the preset
     * @param filterSettings The biquad filter settings
     */
    public void save(int presetNumber, BiquadSettings[] filterSettings) {
        map.put(presetNumber, filterSettings);
    }

    /**
     * Load filter preset.
     * @param presetNumber The number of the preset
     * @return The biquad filter settings
     */
    public BiquadSettings[] load(int presetNumber) {
        return map.get(presetNumber);
    }
}
