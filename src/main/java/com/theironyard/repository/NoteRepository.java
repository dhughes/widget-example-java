package com.theironyard.repository;

import com.theironyard.entity.Note;
import com.theironyard.entity.Widget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer> {
}
