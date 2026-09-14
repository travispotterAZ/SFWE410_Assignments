package com.optimagrowth.license.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.optimagrowth.license.model.Person;

/**
 * Loads the sample data seeded by schema.sql / data.sql and prints every
 * Person's full info to the console.
 */
@SpringBootTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void printAllPeople() {
        List<Person> people = personRepository.findAll();

        assertThat(people).isNotEmpty();

        System.out.println("===== Person Records =====");
        for (Person person : people) {
            System.out.printf(
                    "id=%d, name=%s, major=%s, dept=%s, dateOfBirth=%s, phone=%s, email=%s%n",
                    person.getId(),
                    person.getName(),
                    person.getMajor(),
                    person.getDept(),
                    person.getDateOfBirth(),
                    person.getPhone(),
                    person.getEmail());
        }
        System.out.println("===========================");
    }
}
