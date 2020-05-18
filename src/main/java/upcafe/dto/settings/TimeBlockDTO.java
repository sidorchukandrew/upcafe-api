package upcafe.dto.settings;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TimeBlockDTO {
    
    private String id;

    @JsonFormat(pattern = "EEE MMM dd yyyy")
    private LocalDate day;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime open;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime close;

    public TimeBlockDTO() { }

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

        public TimeBlockDTO build() {
            return new TimeBlockDTO(this);
        }

    }

    private TimeBlockDTO(Builder builder) {
        this.id = builder.id;
        this.close = builder.close;
        this.open = builder.open;
        this.day = builder.day;
    }


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