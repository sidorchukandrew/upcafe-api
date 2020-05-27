package upcafe.entity.feedback;

import upcafe.dto.feedback.FeatureDTO;
import upcafe.dto.users.UserDTO;
import upcafe.entity.signin.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class FeatureRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User reporter;
    private LocalDateTime dateReported;
    private String page;
    private String description;

    public static class Builder {
        private User reporter;
        private int id;
        private LocalDateTime dateReported;
        private String page;
        private String description;

        public Builder reporter(User reporter) {
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

        public FeatureRequest build() {
            return new FeatureRequest(this);
        }
    }

    private FeatureRequest(Builder builder) {
        this.dateReported = builder.dateReported;
        this.description = builder.description;
        this.id = builder.id;
        this.page = builder.page;
        this.reporter = builder.reporter;
    }

    public FeatureRequest () { }

    public User getReporter() {
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
        return "FeatureRequest{" +
                "reporter=" + reporter +
                ", id=" + id +
                ", dateReported=" + dateReported +
                ", page='" + page + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
