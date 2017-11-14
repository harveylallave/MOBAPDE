package valdez.lallave.dagdag.dlsu_profstopick.Beans_Model;

/**
 * Created by Carlo Valdez on 11/15/2017.
 */

public class Suggest {
    private String name;
    private String department;
    private String suggestedBy;

    public Suggest() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Suggest(String name, String department, String suggestedBy) {
        this.name = name;
        this.department = department;
        this.suggestedBy = suggestedBy;
    }

    public String getSuggestedBy() {
        return suggestedBy;
    }

    public void setSuggestedBy(String suggestedBy) {
        this.suggestedBy = suggestedBy;
    }
}
