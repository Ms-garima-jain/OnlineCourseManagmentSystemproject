package com.infobeans.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.infobeans.model.Course;

public class CourseDao {


	private static final String Url = "jdbc:mysql://localhost:3306/OnlineCourseEnrollmentSystem?useSSL=false&serverTimezone=UTC";
	private static final String password = "root";

	private static final String user = "root";
		
		 private Connection getConnection () throws SQLException
		{
			return DriverManager.getConnection(Url, user, password);
		}
		 // insert course 
		 public void addCourse(Course course) throws SQLException {
			    String sql = "INSERT INTO Course (courseName, duration, fees) VALUES (?, ?, ?)";
			    try (Connection con = getConnection();
			    		PreparedStatement pst = con.prepareStatement(sql)) {
			        pst.setString(1, course.getCourseName());
			        pst.setInt(2, course.getDuration());
			        pst.setDouble(3, course.getFees());
			     int rowaffected=   pst.executeUpdate();
			        if ( rowaffected > 0)
					 {
						 System.out.println ( "Data inserted successfully");
					 }
					 else
					 {
						 System.out.println ( " data doesnot inserted successfully");
					 }
			    }
			}
		 // read all list
		 public List<Course> getAllCourses() throws SQLException {
			    List<Course> courses = new ArrayList<>();
			    String sql = "SELECT * FROM Course";
			    try (Connection con = getConnection();
			    		Statement st = con.createStatement(); 
			    		ResultSet rs = st.executeQuery(sql)) {
			        while (rs.next()) {
			            int id = rs.getInt("course_id");
			            String name = rs.getString("courseName");
			            int duration = rs.getInt("duration");
			            double fees = rs.getDouble("fees");
			            courses.add(new Course(id, name, duration, fees));
			        }
			    }
			    return courses;
			}
		 // get course by id
		 public Course getCourseById(int courseId) throws SQLException {
			    String sql = "SELECT * FROM Course WHERE course_id = ?"; 
			    try (Connection con = getConnection(); 
			         PreparedStatement pst = con.prepareStatement(sql)) {
			        
			        pst.setInt(1, courseId); 
			        
			        ResultSet rs = pst.executeQuery(); 
			        if (rs.next()) {
			            return new Course(
			                rs.getInt("course_id"),      
			                rs.getString("courseName"),  
			                rs.getInt("duration"),        
			                rs.getDouble("fees")         
			            );		        }
			    }
			    return null; 
			}
		 // delete course
		 public void deleteCourse ( int courseId) throws SQLException
		 {
			 Scanner scn = new Scanner(System.in);
System.out.println ( " ❗️ Are you sure you want to delete  the course  eith id "+ courseId+ " ?(yes/no)");
String confirmation = scn.nextLine().toLowerCase();
if (confirmation.equals("yes") ||confirmation.equals("y"))
{
    String sql = "DELETE FROM Course WHERE course_id = ?";
try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql))
{
	pst.setInt (1, courseId);
	int rowsAffected = pst.executeUpdate();

    if (rowsAffected > 0)
    {
        System.out.println("✅ Course deleted successfully!");
    }
    else
    {
        System.out.println("❌ Course not found! Course ID: " + courseId);
    }
	}
	}
else
{
    System.out.println("❌ Course deletion canceled.");

	}
		 }

// update all 
public void updateAllCourseDetails(String courseName, int duration, double fees, int courseId) throws SQLException {
    String sql = "UPDATE Course SET courseName = ?, duration = ?, fees = ? WHERE course_id = ?";

    try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setString(1, courseName);   
        pst.setInt(2, duration);        
        pst.setDouble(3, fees);        
        pst.setInt(4, courseId);  
        int rowsAffected = pst.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("✅ All course details updated successfully!");
        } 
        else {
            System.out.println("❌ Course not found!");
        }
    }
}
// update by user choice 
public void updateCourseField(int courseId, int fieldChoice, String newValue) throws SQLException {
    String sql = "";
    switch (fieldChoice) {
        case 1: // Update courseName
            sql = "UPDATE Course SET courseName = ? WHERE course_id = ?";
            break;
        case 2: // Update duration
            sql = "UPDATE Course SET duration = ? WHERE course_id = ?";
            break;
        case 3: // Update fees
            sql = "UPDATE Course SET fees = ? WHERE course_id = ?";
            break;
        default:
            System.out.println("❌ Invalid field choice.");
            return;
    }

    try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
        if (fieldChoice == 1) { // Course name (String)
            pst.setString(1, newValue);
        } else if (fieldChoice == 2) { // Duration (int)
            try {
                pst.setInt(1, Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid duration format. Please enter an integer.");
                return;
            }
        } else if (fieldChoice == 3) { // Fees (double)
            try {
                pst.setDouble(1, Double.parseDouble(newValue));
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid fees format. Please enter a number.");
                return;
            }
        }
        pst.setInt(2, courseId);
        int rowsAffected = pst.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("✅ Course field updated successfully!");
        } else {
            System.out.println("❌ Course not found!");
        }
    }
}
}


