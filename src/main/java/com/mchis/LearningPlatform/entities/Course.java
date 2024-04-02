package com.mchis.LearningPlatform.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "_course")
public class Course {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(
            name = "teacher_id"
    )
    private User teacher;

    @ManyToMany
    @JoinTable(
            name = "students_courses",
            joinColumns = {
                    @JoinColumn(name = "student_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "course_id")
            }
    )
    private List<User> students;
}
