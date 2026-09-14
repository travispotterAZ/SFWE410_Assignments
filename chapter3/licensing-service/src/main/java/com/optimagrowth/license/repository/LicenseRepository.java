package com.optimagrowth.license.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.optimagrowth.license.model.License;

public interface LicenseRepository extends CrudRepository<License, Integer> {

	List<License> findByOrganizationIdAndLicenseId(String organizationId, String licenseId);

	List<License> findByOrganizationId(String organizationId);

}
