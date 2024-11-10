package com.devops.toolbox.cmftemplates;

import com.westpac.murex.devops.model.cmftemplates.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ConfigurationTemplateMapper {
    public List<ConfigurationTemplateDto> toDtos(@NonNull ConfigurationTemplate configurationTemplate,
                                                 @NonNull Map<String, String> mapObjectId) {
        List<ConfigurationTemplateDto> dtos = new ArrayList<>();

        int counter = 1;
        for (ConfigurationItem configurationItem : configurationTemplate.getConfigurationItems()) {
            for (Instances instances : configurationItem.getInstances()) {
                for (Instance instance : instances.getCollectionInstance()) {
                    for (InstanceKey instanceKey : instance.getInstanceKeys()) {
                        String objectId = configurationItem.getObjectId();

                        String description = mapObjectId.get(configurationItem.getObjectId());

                        ConfigurationTemplateDto dto = new ConfigurationTemplateDto(
                                counter++,
                                configurationTemplate.getName(),//ConfigurationTemplateName
                                configurationItem.getName(),//ConfigurationItemName
                                configurationItem.getObjectId(),//ConfigurationItemObjectId
                                instance.getLabel(),//InstanceLabel
                                instanceKey.getProperty(),//InstanceKeyProperty
                                instanceKey.getValue(),//InstanceKeyValue
                                description);

                        dtos.add(dto);
                    }
                }
            }

        }
        return dtos;
    }
}
