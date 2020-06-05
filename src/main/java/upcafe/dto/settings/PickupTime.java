package upcafe.dto.settings;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PickupTime {

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    public PickupTime() {
    }

    public static class Builder {
        private LocalTime time;

        public Builder(LocalTime time) {
            this.time = time;
        }

        public PickupTime build() {
            return new PickupTime(this);
        }
    }

    private PickupTime(Builder builder) {
        this.time = builder.time;
    }


    public LocalTime getTime() {
        return this.time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "{" +
                " time='" + time + "'" +
                "}";
    }
}