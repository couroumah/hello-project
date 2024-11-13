package com.devops.toolbox.finder;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FinderEntityBody {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Lob
    private byte[] data;

    @ManyToOne(fetch = FetchType.EAGER)
    private FinderEntity finderEntity;

    public FinderEntityBody(byte[] data) {
        this.data = data;
    }

}
