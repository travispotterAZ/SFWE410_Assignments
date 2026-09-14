package com.optimagrowth.license.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain model for the {@code Organization} class in the diagram.
 *
 * <p>Each organization has exactly one <b>president</b> (a {@link Person}),
 * while a person may preside over several organizations - the many-to-one
 * side of "president of". Each organization also has one or more
 * <b>members</b> (1..*), and a person may belong to many organizations (*) -
 * the many-to-many "member of" association.</p>
 */
@Entity
@Table(name = "organization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "established_date")
    private LocalDate establishedDate;

    /** The person who is "president of" this organization (many orgs : 1 person). */
    @ManyToOne
    @JoinColumn(name = "president_id")
    @JsonIgnoreProperties({"organizationsPresided", "organizations"})
    private Person president;

    /** The people who are "member of" this organization (many : many). */
    @ManyToMany
    @JoinTable(
            name = "person_organization",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    @JsonIgnoreProperties({"organizationsPresided", "organizations"})
    private List<Person> members = new ArrayList<>();
}
