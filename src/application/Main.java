/*
 * Name: Duy Tran
 * Date: Feb 27th, 2015
 */

package application;
	
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {
	// Create controls.
	private Label status = new Label("No Connection");
	
	// The Statement for processing queries.
	private Statement stmt;
	
	@Override
	public void start(Stage primaryStage) {
		/* CREATE BUTTONS */
		Button btn_view = new Button("View");
		Button btn_insert = new Button("Insert");
		Button btn_update = new Button("Update");
		Button btn_clear = new Button("Clear");
		
		/* CREATE LABEL and TEXTFIELD */
		Label lb_id = new Label("ID ");
		TextField tf_id = new TextField();
		tf_id.setPromptText("e.g. 1234");
		
		Label lb_first = new Label("First Name ");
		TextField tf_first = new TextField();
		tf_first.setPromptText("e.g. Dry");
		
		Label lb_last = new Label("Last Name ");
		TextField tf_last = new TextField();
		tf_last.setPromptText("e.g. Tuna");
		
		Label lb_middle = new Label("MI ");
		TextField tf_middle = new TextField();
		
		Label lb_address = new Label("Address ");
		TextField tf_address = new TextField();
		tf_address.setPromptText("e.g. 9876 Sylvanas Ln.");
		
		Label lb_city = new Label("City ");
		TextField tf_city = new TextField();
		tf_city.setPromptText("e.g. Azeroth");
		
		Label lb_state = new Label("State ");
		TextField tf_state = new TextField();
		tf_state.setPromptText("e.g FL");
		
		Label lb_phone = new Label("Telephone ");
		TextField tf_phone = new TextField();
		tf_phone.setPromptText("e.g. 1234567890");
		
		Label lb_email = new Label("Email ");
		TextField tf_email = new TextField();
		tf_email.setPromptText("e.g. drytuna@draenor.net");
		
		VBox vBox = new VBox(5);

		HBox hBox1 = new HBox(5);
		// Add first row of fields to window.
		hBox1.getChildren().addAll(lb_id, tf_id);
		
		HBox hBox2 = new HBox(5);
		// Add second row of fields to window.
		hBox2.getChildren().addAll(lb_last, tf_last,
								   lb_first, tf_first,
								   lb_middle, tf_middle);
		// Set the size of these fields.
		tf_last.setMaxWidth(100);
		tf_first.setMaxWidth(100);
		tf_middle.setMaxWidth(30);

		HBox hBox3 = new HBox(5);
		// Add third row of fields to window.
		hBox3.getChildren().addAll(lb_address, tf_address);
		
		HBox hBox4 = new HBox(5);
		// Add fourth row of fields to window.
		hBox4.getChildren().addAll(lb_city, tf_city,
								   lb_state, tf_state);
		
		HBox hBox5 = new HBox(5);
		// Add fifth row of fields to window.
		hBox5.getChildren().addAll(lb_phone, tf_phone,
								   lb_email, tf_email);
		tf_phone.setMaxWidth(130);
		tf_email.setMaxWidth(130);
		
		// Add horizontal boxes to vertical box.
		vBox.getChildren().addAll(hBox1, hBox2, hBox3, hBox4, hBox5);
		
		HBox hBox = new HBox(5);
		// Add buttons.
		hBox.getChildren().addAll(btn_view, btn_insert, btn_update, btn_clear);
		// Set the buttons to be centered.
		hBox.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane(vBox, status, null, hBox, null);
		
		// Create a scene and place it in the stage.
		Scene scene = new Scene(pane,400,220);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		// Set the stage title.
		primaryStage.setTitle("Staff GUI");
		// Place the scene in the stage.
		primaryStage.setScene(scene);
		// Display the stage.
		primaryStage.show();
		
		initializeDB();
		
		Map<String, TextField> tf = new HashMap<>();
		tf.put("id", tf_id);
		tf.put("lastName", tf_last);
		tf.put("firstName", tf_first);
		tf.put("mi", tf_middle);
		tf.put("address", tf_address);
		tf.put("city", tf_city);
		tf.put("state", tf_state);
		tf.put("telephone", tf_phone);
		tf.put("email", tf_email);
		
		// Set the actions for the buttons.
		btn_view.setOnAction((ActionEvent e) -> { view(tf); });
		btn_insert.setOnAction((ActionEvent e) -> { insert(tf); });
		btn_update.setOnAction((ActionEvent e) -> { update(tf); });
		btn_clear.setOnAction((ActionEvent e) -> { clear(tf); });
	}
	
	private void initializeDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/test", 
					"drytuna", "Pa$$word");
			
			status.setText("Database Connected.");
			
			// Create a SQL statement.
			stmt = conn.createStatement();
			
			stmt.executeUpdate("drop table if exists Staff");
			
			String tb_staff = "create table Staff("
					+ "id char(9) not null, "
					+ "lastName varchar(15), "
					+ "firstName varchar(15), "
					+ "mi char(1), "
					+ "address varchar(20), "
					+ "city varchar(20), "
					+ "state char(2), "
					+ "telephone char(10), "
					+ "email varchar(40), "
					+ "constraint staff_id_pk primary key (id))";
			
			stmt.executeUpdate(tb_staff);
			
		} catch (Exception ex) {
			status.setText(ex.getMessage());
		}
	}

	/** View record by ID. */
	private void view(Map<String, TextField> tf) {
		// Build a SQL SELECT statement.
		String query = "select * from Staff where id = " + tf.get("id").getText();
		
		try {
			// Execute query.
			ResultSet rs = stmt.executeQuery(query);

			// Update window.
			loadToTextField(rs, tf);
		} catch (SQLException ex) {
			status.setText(ex.getMessage());
		}
		
//		try {
//			System.out.println();
//			tablePrint("select * from Staff");
//		} catch (SQLException e) {
//			System.out.println(e.getMessage());
//		}
	}
	
	/** Load the record into text fields. */
	private void loadToTextField(ResultSet rs, Map<String, TextField> tf) throws SQLException {
		// Add record to window or display an error message.
		while (rs.next()) {
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				String key = rs.getMetaData().getColumnName(i);
				tf.get(key).setText(rs.getString(i));
			}
		}
		
		status.setText("Displaying information for ID:" + tf.get("id").getText() + ".");
	}
	
	/** Insert a new record. */
	private void insert(Map<String, TextField> tf) {
		// Build a SQL INSERT statement.
		String attribs = "(";
		String values = "(";
		for (String key: tf.keySet()) {
			attribs += key + ",";
			values += "'" + tf.get(key).getText() + "',";
		}
		attribs = attribs.substring(0, attribs.length() - 1) +")";
		values = values.substring(0, values.length() - 1) +")";
		
		String insert_into = "insert into Staff" + attribs + " values" + values;
		
		try {
			// Execute SQL statement.
			stmt.executeUpdate(insert_into);
			
			// Update the status in the window.
			status.setText("Insert Success!");
		} catch (SQLException ex) {
			// Update the status in the window with error message.
			status.setText(ex.getMessage());
		}
	}
	
	/** Update a record. */
	private void update(Map<String, TextField> tf) {
		// Build a SQL UPDATE statement.
		String update_row = "update Staff set ";
		
		/* Only update non empty inputs */
		for (String key: tf.keySet()) {
			String value = tf.get(key).getText();
			if (!key.equals("id") && value != null && !value.isEmpty())
				update_row += key + " = '" + value + "', ";
		}
		
		// Removing the last ',' in the statement
		if (update_row.charAt(update_row.length() - 2) == ',')
			update_row = update_row.substring(0, update_row.length() - 2);
		
		update_row += " where id = " + tf.get("id").getText();
		
		try {
			// Execute statement.
			stmt.executeUpdate(update_row);
			
			// Update status in window.
			status.setText("Successfully updated ID:" + tf.get("id").getText());
		} catch (SQLException ex) {
			// Update the status in the window with error message.
			status.setText(ex.getMessage());
		}
	}
	
	/** Clear text fields. */
	private void clear(Map<String, TextField> tf) {
		for (String key: tf.keySet())
			tf.get(key).setText(null);
	}
	
	/**
	 * The main method is only needed for the IDE with limited javaFX support.
	 * Not needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	
	/**
	 * Name: tablePrint()
	 * @param query - a String with select statement
	 * @throws SQLException
	 * Process: Print the query results with organized spacing
	 */
	private void tablePrint(String query) throws SQLException {
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
