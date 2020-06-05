package upcafe.dto.feedback;

import upcafe.dto.users.UserDTO;

import java.time.LocalDateTime;

public class BugDTO {

    private UserDTO reporter;
    private LocalDateTime dateReported;
    private String platform;
    private String browser;

    private String expectation;
    private String actual;
    private String page;
    private String extraInformation;
    private int id;

    public static class Builder {
        private UserDTO reporter;
        private LocalDateTime dateReported;
        private String platform;
        private String browser;

        private String expectation;
        private String actual;
        private String page;
        private String extraInformation;

        private int id;

        public Builder reporter(UserDTO reporter) {
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

        public BugDTO build() {
            return new BugDTO(this);
        }
    }

    private BugDTO(Builder builder) {
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

    public BugDTO() {
    }

    public UserDTO getReporter() {
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
