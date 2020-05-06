package upcafe.entity.settings;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

//@Entity
public class TimeBlock {

	@Id
	@Column(name = "id", length = 36)
	private String id;
	private LocalDate day;
	private LocalTime open;
	private LocalTime close;

	@ManyToOne(cascade = {CascadeType.ALL})
	private WeekBlocks weekOf;
	
	public static class Builder {
		private final String id;
		private LocalDate day;
		private LocalTime open;
		private LocalTime close;
		private WeekBlocks weekOf;
		
		public Builder(String id) {
			this.id = id;
		}

		public Builder day(LocalDate day) {
			this.day = day;
			return this;
		}

		public Builder open(LocalTime open) {
			this.open = open;
			return this;
		}

		public Builder close(LocalTime close) {
			this.close = close;
			return this;
		}

		public Builder weekOf(WeekBlocks weekOf) {
			this.weekOf = weekOf;
			return this;
		}

		public TimeBlock build() {
			return new TimeBlock(this);
		}
	}

	private TimeBlock(Builder builder) {
		this.id = builder.id;
		this.day = builder.day;
		this.open = builder.open;
		this.close = builder.close;
		this.weekOf = builder.weekOf;
	}
	
	public TimeBlock() { }


	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getDay() {
		return this.day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public LocalTime getOpen() {
		return this.open;
	}

	public void setOpen(LocalTime open) {
		this.open = open;
	}

	public LocalTime getClose() {
		return this.close;
	}

	public void setClose(LocalTime close) {
		this.close = close;
	}

	public WeekBlocks getWeekOf() {
		return this.weekOf;
	}

	public void setWeekOf(WeekBlocks weekOf) {
		this.weekOf = weekOf;
	}

	@Override
	public String toString() {
		return "{" +
			" id='" + id + "'" +
			", day='" + day + "'" +
			", open='" + open + "'" +
			", close='" + close + "'" +
			", weekOf='" + weekOf + "'" +
			"}";
	}
}
