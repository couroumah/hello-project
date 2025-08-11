package com.devops.toolbox.finder.batch.wizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    @JsonProperty
    private String name;
    @JsonProperty
    private String type;
    @JsonProperty
    private String value;
}
