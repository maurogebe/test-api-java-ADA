package com.example.demo.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@Entity
@Table(name = "version_company")
public class VersionCompany {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "description")
    private String description;
    
    @OneToOne
    @JoinColumn(name = "company_id", unique = true)
    private Company company;

    @OneToOne
    @JoinColumn(name = "version_id", unique = true)
    private Version version;

    public VersionCompany() {
    }
}
