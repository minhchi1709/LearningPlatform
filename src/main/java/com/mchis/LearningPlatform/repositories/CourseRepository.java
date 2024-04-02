package com.mchis.LearningPlatform.repositories;

import com.mchis.LearningPlatform.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {

}
