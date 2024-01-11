package pages;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import component.AdminNavigationBar;
import component.CustomerNavigationBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.Product;
import models.User;
import util.Connect;

public class HomePage {
	private CustomerNavigationBar navbar;
	private AdminNavigationBar adminNavbar;
	
	private Label titleLbl, productNameLbl, productDescLbl, priceLbl, 
		totalPriceLbl, quantityLbl, welcomeMsgLbl, instructionLbl;
	
	private ListView<Product> productListView;
	private Spinner<Integer> quantitySpinner;
	private Button cartBtn;
	
	private VBox productDetailBox, welcomeBox;
	private HBox hbox;
	
	private GridPane gridContainer;
	private BorderPane borderContainer;
	
	private Scene homeScene;
	
	private Stage primaryStage;
	
	private User user;
	private ArrayList<Product> productList;
	
	private Connect connect = Connect.getConnection();
	
	public HomePage(Stage primaryStage, User user) {
		this.primaryStage = primaryStage;
		this.user = user;
		productList = new ArrayList<>();
		initialize();
		arrangeComponent();
		setProductListView();
		showProductList();
		eventHandler();
	}
	
	private void initialize() {
		navbar = new CustomerNavigationBar(primaryStage, user);
		adminNavbar = new AdminNavigationBar(primaryStage, user);
		String name = user.getUsername();
		
		titleLbl = new Label("SeRuput Teh");
		productNameLbl = new Label("Product Name");
		productDescLbl = new Label("Product Detail");
		priceLbl = new Label("Price");
		totalPriceLbl = new Label();
		quantityLbl = new Label("Quantity : ");
		welcomeMsgLbl = new Label("Welcome, " + name);
		instructionLbl = new Label("Select a product to view");
		
		productListView = new ListView<>();
		quantitySpinner = new Spinner<>(1, 100, 1);
		
		cartBtn = new Button("Add to Cart");
		
		productDetailBox = new VBox();
		welcomeBox = new VBox();
		hbox = new HBox();
		
		gridContainer = new GridPane();
		
		borderContainer = new BorderPane();
	}
	
	private void arrangeComponent() {
		gridContainer.setPadding(new Insets(40));
		gridContainer.setHgap(10);
		gridContainer.setVgap(10);
		productDetailBox.setSpacing(10);
		welcomeBox.setSpacing(10);
		hbox.setSpacing(8);
				
		productListView.setMinWidth(400);
		
		//Styling
		productNameLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
		welcomeMsgLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
		productDescLbl.setFont(Font.font("Verdana", FontWeight.NORMAL, 13));
		instructionLbl.setFont(Font.font("Verdana", FontWeight.NORMAL, 13));
		titleLbl.setFont(Font.font("", FontWeight.BOLD, 40));
		cartBtn.setMinWidth(100);
		
		productDescLbl.setWrapText(true);
		
		productDetailBox.setVisible(false);
	}
	
	public void setHomePage() {
		String role = user.getRole();
		if (role.equals("Admin")) {
			addAdminHomePage(primaryStage);
		} else {
			addCustomerHomePage(primaryStage);
		}
		
		gridContainer.add(titleLbl, 0, 0);
		gridContainer.add(productListView, 0, 1);
		gridContainer.add(productDetailBox, 1, 1);
		gridContainer.add(welcomeBox, 1, 1);
		
		homeScene = new Scene(borderContainer, 1000, 700);
		primaryStage.setScene(homeScene);
	}
	
	private void addCustomerHomePage(Stage primaryStage) {
		productDetailBox.getChildren().addAll(productNameLbl, productDescLbl, priceLbl, hbox, cartBtn);
		hbox.getChildren().addAll(quantityLbl, quantitySpinner, totalPriceLbl);
		welcomeBox.getChildren().addAll(welcomeMsgLbl, instructionLbl);
		
		borderContainer.setTop(navbar.getCustomerMenu());
		borderContainer.setCenter(gridContainer);			
	}
	
	private void addAdminHomePage(Stage primaryStage) {
		productDetailBox.getChildren().addAll(productNameLbl, productDescLbl, priceLbl);
		welcomeBox.getChildren().addAll(welcomeMsgLbl, instructionLbl);
		
		borderContainer.setTop(adminNavbar.getMenuBar());
		borderContainer.setCenter(gridContainer);
	}
	
