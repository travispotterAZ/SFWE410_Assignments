package com.optimagrowth.license.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain model for the {@code Person} class in the diagram.
 *
 * <p>A person can be the <b>president of</b> zero or more organizations
 * (the "1" on the Person side / "*" on the Organization side means one
 * president per organization, but a person may head more than one),
 * and can independently be a <b>member of</b> zero or more organizations
 * (many-to-many).</p>
 */
@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "major")
    private String major;

    @Column(name = "dept")
    private String dept;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    /** Organizations for which this person is the president ("president of"). */
    @OneToMany(mappedBy = "president")
    @JsonIgnoreProperties({"president", "members"})
    private List<Organization> organizationsPresided = new ArrayList<>();

    /** Organizations this person belongs to as a member ("member of"). */
    @ManyToMany(mappedBy = "members")
    @JsonIgnoreProperties({"president", "members"})
    private List<Organization> organizations = new ArrayList<>();
}
