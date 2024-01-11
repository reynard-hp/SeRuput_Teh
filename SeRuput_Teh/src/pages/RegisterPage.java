package pages;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import util.Connect;

public class RegisterPage {
	private Label registerTitle, usernameLbl, emailLbl, passwordLbl, confirmPassLbl, phoneNumberLbl,
		addressLbl, genderLbl, descriptionLbl, loginLbl;
	
	private TextField usernameTxt, emailTxt, phoneNumberTxt;
	private PasswordField passwordTxt, confirmPassTxt;
	private TextArea addressTxt;
	private RadioButton maleButton, femaleButton;
	private ToggleGroup genderGroup;
	private CheckBox agreeCheckBox;
	private Button registerBtn;
	
	private GridPane gridContainer;
	private FlowPane flowContainer;
	private HBox genderContainer;
	
	private Scene registerScene;
	
	private Stage primaryStage;
	
	private Connect connect = Connect.getConnection();
	
	private ArrayList<String> usernameList = new ArrayList<>();
	
	private Integer maxCustomerIndex;
	private Integer newCustomerIndex;
	
	
	public RegisterPage(Stage primaryStage) {
		this.primaryStage = primaryStage;
		initializeRegisterPage();
		addComponentRegisterPage();
		arrangeComponent();
		eventHandler();
	}
	
	private void initializeRegisterPage() {
		registerTitle = new Label("Register");
		emailLbl = new Label("Email : ");
		usernameLbl = new Label("Username : ");
		passwordLbl = new Label("Password : ");
		confirmPassLbl = new Label("Confirm password : ");
		phoneNumberLbl = new Label("Phone number : ");
		addressLbl = new Label("Address : ");
		genderLbl = new Label("Gender : ");
		descriptionLbl = new Label("Have an account? ");
		loginLbl = new Label("login here");
		
		usernameTxt = new TextField();
		emailTxt = new TextField();
		passwordTxt = new PasswordField();
		confirmPassTxt = new PasswordField();
		phoneNumberTxt = new TextField();
		addressTxt = new TextArea();
		maleButton = new RadioButton("Male");
		femaleButton = new RadioButton("Female");
		genderGroup = new ToggleGroup();
		agreeCheckBox = new CheckBox("I agree to all terms and condition");
		registerBtn = new Button("Register");
		
		gridContainer = new GridPane();
		
		flowContainer = new FlowPane();
		
		genderContainer = new HBox();
	}
	
	private void addComponentRegisterPage() {
		flowContainer.getChildren().addAll(descriptionLbl, loginLbl);
		maleButton.setToggleGroup(genderGroup);
		femaleButton.setToggleGroup(genderGroup);
		genderContainer.getChildren().addAll(maleButton, femaleButton);
		
		gridContainer.add(registerTitle, 1, 0);
		gridContainer.add(usernameLbl, 0, 1);
		gridContainer.add(usernameTxt, 1, 1);
		gridContainer.add(emailLbl, 0, 2);
		gridContainer.add(emailTxt, 1, 2);
		gridContainer.add(passwordLbl, 0, 3);
		gridContainer.add(passwordTxt, 1, 3);
		gridContainer.add(confirmPassLbl, 0, 4);
		gridContainer.add(confirmPassTxt, 1, 4);
		gridContainer.add(phoneNumberLbl, 0, 5);
		gridContainer.add(phoneNumberTxt, 1, 5);
		gridContainer.add(addressLbl, 0, 6);
		gridContainer.add(addressTxt, 1, 6);
		gridContainer.add(genderLbl, 0, 7);
		gridContainer.add(genderContainer, 1, 7);
		gridContainer.add(agreeCheckBox, 1, 8);
		gridContainer.add(flowContainer, 1, 9);
		gridContainer.add(registerBtn, 1, 10);
	}
	
	private void arrangeComponent() {
		// Arrange Component
		gridContainer.setVgap(10);
		gridContainer.setHgap(10);
		gridContainer.setPadding(new Insets(40));
				
		gridContainer.setAlignment(Pos.CENTER);
				
		genderContainer.setSpacing(10);
		registerBtn.setMinWidth(150);
				
		// Styling
		usernameTxt.setPromptText("input username..");
		emailTxt.setPromptText("input email..");
		passwordTxt.setPromptText("input password..");
		confirmPassTxt.setPromptText("input confirm password..");
		phoneNumberTxt.setPromptText("input phone number..");
		addressTxt.setPromptText("input address..");
				
		registerTitle.setFont(Font.font("", FontWeight.BOLD, 30));
		loginLbl.setTextFill(Color.BLUE);
	}
	
