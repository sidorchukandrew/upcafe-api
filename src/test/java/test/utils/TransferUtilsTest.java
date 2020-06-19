package test.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import upcafe.dto.catalog.ImageDTO;
import upcafe.dto.catalog.ModifierDTO;
import upcafe.dto.catalog.ModifierListDTO;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Item;
import upcafe.entity.catalog.Modifier;
import upcafe.entity.catalog.ModifierList;
import upcafe.error.MissingParameterException;
import upcafe.utils.TransferUtils;

@ExtendWith(MockitoExtension.class)
public class TransferUtilsTest {
	
	private static Image mockImage() {
		return new Image.Builder("ANW92B2FB2939JFWASD")
				.name("Croissant Item Image")
				.batchUpdateId("SXLLQP10023EJ9E92N9921")
				.lastUpdated(LocalDateTime.of(2020, 6, 20, 10, 12))
				.url("http://www.google.com")
				.caption("An image of a croissant")
				.build();
	}
	
	private static ModifierList mockModifierList() {
		return new ModifierList.Builder("QPPCNCEOO102NNCEMMQLWMWWD")
				.image(mockImage())
				.batchUpdateId("cbbmmllq010OSNnc02nqwmx")
				.items(new HashSet<>())
				.modifiers(new ArrayList<>())
				.name("Cheese")
				.selectionType("MULTIPLE")
				.build();
	}
	
	@Test
	public void toModifierListDTOs_NonEmptySetPassed_Success() {
		
		final String BATCH_UPDATE_ID = "WOQD020FNWEF9N29I3R";
		final LocalDateTime LAST_UPDATED = LocalDateTime.of(2020, 6, 20, 10, 12);
		
		Set<ModifierList> modifierListEntities = new HashSet<>();
		
		modifierListEntities.add(mockModifierList());
		modifierListEntities.add(new ModifierList.Builder("AP120FNV99N390EF010J9RGFN2")
				.batchUpdateId(BATCH_UPDATE_ID)
				.modifiers(Arrays.asList(
						  new Modifier.Builder("APWKCQOQWDNBIWOQN").image(mockImage()).inStock(true).onByDefault(true).modifierList(mockModifierList())
						  .batchUpdateId(BATCH_UPDATE_ID).lastUpdated(LAST_UPDATED).name("American").price(.5).build(),
						  new Modifier.Builder("pqpqmqiod2092nfe").image(mockImage()).inStock(true).onByDefault(true).modifierList(mockModifierList())
						  .batchUpdateId(BATCH_UPDATE_ID).lastUpdated(LAST_UPDATED).name("Provolone").price(.6).build()
						))
				.image(mockImage())
				.name("Cheese")
				.selectionType("SINGLE")
				.items(new HashSet<>())
				.lastUpdated(LAST_UPDATED)
				.build());
		
		Set<ModifierListDTO> modifierListDTOs = TransferUtils.toModifierListDTOs(modifierListEntities);
		
		assertNotNull(modifierListDTOs);
		
		assertEquals(modifierListEntities.size(), modifierListDTOs.size());
	}
	
	@Test
	public void toModifierListDTOs_EmptySetPassed_Success() {
		Set<ModifierListDTO> modifierLists = TransferUtils.toModifierListDTOs(new HashSet<ModifierList>());
		
		assertNotNull(modifierLists);
		
		assertEquals(0, modifierLists.size());
	}
	
	@Test
	public void toModifierListDTOs_NullPassed_NullReturned() {
		assertNull(TransferUtils.toModifierListDTOs(null));
	}
	
	@Test
	public void toModifierListDTO_NonEmptyModifiers_Success() {

		final String BATCH_UPDATE_ID = "WOQD020FNWEF9N29I3R";
		final LocalDateTime LAST_UPDATED = LocalDateTime.of(2020, 6, 20, 10, 12);
		
		ModifierList modifierListEntity = new ModifierList.Builder("OWJF0WWE00129I9923RNNVWE1")
				.image(mockImage())
				.name("Cheese")
				.batchUpdateId("OEFN9123RNIWVNWEO")
				.items(new HashSet<Item>())
				.selectionType("MULTIPLE")
				.modifiers(Arrays.asList(
						  new Modifier.Builder("APWKCQOQWDNBIWOQN").image(mockImage()).inStock(true).onByDefault(true).modifierList(mockModifierList())
						  .batchUpdateId(BATCH_UPDATE_ID).lastUpdated(LAST_UPDATED).name("American").price(.5).build(),
						  new Modifier.Builder("pqpqmqiod2092nfe").image(mockImage()).inStock(true).onByDefault(true).modifierList(mockModifierList())
						  .batchUpdateId(BATCH_UPDATE_ID).lastUpdated(LAST_UPDATED).name("Provolone").price(.6).build()
						))
				.lastUpdated(LocalDateTime.of(2020, 6, 20, 10, 12))
				.build();
		
		ModifierListDTO modifierListDTO = TransferUtils.toModifierListDTO(modifierListEntity);
		
		assertNotNull(modifierListDTO);
		
		assertEquals(modifierListEntity.getId(), modifierListDTO.getId());
		assertEquals(modifierListEntity.getName(), modifierListDTO.getName());
		assertEquals(modifierListEntity.getSelectionType(), modifierListDTO.getSelectionType());
		assertEquals(modifierListEntity.getImage().getUrl(), modifierListDTO.getImage().getUrl());
		
		assertEquals(modifierListEntity.getModifiers().size(), modifierListDTO.getModifiers().size());
		
		for(int i = 0; i < modifierListEntity.getModifiers().size(); ++i) {
			assertEquals(modifierListEntity.getModifiers().get(i).getId(), modifierListDTO.getModifiers().get(i).getId());
		}
	}
	
