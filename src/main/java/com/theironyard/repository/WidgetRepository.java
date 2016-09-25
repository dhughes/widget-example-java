package com.theironyard.repository;

import com.theironyard.entity.Type;
import com.theironyard.entity.Widget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WidgetRepository extends JpaRepository<Widget, Integer> {

    @Query(value = "SELECT w FROM Widget w WHERE (?1 = '' OR upper(w.name) LIKE upper(?1)) AND (?2 IS NULL OR ?2 MEMBER OF w.types) AND (?3 IS NULL OR w.id = ?3)")
    List<Widget> search(String name, Type type, Integer id);

    @Query(value = "SELECT w FROM Widget w WHERE (?1 = '' OR upper(w.name) LIKE upper(?1)) AND (?2 IS NULL OR ?2 MEMBER OF w.types) AND (?3 IS NULL OR w.id = ?3)")
    Page<Widget> search(String name, Type type, Integer id, Pageable pageable);

}
