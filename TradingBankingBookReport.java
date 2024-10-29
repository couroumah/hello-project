package com.devops.toolbox.finder;

import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TradingBankingBookReport {

    private Long id;
    private String businessObject;
    private String shortName;
    private int nbInstances;
    private String foundPatterns;
    private String sourcePortfolio;
    private String targetPortfolio;
    private String absolutePath;
    private String action;
    private String item;
    private String status;
    private String comments;
    private Map<String, List<String>> mapMatchedLines;

    @Override
    public String toString() {
        return "TradingBankingBookReport{" +
                ", businessObject='" + businessObject + '\'' +
                ", shortName='" + shortName + '\'' +
                ", nbInstances=" + nbInstances +
                ", foundPatterns='" + foundPatterns + '\'' +
                ", sourcePortfolio='" + sourcePortfolio + '\'' +
                ", targetPortfolio='" + targetPortfolio + '\'' +
                ", absolutePath='" + absolutePath + '\'' +
                ", action='" + action + '\'' +
                ", item='" + item + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
