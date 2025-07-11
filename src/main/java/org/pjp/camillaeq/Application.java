package org.pjp.camillaeq;

import java.util.Arrays;

import org.pjp.camillaeq.model.BiquadSettings;
import org.pjp.camillaeq.model.camilla.Config;
import org.pjp.camillaeq.yaml.Reconfigure;
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
public class Application implements AppShellConfigurator {

    private static final long serialVersionUID = 1L;

    private static BiquadSettings[] filterSettings;

    public static synchronized BiquadSettings[] getFilterSettings() {
        return filterSettings;
    }

    public static synchronized void setFilterSettings(BiquadSettings[] filterSettings) {
        Application.filterSettings = filterSettings;
    }

    private BiquadSettings[] lastFilterSettings;

    /**
     * The entry point for the application
     * @param args The command line arguments for the program
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Reconfigure the Camilla DSP using the current biquad settings from the WEQ8 UI, if they have changed.
     */
    @Scheduled(fixedDelay = 500)
    public void doReconfigure() {
        BiquadSettings[] filterSettings = getFilterSettings();

        if (filterSettings != null) {
            if (!Arrays.equals(filterSettings, lastFilterSettings)) {
                Reconfigure r = new Reconfigure();
                Config newConfig = r.getFilterConfig(filterSettings);
                r.reconfigure(newConfig);

                lastFilterSettings = filterSettings;
            }
        }
    }
}
