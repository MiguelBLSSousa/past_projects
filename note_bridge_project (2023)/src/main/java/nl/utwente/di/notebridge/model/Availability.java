package nl.utwente.di.notebridge.model;

import sql.Database;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Availability {

    int dayOfWeek;
    int weekOfYear;
    int year;
    String startTime;
    String endTime;
    boolean open;

    public Availability() {
    }

    public Availability(int dayOfWeek, int weekOfYear, int year, String startTime, String endTime) {
        this.dayOfWeek = dayOfWeek;
        this.weekOfYear = weekOfYear;
        this.year = year;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Availability(int dayOfWeek, int weekOfYear, int year, String startTime, String endTime, boolean open) {
        this.dayOfWeek = dayOfWeek;
        this.weekOfYear = weekOfYear;
        this.year = year;
        this.startTime = startTime;
        this.endTime = endTime;
        this.open = open;
    }

    public Availability(int dayOfWeek, int weekOfYear, String startTime) {
        this.dayOfWeek = dayOfWeek;
        this.weekOfYear = weekOfYear;
        this.startTime = startTime;
    }

    public static List<Availability> toAvailabilitiesList(Array sqlArr) throws SQLException {
        String[] sArr = (String[]) sqlArr.getArray();
        List<Availability> availabilities = new ArrayList<>();
        for (String s : sArr) {
            String[] sAvailability = s.split(Database.SPLIT_REGEX);
            if (sAvailability.length < 5) {
                continue;
            }
            if (sAvailability.length == 5) {
                try {
                    Availability availability = new Availability(Integer.parseInt(sAvailability[0]),
                            Integer.parseInt(sAvailability[1]),
                            Integer.parseInt(sAvailability[2]),
                            sAvailability[3],
                            sAvailability[4]);
                    availabilities.add(availability);
                } catch (NumberFormatException e) {

                }
            } else if (sAvailability.length == 6) {
                try {
                    Availability availability = new Availability(Integer.parseInt(sAvailability[0]),
                            Integer.parseInt(sAvailability[1]),
                            Integer.parseInt(sAvailability[2]),
                            sAvailability[3],
                            sAvailability[4],
                            sAvailability[5].equals("open"));
                    availabilities.add(availability);
                } catch (NumberFormatException e) {

                }
            }
        }
        return availabilities;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
