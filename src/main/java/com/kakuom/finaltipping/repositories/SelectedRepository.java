package com.kakuom.finaltipping.repositories;

import com.kakuom.finaltipping.model.Selected;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SelectedRepository extends JpaRepository<Selected, Long> {
}
