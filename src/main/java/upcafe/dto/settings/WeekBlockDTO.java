package upcafe.dto.settings;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class WeekBlockDTO {
 
    @JsonFormat(pattern = "EEE MMM dd yyyy")
    private LocalDate weekOf;

    private TimeBlockDTO block;

    public static class Builder {
        private final LocalDate weekOf;
        private TimeBlockDTO block;

        public Builder(LocalDate weekOf) {
            this.weekOf = weekOf;
        }

        public Builder block(TimeBlockDTO block) {
            this.block = block;
            return this;
        }
 
        public WeekBlockDTO build() {
            return new WeekBlockDTO(this);
        }
    }

    private WeekBlockDTO(Builder builder) {
        this.weekOf = builder.weekOf;
        this.block = builder.block;
    }

    public WeekBlockDTO() {

    }


    public LocalDate getWeekOf() {
        return this.weekOf;
    }

    public void setWeekOf(LocalDate weekOf) {
        this.weekOf = weekOf;
    }

    public TimeBlockDTO getBlock() {
        return this.block;
    }

    public void setBlock(TimeBlockDTO block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "{" +
            " weekOf='" + weekOf + "'" +
            ", block='" + block + "'" +
            "}";
    }
}