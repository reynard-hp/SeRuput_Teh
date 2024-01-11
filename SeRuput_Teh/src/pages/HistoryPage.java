package pages;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import component.CustomerNavigationBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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

public class HistoryPage {
	private CustomerNavigationBar navbar;
	
	private Label titleLbl, transactionIDLbl, usernameLbl, phoneLbl, addressLbl, emptyTitleLbl, emptyMsgLbl, welcomeMsgLbl, totalPriceLbl;
	
	private ListView<String> transactionListView;
	private ListView<Product> productListView;
	
	private ArrayList<String> transactionList;
	private ArrayList<Product> productList;
	
	private HBox hbox;
	private VBox emptyMsgBox, welcomeMsgBox, transactionDetailBox;
	private GridPane gridContainer;
	private BorderPane borderContainer;
	
	private Scene historyScene;
	
	private Stage primaryStage;
	
	private User user;
	
	private Connect connect = Connect.getConnection();
	
	public HistoryPage(Stage primaryStage, User user) {
		this.primaryStage = primaryStage;
		this.user = user;
		initialize();
		addComponent();
		arrangeComponent();
		refreshTransactionListView();
		eventHandler();
		setProductListView();
		initialDisplay(); // First Look of the Page
	}
	
	private void initialize() {
		navbar = new CustomerNavigationBar(primaryStage, user);
		
		// Transaction Detail
		transactionDetailBox = new VBox();
		titleLbl = new Label(this.user.getUsername() + "'s Purchase History");
		transactionIDLbl = new Label("Transaction ID");
		usernameLbl = new Label("Username : "+ this.user.getUsername());
		phoneLbl = new Label("Phone Number : " + this.user.getPhone_num());
		addressLbl = new Label("Address : " + this.user.getAddress());
		totalPriceLbl = new Label("Total Price: ");
		
		// Empty Message
		emptyMsgBox = new VBox();
		emptyTitleLbl = new Label("There's No History");
		emptyMsgLbl = new Label("Consider Purchasing Our Products");
		
		// Welcome Message
		welcomeMsgBox = new VBox();
		welcomeMsgLbl = new Label("Select a Transaction to View Details");
		
		transactionListView = new ListView<>();
		productListView = new ListView<>();
		
		transactionList = new ArrayList<>();
		productList = new ArrayList<>();
		
		hbox = new HBox();
		
		gridContainer = new GridPane();
		borderContainer = new BorderPane();
	}
	
	private void addComponent() {
		emptyMsgBox.getChildren().addAll(emptyTitleLbl, emptyMsgLbl);
		welcomeMsgBox.getChildren().add(welcomeMsgLbl);
		transactionDetailBox.getChildren().addAll(transactionIDLbl, usernameLbl, phoneLbl, addressLbl, totalPriceLbl, productListView);
		
		gridContainer.add(titleLbl, 0, 0);
		gridContainer.add(hbox, 0, 1);
		
		borderContainer.setTop(navbar.getCustomerMenu());
		borderContainer.setCenter(gridContainer);
	}
	
	private void arrangeComponent() {
		emptyMsgBox.setSpacing(10);
		welcomeMsgBox.setSpacing(10);
		transactionDetailBox.setSpacing(10);
		hbox.setSpacing(20);
		
		gridContainer.setPadding(new Insets(40));
		gridContainer.setVgap(10);
		
		titleLbl.setFont(Font.font("", FontWeight.BOLD, 40));
		transactionIDLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		usernameLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		emptyTitleLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		welcomeMsgLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		totalPriceLbl.setFont(Font.font("Verdana", FontWeight.MEDIUM, 13));
		
		transactionListView.setMaxHeight(450);
		productListView.setMaxHeight(225);
		productListView.setMinWidth(350);
	}
	
	private void eventHandler() {
		transactionListView.setOnMouseClicked((e) -> {
			changeDisplay();
			refreshProductListView();
		});
	}
	
