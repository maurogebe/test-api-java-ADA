package com.example.demo.domain.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@Entity
@Table(name = "version")
public class Version {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "version")
    private String version;

    @Column(name = "description")
    private String description;
    
    @OneToOne
    @JoinColumn(name = "application_id", unique = true)
    private Application application;
    
    @OneToOne(mappedBy = "version", cascade = CascadeType.ALL)
    private VersionCompany versionCompany;

    public Version() {
    }
}
