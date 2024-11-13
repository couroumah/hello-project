package com.devops.toolbox;

import com.devops.toolbox.finder.FinderEntityDto;
import com.devops.toolbox.finder.FinderHelper;
import com.devops.toolbox.finder.FinderService;
import com.devops.toolbox.finder.FinderSettings;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static java.lang.Thread.currentThread;

@SpringBootApplication
public class DevopsConfigurationFinderApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(DevopsConfigurationFinderApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(DevopsConfigurationFinderApplication.class, args);
    }

    private void usage() {
        System.out.println("--------------------------------------------------");
        System.out.println("Missing parameters");
        System.out.println("Required parameter -DpathFinderSettingsJSON=<Finder settings JSON file>");
        System.out.println("Usage:");
        System.out.println("java -DpathFinderSettingsJSON=<pathFinderSettingsJSON file name>");
        System.out.println("Example java -DpathFinderSettingsJSON=FinderSettingsJSON.json");
        System.out.println("--------------------------------------------------");

        LOGGER.info("***********************************************************************************");
        LOGGER.info("***** Ending [{}]", currentThread().getStackTrace()[1].getClassName());
        LOGGER.info("***********************************************************************************");

        System.exit(-1);
    }

    @Bean
    CommandLineRunner runner(FinderService service) {
        return args -> {
            LOGGER.info("***********************************************************************************");
            LOGGER.info("***** Starting [{}] ...", DevopsConfigurationFinderApplication.class.getName());
            LOGGER.info("***********************************************************************************");
            Instant startApplication = Instant.now();
            String pathFinderSettingsJSON = System.getProperty("pathJSON");
            if (pathFinderSettingsJSON == null) {
                usage();
            }

            createConfigurationFinderReport(service, Path.of(pathFinderSettingsJSON));

            Instant finishApplication = Instant.now();
            Duration durationAplication = new Duration(startApplication.toEpochMilli(), finishApplication.toEpochMilli());
            Period periodApplication = durationAplication.toPeriod().normalizedStandard(PeriodType.time());

            LOGGER.info("***********************************************************************************");
            LOGGER.info("***** Ending [{}] in {}",
                    currentThread().getStackTrace()[1].getClassName(),
                    PeriodFormat.getDefault().print(periodApplication));
            LOGGER.info("***********************************************************************************");
        };
    }

    private static void createConfigurationFinderReport(FinderService service, Path pathFinderSettingsJSON) {
        FinderSettings settings = FinderHelper.convertFromJson(pathFinderSettingsJSON);
        List<FinderEntityDto> dtos =  service.writeReport(settings);
    }


}
