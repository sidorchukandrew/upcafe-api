package upcafe.dto.settings;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public class WeekBlockDTO {
 
    @JsonFormat(pattern = "EEE MMM dd yyyy")
    private LocalDate weekOf;

    private List<TimeBlockDTO> blocks;

    public static class Builder {
        private final LocalDate weekOf;
        private List<TimeBlockDTO> blocks;

        public Builder(LocalDate weekOf) {
            this.weekOf = weekOf;
        }

        public Builder blocks(List<TimeBlockDTO> blocks) {
            this.blocks = blocks;
            return this;
        }
 
        public WeekBlockDTO build() {
            return new WeekBlockDTO(this);
        }
    }

    private WeekBlockDTO(Builder builder) {
        this.weekOf = builder.weekOf;
        this.blocks = builder.blocks;
    }

    public WeekBlockDTO() {

    }


    public LocalDate getWeekOf() {
        return this.weekOf;
    }

    public void setWeekOf(LocalDate weekOf) {
        this.weekOf = weekOf;
    }

    public List<TimeBlockDTO> getBlocks() {
        return this.blocks;
    }

    public void setBlock(List<TimeBlockDTO> blocks) {
        this.blocks = blocks;
    }

    @Override
    public String toString() {
        return "{" +
            " weekOf='" + weekOf + "'" +
            ", blocks='" + blocks + "'" +
            "}";
    }
}