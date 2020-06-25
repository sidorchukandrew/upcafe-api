package upcafe.entity.settings;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class WeekBlocks {

    @Id
    private Date weekOf;

    @OneToMany(mappedBy = "weekOf")
    List<TimeBlock> timeBlocks;

    public static class Builder {
        private final Date weekOf;
        private List<TimeBlock> timeBlocks;

        public Builder(Date weekOf) {
            this.weekOf = weekOf;
        }

        public Builder timeBlocks(List<TimeBlock> timeBlocks) {
            this.timeBlocks = timeBlocks;
            return this;
        }

        public WeekBlocks build() {
            return new WeekBlocks(this);
        }
    }

    private WeekBlocks(Builder builder) {
        this.weekOf = builder.weekOf;
        this.timeBlocks = builder.timeBlocks;
    }

    public WeekBlocks() {
    }

    public Date getWeekOf() {
        return this.weekOf;
    }

    public void setWeekOf(Date weekOf) {
        this.weekOf = weekOf;
    }

    public List<TimeBlock> getTimeBlocks() {
        return this.timeBlocks;
    }

    public void setTimeBlocks(List<TimeBlock> timeBlocks) {
        this.timeBlocks = timeBlocks;
    }

    @Override
    public String toString() {
        return "{" +
                " weekOf='" + weekOf + "'" +
                ", timeBlocks='" + timeBlocks + "'" +
                "}";
    }
}
