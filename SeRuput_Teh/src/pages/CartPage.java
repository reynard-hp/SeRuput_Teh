package pages;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import component.CustomerNavigationBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
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

public class CartPage implements EventHandler<ActionEvent>{
	private CustomerNavigationBar navbar;
	
	
	Label titleLbl, productNameLbl, productDescLbl, productPriceLbl, 
		quantityLbl, totalLbl, totalPriceCartLbl, orderInfoLbl, usernameLbl, 
		phoneLbl, addressLbl, welcomeMsgLbl, instructionLbl, emptyMsgLbl, emptyInstructionLbl;
	
	Spinner<Integer> quantitySpinner;
	
	Button updateBtn, removeBtn, purchaseBtn;
	
	private VBox productDetailBox, welcomeMsgBox, infoBox, emptyMsgBox;
	private HBox quantityPriceBox, updateRemoveBox;
	
	private GridPane gridContainer;
	private BorderPane borderContainer;
	
	private Scene cartScene;
	
	private Stage primaryStage;
	private User user;
	
	private ArrayList<Product> productInCartList;
	private ListView<Product> cartListView;
	
	private Connect connect = Connect.getConnection();
	
	public CartPage(Stage primaryStage, User user) {
		this.primaryStage = primaryStage;
		this.user = user;
		initialize();
		addComponent();
		arrangeComponent();
		eventHandler();
		setCartListView();
		refreshListView();
		initialDisplay();
		updateTotalPriceCart();
	}
	
	private void initialize() {
		navbar = new CustomerNavigationBar(primaryStage, user);
		
		titleLbl = new Label(this.user.getUsername() + "'s Cart");
		productNameLbl = new Label("Product Name");
		productDescLbl = new Label("Product Description");
		productPriceLbl = new Label("Product Price");
		quantityLbl = new Label("Quantity : ");
		totalLbl = new Label("");
		totalPriceCartLbl = new Label("Total Cart Price");
		orderInfoLbl = new Label("Order Information");
		usernameLbl = new Label("Username : " + this.user.getUsername());
		phoneLbl = new Label("Phone Number : " + this.user.getPhone_num());
		addressLbl = new Label("Address : " + this.user.getAddress());
		welcomeMsgLbl = new Label("Welcome User");
		instructionLbl = new Label("Select a product to add and remove");
		emptyMsgLbl = new Label("No Item in Cart");
		emptyInstructionLbl = new Label("Consider adding one!");  
		
		quantitySpinner = new Spinner<>(-100, 100, 0);
		
		productDetailBox = new VBox();
		welcomeMsgBox = new VBox();
		infoBox = new VBox();
		emptyMsgBox = new VBox();
		
		quantityPriceBox = new HBox();
		updateRemoveBox = new HBox();
		
		updateBtn = new Button("Update Cart");
		removeBtn = new Button("Remove from Cart");
		purchaseBtn = new Button("Make Purchase");
		
		gridContainer = new GridPane();
		borderContainer = new BorderPane();
		
		productInCartList = new ArrayList<>();
		cartListView = new ListView<Product>();
	}
	
	private void addComponent() {
		productDetailBox.getChildren().addAll(productNameLbl, productDescLbl, productPriceLbl, quantityPriceBox, updateRemoveBox);
		quantityPriceBox.getChildren().addAll(quantityLbl, quantitySpinner, totalLbl);
		updateRemoveBox.getChildren().addAll(updateBtn, removeBtn);
		
		welcomeMsgBox.getChildren().addAll(welcomeMsgLbl, instructionLbl);
		emptyMsgBox.getChildren().addAll(emptyMsgLbl, emptyInstructionLbl);
		
		infoBox.getChildren().addAll(totalPriceCartLbl, orderInfoLbl, usernameLbl, phoneLbl, addressLbl, purchaseBtn);
		
		gridContainer.add(titleLbl, 0, 0);
		gridContainer.add(cartListView, 0, 1);
		gridContainer.add(productDetailBox, 1, 1);
		gridContainer.add(welcomeMsgBox, 1, 1);
		gridContainer.add(emptyMsgBox, 1, 1);
		gridContainer.add(infoBox, 0, 2);
		
		borderContainer.setTop(navbar.getCustomerMenu());
		borderContainer.setCenter(gridContainer);
	}
	
