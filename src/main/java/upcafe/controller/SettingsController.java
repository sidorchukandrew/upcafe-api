package upcafe.controller;

import java.time.LocalDate;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import upcafe.dto.settings.PickupSettingsDTO;
import upcafe.dto.settings.PickupTime;
import upcafe.dto.settings.TimeBlockDTO;
import upcafe.dto.settings.WeekBlocksDTO;
import upcafe.service.CafeHoursService;
import upcafe.service.PickupTimesService;
import upcafe.utils.TimeUtils;

@RestController
@CrossOrigin(origins = "*")
public class SettingsController {

    @Autowired
    private PickupTimesService pickupService;
    @Autowired
    private CafeHoursService hoursService;

    @GetMapping(path = "/cafe/hours", params = "weekOf")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF')")
    public WeekBlocksDTO getBlocksForWeek(@RequestParam("weekOf") String day) {
    	System.out.println("Received GET request @ '/cafe/hours' for the week of : " + day);
        LocalDate weekOf = TimeUtils.toLocalDate(day);
        return hoursService.getBlocksForWeek(weekOf);
    }

    @PutMapping("/cafe/hours")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF')")
    public TimeBlockDTO updateBlock(@RequestBody TimeBlockDTO blockToUpdate) {
        return hoursService.updateBlock(blockToUpdate);
    }

    @PostMapping(path = "/cafe/hours", params = "weekOf")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF')")
    public TimeBlockDTO saveNewBlock(@RequestBody TimeBlockDTO timeBlock, @RequestParam("weekOf") String weekOf) {
    	System.out.println("Received POST request @ '/cafe/hours' to save TimeBlock : " + timeBlock + "\n Week of : " + weekOf);
        return hoursService.saveNewBlock(timeBlock, weekOf);
    }


    @DeleteMapping(path = "/cafe/hours", params = "blockId")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF')")
    public boolean deleteBlock(@RequestParam("blockId") String blockId) {
        hoursService.deleteBlock(blockId);
        return true;
    }

    @GetMapping(path = "/cafe/hours", params = "search")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public List<PickupTime> getTimesBySearchQuery(@RequestParam("search") String searchQuery) {
    	System.out.println("Received GET request @ '/cafe/hours' where the searchQuery was : " + searchQuery );
        if (searchQuery.toLowerCase().compareTo("available") == 0)
            return pickupService.getAvailablePickupTimes();

        return null;
    }

    @GetMapping(path = "/cafe/hours", params = "day")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF')")
    public List<TimeBlockDTO> getBlocksForDay(@RequestParam(name = "day") String day) {
        LocalDate dayDate = TimeUtils.toLocalDate(day);
        return hoursService.getTimeBlocksForDay(dayDate);
    }

    @GetMapping(path = "/cafe/settings/pickup")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF')")
    public PickupSettingsDTO getPickupSettings() {
        return pickupService.getPickupSettings();
    }

    @PutMapping(path = "/cafe/settings/pickup")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF')")
    public PickupSettingsDTO updatePickupSettings(@RequestBody PickupSettingsDTO settings) {
        return pickupService.updatePickupSettings(settings);
    }

}
