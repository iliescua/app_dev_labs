/*
 * Course
 * Author: Andrew Iliescu
 * Date: 4/12/21
 * This class is used to create a Course object that has the desired
 * attributes of a course
 */

package edu.msoe.andrewiliescu;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {
    private String name;
    private int credits;
    private String grade;

    /**
     * Default constructor to generate an empty Course object
     */
    public Course() {
    }

    /**
     * Secondary constructor where parameters are passed in on Course creation
     *
     * @param courseName    name of the course
     * @param courseCredits number of credits the course is worth
     * @param courseGrade   grade received in the course
     */
    public Course(String courseName, int courseCredits, String courseGrade) {
        name = courseName;
        credits = courseCredits;
        grade = courseGrade;
    }

    /**
     * Constructor needed for implementing Parcelable
     *
     * @param in the passed in parcel
     */
    protected Course(Parcel in) {
        name = in.readString();
        credits = in.readInt();
        grade = in.readString();
    }

    /**
     * Creating Parcel of Course objects
     */
    public static final Creator<Course> CREATOR = new Creator<Course>() {
        /**
         * Creating Courses from the parcel
         * @param in passes in the parcel with all the data
         * @return the newly generated Courses
         */
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        /**
         * Method for creating an array of Courses
         * @param size size of the array
         * @return the generated Course array
         */
        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    /**
     * Method that needs to present because of implementing Parcelable
     *
     * @return just returns 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Method called to generate the parcel from a list of Courses
     *
     * @param dest  passing in the parcel to be written to
     * @param flags any flags that need to be set
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(credits);
        dest.writeString(grade);
    }

    public void setName(String className) {
        name = className;
    }

    public void setCredits(int numCredits) {
        credits = numCredits;
    }

    public void setGrade(String studentGrade) {
        grade = studentGrade;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public String getGrade() {
        return grade;
    }
}
