package nl.utwente.di.notebridge.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Review {
    int sid;
    int tid;
    int rating;
    String review;
    String reviewer;

    public Review() {}

    public Review(String review, int sid, int tid, int rating) {
        this.review = review;
        this.sid = sid;
        this.tid = tid;
        this.rating = rating;
    }

    public Review(String review, String reviewer, int rating) {
        this.review = review;
        this.reviewer = reviewer;
        this.rating = rating;
    }

    public int getTid() {
        return tid;
    }

    public String getReviewer(){return reviewer;}

    public int getSid() {
        return sid;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }
}