	@Test
	public void toModifierListDTO_EmptyModifiers_Success() {
		ModifierList modifierListEntity = new ModifierList.Builder("OWJF0WWE00129I9923RNNVWE1")
				.image(mockImage())
				.name("Cheese")
				.batchUpdateId("OEFN9123RNIWVNWEO")
				.items(new HashSet<Item>())
				.selectionType("MULTIPLE")
				.modifiers(new ArrayList<Modifier>())
				.lastUpdated(LocalDateTime.of(2020, 6, 20, 10, 12))
				.build();
		
		ModifierListDTO modifierListDTO = TransferUtils.toModifierListDTO(modifierListEntity);
		
		assertNotNull(modifierListDTO);
		
		assertEquals(modifierListEntity.getId(), modifierListDTO.getId());
		assertEquals(modifierListEntity.getName(), modifierListDTO.getName());
		assertEquals(modifierListEntity.getSelectionType(), modifierListDTO.getSelectionType());
		assertEquals(modifierListEntity.getImage().getUrl(), modifierListDTO.getImage().getUrl());
		assertEquals(modifierListEntity.getModifiers().size(), modifierListDTO.getModifiers().size());
	}
	
	@Test
	public void toModifierListDTO_MissingIdParameter_ExceptionThrown() {
		ModifierList modifierListEntity = new ModifierList.Builder("")
			.image(mockImage())
			.name("Cheese")
			.batchUpdateId("OEFN9123RNIWVNWEO")
			.items(new HashSet<Item>())
			.selectionType("MULTIPLE")
			.modifiers(new ArrayList<Modifier>())
			.lastUpdated(LocalDateTime.of(2020, 6, 20, 10, 12))
			.build();
		
		assertThrows(MissingParameterException.class, () -> TransferUtils.toModifierListDTO(modifierListEntity));
	}
	
	@Test
	public void toModifierListDTO_NullPassed_NullReturned() {
		assertNull(TransferUtils.toModifierListDTO(null));
	}
	
	@Test
	public void toModifierDTOs_NullPassed_NUllReturned() {
		assertNull(TransferUtils.toModifierDTOs(null));
	}
	
	@Test
	public void toModifierDTOs_EmptyListPassed_Success() {
		List<ModifierDTO> modifiers = TransferUtils.toModifierDTOs(new ArrayList<Modifier>());
		assertNotNull(modifiers);
		assertEquals(0, modifiers.size());
	}
	
	@Test
	public void toModifierDTOs_ListOfModifiersPassed_Success() {
		final String BATCH_UPDATE_ID = "WOQD020FNWEF9N29I3R";
		final LocalDateTime LAST_UPDATED = LocalDateTime.of(2020, 6, 20, 10, 12);
		
		List<Modifier> modifiers = Arrays.asList(
		  new Modifier.Builder("APWKCQOQWDNBIWOQN").image(mockImage()).inStock(true).onByDefault(true).modifierList(mockModifierList())
		  .batchUpdateId(BATCH_UPDATE_ID).lastUpdated(LAST_UPDATED).name("American").price(.5).build(),
		  new Modifier.Builder("pqpqmqiod2092nfe").image(mockImage()).inStock(true).onByDefault(true).modifierList(mockModifierList())
		  .batchUpdateId(BATCH_UPDATE_ID).lastUpdated(LAST_UPDATED).name("Provolone").price(.6).build()
		);
		
		List<ModifierDTO> modifierDTOs = TransferUtils.toModifierDTOs(modifiers);
		
		assertNotNull(modifierDTOs);
		
		assertEquals(modifiers.size(), modifierDTOs.size());
		
		for(int i = 0; i < modifiers.size(); ++i) {
			assertEquals(modifiers.get(i).getId(), modifierDTOs.get(i).getId());
		}
	}
	
	@Test
	public void toModifierDTO_MissingId_ExceptionThrown() {
		Modifier modifierEntity = new Modifier.Builder(null)
				.batchUpdateId("PVNEVN29023RBVBVJXAKMRV8E01")
				.inStock(true)
				.name("American")
				.lastUpdated(LocalDateTime.of(2020, 6, 20, 10, 12))
				.onByDefault(true)
				.price(0)
				.image(mockImage())
				.modifierList(mockModifierList())
				.build();
		
		assertThrows(MissingParameterException.class, () -> TransferUtils.toModifierDTO(modifierEntity));
	}
	
