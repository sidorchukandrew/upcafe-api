package upcafe.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upcafe.entity.settings.PickupSettings;
import upcafe.entity.settings.TimeBlock;
import upcafe.model.settings.AvailablePickupTimes;
import upcafe.repository.settings.PickupSettingsRepository;
import upcafe.utils.TimeUtils;

@Service
public class PickupTimesService {

	@Autowired
	private PickupSettingsRepository pickupRepository;

	@Autowired
	private CafeHoursService hoursService;

	public PickupSettings getPickupSettings() {
		return pickupRepository.findById("1").get();
	}

	public PickupSettings updatePickupSettings(PickupSettings settings) {
		return pickupRepository.save(settings);
	}

	public AvailablePickupTimes getAvailablePickupTimes() {

		AvailablePickupTimes available = new AvailablePickupTimes();
		List<String> pickupTimes = new ArrayList<String>();
//		
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy");
//		LocalDate date = LocalDate.now();
//		List<TimeBlock> allBlocksForTheDay = hoursService.getTimeBlocksForDay(date.format(formatter));

		List<TimeBlock> allBlocksForTheDay = getTestTimes();

		List<TimeBlock> futureBlocks = retrieveFutureBlocks(allBlocksForTheDay);
		futureBlocks = futureBlocks.stream().sorted((a, b) -> {
			boolean result = TimeUtils.getTime(a.getOpen()).isBefore(TimeUtils.getTime(b.getOpen()));
			if (result)
				return -1;
			else
				return 1;
		}).collect(Collectors.toList());

		PickupSettings pickupSettings = pickupRepository.findById("1").get();

		futureBlocks.forEach(block -> {

			LocalTime runningTime = TimeUtils.getTime(block.getOpen());

			if (pickupSettings.isPickupOnOpen() && runningTime.isAfter(
					TimeUtils.getTimeNowWithIntervalPadding(pickupSettings.getIntervalBetweenPickupTimes()))) {
				pickupTimes.add(TimeUtils.getTimeString(runningTime));
				runningTime = runningTime.plusMinutes(pickupSettings.getIntervalBetweenPickupTimes());
			}

			while (runningTime.isBefore(TimeUtils.getTime(block.getClose()))) {

				if (runningTime.isBefore(
						TimeUtils.getTimeNowWithIntervalPadding(pickupSettings.getIntervalBetweenPickupTimes()))) {
					runningTime = runningTime.plusMinutes(pickupSettings.getIntervalBetweenPickupTimes());
				} else {
					pickupTimes.add(TimeUtils.getTimeString(runningTime));
					runningTime = runningTime.plusMinutes(pickupSettings.getIntervalBetweenPickupTimes());
				}
			}

			if (pickupSettings.isPickupOnClose()) {
				pickupTimes.add(block.getClose());
			}
		});
		
		System.out.println(pickupTimes);
		System.out.println(pickupSettings);
		
		available.setAvailableTimes(pickupTimes);

		return available;
	}

	private List<TimeBlock> retrieveFutureBlocks(List<TimeBlock> allBlocks) {
		return allBlocks.stream().filter(block -> {

			return TimeUtils.getTime(block.getClose()).isAfter(TimeUtils.getTimeNow());

		}).collect(Collectors.toList());
	}

	private List<TimeBlock> getTestTimes() {
		List<TimeBlock> blocks = new ArrayList<TimeBlock>();

		blocks.add(new TimeBlock(null, null, "12:00", "13:00"));
		blocks.add(new TimeBlock(null, null, "13:00", "15:00"));

		return blocks;
	}

}
