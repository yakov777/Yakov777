package stu.cn.ua.kit181.model;
import android.os.*;
import java.util.*;
public class Student implements Parcelable {
    public static final List<String> GROUPS = Arrays.asList("KIT-181" , "KI-161", "KI-162", "KI-163", "KIт-181", "МРАп-191" );
    public static final int MAX_VARIANT = 20;
    private String firstName;
    private String lastName;
    private String group;
    public Student(String firstName, String lastName, String group) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getGroup() {
        return group;
    }
    public boolean isValid() {
        return !isEmpty(firstName)
                && !isEmpty(lastName)
                && !isEmpty(group);
    }
    private boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
    // --- auto-generated
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.group);
    }
    protected Student(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.group = in.readString();
    }
    public static final Creator<Student> CREATOR =
            new Creator<Student>() {
                @Override
                public Student createFromParcel(Parcel source) {
                    return new Student(source);
                }
                @Override
                public Student[] newArray(int size) {
                    return new Student[size];
                }
            };
}
