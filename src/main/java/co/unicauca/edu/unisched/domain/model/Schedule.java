package co.unicauca.edu.unisched.domain.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Represents a specific class schedule.
 * Defines the day of the week, start time, and end time.
 */
public class Schedule {
    private final DayOfWeek dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Schedule(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DayOfWeek getDayOfWeek() {return dayOfWeek;}
    public LocalTime getStartTime() {return startTime;}
    public LocalTime getEndTime() {return endTime;}

    /**
     * Checks if this schedule overlaps with another.
     *
     * @param other the other schedule to compare
     * @return true if there is an overlap, false otherwise
     */
    public boolean overlapsWith(Schedule other) {
        if (!this.dayOfWeek.equals(other.dayOfWeek)) return false;
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }
}
