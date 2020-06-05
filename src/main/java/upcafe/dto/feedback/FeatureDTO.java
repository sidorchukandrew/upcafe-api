package upcafe.dto.feedback;

import upcafe.dto.users.UserDTO;

import java.time.LocalDateTime;

public class FeatureDTO {

    private UserDTO reporter;
    private int id;
    private LocalDateTime dateReported;
    private String page;
    private String description;

    public static class Builder {
        private UserDTO reporter;
        private int id;
        private LocalDateTime dateReported;
        private String page;
        private String description;

        public Builder reporter(UserDTO reporter) {
            this.reporter = reporter;
            return this;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder dateReported(LocalDateTime dateReported) {
            this.dateReported = dateReported;
            return this;
        }

        public Builder page(String page) {
            this.page = page;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public FeatureDTO build() {
            return new FeatureDTO(this);
        }
    }

    private FeatureDTO(Builder builder) {
        this.dateReported = builder.dateReported;
        this.description = builder.description;
        this.id = builder.id;
        this.page = builder.page;
        this.reporter = builder.reporter;
    }

    public FeatureDTO() {
    }

    public UserDTO getReporter() {
        return reporter;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDateReported() {
        return dateReported;
    }

    public String getPage() {
        return page;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "FeatureDTO{" +
                "reporter=" + reporter +
                ", id=" + id +
                ", dateReported=" + dateReported +
                ", page='" + page + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
