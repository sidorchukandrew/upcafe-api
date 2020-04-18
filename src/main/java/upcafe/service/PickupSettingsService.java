package upcafe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upcafe.entity.settings.PickupSettings;
import upcafe.repository.settings.PickupSettingsRepository;

@Service
public class PickupSettingsService {

	@Autowired private PickupSettingsRepository pickupRepository;
	
	public PickupSettings getPickupSettings() {
		return pickupRepository.findById("1").get();	
	}
	
	public PickupSettings updatePickupSettings(PickupSettings settings) {
		return pickupRepository.save(settings);
	}
}
