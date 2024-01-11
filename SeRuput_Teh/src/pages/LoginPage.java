package pages;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.User;
import util.Connect;

public class LoginPage {
	private Label loginTitle, usernameLbl, passwordLbl,
		registerLbl, descriptionLbl;
	private TextField usernameTxt;
	private PasswordField passwordTxt;
	private Button loginBtn;
	
	private GridPane gridContainer;
	private FlowPane flowContainer;
	
	private Scene loginScene;
	private Stage primaryStage;
	
	private Connect connect = Connect.getConnection();
	
	public LoginPage(Stage primaryStage) {
		this.primaryStage = primaryStage;
		initializeLoginPage();
		addLoginComponent();
		arrangeLoginComponent();
		eventHandler();
	}
	
	private void initializeLoginPage() {
		loginTitle = new Label("Login");
		usernameLbl = new Label("Username : ");
		passwordLbl = new Label("Password : ");
		registerLbl = new Label("register here");
		descriptionLbl = new Label("Don't have an account yet? ");
		
		usernameTxt = new TextField();
		passwordTxt = new PasswordField();
		
		loginBtn = new Button("Login");
		
		gridContainer = new GridPane();
		flowContainer = new FlowPane();
	}
	
	private void addLoginComponent() {
		flowContainer.getChildren().addAll(descriptionLbl, registerLbl);
		
		gridContainer.add(loginTitle, 1, 0);
		gridContainer.add(usernameLbl, 0, 1);
		gridContainer.add(usernameTxt, 1, 1);
		gridContainer.add(passwordLbl, 0, 2);
		gridContainer.add(passwordTxt, 1, 2);
		gridContainer.add(flowContainer, 1, 3);
		gridContainer.add(loginBtn, 1, 4);
	}
	
	private void arrangeLoginComponent() {
		loginBtn.setMinWidth(150);
		usernameTxt.setMaxWidth(250);
		passwordTxt.setMaxWidth(250);
		
		gridContainer.setVgap(10);
		gridContainer.setHgap(10);
		gridContainer.setPadding(new Insets(40));
		gridContainer.setAlignment(Pos.CENTER);
		
		//Styling
		usernameTxt.setPromptText("input username..");
		passwordTxt.setPromptText("input password..");
		
		loginTitle.setFont(Font.font("", FontWeight.BOLD, 30));
		registerLbl.setTextFill(Color.BLUE);
	}
	
	private void eventHandler() {
		registerLbl.setOnMouseEntered((event) -> {
			registerLbl.setCursor(Cursor.HAND);
		});
		
		registerLbl.setOnMouseClicked((event) -> {
			RegisterPage registerPage = new RegisterPage(this.primaryStage);
			registerPage.setRegisterPage(1000,700);
		});
		
		loginBtn.setOnMouseClicked((event) -> {
			boolean isValid = validateLogin();
			if(isValid) {
				User loggedinUser = getUserFromDatabase();
				HomePage homePage = new HomePage(primaryStage, loggedinUser);
				homePage.setHomePage();
			}
		});
	}
	
	public void setLoginPage(double width, double height) {
		loginScene = new Scene(gridContainer, width, height);
		primaryStage.setScene(loginScene);
	}
	
	public boolean validateLogin() {
		boolean isValid = false;
		String errorMsg = "";
		if (usernameTxt.getText().length() == 0 || passwordTxt.getText().length() == 0) {
			errorMsg = "All fields must be filled.";
		} else if (!isCredentialValid()){
			errorMsg = "invalid credential";
		} else {
			isValid = true;
		}
		
		if (!isValid) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Failed to Login");
			alert.setContentText(errorMsg);
			alert.show();
		} 
		return isValid;
	}
	
	
	private boolean isCredentialValid() {
		String query = "SELECT * FROM user WHERE username = ? AND password = ?";
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		try {
	        preparedStatement.setString(1, usernameTxt.getText());
	        preparedStatement.setString(2, passwordTxt.getText());

	        ResultSet resultSet = preparedStatement.executeQuery();
	        
	        return resultSet.next();

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; 
	    }
	}
	
	private User getUserFromDatabase() {
	    User user = null;
	    String query = "SELECT * FROM user WHERE username = ? AND password = ?";
	    try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
	        preparedStatement.setString(1, usernameTxt.getText());
	        preparedStatement.setString(2, passwordTxt.getText());

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                user = new User(
	                        resultSet.getString("userID"),
	                        resultSet.getString("username"),
	                        resultSet.getString("password"),
	                        resultSet.getString("role"),
	                        resultSet.getString("address"),
	                        resultSet.getString("phone_num"),
	                        resultSet.getString("gender")
	                );
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } 
	    return user;
	}

}
