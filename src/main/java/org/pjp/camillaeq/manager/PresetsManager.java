package org.pjp.camillaeq.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
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

    /**
     * Indicates that no preset is active.
     */
    public static final int NONE = -1;

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

    private File serFile = new File("camillaeq-filter-presets.ser");

    private File txtFile = new File("camillaeq-active-preset.txt");

    /**
     * Save filter preset.
     *
     * @param presetNumber The number of the preset
     * @param filterSettings The biquad filter settings
     */
    public void save(int presetNumber, BiquadSettings[] filterSettings) {
        Map<Integer, BiquadSettings[]> map = readMap(serFile);

        map.put(presetNumber, filterSettings);

        writeMap(serFile, map);
    }

    /**
     * Load filter preset.
     *
     * @param presetNumber The number of the preset
     * @return The biquad filter settings
     */
    public BiquadSettings[] load(int presetNumber) {
        Map<Integer, BiquadSettings[]> map = readMap(serFile);

        return map.get(presetNumber);
    }

    /**
     * Clear filter preset.
     *
     * @param presetNumber The number of the preset
     */
    public void clear(int presetNumber) {
        Map<Integer, BiquadSettings[]> map = readMap(serFile);

        map.remove(presetNumber);

        writeMap(serFile, map);
    }

    /**
     * Set the active preset (number).
     * @param num Preset number
     */
    public void setActivePreset(int num) {
        try {
            Files.writeString(txtFile.toPath(), Integer.toString(num));
        } catch (IOException e) {
            LOGGER.error("failed to set active preset number to file", e);
        }
    }

    /**
     * Get the active preset (number).
     * @return Preset number or NONE
     */
    public int getActivePreset() {
        int result = NONE;

        try {
            result = Integer.parseInt(Files.readString(txtFile.toPath()));
        } catch (IOException e) {
            LOGGER.debug("failed to get active preset number from file", e);
        }

        return result;
    }
}
