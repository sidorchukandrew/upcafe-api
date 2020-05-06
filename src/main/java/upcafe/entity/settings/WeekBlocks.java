package upcafe.entity.settings;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

//@Entity
//@IdClass(WeekBlockId.class)
public class WeekBlocks {
	
	@Id
	private LocalDate weekOf;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	List<TimeBlock> timeBlocks;

	public static class Builder {
		private final LocalDate weekOf;
		private List<TimeBlock> timeBlocks;
		
		public Builder(LocalDate weekOf) {
			this.weekOf = weekOf;
		}

		public Builder timeBlocks(List<TimeBlock> timeBlocks) {
			this.timeBlocks = timeBlocks;
			return this;
		}

		public WeekBlocks build() {
			return new WeekBlocks(this);
		}
	}

	private WeekBlocks(Builder builder) {
		this.weekOf = builder.weekOf;
		this.timeBlocks = builder.timeBlocks;
	}


	public LocalDate getWeekOf() {
		return this.weekOf;
	}

	public void setWeekOf(LocalDate weekOf) {
		this.weekOf = weekOf;
	}

	public List<TimeBlock> getTimeBlocks() {
		return this.timeBlocks;
	}

	public void setTimeBlocks(List<TimeBlock> timeBlocks) {
		this.timeBlocks = timeBlocks;
	}

	@Override
	public String toString() {
		return "{" +
			" weekOf='" + weekOf + "'" +
			", timeBlocks='" + timeBlocks + "'" +
			"}";
	}
}
