package upcafe.controller;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import upcafe.dto.settings.TimeBlockDTO;
import upcafe.dto.settings.WeekBlocksDTO;
import upcafe.entity.settings.PickupSettings;
import upcafe.entity.settings.TimeBlock;
import upcafe.error.MissingParameterException;
import upcafe.service.CafeHoursService;
// import upcafe.service.PickupTimesService;
import upcafe.utils.TimeUtils;

@RestController
@CrossOrigin(origins = "*")
public class SettingsController {
	
	// @Autowired private PickupTimesService pickupService;
	@Autowired private CafeHoursService hoursService;
	
	@GetMapping(path = "/cafe/hours", params="weekOf")
	public WeekBlocksDTO getBlocksForWeek(@RequestParam(name = "weekOf") String day) {
		LocalDate weekOf = TimeUtils.toLocalDate(day);
		return hoursService.getBlocksForWeek(weekOf);
	}
	
	@PutMapping("/cafe/hours")
	public TimeBlockDTO updateBlock(@RequestBody TimeBlockDTO blockToUpdate) {
		return hoursService.updateBlock(blockToUpdate);
	}
	
	@PostMapping("/cafe/hours")
	public TimeBlockDTO saveNewBlock(@RequestBody TimeBlockDTO timeBlock) {
		
		return hoursService.saveNewBlock(timeBlock);
	}
	
	@DeleteMapping(path = "/cafe/hours", params="blockId")
	public boolean deleteBlock(@RequestParam("blockId") String blockId) {
		hoursService.deleteBlock(blockId);
		return true;
	}

	// @GetMapping("/cafe/pickup")
	// public void getAvailablePickupTimes() {
	// // return pickupService.getAvailablePickupTimes();

	// }
	// @GetMapping(path = "/cafe/hours", params="day")
	// public List<TimeBlock> getBlocksForDay(@RequestParam(name = "day") String day) {
	// 	return hoursService.getTimeBlocksForDay(day);
	// }
	
	// @GetMapping(path = "/cafe/settings/pickup")
	// public PickupSettings getPickupSettings() {
	// 	return pickupService.getPickupSettings();
	// }
	
	// @PutMapping(path = "/cafe/settings/pickup")
	// public PickupSettings updatePickupSettings(@RequestBody PickupSettings settings) {
		
	// 	if(settings.getIntervalBetweenPickupTimes() == 0)
	// 		throw new MissingParameterException("interval between pickup times");

	// 	return pickupService.updatePickupSettings(settings);
	// }
	
}
