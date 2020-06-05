package upcafe.entity.feedback;

import upcafe.dto.feedback.BugDTO;
import upcafe.dto.users.UserDTO;
import upcafe.entity.signin.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Bug {

    @ManyToOne
    private User reporter;
    private LocalDateTime dateReported;
    private String platform;
    private String browser;

    private String expectation;
    private String actual;
    private String page;
    private String extraInformation;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public static class Builder {
        private User reporter;
        private LocalDateTime dateReported;
        private String platform;
        private String browser;

        private String expectation;
        private String actual;
        private String page;
        private String extraInformation;

        private int id;

        public Builder reporter(User reporter) {
            this.reporter = reporter;
            return this;
        }

        public Builder dateReported(LocalDateTime dateReported) {
            this.dateReported = dateReported;
            return this;
        }

        public Builder platform(String platform) {
            this.platform = platform;
            return this;
        }

        public Builder browser(String browser) {
            this.browser = browser;
            return this;
        }

        public Builder expectation(String expectation) {
            this.expectation = expectation;
            return this;
        }

        public Builder actual(String actual) {
            this.actual = actual;
            return this;
        }

        public Builder page(String page) {
            this.page = page;
            return this;
        }

        public Builder extraInformation(String extraInformation) {
            this.extraInformation = extraInformation;
            return this;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Bug build() {
            return new Bug(this);
        }
    }

    private Bug(Builder builder) {
        this.id = builder.id;
        this.actual = builder.actual;
        this.expectation = builder.expectation;
        this.browser = builder.browser;
        this.dateReported = builder.dateReported;
        this.extraInformation = builder.extraInformation;
        this.page = builder.page;
        this.platform = builder.platform;
        this.reporter = builder.reporter;
    }

    public Bug() {
    }

    public User getReporter() {
        return reporter;
    }

    public LocalDateTime getDateReported() {
        return dateReported;
    }

    public String getPlatform() {
        return platform;
    }

    public String getBrowser() {
        return browser;
    }

    public String getExpectation() {
        return expectation;
    }

    public String getActual() {
        return actual;
    }

    public String getPage() {
        return page;
    }

    public String getExtraInformation() {
        return extraInformation;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "BugDTO{" +
                "reporter=" + reporter +
                ", dateReported=" + dateReported +
                ", platform='" + platform + '\'' +
                ", browser='" + browser + '\'' +
                ", expectation='" + expectation + '\'' +
                ", actual='" + actual + '\'' +
                ", page='" + page + '\'' +
                ", extraInformation='" + extraInformation + '\'' +
                ", id=" + id +
                '}';
    }
}
