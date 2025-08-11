package com.devops.toolbox.finder.batch.wizard;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyGroups {
    private List<PropertyGroup> propertyGroups;
}

