package com.optimagrowth.license.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.optimagrowth.license.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    /** Fetches every organization together with its members in one query, avoiding lazy-loading issues. */
    @Query("SELECT DISTINCT o FROM Organization o LEFT JOIN FETCH o.members")
    List<Organization> findAllWithMembers();
}
