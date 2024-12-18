package com.github.arifjehoh.taskmanager.tasks;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
}