	private void changeDisplay() {
	    Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
	    if (selectedProduct == null) {
	        productDetailBox.setVisible(false);
	        welcomeBox.setVisible(true); // Show welcomeBox when no cell is selected
	    } else {
	        productDetailBox.setVisible(true);
	        welcomeBox.setVisible(false);
	        productNameLbl.setText(selectedProduct.getProductName());
	        productDescLbl.setText(selectedProduct.getProductDes());
	        priceLbl.setText("Price : Rp." + selectedProduct.getProductPrice().toString());
	    }
	}
	
	private void eventHandler() {
		productListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	changeDisplay();
	        	updateTotalPrice();
	        	quantitySpinner.getValueFactory().setValue(1);
	        }
	    });
		
		quantitySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
		    updateTotalPrice();
		});
		
		cartBtn.setOnMouseClicked((e) -> {
			addToCart();
		});
		
	}
	
	private void updateTotalPrice() {
		Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
	    if (selectedProduct != null) {
	        int quantity = quantitySpinner.getValue();
	        if (quantity > 1) {
	        	BigInteger totalPrice = selectedProduct.getProductPrice().multiply(BigInteger.valueOf(quantity));
	        	totalPriceLbl.setText("Total Price: Rp." + totalPrice.toString());	
	        } else {
	        	totalPriceLbl.setText("");
	        }
	    }
	}
	
	// ========== DATA & QUERY =============
	private void getProductListData() {
		productList.clear();
		String query = "SELECT * FROM product";
		ResultSet resultSet = connect.executeQuery(query);
		try {
			while (resultSet.next()) {
				String productID = resultSet.getString("productID");
				String productName = resultSet.getString("product_name");
				BigInteger productPrice = BigInteger.valueOf(resultSet.getLong("product_price"));
				String productDes = resultSet.getString("product_des");
				productList.add(new Product(productID, productName, productPrice, productDes));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void setProductListView() {
		productListView.setCellFactory(param -> new ListCell<Product>() {
			@Override
			protected void updateItem(Product item, boolean empty) {
				super.updateItem(item, empty);
				 if (empty || item == null || item.getProductName() == null) {
					 setText(null);
			     } else {
			    	 setText(item.getProductName());
			     }
			};
		});
	}
	
	private void showProductList() {
		getProductListData();
		ObservableList<Product> productListViewItem = FXCollections.observableArrayList(productList);
	    productListView.setItems(productListViewItem);
	}
	
	private void addToCart() {
		Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
		String productID = selectedProduct.getProductID();
		String userID = this.user.getUserID();
		Integer quantity = quantitySpinner.getValue();
		
		try {
			String checkQuery = "SELECT * FROM cart WHERE productID = ? AND userID = ?";
	        PreparedStatement checkStatement = connect.prepareStatement(checkQuery);
	        checkStatement.setString(1, productID);
	        checkStatement.setString(2, userID);

	        ResultSet resultSet = checkStatement.executeQuery();
	        
	        if (resultSet.next()) {
	        	// If the selected product is already in the current user’s cart, 
	        	int currentQuantity = resultSet.getInt("quantity");
	            quantity += currentQuantity;
	            
	            String updateQuery = "UPDATE cart SET quantity = ? WHERE productID = ? AND userID = ?";
	            PreparedStatement updateStatement = connect.prepareStatement(updateQuery);
	            updateStatement.setInt(1, quantity);
	            updateStatement.setString(2, productID);
	            updateStatement.setString(3, userID);

	            updateStatement.executeUpdate();
	        } else {
	        	// If the selected product is not in the current user’s cart
	        	String insertQuery = "INSERT INTO cart VALUES (?, ?, ?)";
	            PreparedStatement insertStatement = connect.prepareStatement(insertQuery);
	            insertStatement.setString(1, productID);
	            insertStatement.setString(2, userID);
	            insertStatement.setInt(3, quantity);

	            insertStatement.executeUpdate();
	        }
			
			Alert successAlert = new Alert(AlertType.INFORMATION);
			successAlert.setHeaderText("Added to Cart");
			successAlert.show();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Add to Cart Failed!");
			errorAlert.show();
		}
	}
	
}
