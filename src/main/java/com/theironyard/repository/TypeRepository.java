package com.theironyard.repository;

import com.theironyard.entity.Type;
import com.theironyard.entity.Widget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Integer> {
    Type findByType(String name);
}
