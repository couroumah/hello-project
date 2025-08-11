package com.devops.toolbox.finder.batch.wizard;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalProperties {
    private List<GlobalProperty> globalProperty;

    @Override
    public String toString() {
        return "GlobalProperties{" +
                "globalProperty=" + globalProperty +
                '}';
    }
}
