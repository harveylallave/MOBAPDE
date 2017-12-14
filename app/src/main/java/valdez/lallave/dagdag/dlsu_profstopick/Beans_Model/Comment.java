package valdez.lallave.dagdag.dlsu_profstopick.Beans_Model;

public class Comment {
    private String title,
                   body,
                   reviewer;

    private float  rate;
    private String teacher;


    public Comment(String title, String body, float rate, String reviewer, String teacherID){
        this.title     = title;
        this.body      = body;
        this.rate      = rate;
        this.reviewer = reviewer;
        this.teacher = teacherID;
    }

    public Comment() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getBody() {

        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "title='" + title + '\'' +
                ", rbComment=" + rate +
                '}';
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacherID) {
        this.teacher = teacherID;
    }


    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }
}