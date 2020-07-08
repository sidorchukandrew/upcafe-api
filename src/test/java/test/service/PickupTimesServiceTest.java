package test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import upcafe.dto.settings.PickupSettingsDTO;
import upcafe.dto.settings.PickupTime;
import upcafe.dto.settings.TimeBlockDTO;
import upcafe.entity.settings.PickupSettings;
import upcafe.error.MissingParameterException;
import upcafe.repository.settings.PickupSettingsRepository;
import upcafe.service.CafeHoursService;
import upcafe.service.PickupTimesService;

@ExtendWith(MockitoExtension.class)
public class PickupTimesServiceTest {

	@InjectMocks
	private PickupTimesService pickupTimesService;

	@Mock
	private PickupSettingsRepository pickupSettingsRepository;

	@Mock
	private CafeHoursService hoursService;

	private final int INTERVAL = 10;
	
//	@Test
//	public void getAvailablePickupTimes_TwoBlocksInOneDay() {
//		PickupSettings pickupSettings = new PickupSettings.Builder("1").intervalBetweenPickupTimes(INTERVAL)
//				.pickupOnOpen(false).pickupOnClose(false).build();
//
//		List<PickupTime> expectedPickupTimes = Arrays.asList(new PickupTime.Builder(LocalTime.of(20, 10)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 20)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 30)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 40)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 50)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 0)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 10)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 20)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 30)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 40)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 50)).build(),
//				new PickupTime.Builder(LocalTime.of(22, 10)).build(),
//				new PickupTime.Builder(LocalTime.of(22, 20)).build(),
//				new PickupTime.Builder(LocalTime.of(22, 30)).build(),
//				new PickupTime.Builder(LocalTime.of(22, 40)).build(),
//				new PickupTime.Builder(LocalTime.of(22, 50)).build());
//
//		when(hoursService.getTimeBlocksForDay(Mockito.any(LocalDate.class)))
//				.thenReturn(Arrays.asList(
//						new TimeBlockDTO.Builder("1").day("Monday").open(LocalTime.of(20, 0)).close(LocalTime.of(22, 0)).build(),
//						new TimeBlockDTO.Builder("2").day("Monday").open(LocalTime.of(22, 0)).close(LocalTime.of(23, 0)).build()
//		));
//
//		when(pickupSettingsRepository.findById("1")).thenReturn(Optional.of(pickupSettings));
//
//		List<PickupTime> pickupTimes = pickupTimesService.getAvailablePickupTimes();
//		
//		System.out.println(pickupTimes);
//		
//		assertNotNull(pickupTimes);
//		
//		assertEquals(LocalTime.of(20, 10), pickupTimes.get(0).getTime());
//		assertEquals(LocalTime.of(22, 50), pickupTimes.get(pickupTimes.size() - 1).getTime());
//
//		for (int i = 0; i < expectedPickupTimes.size(); ++i)
//			assertEquals(expectedPickupTimes.get(i).getTime(), pickupTimes.get(i).getTime());	
//	}
//
//	@Test
//	public void getAvailablePickupTimes_PassedClose_EmptyReturned() {
//		PickupSettings pickupSettings = new PickupSettings.Builder("1").intervalBetweenPickupTimes(INTERVAL)
//				.pickupOnOpen(false).pickupOnClose(false).build();
//
//		when(hoursService.getTimeBlocksForDay(Mockito.any(LocalDate.class)))
//				.thenReturn(Arrays.asList(new TimeBlockDTO.Builder("1").day("Monday").close(LocalTime.of(10, 0))
//						.open(LocalTime.of(8, 0)).build()));
//
//		when(pickupSettingsRepository.findById("1")).thenReturn(Optional.of(pickupSettings));
//
//		List<PickupTime> pickupTimes = pickupTimesService.getAvailablePickupTimes();
//
//		assertNotNull(pickupTimes);
//		assertEquals(0, pickupTimes.size());
//	}
//
//	@Test
//	public void getAvailablePickupTimes_NotOpenedYet_NoPickupOnCloseOrOpen() {
//		PickupSettings pickupSettings = new PickupSettings.Builder("1").intervalBetweenPickupTimes(INTERVAL)
//				.pickupOnOpen(false).pickupOnClose(false).build();
//
//		List<PickupTime> expectedPickupTimes = Arrays.asList(new PickupTime.Builder(LocalTime.of(20, 10)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 20)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 30)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 40)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 50)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 0)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 10)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 20)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 30)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 40)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 50)).build());
//
//		when(hoursService.getTimeBlocksForDay(Mockito.any(LocalDate.class)))
//				.thenReturn(Arrays.asList(new TimeBlockDTO.Builder("1").day("Monday").close(LocalTime.of(22, 0))
//						.open(LocalTime.of(20, 0)).build()));
//
//		when(pickupSettingsRepository.findById("1")).thenReturn(Optional.of(pickupSettings));
//
//		List<PickupTime> pickupTimes = pickupTimesService.getAvailablePickupTimes();
//
//		assertNotNull(pickupTimes);
//		assertEquals(expectedPickupTimes.size(), pickupTimes.size());
//
//		for (int i = 0; i < expectedPickupTimes.size(); ++i)
//			assertEquals(expectedPickupTimes.get(i).getTime(), pickupTimes.get(i).getTime());
//	}
//
//	@Test
//	public void getAvailabledPickupTimes_NotOpenedYet_PickupOnCloseAndOpen() {
//		PickupSettings pickupSettings = new PickupSettings.Builder("1").intervalBetweenPickupTimes(INTERVAL)
//				.pickupOnOpen(true).pickupOnClose(true).build();
//
//		List<PickupTime> expectedPickupTimes = Arrays.asList(new PickupTime.Builder(LocalTime.of(20, 0)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 10)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 20)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 30)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 40)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 50)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 0)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 10)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 20)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 30)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 40)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 50)).build(),
//				new PickupTime.Builder(LocalTime.of(22, 0)).build());
//
//		when(hoursService.getTimeBlocksForDay(Mockito.any(LocalDate.class)))
//				.thenReturn(Arrays.asList(new TimeBlockDTO.Builder("1").day("Monday").close(LocalTime.of(22, 0))
//						.open(LocalTime.of(20, 0)).build()));
//
//		when(pickupSettingsRepository.findById("1")).thenReturn(Optional.of(pickupSettings));
//
//		List<PickupTime> pickupTimes = pickupTimesService.getAvailablePickupTimes();
//
//		assertNotNull(pickupTimes);
//
//		assertEquals(LocalTime.of(20, 0), pickupTimes.get(0).getTime());
//		assertEquals(LocalTime.of(22, 0), pickupTimes.get(pickupTimes.size() - 1).getTime());
//
//		assertEquals(expectedPickupTimes.size(), pickupTimes.size());
//
//		for (int i = 0; i < expectedPickupTimes.size(); ++i)
//			assertEquals(expectedPickupTimes.get(i).getTime(), pickupTimes.get(i).getTime());
//	}
//
//	@Test
//	public void getAvailablePickupTimes_NotOpenedYetAndPickupOnCloseOnly_Successful() {
//		PickupSettings pickupSettings = new PickupSettings.Builder("1").intervalBetweenPickupTimes(INTERVAL)
//				.pickupOnOpen(false).pickupOnClose(true).build();
//
//		List<PickupTime> expectedPickupTimes = Arrays.asList(new PickupTime.Builder(LocalTime.of(20, 10)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 20)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 30)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 40)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 50)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 0)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 10)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 20)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 30)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 40)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 50)).build(),
//				new PickupTime.Builder(LocalTime.of(22, 0)).build());
//
//		when(hoursService.getTimeBlocksForDay(Mockito.any(LocalDate.class)))
//				.thenReturn(Arrays.asList(new TimeBlockDTO.Builder("1").day("Monday").close(LocalTime.of(22, 0))
//						.open(LocalTime.of(20, 0)).build()));
//
//		when(pickupSettingsRepository.findById("1")).thenReturn(Optional.of(pickupSettings));
//
//		List<PickupTime> pickupTimes = pickupTimesService.getAvailablePickupTimes();
//
//		assertNotNull(pickupTimes);
//
//		assertEquals(LocalTime.of(22, 0), pickupTimes.get(pickupTimes.size() - 1).getTime());
//		assertEquals(expectedPickupTimes.size(), pickupTimes.size());
//
//		for (int i = 0; i < expectedPickupTimes.size(); ++i)
//			assertEquals(expectedPickupTimes.get(i).getTime(), pickupTimes.get(i).getTime());
//	}
//
//	@Test
//	public void getAvailablePickupTimes_NotOpenedYetPickupOnOpenOnly_Successful() {
//
//		PickupSettings pickupSettings = new PickupSettings.Builder("1").intervalBetweenPickupTimes(INTERVAL)
//				.pickupOnOpen(true).pickupOnClose(false).build();
//
//		List<PickupTime> expectedPickupTimes = Arrays.asList(new PickupTime.Builder(LocalTime.of(20, 0)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 10)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 20)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 30)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 40)).build(),
//				new PickupTime.Builder(LocalTime.of(20, 50)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 0)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 10)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 20)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 30)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 40)).build(),
//				new PickupTime.Builder(LocalTime.of(21, 50)).build());
//
//		when(hoursService.getTimeBlocksForDay(Mockito.any(LocalDate.class)))
//				.thenReturn(Arrays.asList(new TimeBlockDTO.Builder("1").day("Monday").close(LocalTime.of(22, 0))
//						.open(LocalTime.of(20, 0)).build()));
//
//		when(pickupSettingsRepository.findById("1")).thenReturn(Optional.of(pickupSettings));
//
//		List<PickupTime> pickupTimes = pickupTimesService.getAvailablePickupTimes();
//
//		assertNotNull(pickupTimes);
//
//		assertEquals(expectedPickupTimes.get(0).getTime(), pickupTimes.get(0).getTime());
//		assertNotEquals(LocalTime.of(22, 0), pickupTimes.get(pickupTimes.size() - 1).getTime());
//
//		assertEquals(expectedPickupTimes.size(), pickupTimes.size());
//
//		for (int i = 0; i < expectedPickupTimes.size(); ++i)
//			assertEquals(expectedPickupTimes.get(i).getTime(), pickupTimes.get(i).getTime());
//	}
//
//	@Test
//	public void updatePickupSettings_MissingParameterId_ExceptionThrown() {
//		PickupSettingsDTO updateSettingsRequest = new PickupSettingsDTO.Builder().id(null)
//				.intervalBetweenPickupTimes(INTERVAL).build();
//
//		assertThrows(MissingParameterException.class,
//				() -> pickupTimesService.updatePickupSettings(updateSettingsRequest));
//	}
//
//	@Test
//	public void updatePickupSettings_MissingParameterInterval_ExceptionThrown() {
//		PickupSettingsDTO updateSettingsRequest = new PickupSettingsDTO.Builder().id("1").build();
//
//		assertThrows(MissingParameterException.class,
//				() -> pickupTimesService.updatePickupSettings(updateSettingsRequest));
//	}
//
//	@Test
//	public void updatePickupSettings_ValidParameters_Successful() {
//		PickupSettingsDTO updateSettingsRequest = new PickupSettingsDTO.Builder().id("1").intervalBetweenPickupTimes(15)
//				.pickupOnOpen(true).pickupOnClose(true).build();
//
//		PickupSettings savedSettings = new PickupSettings.Builder("1").intervalBetweenPickupTimes(15).pickupOnOpen(true)
//				.pickupOnClose(true).build();
//
//		when(pickupSettingsRepository.save(savedSettings)).thenReturn(savedSettings);
//
//		PickupSettingsDTO updatedSettings = pickupTimesService.updatePickupSettings(updateSettingsRequest);
//
//		assertNotNull(updatedSettings);
//
//		assertEquals("1", updatedSettings.getId());
//		assertEquals(updateSettingsRequest.getIntervalBetweenPickupTimes(),
//				updatedSettings.getIntervalBetweenPickupTimes());
//		assertTrue(updatedSettings.getPickupOnClose());
//		assertTrue(updatedSettings.getPickupOnOpen());
//	}
//
//	@Test
//	public void getPickupSettings_NotSetYet_Successful() {
//
//		PickupSettings pickupSettingsDB = new PickupSettings.Builder("1").intervalBetweenPickupTimes(INTERVAL)
//				.pickupOnClose(false).pickupOnOpen(false).build();
//
//		when(pickupSettingsRepository.findById("1")).thenReturn(Optional.empty());
//		when(pickupSettingsRepository.save(pickupSettingsDB)).thenReturn(pickupSettingsDB);
//
//		PickupSettingsDTO pickupSettings = pickupTimesService.getPickupSettings();
//
//		assertNotNull(pickupSettings, "Pickup settings was null. Non null value was expected.");
//
//		assertEquals(10, pickupSettings.getIntervalBetweenPickupTimes(),
//				"Expected: 10, Actual: " + pickupSettings.getIntervalBetweenPickupTimes());
//		assertFalse(pickupSettings.getPickupOnClose());
//		assertFalse(pickupSettings.getPickupOnOpen());
//		assertEquals("1", pickupSettings.getId());
//	}
//
//	@Test
//	public void getPickupSettings_AlreadySet_Successful() {
//		PickupSettings pickupSettingsAlreadySaved = new PickupSettings.Builder("1").intervalBetweenPickupTimes(15)
//				.pickupOnClose(true).pickupOnOpen(true).build();
//
//		when(pickupSettingsRepository.findById("1")).thenReturn(Optional.of(pickupSettingsAlreadySaved));
//
//		PickupSettingsDTO pickupSettings = pickupTimesService.getPickupSettings();
//
//		assertNotNull(pickupSettings);
//		assertEquals("1", pickupSettings.getId());
//		assertEquals(15, pickupSettings.getIntervalBetweenPickupTimes());
//		assertEquals(true, pickupSettings.getPickupOnClose());
//		assertEquals(true, pickupSettings.getPickupOnOpen());
//	}
}
