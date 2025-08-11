package com.devops.toolbox.finder.batch.wizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompositeProperty {
    @JsonProperty
    private String name;
    private List<Item> items;
}
