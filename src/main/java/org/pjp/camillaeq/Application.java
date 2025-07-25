package org.pjp.camillaeq;

import java.util.Arrays;

import org.pjp.camillaeq.manager.ConfigManager;
import org.pjp.camillaeq.model.BiquadSettings;
import org.pjp.camillaeq.ws.CamillaAccess;
import org.pjp.camillaeq.yaml.Reconfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

/**
 * The entry point of the CamillaEq (Spring Boot) application.
 *
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@PWA(name = "Equaliser for CamillaDSP", shortName = "CamillaEQ")
@Theme("my-theme")
public class Application implements AppShellConfigurator, CommandLineRunner {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private static int getMajorVersion(String version) {
        return Integer.parseInt(version.substring(0, version.indexOf('.')));
    }

    private static BiquadSettings[] filterSettings;

    public static synchronized BiquadSettings[] getFilterSettings() {
        return filterSettings;
    }

    public static synchronized void setFilterSettings(BiquadSettings[] filterSettings) {
        Application.filterSettings = filterSettings;
    }

    @Value("${camilla.url:ws://localhost:1234}")
    private String camillaUrl;

    @Value("${camilla.version:3}")
    private int camillaVersion;

    @Value("${config.backup:false}")
    private boolean configBackup;

    @Autowired
    ConfigManager configManager;

    private BiquadSettings[] lastFilterSettings;

    /**
     * The entry point for the application
     * @param args The command line arguments for the program
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("camillaUrl = " + camillaUrl);
        CamillaAccess.setCamillaUrl(camillaUrl);

        String version = CamillaAccess.queryForValue(CamillaAccess.GET_VERSION);
        LOGGER.info("CamillaDSP version = " + version);

        if (getMajorVersion(version) != camillaVersion) {
            LOGGER.warn("Expected CamillaDSP version " + camillaVersion + ", this app may not work!");
        }

        if (configBackup) {
            configManager.backup();
        }
    }

    /**
     * Reconfigure the Camilla DSP using the current biquad settings from the WEQ8 UI, if they have changed.
     */
    @Scheduled(fixedDelay = 500)
    public void doReconfigure() {
        BiquadSettings[] filterSettings = getFilterSettings();

        if (filterSettings != null) {
            if (!Arrays.equals(filterSettings, lastFilterSettings)) {
                Reconfigure r = new Reconfigure(filterSettings);
                r.getFilterConfig();

                if (r.reconfigure()) {
                    lastFilterSettings = filterSettings;
                }
            }
        }
    }
}
