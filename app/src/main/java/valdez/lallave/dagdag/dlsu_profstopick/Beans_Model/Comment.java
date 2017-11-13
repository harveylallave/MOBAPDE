package valdez.lallave.dagdag.dlsu_profstopick.Beans_Model;

public class Comment {
    private String title,
                   body,
                   reviewer;

    private float  rate;
    private int    id,
                   teacherID;


    public Comment(String title, String body, float rate, String reviewer, int teacherID){
        this.title     = title;
        this.body      = body;
        this.rate      = rate;
        this.reviewer = reviewer;
        this.teacherID = teacherID;
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

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }
}