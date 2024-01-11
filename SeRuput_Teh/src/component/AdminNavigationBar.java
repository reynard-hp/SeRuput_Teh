package component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import models.User;
import pages.HomePage;
import pages.LoginPage;
import pages.ManageProductPage;

public class AdminNavigationBar implements EventHandler<ActionEvent>{
	private MenuBar adminMenu;
	private Menu homeMenu, manageProductMenu, accountMenu;
	private MenuItem homeMenuItem, manageProductMenuItem, logOutMenuItem;
	
	private Stage primaryStage;
	private User user;
	
	public AdminNavigationBar(Stage primaryStage, User user) {
		this.primaryStage = primaryStage;
		this.user =  user;
		initialize();
		initializeEventHandler();
	}
	
	private void initialize() {
		adminMenu = new MenuBar();
		
		homeMenu = new Menu("Home");
		manageProductMenu = new Menu("Manage Products");
		accountMenu = new Menu("Account");
		
		homeMenuItem = new MenuItem("Home Page");
		manageProductMenuItem = new MenuItem("Manage Products");
		logOutMenuItem = new MenuItem("Logout");
		
		homeMenu.getItems().add(homeMenuItem);
		manageProductMenu.getItems().add(manageProductMenuItem);
		accountMenu.getItems().add(logOutMenuItem);
		
		adminMenu.getMenus().addAll(homeMenu, manageProductMenu, accountMenu);
	}
	
	public MenuBar getMenuBar(){
		return adminMenu;
	}

	public void initializeEventHandler() {
		logOutMenuItem.setOnAction(this);
		homeMenuItem.setOnAction(this);
		manageProductMenuItem.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource() == logOutMenuItem) {
			LoginPage loginPage = new LoginPage(primaryStage);
			loginPage.setLoginPage(1000, 700);
		} else if (event.getSource() == homeMenuItem) {
			HomePage homePage = new HomePage(primaryStage, user);
			homePage.setHomePage();
		} else if (event.getSource() == manageProductMenuItem) {
			ManageProductPage manageProductPage = new ManageProductPage(primaryStage, user);
			manageProductPage.setManageProductPage();
		}
	}
}
