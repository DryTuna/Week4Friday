/*
 * Name: Duy Tran
 * Date: Feb 27th, 2015
 */

package assignment.week4.friday;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CopyStudents {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		//System.out.println("Driver Loaded.");
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/test", 
				"drytuna", "Pa$$word");
		//System.out.println("Database Connected.");
		
		Statement stmt = conn.createStatement();
		
		tableCreateInsert(stmt);
			
		String query = "select * from Student1";
		tablePrint(stmt, query);
			
		tableCopy(stmt);
			
		query = "select * from Student2";
		tablePrint(stmt, query);
		
		System.out.println();
		conn.close();
	}

	/**
	 * Name: tableCreateInsert
	 * @param stmt - a Statement object
	 * @throws SQLException
	 * Process: Create tables Student1 and Student2
	 * 			Fill Student1 with data
	 */
	private static void tableCreateInsert(Statement stmt) throws SQLException {
		stmt.executeUpdate("drop table if exists Student1");
		stmt.executeUpdate("drop table if exists Student2");
		
		String tb_student_1 = "create table Student1("
				+ "username varchar(50) not null,"
				+ "password varchar(50) not null,"
				+ "fullname varchar(100) not null,"
				+ "constraint student_username_pk primary key(username))";
		String tb_student_2 = "create table Student2("
				+ "username varchar(50) not null,"
				+ "password varchar(50) not null,"
				+ "firstname varchar(50),"
				+ "lastname varchar(50),"
				+ "constraint student_username_pk primary key(username))";
		
		stmt.executeUpdate(tb_student_1);
		stmt.executeUpdate(tb_student_2);
		
		stmt.executeUpdate("insert into Student1 values("
				+ "'asdf', 'asdf', 'Duy N Tran')");
		stmt.executeUpdate("insert into Student1 values("
				+ "'potus', 'potus', 'Barack Obama')");
		stmt.executeUpdate("insert into Student1 values("
				+ "'qwer', 'qwer', 'George Washington')");
		stmt.executeUpdate("insert into Student1 values("
				+ "'uiop', 'uiop', 'George W Bush')");
		stmt.executeUpdate("insert into Student1 values("
				+ "'zxcv', 'zxcv', 'Abraham Lincoln')");
	}

	/**
	 * Name: tablePrint()
	 * @param stmt - a Statement object
	 * @param query - a String with select statement
	 * @throws SQLException
	 * Process: Print the query results with organized spacing
	 */
	private static void tablePrint(Statement stmt, String query) throws SQLException {
		ResultSet results = stmt.executeQuery(query);
		
		// Printing the Attribute Names
		for (int i = 1; i <= results.getMetaData().getColumnCount(); i++)
			System.out.printf("%-20s",
					results.getMetaData().getColumnName(i).toUpperCase());
		
		System.out.println();
		
		// Printing the Rows
		while (results.next()) {
			for (int i = 1; i <= results.getMetaData().getColumnCount(); i++){
				System.out.printf("%-20s",results.getString(i));
			}
			System.out.println();
		}
		
		System.out.println();
		results.close();
	}
	
	/**
	 * Name: tableCopy()
	 * @param stmt - a Statement object
	 * @throws SQLException
	 * Process: Insert into table 2 with the parsed data
	 */
	private static void tableCopy(Statement stmt) throws SQLException {
		stmt.executeUpdate("delete from Student2");
		ArrayList<String[]> stu_list = new ArrayList<>();
		
		nameParsing(stmt, stu_list);
		
		for (String[] record: stu_list) {
			stmt.executeUpdate("insert into Student2 values("
					+ "'" + record[0] + "',"
					+ "'" + record[1] + "',"
					+ "'" + record[2] + "',"
					+ "'" + record[3] + "')");
		}
	}
	
	/**
	 * Name: nameParsing()
	 * @param stmt - a Statement object
	 * @param stu_list - each element is a parsed record in String[]
	 * @throws SQLException
	 * Process: Parse each record and load them into a String[]
	 * 			Add each parsed record to the ArrayList
	 */
	private static void nameParsing(Statement stmt, ArrayList<String[]> stu_list) 
							throws SQLException {
		ResultSet results = stmt.executeQuery("select * from Student1");
		
		while (results.next()) {
			String[] full_name = results.getString(3).split("\\s+");
			stu_list.add(new String[] {results.getString(1),
									   results.getString(2),
									   full_name[0],
									   full_name[full_name.length - 1]});
		}
		
		results.close();
	}
	
}