	private void initialDisplay() {
		transactionListView.getSelectionModel().clearSelection();
		hbox.getChildren().clear();
		if (transactionList.isEmpty()) {
			//Show Empty Messages
			hbox.getChildren().addAll(transactionListView, emptyMsgBox);
		} else {
			hbox.getChildren().addAll(transactionListView, welcomeMsgBox);
		}
	}
	
	public void setHistoryPage() {
		historyScene = new Scene(borderContainer, 1000, 700);
		primaryStage.setScene(historyScene);
	}
	
	private void changeDisplay() {
		String selectedTransaction = transactionListView.getSelectionModel().getSelectedItem();
		hbox.getChildren().clear();
	    if (selectedTransaction != null) {
	        transactionIDLbl.setText("Transaction ID : " + selectedTransaction);
	        totalPriceLbl.setText("Total : Rp." + calculateTotalPrice(selectedTransaction));
	        hbox.getChildren().addAll(transactionListView, transactionDetailBox);
	    } else {
	        hbox.getChildren().addAll(transactionListView, welcomeMsgBox);
	    }
	}
	
	// ========== DATA & QUERY ====================
	private void getTransactionData() {
		transactionList.clear();
		
		String query = "SELECT * FROM transaction_header WHERE userID = ?";
		PreparedStatement getQuery = connect.prepareStatement(query);
		
		try {
			getQuery.setString(1, this.user.getUserID());
			
			ResultSet resultSet = getQuery.executeQuery();
			
			while (resultSet.next()) {
				String transactionID = resultSet.getString("transactionID");
				transactionList.add(transactionID);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void refreshTransactionListView() {
		getTransactionData();
		ObservableList<String> transactionListViewItems = FXCollections.observableArrayList(transactionList);
	    transactionListView.setItems(transactionListViewItems);
	}
	
	private void getProductFromTransaction(String transactionID) {
		productList.clear();
		String query = "SELECT * FROM transaction_detail td JOIN Product p ON td.productID = p.productID "
				+ "WHERE transactionID = ?";
		PreparedStatement getQuery = connect.prepareStatement(query);
		
		try {
			getQuery.setString(1, transactionID);
			
			ResultSet resultSet = getQuery.executeQuery();
			while (resultSet.next()) {
				String productID = resultSet.getString("productID");
				String productName = resultSet.getString("product_name");
				BigInteger productPrice = BigInteger.valueOf(resultSet.getLong("product_price"));
				String productDes = resultSet.getString("product_des");
				productList.add(new Product(productID, productName, productPrice, productDes));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
			    	 int quantity = getQuantityOfProduct(item.getProductID());
		             BigInteger totalPrice = item.getProductPrice().multiply(BigInteger.valueOf(quantity));
		             setText(String.format("%dx %s (Rp.%s)", quantity, item.getProductName(), totalPrice));
			     }
			}
		});
	}
	
	private int getQuantityOfProduct(String productID) {
		String transactionID = transactionListView.getSelectionModel().getSelectedItem();
        int quantity = 0;
        String query = "SELECT quantity FROM transaction_detail WHERE transactionID = ? AND productID = ?";
        
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        try {
            preparedStatement.setString(1, transactionID);
            preparedStatement.setString(2, productID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                quantity = resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quantity;
    }
	
	private BigInteger calculateTotalPrice(String transactionID) {
		BigInteger totalPrice = BigInteger.valueOf(0);
		String query = "SELECT SUM(product_price * quantity) AS totalPrice FROM transaction_detail td JOIN product p "
				+ "ON td.productID = p.productID WHERE transactionID = ?";
		PreparedStatement getTotalPriceQuery = connect.prepareStatement(query);
		try {
			getTotalPriceQuery.setString(1, transactionID);
			
			ResultSet resultSet = getTotalPriceQuery.executeQuery();
			if (resultSet.next()) {
	            totalPrice = BigInteger.valueOf(resultSet.getLong("totalPrice"));
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return totalPrice;
	}
	
	private void refreshProductListView() {
		String transactionID = transactionListView.getSelectionModel().getSelectedItem();
		getProductFromTransaction(transactionID);
		ObservableList<Product> productListViewItems = FXCollections.observableArrayList(productList);
	    productListView.setItems(productListViewItems);
	}
}
