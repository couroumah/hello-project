package com.devops.toolbox.finder;

import lombok.Builder;

@Builder
public record FinderEntityDto(
        Integer ID,
        String businessObject,
        String shortName,
        int nbInstances,
        String foundPatterns,
        String sourcePattern,
        String targetPattern,
        String absolutePath,
        String action,
        String item,
        String status,
        String comments,
        int nbMatchedLines) {
}
