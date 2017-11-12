package valdez.lallave.dagdag.dlsu_profstopick.Beans_Model;

public class Student {
    private String email,
                   hashedPass;
    private int studentId;


    public Student() {}

    public Student(String email, String hashedPass) {
        this.email = email;
        this.hashedPass = hashedPass;
    }


    public Student(int studentId, String email, String hashedPass) {
        this.email = email;
        this.hashedPass = hashedPass;
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPass() {
        return hashedPass;
    }

    public void setHashedPass(String hashedPass) {
        this.hashedPass = hashedPass;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", email='" + email + '\'' +
                '}';
    }
}
