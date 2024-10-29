package com.devops.toolbox.cmftemplates;

public record ConfigurationTemplateDto(
        Integer id,
        String templateName,
        String itemName,
        String itemObjectId,
        String instanceLabel,
        String instanceKeyProperty,
        String instanceKeyValue,
        String description
) {
}
