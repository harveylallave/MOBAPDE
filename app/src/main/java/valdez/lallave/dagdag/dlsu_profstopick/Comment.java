package valdez.lallave.dagdag.dlsu_profstopick;

public class Comment {
    private String title,
                   body;
    private float rate;


    public Comment(String title, String body, float rate){
        this.title = title;
        this.body = body;
        this.rate = rate;
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
}