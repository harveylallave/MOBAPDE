package valdez.lallave.dagdag.dlsu_profstopick.Beans_Model;

import android.os.Parcel;
import android.os.Parcelable;


public class Teacher implements Parcelable{

    private String name,
                   department;
    private int    icon,
                   teacherId,
                   nReviews;
    private float  aveRating = (float) 2.5;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(teacherId);
        dest.writeString(name);
        dest.writeString(department);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Teacher> CREATOR = new Parcelable.Creator<Teacher>() {
        public Teacher createFromParcel(Parcel in) {
            return new Teacher(in);
        }

        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Teacher(Parcel in) {
        teacherId  = in.readInt();
        name       = in.readString();
        department = in.readString();
    }

    public float getAveRating() {
        return aveRating;
    }

    public void setAveRating(float aveRating) {
        this.aveRating = aveRating;
    }

    public int getnReviews() {
        return nReviews;
    }

    public void setnReviews(int nReviews) {
        this.nReviews = nReviews;
    }
}