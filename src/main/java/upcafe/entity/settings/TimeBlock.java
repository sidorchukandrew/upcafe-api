package upcafe.entity.settings;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

//@Entity(name = "time_block")
public class TimeBlock {

	@Id
	@Column(name = "id", length = 36)
	private String id;
	private LocalDate day;
	private LocalTime open;
	private LocalTime close;
	
	public static class Builder {
		private final String id;
		private LocalDate day;
		private LocalTime open;
		private LocalTime close;
		
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

		public TimeBlock build() {
			return new TimeBlock(this);
		}
	}

	private TimeBlock(Builder builder) {
		this.id = builder.id;
		this.day = builder.day;
		this.open = builder.open;
		this.close = builder.close;
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

	@Override
	public String toString() {
		return "{" +
			" id='" + id + "'" +
			", day='" + day + "'" +
			", open='" + open + "'" +
			", close='" + close + "'" +
			"}";
	}

	
}
