package com.optimagrowth.license.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.optimagrowth.license.model.Organization;

/**
 * Loads the sample data seeded by schema.sql / data.sql and prints a report
 * of every Organization, including its student (member) count.
 */
@SpringBootTest
class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    void printOrganizationReport() {
        List<Organization> organizations = organizationRepository.findAllWithMembers();

        assertThat(organizations).isNotEmpty();

        System.out.println("===================== Organization Report =====================");
        System.out.printf("%-22s %-12s %-16s %-14s%n", "Organization", "Category", "President", "# of Students");
        System.out.println("-----------------------------------------------------------------");

        int totalStudents = 0;
        for (Organization org : organizations) {
            String presidentName = org.getPresident() != null ? org.getPresident().getName() : "N/A";
            int studentCount = org.getMembers().size();
            totalStudents += studentCount;

            System.out.printf("%-22s %-12s %-16s %-14d%n",
                    org.getName(), org.getCategory(), presidentName, studentCount);
        }

        System.out.println("-----------------------------------------------------------------");
        System.out.printf("Total organizations: %d, Total student memberships: %d%n",
                organizations.size(), totalStudents);
        System.out.println("===================================================================");
    }
}
