package upcafe.repository.settings;

import org.springframework.data.jpa.repository.JpaRepository;

import upcafe.entity.settings.PickupSettings;

public interface PickupSettingsRepository extends JpaRepository<PickupSettings, String> {

}
