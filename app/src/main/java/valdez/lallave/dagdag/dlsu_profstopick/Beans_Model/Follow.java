package valdez.lallave.dagdag.dlsu_profstopick.Beans_Model;

/**
 * Created by DAGDAG on 12/14/2017.
 */

public class Follow {
    private String student;
    private String teacher;

    public Follow() {}

    public Follow(String student, String teacher) {
        this.student = student;
        this.teacher = teacher;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
