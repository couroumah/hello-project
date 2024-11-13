package com.devops.toolbox.finder;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class FinderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    private String businessObject;
    private String shortName;
    private int nbInstances;
    private String foundPatterns;
    private String sourcePattern;
    private String targetPattern;
    private String absolutePath;
    private String action;
    private String item;
    private String status;
    private String comments;

    @OneToMany(
            mappedBy = "finderEntity",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private List<FinderEntityBody> finderEntityBodies;

    public void addFinderEntityBody(FinderEntityBody finderEntityBody){
        if (finderEntityBodies == null){
            finderEntityBodies = new ArrayList<>();
        }
        finderEntityBodies.add(finderEntityBody);
        finderEntityBody.setFinderEntity(this);
    }

    public void removeFinderEntityBody(FinderEntityBody finderEntityBody){
        if (finderEntityBodies != null){
            finderEntityBodies.remove(finderEntityBody);
            finderEntityBody.setFinderEntity(null);
        }
    }

    @Override
    public String toString() {
        return "FinderEntity{" +
                ", businessObject='" + businessObject + '\'' +
                ", shortName='" + shortName + '\'' +
                ", nbInstances=" + nbInstances +
                ", foundPatterns='" + foundPatterns + '\'' +
                ", sourcePortfolio='" + sourcePattern + '\'' +
                ", targetPortfolio='" + targetPattern + '\'' +
                ", absolutePath='" + absolutePath + '\'' +
                ", action='" + action + '\'' +
                ", item='" + item + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
