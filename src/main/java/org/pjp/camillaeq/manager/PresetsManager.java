package org.pjp.camillaeq.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.pjp.camillaeq.model.BiquadSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This component manages the biquad filter node presets.
 *
 */
@Component
public class PresetsManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PresetsManager.class);

    private static void writeMap(File file, Map<Integer, BiquadSettings[]> map) {
        try (FileOutputStream fileOutput = new FileOutputStream(file); ObjectOutputStream objectStream = new ObjectOutputStream(fileOutput)) {
            objectStream.writeObject(map);
        } catch (IOException e) {
            LOGGER.error("failed to serialise map of filter settings to file", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, BiquadSettings[]> readMap(File file) {
        Map<Integer, BiquadSettings[]> result = new HashMap<>();

        try (FileInputStream fileReader = new FileInputStream(file); ObjectInputStream objectStream = new ObjectInputStream(fileReader)) {
            result = (HashMap<Integer, BiquadSettings[]>) objectStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.debug("failed to deserialise map of filter settings from file", e);
        }

        return result;
    }

    private File file = new File("camillaeq-presets.ser");

    /**
     * Save filter preset.
     *
     * @param presetNumber The number of the preset
     * @param filterSettings The biquad filter settings
     */
    public void save(int presetNumber, BiquadSettings[] filterSettings) {
        Map<Integer, BiquadSettings[]> map = readMap(file);

        map.put(presetNumber, filterSettings);

        writeMap(file, map);
    }

    /**
     * Load filter preset.
     *
     * @param presetNumber The number of the preset
     * @return The biquad filter settings
     */
    public BiquadSettings[] load(int presetNumber) {
        Map<Integer, BiquadSettings[]> map = readMap(file);

        return map.get(presetNumber);
    }

    /**
     * Clear filter preset.
     *
     * @param presetNumber The number of the preset
     */
    public void clear(int presetNumber) {
        Map<Integer, BiquadSettings[]> map = readMap(file);

        map.remove(presetNumber);

        writeMap(file, map);
    }

}
