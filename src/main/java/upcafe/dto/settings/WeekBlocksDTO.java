package upcafe.dto.settings;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public class WeekBlocksDTO {

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

        public WeekBlocksDTO build() {
            return new WeekBlocksDTO(this);
        }
    }

    private WeekBlocksDTO(Builder builder) {
        this.weekOf = builder.weekOf;
        this.blocks = builder.blocks;
    }

    public WeekBlocksDTO() {

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