	private void arrangeComponent() {
		gridContainer.setPadding(new Insets(40));
		gridContainer.setHgap(10);
		gridContainer.setVgap(10);
		
		cartListView.setMaxHeight(250);
		cartListView.setMinWidth(400);
		
		productDetailBox.setSpacing(10);
		quantityPriceBox.setSpacing(10);
		updateRemoveBox.setSpacing(10);
		
		welcomeMsgBox.setSpacing(10);
		emptyMsgBox.setSpacing(10);
		
		infoBox.setSpacing(10);
		
		titleLbl.setFont(Font.font("", FontWeight.BOLD, 40));
		productNameLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
		productDescLbl.setFont(Font.font("Verdana", FontWeight.MEDIUM, 13));
		
		welcomeMsgLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
		instructionLbl.setFont(Font.font("Verdana", FontWeight.MEDIUM, 13));
		
		emptyMsgLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
		emptyInstructionLbl.setFont(Font.font("Verdana", FontWeight.MEDIUM, 13));
		
		orderInfoLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		
		removeBtn.setMinWidth(100);
		updateBtn.setMinWidth(100);
	}
	
	public void setCartPage() {
		cartScene = new Scene(borderContainer, 1000, 700);
		primaryStage.setScene(cartScene);
	}
	
	private void initialDisplay() {
		//Initial Page Display
		productDetailBox.setVisible(false);
		
		if (productInCartList.isEmpty()) {
			welcomeMsgBox.setVisible(false);
			emptyMsgBox.setVisible(true);
		} else {
			// If Cart is not Empty
			welcomeMsgBox.setVisible(true);
			emptyMsgBox.setVisible(false);
		}
	}
	
