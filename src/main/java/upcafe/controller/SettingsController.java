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

import upcafe.entity.settings.TimeBlock;
import upcafe.entity.settings.WeekBlock;
import upcafe.service.CafeHoursService;

@RestController
@CrossOrigin(origins = "*")
public class SettingsController {
	
	@Autowired private CafeHoursService hoursService;
	
	@GetMapping(path = "/cafe/hours", params="weekOf")
	public List<TimeBlock> getBlocksFor(@RequestParam(name = "weekOf") String weekOf) {
		return hoursService.getBlocksForWeek(weekOf);
	}
	
	@PutMapping("/cafe/hours")
	public TimeBlock updateBlock(@RequestBody upcafe.model.settings.WeekBlock weekBlock) {
		return hoursService.updateBlock(weekBlock);
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

}
