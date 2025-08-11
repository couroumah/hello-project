package com.devops.toolbox.finder.batch.wizard;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WizardConfiguration {
    private GlobalProperties globalProperties;
    private PropertyGroups propertyGroups;
}
