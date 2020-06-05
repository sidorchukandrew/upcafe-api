package upcafe.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upcafe.dto.settings.PickupSettingsDTO;
import upcafe.dto.settings.PickupTime;
import upcafe.dto.settings.TimeBlockDTO;
import upcafe.entity.settings.PickupSettings;
import upcafe.error.MissingParameterException;
import upcafe.repository.settings.PickupSettingsRepository;
import upcafe.utils.TimeUtils;

@Service
public class PickupTimesService {

    @Autowired
    private PickupSettingsRepository pickupRepository;

    @Autowired
    private CafeHoursService hoursService;

    public PickupSettingsDTO getPickupSettings() {

        Optional<PickupSettings> pickupSettingsOpt = pickupRepository.findById("1");

        PickupSettings pickupSettings;

        if (pickupSettingsOpt.isPresent()) {
            pickupSettings = pickupSettingsOpt.get();
            pickupSettingsOpt = null;
        } else {
            pickupSettings = generateSettingsIfNotSaved();
        }

        return new PickupSettingsDTO.Builder().id(pickupSettings.getId())
                .intervalBetweenPickupTimes(pickupSettings.getIntervalBetweenPickupTimes())
                .pickupOnClose(pickupSettings.isPickupOnClose()).pickupOnOpen(pickupSettings.isPickupOnOpen()).build();
    }

    private PickupSettings generateSettingsIfNotSaved() {
        return pickupRepository.save(new PickupSettings.Builder("1").intervalBetweenPickupTimes(10).pickupOnOpen(false)
                .pickupOnClose(false).build());
    }

    public PickupSettingsDTO updatePickupSettings(PickupSettingsDTO settings) {
        if (validateUpdatePickupSettingsRequest(settings)) {
            PickupSettings settingsUpdated = new PickupSettings.Builder(settings.getId())
                    .intervalBetweenPickupTimes(settings.getIntervalBetweenPickupTimes())
                    .pickupOnClose(settings.isPickupOnClose())
                    .pickupOnOpen(settings.isPickupOnOpen())
                    .build();

            pickupRepository.save(settingsUpdated);

            return settings;
        }

        return null;
    }

    private boolean validateUpdatePickupSettingsRequest(PickupSettingsDTO settings) {
        if (settings.getId() == null)
            throw new MissingParameterException("id");

        if (settings.getIntervalBetweenPickupTimes() == 0)
            throw new MissingParameterException("interval between pickup times");

        return true;
    }

    public List<PickupTime> getAvailablePickupTimes() {

        List<PickupTime> pickupTimes = new ArrayList<PickupTime>();

        List<TimeBlockDTO> availableBlocks = getSortedFutureBlocksForToday();

        Optional<PickupSettings> pickupSettingsOpt = pickupRepository.findById("1");
        PickupSettings pickupSettings;

        if (pickupSettingsOpt.isPresent())
            pickupSettings = pickupSettingsOpt.get();
        else
            pickupSettings = generateSettingsIfNotSaved();

        availableBlocks.forEach(block -> {

            LocalTime runningTime = block.getOpen();

            // Check if we should add open time to the available pickup times
            if (pickupSettings.isPickupOnOpen() && runningTime.isBefore(
                    TimeUtils.getTimeNowWithIntervalPadding(pickupSettings.getIntervalBetweenPickupTimes()))) {

                pickupTimes.add(new PickupTime.Builder(block.getOpen()).build());
                runningTime = runningTime.plusMinutes(pickupSettings.getIntervalBetweenPickupTimes());
            }

            while (runningTime.isBefore(block.getClose())) {

                if (runningTime.isBefore(
                        TimeUtils.getTimeNowWithIntervalPadding(pickupSettings.getIntervalBetweenPickupTimes()))) {
                    runningTime = runningTime.plusMinutes(pickupSettings.getIntervalBetweenPickupTimes());
                } else {
                    pickupTimes.add(new PickupTime.Builder(runningTime).build());
                    runningTime = runningTime.plusMinutes(pickupSettings.getIntervalBetweenPickupTimes());
                }
            }

            if (pickupSettings.isPickupOnClose()) {
                pickupTimes.add(new PickupTime.Builder(block.getClose()).build());
            }
        });

        return pickupTimes;
    }

    private List<TimeBlockDTO> retrieveFutureBlocks(List<TimeBlockDTO> allBlocks) {
        return allBlocks.stream().filter(block -> {

            return block.getClose().isAfter(TimeUtils.getTimeNow());

        }).collect(Collectors.toList());
    }

    private List<TimeBlockDTO> getSortedFutureBlocksForToday() {
        List<TimeBlockDTO> allBlocksForTheDay = hoursService.getTimeBlocksForDay(LocalDate.now());
        List<TimeBlockDTO> futureBlocks = retrieveFutureBlocks(allBlocksForTheDay);

        // Sort the remaining time blocks in ascending time
        futureBlocks = futureBlocks.stream().sorted((a, b) -> {
            boolean result = a.getOpen().isBefore(b.getOpen());
            if (result)
                return -1;
            else
                return 1;
        }).collect(Collectors.toList());

        return futureBlocks;
    }
}
