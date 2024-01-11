package component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import models.User;
import pages.CartPage;
import pages.HistoryPage;
import pages.HomePage;
import pages.LoginPage;

public class CustomerNavigationBar implements EventHandler<ActionEvent>{
	private MenuBar customerMenu;
	private Menu homeMenu, cartMenu, accountMenu;
	private MenuItem homeMenuItem, cartMenuItem, logOutMenuItem, purchaseHistoryMenuItem;
	
	private Stage primaryStage;
	
	private User user;
	
	public CustomerNavigationBar(Stage primaryStage, User user) {
		this.primaryStage = primaryStage;
		this.user = user;
		initialize();
		initializeEventHandler();
	}
	
	private void initialize() {
		customerMenu = new MenuBar();
		
		homeMenu = new Menu("Home");
		cartMenu = new Menu("Cart");
		accountMenu = new Menu("Account");
		
		homeMenuItem = new MenuItem("Home Page");
		cartMenuItem = new MenuItem("My Cart");
		logOutMenuItem = new MenuItem("Logout");
		purchaseHistoryMenuItem = new MenuItem("Purchase History");
		
		homeMenu.getItems().add(homeMenuItem);
		cartMenu.getItems().add(cartMenuItem);
		accountMenu.getItems().addAll(purchaseHistoryMenuItem, logOutMenuItem);
		
		customerMenu.getMenus().addAll(homeMenu, cartMenu, accountMenu);
	}
	
	private void initializeEventHandler() {
		logOutMenuItem.setOnAction(this);
		homeMenuItem.setOnAction(this);
		cartMenuItem.setOnAction(this);
		purchaseHistoryMenuItem.setOnAction(this);
	}
	
	
	public MenuBar getCustomerMenu() {
		return customerMenu;
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
		} else if (event.getSource() == cartMenuItem) {
			CartPage cartPage = new CartPage(primaryStage, user);
			cartPage.setCartPage();
		} else if (event.getSource() == purchaseHistoryMenuItem) {
			HistoryPage historyPage = new HistoryPage(primaryStage, user);
			historyPage.setHistoryPage();
		}
	}
}
