package valdez.lallave.dagdag.dlsu_profstopick.Beans_Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Teacher {
    private String name,
                   department;
    private int icon,
                teacherId;

    public Teacher() {}

    public Teacher(String name, String department) {
        this.name = name;
        this.department = department;
    }

    public Teacher(String name, int icon){
        this.name = name;
        this.icon = icon;
    }

    public Teacher(String name, int icon, int teacherId) {
        this.name = name;
        this.icon = icon;
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}