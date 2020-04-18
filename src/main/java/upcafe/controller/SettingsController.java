package upcafe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import upcafe.entity.settings.PickupSettings;
import upcafe.entity.settings.TimeBlock;
import upcafe.model.settings.AvailablePickupTimes;
import upcafe.service.CafeHoursService;
import upcafe.service.PickupTimesService;

@RestController
@CrossOrigin(origins = "*")
public class SettingsController {
	
	@Autowired private PickupTimesService pickupService;
	@Autowired private CafeHoursService hoursService;
	
	@GetMapping(path = "/cafe/hours", params="weekOf")
	public List<TimeBlock> getBlocksFor(@RequestParam(name = "weekOf") String weekOf) {
		return hoursService.getBlocksForWeek(weekOf);
	}
	
	@PutMapping("/cafe/hours")
	public TimeBlock updateBlock(@RequestBody upcafe.model.settings.WeekBlock weekBlock) {
		return hoursService.updateBlock(weekBlock);
	}
	
	@GetMapping("/cafe/pickup")
	public AvailablePickupTimes getAvailablePickupTimes() {
		return pickupService.getAvailablePickupTimes();
	}
	
	@PostMapping("/cafe/hours")
	public TimeBlock saveNewBlockFor(@RequestBody upcafe.model.settings.WeekBlock weekBlock) {
		return hoursService.saveNewBlock(weekBlock);
	}
	
	@DeleteMapping(path = "/cafe/hours", params="blockId")
	public boolean deleteBlock(@RequestParam(name = "blockId") String blockId, @RequestParam(name = "weekOf") String weekOf) {

		hoursService.deleteBlock(blockId, weekOf);
		return true;
	}

	@GetMapping(path = "/cafe/hours", params="day")
	public List<TimeBlock> getBlocksForDay(@RequestParam(name = "day") String day) {
		return hoursService.getTimeBlocksForDay(day);
	}
	
	@GetMapping(path = "/cafe/settings/pickup")
	public PickupSettings getPickupSettings() {
		return pickupService.getPickupSettings();
	}
	
	@PutMapping(path = "/cafe/settings/pickup")
	public PickupSettings updatePickupSettings(@RequestBody PickupSettings settings) {
		return pickupService.updatePickupSettings(settings);
	}
	
}
