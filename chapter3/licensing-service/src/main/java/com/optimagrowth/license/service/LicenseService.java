package com.optimagrowth.license.service;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.repository.LicenseRepository;

@Service
public class LicenseService {

	@Autowired
	MessageSource messages;

	@Autowired
	LicenseRepository licenseRepository;

	public License getLicense(String licenseId, String organizationId, Locale locale) {
		List<License> matches = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		if (matches.isEmpty()) {
			throw new IllegalArgumentException(
					String.format(messages.getMessage("license.search.error.message", null, locale), licenseId, organizationId));
		}
		return matches.get(0);
	}

	public String createLicense(License license, String organizationId, Locale locale) {
		String responseMessage = null;
		if (!StringUtils.isEmpty(license)) {
			license.setOrganizationId(organizationId);
			applyExistingId(license, organizationId);

			licenseRepository.save(license);
			responseMessage = String.format(messages.getMessage("license.create.message", null, locale), license.toString());
		}

		return responseMessage;
	}

	public String updateLicense(License license, String organizationId, Locale locale) {
		String responseMessage = null;
		if (!StringUtils.isEmpty(license)) {
			license.setOrganizationId(organizationId);
			applyExistingId(license, organizationId);

			licenseRepository.save(license);
			responseMessage = String.format(messages.getMessage("license.update.message", null, locale), license.toString());
		}

		return responseMessage;
	}

	public String deleteLicense(String licenseId, String organizationId, Locale locale) {
		String responseMessage = null;
		List<License> matches = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		licenseRepository.deleteAll(matches);
		responseMessage = String.format(messages.getMessage("license.delete.message", null, locale), licenseId, organizationId);
		return responseMessage;
	}

	/**
	 * (organizationId, licenseId) is the natural key for a license in this demo app: if a row
	 * already exists for that pair, reuse its generated id so save() updates it in place instead
	 * of inserting a duplicate row.
	 */
	private void applyExistingId(License license, String organizationId) {
		List<License> matches = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, license.getLicenseId());
		if (!matches.isEmpty()) {
			license.setId(matches.get(0).getId());
		} else {
			license.setId(0);
		}
	}
}
