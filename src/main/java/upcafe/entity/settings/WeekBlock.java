package upcafe.entity.settings;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

//@Entity
//@IdClass(WeekBlockId.class)
public class WeekBlock {
	@Id
	@Column(name = "week_of")
	private LocalDate weekOf;
	
	@Id
	@Column(name = "block_id")
	private String blockId;

	public WeekBlock(LocalDate weekOf, String blockId) {
		super();
		this.weekOf = weekOf;
		this.blockId = blockId;
	}

	public WeekBlock() {
		super();
	}

	public String getWeekOf() {
		return weekOf;
	}

	public void setWeekOf(String weekOf) {
		this.weekOf = weekOf;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	@Override
	public String toString() {
		return "WeekBlock [weekOf=" + weekOf + ", blockId=" + blockId + "]";
	}	
}
