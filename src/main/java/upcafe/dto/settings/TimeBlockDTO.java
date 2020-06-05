package upcafe.dto.settings;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TimeBlockDTO {

    private String id;

    private String day;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime open;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime close;

    public TimeBlockDTO() {
    }

    public static class Builder {
        private final String id;
        private String day;
        private LocalTime open;
        private LocalTime close;

        public Builder(String id) {
            this.id = id;
        }

        public Builder day(String day) {
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

    public String getDay() {
        return this.day;
    }

    public LocalTime getOpen() {
        return this.open;
    }

    public LocalTime getClose() {
        return this.close;
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