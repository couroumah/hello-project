package com.westpac.murex.devops.lookupTable.dto;

public record LookupTableDto(
        String businessObject,
        String lookupTableName,
        int nbOccurrence,
        String sourcePattern,
        String targetPattern,
        String action,
        String fullPath,
        String item,
        String status,
        String comment
) {
}
