package com.devops.toolbox.finder;

import org.springframework.stereotype.Component;

@Component
public class FinderEntityMapper {

    public FinderEntityDto toDto(FinderEntity finderEntity){
        return FinderEntityDto.builder()
                .ID(finderEntity.getID())
                .businessObject(finderEntity.getBusinessObject())
                .shortName(finderEntity.getShortName())
                .nbInstances(finderEntity.getNbInstances())
                .foundPatterns(finderEntity.getFoundPatterns())
                .sourcePattern(finderEntity.getSourcePattern())
                .targetPattern(finderEntity.getTargetPattern())
                .absolutePath(finderEntity.getAbsolutePath())
                .action(finderEntity.getAction())
                .item(finderEntity.getItem())
                .status(finderEntity.getStatus())
                .comments(finderEntity.getComments())
                .nbMatchedLines(finderEntity.getFinderEntityBodies().size())
                .build();
    }

    public FinderEntity toFinderEntity(FinderEntityDto dto){
        return FinderEntity.builder()
                .ID(dto.ID())
                .businessObject(dto.businessObject())
                .shortName(dto.shortName())
                .nbInstances(dto.nbInstances())
                .foundPatterns(dto.foundPatterns())
                .sourcePattern(dto.sourcePattern())
                .targetPattern(dto.targetPattern())
                .absolutePath(dto.absolutePath())
                .action(dto.action())
                .item(dto.item())
                .status(dto.status())
                .comments(dto.comments())
                .build();
    }

}
