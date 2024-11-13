package com.devops.toolbox.finder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinderEntityRepository extends JpaRepository<FinderEntity, Integer> {
}
