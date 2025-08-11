package com.devops.toolbox.finder.batch.wizard;

import com.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DemoJSON {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoJson.class);

    private static final Path PATH_JSON_DEMO_DIRECTORY = Paths.get("C:\\Users\\l137427\\Downloads");
    private static final Path PATH_JSON_OUTPUT = PATH_JSON_DEMO_DIRECTORY.resolve("jsonOutput.json");
    private static final Path PATH_JSON_INPUT = PATH_JSON_DEMO_DIRECTORY.resolve("jsonInput.json");

    public static void createDemo() {
        LOGGER.info("-- starting [createDemo]");

        GlobalProperty globalProperty = GlobalProperty.builder()
                .name("globalPaths")
                .pathOut(PATH_JSON_DEMO_DIRECTORY.toString())
                .fileExtensionCSV(".csv")
                .fileExtensionXML(".xml")
                .build();

        GlobalProperties globalProperties = GlobalProperties.builder()
                .globalProperty(List.of(globalProperty))
                .build();

        Property property1 = Property.builder()
                .name("property1")
                .value("value1")
                .type("java.lang.String")
                .build();

        Property property2 = Property.builder()
                .name("property2")
                .value("value2")
                .type("java.lang.Integer")
                .build();

        Property property3 = Property.builder()
                .name("property3")
                .value("value3")
                .type("java.io.File")
                .build();

        PropertyGroup propertyGroup1 = PropertyGroup.builder()
                .name("propertyGroup1")
                .description("description1")
                .properties(List.of(property1, property2))
                .build();

        Item item1 = Item.builder()
                .key("label1")
                .value("value1")
                .type("type1")
                .description("description1")
                .build();

        Item item2 = Item.builder()
                .key("label2")
                .value("value2")
                .type("type2")
                .description("description2")
                .build();

        Item item3 = Item.builder()
                .key("label3")
                .value("value3")
                .type("type3")
                .description("description3")
                .build();

        CompositeProperty compositeProperty1 = CompositeProperty.builder()
                .name("compositeProperty1")
                .items(List.of(item1, item2))
                .build();

        CompositeProperty compositeProperty2 = CompositeProperty.builder()
                .name("compositeProperty2")
                .items(List.of(item3))
                .build();

        PropertyGroup propertyGroup2 = PropertyGroup.builder()
                .name("propertyGroup2")
                .description("description2")
                .properties(List.of(property3))
                .compositeProperties(List.of(compositeProperty1, compositeProperty2))
                .build();

        PropertyGroups propertyGroups = PropertyGroups.builder()
                .propertyGroups(List.of(propertyGroup1, propertyGroup2))
                .build();

        WizardConfiguration wizardConfiguration = WizardConfiguration.builder()
                .globalProperties(globalProperties)
                .propertyGroups(propertyGroups)
                .build();

        LOGGER.info("-- convertObjectToJsonFile");
        JSONUtils.convertObjectToJsonFile(wizardConfiguration, PATH_JSON_OUTPUT);

        LOGGER.info("-- convertJsonFileToObject");
        WizardConfiguration wizardConfiguration1 = JSONUtils.convertJsonFileToObject(WizardConfiguration.class, PATH_JSON_INPUT);
        LOGGER.info(wizardConfiguration1.toString());

        LOGGER.info("-- end [createDemo]");
    }
}