	@Test
	public void toModifierDTO_NullPassed_NullReturned() {
		assertNull(TransferUtils.toModifierDTO(null));
	}
	
	@Test
	public void toModifierDTO_ModifierEntityPassed_Success() {
		final String ID = "AHEIOWEBWEIFHWEF919EFNWEF";
		final String NAME = "American";
		
		Modifier modifierEntity = new Modifier.Builder(ID)
				.batchUpdateId("PVNEVN29023RBVBVJXAKMRV8E01")
				.inStock(true)
				.name(NAME)
				.lastUpdated(LocalDateTime.of(2020, 6, 20, 10, 12))
				.onByDefault(true)
				.price(0)
				.image(mockImage())
				.modifierList(mockModifierList())
				.build();
		
		ModifierDTO modifierDTO = TransferUtils.toModifierDTO(modifierEntity);
		
		assertNotNull(modifierDTO);
		
		
		assertEquals(ID, modifierDTO.getId());
		assertEquals(NAME, modifierDTO.getName());
		assertEquals(modifierEntity.getPrice(), modifierDTO.getPrice());
		assertEquals(modifierEntity.isInStock(), modifierDTO.getInStock());
		assertEquals(modifierEntity.isOnByDefault(), modifierDTO.getOnByDefault());
		assertEquals("QPPCNCEOO102NNCEMMQLWMWWD", modifierDTO.getModifierListId());
		assertEquals(mockImage().getUrl(), modifierDTO.getImage().getUrl());
	}

	@Test
	public void toImageDTO_NullImageEntity_NullReturned() {
		ImageDTO image = TransferUtils.toImageDTO(null);
		
		assertNull(image);
	}
	
	@Test
	public void toImageDTO_ImageEntityPassed_Success() {
		final String ID = "AHBERE99193NFRUDLD";
		final String NAME = "Croissant Item Image";
		final String URL = "http://www.google.com";
		final String CAPTION = "An image of a croissant";
		
		Image imageEntity = new Image.Builder(ID)
				.batchUpdateId("ANBE991002NNFI02RUHBNF")
				.caption(CAPTION)
				.lastUpdated(LocalDateTime.of(2020, 6, 20, 10, 12))
				.url(URL)
				.name(NAME)
				.build();
		
		ImageDTO imageDTO = TransferUtils.toImageDTO(imageEntity);
		
		assertNotNull(imageDTO);
		assertEquals(CAPTION, imageDTO.getCaption());
		assertEquals(URL, imageDTO.getUrl());
		assertEquals(NAME, imageDTO.getName());
	}
	
	@Test
	public void toImageDTO_NameMissing_Success() {
		final String ID = "AHBERE99193NFRUDLD";
		final String URL = "http://www.google.com";
		final String CAPTION = "An image of a croissant";
		
		Image imageEntity = new Image.Builder(ID)
				.batchUpdateId("ANBE991002NNFI02RUHBNF")
				.caption(CAPTION)
				.lastUpdated(LocalDateTime.of(2020, 6, 20, 10, 12))
				.url(URL)
				.build();
		
		ImageDTO imageDTO = TransferUtils.toImageDTO(imageEntity);
		
		assertNotNull(imageDTO);
		assertEquals(CAPTION, imageDTO.getCaption());
		assertEquals(URL, imageDTO.getUrl());
		assertEquals(null, imageDTO.getName());
	}
	
	@Test
	public void toImageDTO_CaptionMissing_Success() {
		final String ID = "AHBERE99193NFRUDLD";
		final String URL = "http://www.google.com";
		final String NAME = "Croissant Item Image";
		
		Image imageEntity = new Image.Builder(ID)
				.batchUpdateId("ANBE991002NNFI02RUHBNF")
				.name(NAME)
				.lastUpdated(LocalDateTime.of(2020, 6, 20, 10, 12))
				.url(URL)
				.build();
		
		ImageDTO imageDTO = TransferUtils.toImageDTO(imageEntity);
		
		assertNotNull(imageDTO);
		assertEquals(null, imageDTO.getCaption());
		assertEquals(URL, imageDTO.getUrl());
		assertEquals(NAME, imageDTO.getName());
	}
	
	@Test
	public void toImageDTO_UrlMissing_ExceptionThrown() {
		final String ID = "AHBERE99193NFRUDLD";
		final String NAME = "Croissant Item Image";
		final String CAPTION = "An image of a croissant";
		
		Image imageEntity = new Image.Builder(ID)
				.batchUpdateId("ANBE991002NNFI02RUHBNF")
				.name(NAME)
				.lastUpdated(LocalDateTime.of(2020, 6, 20, 10, 12))
				.caption(CAPTION)
				.build();
		
		assertThrows(MissingParameterException.class, () -> TransferUtils.toImageDTO(imageEntity));
	}
}
