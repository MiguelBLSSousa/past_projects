package nl.utwente.di.notebridge.model;

import sql.Database;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Lesson extends Availability {
    private int tid;
    private double cost;
    private boolean isPaid;

    public Lesson(int dayOfWeek, int weekOfYear, int year, String startTime, String endTime, int tid, double cost, boolean isPaid) {
        super(dayOfWeek, weekOfYear, year, startTime, endTime);
        this.tid = tid;
        this.cost = cost;
        this.isPaid = isPaid;
    }

    public static List<Lesson> toLessonList(Array sqlArr) throws SQLException {
        String[] sArr = (String[]) sqlArr.getArray();
        List<Lesson> lessons = new ArrayList<>();
        for (String s : sArr) {
            String[] sLesson = s.split(Database.SPLIT_REGEX);
            if (sLesson.length < 8) {
                continue;
            }
            try {
                Lesson lesson = new Lesson(Integer.parseInt(sLesson[3]),
                        Integer.parseInt(sLesson[4]),
                        Integer.parseInt(sLesson[5]),
                        sLesson[6],
                        sLesson[7],
                        Integer.parseInt(sLesson[0]),
                        Double.parseDouble(sLesson[1]),
                        Boolean.parseBoolean(sLesson[2]));
                lessons.add(lesson);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return lessons;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
