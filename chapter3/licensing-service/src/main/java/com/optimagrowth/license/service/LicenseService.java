package com.optimagrowth.license.service;

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

	public License getLicense(String licenseId, String organizationId) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		if (license == null) {
			throw new IllegalArgumentException(
					String.format("License %s for organization %s was not found.", licenseId, organizationId));
		}
		return license;
	}

	public String createLicense(License license, String organizationId, Locale locale) {
		String responseMessage = null;
		if (!StringUtils.isEmpty(license)) {
			license.setId(0);
			license.setOrganizationId(organizationId);
			licenseRepository.save(license);
			responseMessage = String.format(messages.getMessage("license.create.message", null, locale), license.toString());
		}

		return responseMessage;
	}

	public String updateLicense(License license, String organizationId, Locale locale) {
		String responseMessage = null;
		if (!StringUtils.isEmpty(license)) {
			license.setOrganizationId(organizationId);

			License existingLicense = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, license.getLicenseId());
			if (existingLicense != null) {
				license.setId(existingLicense.getId());
			}

			licenseRepository.save(license);
			responseMessage = String.format(messages.getMessage("license.update.message", null, locale), license.toString());
		}

		return responseMessage;
	}

	public String deleteLicense(String licenseId, String organizationId, Locale locale) {
		String responseMessage = null;
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		if (license != null) {
			licenseRepository.delete(license);
		}
		responseMessage = String.format(messages.getMessage("license.delete.message", null, locale), licenseId, organizationId);
		return responseMessage;
	}
}
