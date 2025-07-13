package org.pjp.camillaeq.yaml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This component manages the backup and restore of CamillaDSP configurations.
 *
 */
@Component
public class ConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);

    private File file;

    /**
     * Backup the CamillaDSP configuration to a temporary file.
     * @throws IOException
     */
    public void backup() throws IOException {
        if (file == null) {
            file = File.createTempFile("camillaeq-", ".bak");
            LOGGER.info("backing-up the user configuration to file " + file);
            String configStr = Reconfigure.downloadConfig();
            Files.writeString(file.toPath(), configStr);
        }
}

    /**
     * Restore the CamillaDSP configuration from a temporary file.
     * @throws IOException
     */
    public void restore() throws IOException {
        if (file != null) {
            LOGGER.info("restoring the user configuration from file " + file);
            String configStr = Files.readString(file.toPath());
            Reconfigure.uploadConfig(configStr);
        }
    }

}
