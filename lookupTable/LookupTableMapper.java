package com.westpac.murex.devops.lookupTable.service;

import com.westpac.murex.devops.lookupTable.dto.LookupTableDto;
import com.westpac.murex.devops.lookupTable.model.UserDefinedField;
import com.westpac.murex.devops.util.Messages;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LookupTableMapper {

    public List<LookupTableDto> toDto(List<Map<String, List<UserDefinedField>>> foundPatterns, String fullPath) {
        List<LookupTableDto> lookupTableDtos = new ArrayList<>();

        foundPatterns.stream().forEach(stringListMap -> stringListMap.entrySet().stream()
                .forEach(stringListEntry -> {
                            LookupTableDto dto = new LookupTableDto(
                                    Messages.getString("lut.excel.businessObject"),//businessObject
                                    stringListEntry.getKey(),//lookupTableName
                                    stringListEntry.getValue().size(),//nbOccurrence
                                    stringListEntry.getValue().stream().findFirst().get().getFieldValue(),//sourcePattern
                                    stringListEntry.getValue().stream().findFirst().get().getFieldValue(),//targetPattern
                                    Messages.getString("lut.excel.action"),//action
                                    fullPath,//fullPath
                                    Messages.getString("lut.excel.item"),//item
                                    Messages.getString("lut.excel.status"),//status
                                    Messages.getString("lut.excel.comment")//comment
                            );
                            lookupTableDtos.add(dto);
                        }
                ));

        return lookupTableDtos;
    }
}