	private void eventHandler() {
		//When Cell in List View is Clicked
		cartListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	changeDisplay();
	        	quantitySpinner.getValueFactory().setValue(0);
	        }
	    });
		
		//Update total price of a product when increasing/decreasing value of spinner
		quantitySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
		    updateTotalPrice(cartListView.getSelectionModel().getSelectedItem());
		});
		
		updateBtn.setOnAction(this);
		removeBtn.setOnAction(this);
		purchaseBtn.setOnAction(this);
	}
	
	// Switching from Welcome Messages to Product Detail 
	private void changeDisplay() {
		Product selectedProduct = cartListView.getSelectionModel().getSelectedItem();
	    if (selectedProduct == null) {
	    	// Show welcomeBox when no cell is selected
	        productDetailBox.setVisible(false);
	        welcomeMsgBox.setVisible(true); 
	    } else {
	        productDetailBox.setVisible(true);
	        welcomeMsgBox.setVisible(false);
	        productNameLbl.setText(selectedProduct.getProductName());
	        productDescLbl.setText(selectedProduct.getProductDes());
	        productPriceLbl.setText("Price : Rp." + selectedProduct.getProductPrice().toString());
	    }
	}
	
	private void updateTotalPrice(Product selectedProduct) {
		//update total price of product
		int quantity = quantitySpinner.getValue();
		if (quantity != 0) {
			BigInteger totalPrice = selectedProduct.getProductPrice().multiply(BigInteger.valueOf(quantity));
			totalLbl.setText("Total Price: Rp." + totalPrice.toString());			
		} else {
			totalLbl.setText("");
		}
	}
	
	private void updateTotalPriceCart() {
		BigInteger totalPriceCart = BigInteger.valueOf(0);
		for (Product product : cartListView.getItems()) {
	        int quantity = getQuantityOfProduct(product.getProductID());
	        totalPriceCart = totalPriceCart.add(product.getProductPrice().multiply(BigInteger.valueOf(quantity)));
	    }
	    totalPriceCartLbl.setText("Total : Rp. " + totalPriceCart.toString());
	}
	
	
	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == updateBtn) {
			updateProductInCart();
			
		} else if (event.getSource() == removeBtn) {
			Product selectedProduct = cartListView.getSelectionModel().getSelectedItem();
			removeProductFromCart(selectedProduct.getProductID());
		} else if (event.getSource() == purchaseBtn) {
			purchaseRequest();
		}
		refreshPage();
	}
	
	private void purchaseRequest() {
		Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
		confirmationAlert.setTitle("Order Confirmation");
		confirmationAlert.setHeaderText("Order Confirmation");
		confirmationAlert.setContentText("Are you sure you want to make purchase?");
		
		confirmationAlert.setGraphic(null);
		confirmationAlert.getDialogPane().setMinHeight(150);
		
		ButtonType buttonTypeYes = new ButtonType("Yes", ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonData.NO);
        
        confirmationAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        
		Optional<ButtonType> result = confirmationAlert.showAndWait();
		if (result.get() == buttonTypeYes) {
			makePurchase();
		}
	}
	
	private void refreshPage() {
		refreshListView();
		updateTotalPriceCart();
		initialDisplay();
	}
	
	// =============================================
	// ============= DATA & QUERY ==================
	// =============================================
	private void getProductinCartData() {
		productInCartList.clear();
		String query = "SELECT * FROM cart c join product p ON c.productID = p.productID "
				+ "WHERE c.userID = ?";
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		try {
			preparedStatement.setString(1, this.user.getUserID());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				String productID = resultSet.getString("productID");
				String productName = resultSet.getString("product_name");
				BigInteger productPrice = BigInteger.valueOf(resultSet.getLong("product_price"));
				String productDes = resultSet.getString("product_des");
				productInCartList.add(new Product(productID, productName, productPrice, productDes));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateProductInCart() {
		int quantityChange = quantitySpinner.getValue();
		Product selectedProduct = cartListView.getSelectionModel().getSelectedItem();
		int currentQty = getQuantityOfProduct(selectedProduct.getProductID());
		
		if (quantityChange < 0) {
	        int newQty = currentQty + quantityChange;

	        if (newQty >= 1) {
	            // Decrease the quantity of the selected product
	        	updateProductQuantityInCart(selectedProduct.getProductID(), newQty);
	        } else if (newQty == 0) {
	            removeProductFromCart(selectedProduct.getProductID());
	        } else {
	        	Alert errorAlert = new Alert(AlertType.ERROR);
		        errorAlert.setHeaderText("Quantity is not valid");
		        errorAlert.show();
	        }
	    } else if (quantityChange == 0) {
	        Alert errorAlert = new Alert(AlertType.ERROR);
	        errorAlert.setHeaderText("Quantity is not a valid amount");
	        errorAlert.show();
	    } else {
	        // Increase the quantity of the selected product
	        updateProductQuantityInCart(selectedProduct.getProductID(), quantityChange + currentQty);
	    }
	}
	
	private void removeProductFromCart(String productID) {
		String query = "DELETE FROM cart WHERE productID = ? AND userID = ?";
		PreparedStatement deleteQuery = connect.prepareStatement(query);
		try {
			deleteQuery.setString(1, productID);
			deleteQuery.setString(2, this.user.getUserID());
			
			deleteQuery.executeUpdate();
			Alert successAlert = new Alert(AlertType.INFORMATION);
		    successAlert.setHeaderText("Deleted from Cart");
		    successAlert.show();
		} catch (SQLException e) {
			Alert errorAlert = new Alert(AlertType.ERROR);
	        errorAlert.setHeaderText("Failed to Delete from Cart");
	        errorAlert.show();
			e.printStackTrace();
		}
	}
	
	private void updateProductQuantityInCart(String productID, int qty) {
		String query = "UPDATE cart SET quantity = ? WHERE productID = ? AND userID = ?";
		PreparedStatement updateQuery = connect.prepareStatement(query);
		try {
			updateQuery.setInt(1, qty);
			updateQuery.setString(2, productID);
			updateQuery.setString(3, this.user.getUserID());
			
			updateQuery.executeUpdate();
			Alert successAlert = new Alert(AlertType.INFORMATION);
		    successAlert.setHeaderText("Updated Cart");
		    successAlert.show();
		} catch (SQLException e) {
			Alert errorAlert = new Alert(AlertType.ERROR);
	        errorAlert.setHeaderText("Failed to Update Cart");
	        errorAlert.show();
			e.printStackTrace();
		}
	}
	
	private void setCartListView() {
		cartListView.setCellFactory(param -> new ListCell<Product>() {
			@Override
			protected void updateItem(Product item, boolean empty) {
				super.updateItem(item, empty);
				 if (empty || item == null || item.getProductName() == null) {
					 setText(null);
			     } else {
			    	 int quantity = getQuantityOfProduct(item.getProductID());
		             BigInteger totalPrice = item.getProductPrice().multiply(BigInteger.valueOf(quantity));
		             setText(String.format("%dx %s (Rp.%s)", quantity, item.getProductName(), totalPrice));
			     }
			};
		});
	}
	
	private int getQuantityOfProduct(String productID) {
        int quantity = 0;
        String query = "SELECT quantity FROM cart WHERE productID = ? AND userID = ?";
        
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        try {
            preparedStatement.setString(1, productID);
            preparedStatement.setString(2, this.user.getUserID());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                quantity = resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quantity;
    }
	
	// ==================== CREATE PURCHASE TRANSACTION ========================
	private void makePurchase() {
		if (productInCartList.isEmpty()) {
			Alert errorAlert = new Alert(AlertType.ERROR);
	        errorAlert.setHeaderText("Failed to Make Transaction");
	        errorAlert.show();
		} else {
			String userID = this.user.getUserID();
			String transactionID = String.format("TR%03d", getTransactionIndex());
			insertIntoPurchaseHeader(transactionID, userID);
			//Using For Loops to handle multiple product in single transaction (in transaction_detail)
			for(Product product : cartListView.getItems()) {
				String productID = product.getProductID();
				Integer quantity = getQuantityOfProduct(productID);
				insertIntoPurchaseDetail(transactionID, productID, quantity);
			}
			clearUserCart();
			refreshPage();
			
			Alert informationAlert = new Alert(AlertType.INFORMATION);
		    informationAlert.setHeaderText("Successfully Purchased");
		    informationAlert.show();
		}
	}
	
	private int getTransactionIndex() {
		int maxTransactionIndex = 0;

	    try {
	        String query = "SELECT MAX(SUBSTRING(transactionID, 3)) FROM transaction_header";
	        PreparedStatement preparedStatement = connect.prepareStatement(query);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            maxTransactionIndex = resultSet.getInt(1);
	        }
	    } catch (SQLException e) {
	        // Handle the exception (e.g., log or display an error message)
	        e.printStackTrace();
	    }
	    return maxTransactionIndex + 1;
	}
	
	private void insertIntoPurchaseHeader(String transactionID, String userID) {
		String query = "INSERT INTO transaction_header VALUES (? , ?)";
		PreparedStatement insertQuery = connect.prepareStatement(query);
		try {
			insertQuery.setString(1, transactionID);
			insertQuery.setString(2, userID);
			
			insertQuery.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void insertIntoPurchaseDetail(String transactionID, String productID, Integer quantity) {
		String query = "INSERT INTO transaction_detail VALUES (? , ?, ?)";
		PreparedStatement insertQuery = connect.prepareStatement(query);
		try {
			insertQuery.setString(1, transactionID);
			insertQuery.setString(2, productID);
			insertQuery.setInt(3, quantity);
			
			insertQuery.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void clearUserCart() {
		String query = "DELETE FROM cart WHERE userID = ?";
		PreparedStatement insertQuery = connect.prepareStatement(query);
		try {
			insertQuery.setString(1, this.user.getUserID());
			
			insertQuery.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void refreshListView() {
		getProductinCartData(); 
		ObservableList<Product> cartListViewItem = FXCollections.observableArrayList(productInCartList);
	    cartListView.setItems(cartListViewItem);
	}
}
