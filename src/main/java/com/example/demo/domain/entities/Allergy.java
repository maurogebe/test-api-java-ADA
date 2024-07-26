package com.example.demo.domain.entities;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
@Entity
@Table(name = "allergy")
@JsonIdentityInfo(scope = Allergy.class, generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Allergy {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "allergies")
    private Set<Patient> patients;

    public Allergy() {
    }
}
