package test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import upcafe.dto.settings.TimeBlockDTO;
import upcafe.entity.settings.TimeBlock;
import upcafe.error.MissingParameterException;
import upcafe.repository.settings.BlockRepository;
import upcafe.repository.settings.WeekBlocksRepository;
import upcafe.service.CafeHoursService;
import upcafe.utils.TimeUtils;

@ExtendWith(MockitoExtension.class)
public class CafeHoursServiceTest {

	@InjectMocks
	private CafeHoursService hoursService;
	
	@Mock
	private WeekBlocksRepository weekRepository;
	
	@Mock
	private BlockRepository blockRepository;
	
	
	@Test
	public void getTimeBlocksForDay_ValidDate_NoBlocksReturned() {
		final LocalDate DATE = LocalDate.now();
		final LocalDate MONDAY_OF_WEEK = TimeUtils.getMondayOfWeek(DATE);
		
		when(blockRepository.getTimeBlocksForDay(DATE.getDayOfWeek().toString(), MONDAY_OF_WEEK)).thenReturn(new ArrayList<TimeBlock>());
		
		
		List<TimeBlockDTO> returnedBlocks = hoursService.getTimeBlocksForDay(DATE);
		
		assertNotNull(returnedBlocks);
		assertEquals(0, returnedBlocks.size());
	}
	
	@Test
	public void getTimeBlocksForDay_ValidDate_Successful() {
		final LocalDate DATE = LocalDate.now();
		final LocalDate MONDAY_OF_WEEK = TimeUtils.getMondayOfWeek(DATE);
		
		when(blockRepository.getTimeBlocksForDay(DATE.getDayOfWeek().toString(), MONDAY_OF_WEEK)).thenReturn(Arrays.asList(
				new TimeBlock.Builder(UUID.randomUUID().toString())
				.close(LocalTime.of(10, 0)).open(LocalTime.of(8, 0)).day("Tuesday").build(),
				new TimeBlock.Builder(UUID.randomUUID().toString())
				.close(LocalTime.of(10, 0)).open(LocalTime.of(8, 0)).day("Wednesday").build()
				));
		
		
		List<TimeBlockDTO> returnedBlocks = hoursService.getTimeBlocksForDay(DATE);
		assertNotNull(returnedBlocks);
		assertEquals(2, returnedBlocks.size());
	}
	
	@Test
	public void validateSaveRequest_ParametersPresent_Succeed() {
		TimeBlockDTO block = new TimeBlockDTO.Builder(UUID.randomUUID().toString())
				.close(LocalTime.of(10, 0, 0))
				.open(LocalTime.of(9, 0))
				.day("Monday")
				.build();
		
		assertTrue(hoursService.validateTimeBlockSaveRequest(block));
	}
	
	@Test
	public void validateSaveRequest_CloseParameterMissing_ExceptionThrown() {
		TimeBlockDTO block = new TimeBlockDTO.Builder(UUID.randomUUID().toString())
				.open(LocalTime.of(9, 0))
				.day("Monday")
				.build();
		
		assertThrows(MissingParameterException.class, () -> hoursService.validateTimeBlockSaveRequest(block));
	}
	
	@Test
	public void validateSaveRequest_OpenParameterMissing_ExceptionThrown() {
		TimeBlockDTO block = new TimeBlockDTO.Builder(UUID.randomUUID().toString())
				.close(LocalTime.of(9, 0))
				.day("Monday")
				.build();
		
		assertThrows(MissingParameterException.class, () -> hoursService.validateTimeBlockSaveRequest(block));
	}
	
	@Test
	public void validateSaveRequest_DayParameterMissing_ExceptionThrown() {
		TimeBlockDTO block = new TimeBlockDTO.Builder(UUID.randomUUID().toString())
				.close(LocalTime.of(10, 0, 0))
				.open(LocalTime.of(9, 0))
				.build();
		
		assertThrows(MissingParameterException.class, () -> hoursService.validateTimeBlockSaveRequest(block));
	}
	
	@Test
	public void saveNewBlock_Successful() {
		
		final LocalTime CLOSE = LocalTime.of(10, 0, 0);
		final LocalTime OPEN = LocalTime.of(9, 0, 0);
		
		TimeBlockDTO block = new TimeBlockDTO.Builder("")
				.close(CLOSE)
				.open(OPEN)
				.day("Monday")
				.build();
		
		TimeBlockDTO returnedBlock = hoursService.saveNewBlock(block, "Mon Jun 01 2020");
		
		
		assertNotNull(returnedBlock);
		
		assertNotEquals("", returnedBlock.getId());
		assertEquals(OPEN, returnedBlock.getOpen());
		assertEquals(CLOSE, returnedBlock.getClose());
		assertEquals("Monday", returnedBlock.getDay());
	}
	
	@Test
	public void saveNewBlock_ValidatorsFail_ExceptionThrown() {
		
		final LocalTime CLOSE = LocalTime.of(10, 0, 0);
		final LocalTime OPEN = LocalTime.of(9, 0, 0);
		
		TimeBlockDTO block = new TimeBlockDTO.Builder("")
				.close(CLOSE)
				.day("Monday")
				.build();
		
		assertThrows(MissingParameterException.class, () -> hoursService.saveNewBlock(block, "Mon Jun 01 2020"));
		
	}
}
