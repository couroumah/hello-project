package com.devops.toolbox.finder.batch.wizard;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyGroup {
    private String name;
    private String description;
    private List<Property> properties;
    private List<CompositeProperty> compositeProperties;
}
