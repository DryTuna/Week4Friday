/*
 * Name: Duy Tran
 * Date: Feb 27th, 2015
 */

package assignment.week4.friday;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Scanner;

public class PopulateSalary {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		//System.out.println("Driver Loaded.");
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/test", 
				"drytuna", "Pa$$word");
		//System.out.println("Database Connected.");
		
		Statement stmt = conn.createStatement();

		stmt.executeUpdate("drop table if exists Salary");
		
		String tb_salary = "Create table Salary ("
				+ "firstName varchar(20), "
				+ "lastName varchar(20), "
				+ "rank varchar(15), " 
				+ "salary decimal(10,2), "
				+ "constraint first_name_pk primary key(firstName))";
		stmt.executeUpdate(tb_salary);
		
		try (Scanner sc = new Scanner(new FileInputStream("Salary.txt"))) {
			DecimalFormat df = new DecimalFormat("#.00");
			
			while (sc.hasNext()) {
				String[] record = sc.nextLine().trim().split("\\s+");
				String insert_st = "insert into Salary values("
						+ "'" + record[0] + "',"
						+ "'" + record[1] + "',"
						+ "'" + record[2] + "',"
						+ "" + df.format(Double.parseDouble(record[3])) + ")";
				stmt.executeUpdate(insert_st);
			}
		}
		catch (FileNotFoundException fnfe) {
			System.out.println("'Salary.txt' can't be found on the first level"
					+ " of the project folder.");
		}

		tablePrint(stmt, "select * from Salary");
		
		conn.close();
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
			System.out.printf("%-20s",results.getMetaData().getColumnName(i).toUpperCase());
		
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

}