	private void eventHandler() {
		LoginPage loginPage = new LoginPage(this.primaryStage);
		
		loginLbl.setOnMouseEntered((e) -> {
			loginLbl.setCursor(Cursor.HAND);
		});
		
		loginLbl.setOnMouseClicked((e) -> {
			loginPage.setLoginPage(1000, 700);
		});
		
		registerBtn.setOnMouseClicked((e) -> {
			boolean isValid = validateRegister();
			//Implement Later
			if (isValid) {
				loginPage.setLoginPage(1000, 700);
			}
		});
	}
	
	public void setRegisterPage(double width, double height) {
		registerScene = new Scene(gridContainer, width, height);
		primaryStage.setScene(registerScene);
	}
	
	public boolean validateRegister() {
		boolean isValid = false;
		String errorMsg = "";
		
		String username = usernameTxt.getText();
		String email = emailTxt.getText();
		String password = passwordTxt.getText();
		String confirmPassword = confirmPassTxt.getText();
		String phoneNumber = phoneNumberTxt.getText();
		String address = addressTxt.getText();
		
		if (username.length() == 0 || email.length() == 0 || password.length() == 0 ||
				confirmPassword.length() == 0 || phoneNumber.length() == 0 || address.length() == 0) {
			errorMsg = "All fields must be filled.";
		} else if (username.length() < 5 || username.length() > 20) {
			errorMsg = "Username must be 5-20 characters.";
		} else if (!email.endsWith("@gmail.com"))  {
			errorMsg = "Email must end with ‘@gmail.com’";
		} else if (password.length() < 5) {
			errorMsg = "Password must be at least 5 characters.";
		} else if (!(isAlphanumeric(password))) {
			errorMsg = "Password must be alphanumeric.";
		} else if (!confirmPassTxt.getText().equals(passwordTxt.getText())) {
			errorMsg = "Confirm password must equals to password.";
		} else if (!phoneNumberTxt.getText().startsWith("+62")) {
			errorMsg = "Phone number must start with '+62'";
		} else if (!isNumeric(phoneNumberTxt.getText().substring(3))) {
			errorMsg = "Phone number must be numeric.";
		} else if (genderGroup.getSelectedToggle() == null) {
			errorMsg = "Gender must be selected.";
		} else if (!agreeCheckBox.isSelected()) {
			errorMsg = "Checkbox must be checked.";
		} else if (!isUsernameUnique(username)){
			errorMsg = "Username must be unique.";
		} else {
			isValid = true;
		}		

		if (!isValid) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Failed to Register");
			alert.setContentText(errorMsg);
			alert.show();
		} else {
			// Register User
			registerUser();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText("Registered Successfully!");
			alert.show();
		}
		
		return isValid;
	}

	
	private boolean isUsernameUnique(String input) {
		getUserList();
		boolean isUnique = true;
		for (String username : usernameList) {
			if(username.equals(input)) {
				isUnique = false;
				break;
			}
		}
		return isUnique;
	}
	
	private boolean isNumeric(String input) {
		try {
	        Integer.parseInt(input);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	
	private static boolean isAlphanumeric(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }
	
	private void getUserList() {
		usernameList.clear();
		String query = "SELECT username FROM user";
		ResultSet resultSet = connect.executeQuery(query);
		try {
			while(resultSet.next()) {
				String username = resultSet.getString("username");
				usernameList.add(username);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void registerUser() {
		String username = usernameTxt.getText();
		String password = passwordTxt.getText();
		String phoneNumber = phoneNumberTxt.getText();
		String address = addressTxt.getText();
		RadioButton selectedBtn = (RadioButton) genderGroup.getSelectedToggle();
		String gender = selectedBtn.getText();
		
		getMaxCustomerIndex();
		newCustomerIndex = maxCustomerIndex + 1;
		String userid = String.format("CU%03d", newCustomerIndex);
		
		String query = String.format("INSERT INTO user (userID, username, password, role, address, phone_num, gender) " +
			    "VALUES ('%s', '%s', '%s', 'Customer', '%s', '%s', '%s')", userid, username, password, address, phoneNumber, gender);
		
		connect.executeUpdate(query);
	}
	
	private void getMaxCustomerIndex() {
		String query = "SELECT MAX(SUBSTRING(userID, 3)) AS maxIndex FROM user";
		ResultSet resultSet = connect.executeQuery(query);
		try {
			while(resultSet.next()) {
				Integer maxindex = resultSet.getInt("maxIndex");
				maxCustomerIndex = maxindex;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
