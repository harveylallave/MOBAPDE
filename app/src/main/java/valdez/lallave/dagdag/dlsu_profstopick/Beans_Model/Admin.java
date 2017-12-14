package valdez.lallave.dagdag.dlsu_profstopick.Beans_Model;

public class Admin {
    private String email,
                   hashedPass;


    public Admin() {}

    public Admin(String email, String hashedPass) {
        this.email = email;
        this.hashedPass = hashedPass;
    }


    public Admin(int adminId, String email, String hashedPass) {
        this.email = email;
        this.hashedPass = hashedPass;
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



    @Override
    public String toString() {
        return "Admin{" +
                ", email='" + email + '\'' +
                '}';
    }
}
