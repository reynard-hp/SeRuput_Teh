package pages;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import component.AdminNavigationBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class ManageProductPage implements EventHandler<ActionEvent>{
	private AdminNavigationBar navbar;
	
	private Label titleLbl, welcomeMsgLbl, instructionLbl, productNameLbl, productDesLbl, productPriceLbl,
		inputProductNameLbl, inputProductDesLbl, inputProductPriceLbl,
		updateProductLbl, removeConfirmLbl;	
	
	private Button addBtn, updateBtn, removeBtn,
		inputAddBtn, inputUpdateBtn, inputRemoveBtn,
		backAddBtn, backUpdateBtn, backRemoveBtn;
	
	private TextField productNameTF, productPriceTF, newProductPriceTF;
	private TextArea productDesTA;
	
	private ListView<Product> productListView;
	private ArrayList<Product> productList;
	
	private VBox infoVbox, welcomeMsgBox, productDetailBox, addFormBox, updateFormBox, removeFormBox;
	private HBox updateRemoveBox, addBackBox, updateBackBox, removeBackBox;
	
	private GridPane gridContainer;
	private BorderPane borderContainer;
	
	private Scene ManageProductScene;
	
	private Stage primaryStage;
	
	private User user;
	
	private Connect connect = Connect.getConnection();
	
	public ManageProductPage(Stage primaryStage, User user) {
		this.primaryStage = primaryStage;
		this.user = user;
		initialize();
		addComponent();
		arrangeComponent();
		setProductListView();
		initialDisplay();
	}
	
	private void initialize() {
		navbar = new AdminNavigationBar(primaryStage, user);
		
		titleLbl = new Label("Manage Products");
		
		// Welcome Message
		welcomeMsgBox = new VBox();
		welcomeMsgLbl = new Label("Welcome, " + this.user.getUsername());
		instructionLbl = new Label("Select a Product to Update");
		
		// Product Detail
		productNameLbl = new Label("Product Name");
		productDesLbl = new Label("Product Description");
		productPriceLbl = new Label("Product Price");
		
		addBtn = new Button("Add Product");
		
		// Add Form
		inputProductNameLbl = new Label("Input product name");
		inputProductDesLbl = new Label("Input product description..");
		inputProductPriceLbl = new Label("Input product price");
		
		productNameTF = new TextField();
		productPriceTF = new TextField();
		productDesTA = new TextArea();
		
		inputAddBtn = new Button("Add Product");
		backAddBtn = new Button("Back");
		
		addFormBox = new VBox(); // Add form Container
		addBackBox = new HBox();
		
		// Update Form
		updateProductLbl = new Label("Update Product");
		inputUpdateBtn = new Button("Update Product");
		backUpdateBtn = new Button("Back");
		
		newProductPriceTF = new TextField();
		
		updateFormBox = new VBox();
		updateBackBox = new HBox();
		
		// Remove Form
		removeConfirmLbl = new Label("Are you sure, you want to remove this product?");
		inputRemoveBtn = new Button("Remove Product");
		backRemoveBtn = new Button("Back");
		
		removeFormBox = new VBox();
		removeBackBox = new HBox();
		
		// 
		updateBtn = new Button("Update Product");
		removeBtn = new Button("Remove Product");		
		
		productListView = new ListView<>();
		productList = new ArrayList<>();
		
		infoVbox = new VBox();
		
		productDetailBox = new VBox();
	
		updateRemoveBox = new HBox(); //Container for Update and Remove Button	
		
		gridContainer = new GridPane();
		borderContainer = new BorderPane();
	}
	
	private void addComponent() {
		welcomeMsgBox.getChildren().addAll(welcomeMsgLbl, instructionLbl);
		productDetailBox.getChildren().addAll(productNameLbl, productDesLbl, productPriceLbl);
		updateRemoveBox.getChildren().addAll(updateBtn, removeBtn);
		
		// Add Form 
		addBackBox.getChildren().addAll(inputAddBtn, backAddBtn);
		addFormBox.getChildren().addAll(inputProductNameLbl, productNameTF, inputProductPriceLbl, productPriceTF, 
				inputProductDesLbl, productDesTA);
		
		// Update Form
		updateFormBox.getChildren().addAll(updateProductLbl, newProductPriceTF);
		updateBackBox.getChildren().addAll(inputUpdateBtn, backUpdateBtn);
		
		// Remove Form 
		removeFormBox.getChildren().addAll(removeConfirmLbl, removeBackBox);
		removeBackBox.getChildren().addAll(inputRemoveBtn, backRemoveBtn);
		
		// Layer
		gridContainer.add(titleLbl, 0, 0);
		gridContainer.add(productListView, 0, 1);
		gridContainer.add(infoVbox, 1, 1);
		
		borderContainer.setTop(navbar.getMenuBar());
		borderContainer.setCenter(gridContainer);
	}
	
	private void arrangeComponent() {
		// gridContainer
		gridContainer.setPadding(new Insets(40));
		gridContainer.setVgap(15);
		gridContainer.setHgap(15);
		
		// infoBox
		infoVbox.setSpacing(20);
		updateRemoveBox.setSpacing(10);
		
		// Initial Display
		titleLbl.setFont(Font.font("", FontWeight.BOLD, 40));
		
		productListView.setMinWidth(350);
		
		addBtn.setMinWidth(150);
		removeBtn.setMinWidth(150);
		updateBtn.setMinWidth(150);
		
		// Welcome Message Box
		welcomeMsgBox.setSpacing(10);
		
		welcomeMsgLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		instructionLbl.setFont(Font.font("Verdana", FontWeight.MEDIUM, 13));
		
		// Product Detail Box 
		productDetailBox.setSpacing(10);
		productDesLbl.setWrapText(true);
		productDetailBox.setMaxWidth(500);
		
		productNameLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		productDesLbl.setFont(Font.font("Verdana", FontWeight.MEDIUM, 14));
		productPriceLbl.setFont(Font.font("Verdana", FontWeight.MEDIUM, 14));
		
		// Add Form Box
		addFormBox.setSpacing(10);
		addBackBox.setSpacing(10);
		backAddBtn.setMinWidth(150);
		inputAddBtn.setMinWidth(150);
		
		inputProductNameLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		inputProductPriceLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		inputProductDesLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		
		productNameTF.setPromptText("Input product name..");
		productPriceTF.setPromptText("Input product price..");
		productDesTA.setPromptText("Input product description");
		
		// Update Form Box
		updateFormBox.setSpacing(10);
		updateBackBox.setSpacing(10);
		inputUpdateBtn.setMinWidth(150);
		backUpdateBtn.setMinWidth(150);

		updateProductLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		newProductPriceTF.setPromptText("Input new price..");
		newProductPriceTF.setMinWidth(400);
	
		// Remove Form Box
		removeFormBox.setSpacing(10);
		removeBackBox.setSpacing(10);
		inputRemoveBtn.setMinWidth(150);
		backRemoveBtn.setMinWidth(150);
		
		removeConfirmLbl.setFont(Font.font("", FontWeight.BOLD, 15));
	}
	
	public void setManageProductPage() {
		ManageProductScene = new Scene(borderContainer, 1000, 750);
		primaryStage.setScene(ManageProductScene);
	}
	
	private void eventHandlerInitialPage() {
		productListView.setOnMouseClicked((event) -> {
			displayProductInfoInitialPage();
		});
		
		addBtn.setOnAction(this);
		updateBtn.setOnAction(this);
		removeBtn.setOnAction(this);
		
		inputUpdateBtn.setOnAction(this);
		backUpdateBtn.setOnAction(this);
		
		inputRemoveBtn.setOnAction(this);
		backRemoveBtn.setOnAction(this);
	}
	
	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == addBtn) {
			displayAddForm();
		} else if (event.getSource() == backAddBtn) {
			initialDisplay();
		} else if (event.getSource() == updateBtn) {
			displayUpdateForm();
		} else if (event.getSource() == backUpdateBtn) {
			initialDisplay();
		} else if (event.getSource() == removeBtn) {
			displayRemoveForm();
		} else if (event.getSource() == backRemoveBtn) {
			initialDisplay();
		} else if (event.getSource() == inputAddBtn) { // Trigger Query Operation
			addProduct();
		} else if (event.getSource() == inputUpdateBtn) { // Trigger Query Operation
			updateProduct();
		} else if (event.getSource() == inputRemoveBtn) { // Trigger Query Operation
			removeProductFromDatabase();
		}
	}
	
	// Initial Page
	private void initialDisplay() {
		productListView.getSelectionModel().clearSelection();
		showProductListView();
		infoVbox.getChildren().clear();
		infoVbox.getChildren().addAll(welcomeMsgBox, addBtn);
		eventHandlerInitialPage();
	}
	
	private void displayProductInfoInitialPage() {
		infoVbox.getChildren().clear();
		Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
		if (selectedProduct == null) {
			infoVbox.getChildren().addAll(welcomeMsgBox, addBtn);
		} else {
			productNameLbl.setText(selectedProduct.getProductName());
			productDesLbl.setText(selectedProduct.getProductDes());
			productPriceLbl.setText("Price: Rp."+ selectedProduct.getProductPrice().toString());
			infoVbox.getChildren().addAll(productDetailBox, addBtn, updateRemoveBox);
		}
	}
	
	// Add Form
	private void displayAddForm() {
		productNameTF.clear();
		productPriceTF.clear();
		productDesTA.clear();
		infoVbox.getChildren().clear();
		infoVbox.getChildren().addAll(welcomeMsgBox, addFormBox, addBackBox);
		eventHandlerAddPage();
	}
	
	private void eventHandlerAddPage() {
		productListView.setOnMouseClicked((event) -> {
			displayProductInfoAddPage();
		});
		
		inputAddBtn.setOnAction(this);
		backAddBtn.setOnAction(this);
	}
	
	private void displayProductInfoAddPage() {
		infoVbox.getChildren().clear();
		Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
		if (selectedProduct == null) {
			infoVbox.getChildren().addAll(welcomeMsgBox, addFormBox, addBackBox);
		} else {
			productNameLbl.setText(selectedProduct.getProductName());
			productDesLbl.setText(selectedProduct.getProductDes());
			productPriceLbl.setText("Price: Rp."+ selectedProduct.getProductPrice().toString());
			infoVbox.getChildren().addAll(productDetailBox, addFormBox, addBackBox);
		}
	}
	
	// Update Form
	private void displayUpdateForm() {
		newProductPriceTF.clear();
		Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
		if (selectedProduct != null) {
			infoVbox.getChildren().removeAll(addBtn, updateRemoveBox);
			infoVbox.getChildren().addAll(updateFormBox, updateBackBox);
		}
	}
	
	// Remove Form
	private void displayRemoveForm() {
		Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
		if (selectedProduct != null) {
			infoVbox.getChildren().removeAll(addBtn, updateRemoveBox);
			infoVbox.getChildren().addAll(removeFormBox, removeBackBox);	
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
	//=============== DATA & QUERY ==================
	private void getProductData() {
		productList.clear();
		String query = "SELECT * FROM product";
		PreparedStatement preparedStatement = connect.prepareStatement(query);
		try {
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				String productID = resultSet.getString("productID");
				String product_name = resultSet.getString("product_name");
				BigInteger product_price = BigInteger.valueOf(resultSet.getLong("product_price"));
				String product_des = resultSet.getString("product_des");
				productList.add(new Product(productID, product_name, product_price, product_des));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void showProductListView() {
		getProductData();
		ObservableList<Product> productListViewItem = FXCollections.observableArrayList(productList);
		productListView.setItems(productListViewItem);
	}
	
	// Add Product Functionality
	private void addProduct() {
		boolean isValid = validateInputProduct();
		if (isValid) {
			String productID = String.format("TE%03d", getProductIndex());
			addProductToDatabase(productID);
			initialDisplay();
		}
	}
	
	private int getProductIndex() {
		int maxProductIndex = 0;
	    try {
	        String query = "SELECT MAX(SUBSTRING(productID, 3)) FROM product";
	        PreparedStatement preparedStatement = connect.prepareStatement(query);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            maxProductIndex = resultSet.getInt(1);
	        }
	    } catch (SQLException e) {
	        // Handle the exception (e.g., log or display an error message)
	        e.printStackTrace();
	    }
	    return maxProductIndex + 1;
	}
	
	private boolean validateInputProduct() {
	    boolean isValid = false;
	    String product_name = productNameTF.getText();
	    String input_price = productPriceTF.getText();
	    String product_des = productDesTA.getText();
	    String errorMsg = "";

	    if (product_name.length() == 0 || input_price.length() == 0 || product_des.length() == 0) {
	        errorMsg = "All Fields Must be Filled Out";
	    } else {
	        try {
	            Long product_price = Long.parseLong(input_price);
	            if (!isProductUnique(product_name)) {
	                errorMsg = "Product name must be unique";
	            } else if (product_price <= 0) {
	                errorMsg = "Product price must be more than 0";
	            } else {
	                isValid = true; // Set isValid to true if all conditions pass
	            }
	        } catch (NumberFormatException e) {
	            // Handle the exception (e.g., display an error message)
	            errorMsg = "Invalid Product Price";
	        }
	    }

	    if (!isValid) {
	        Alert errorAlert = new Alert(AlertType.ERROR);
	        errorAlert.setHeaderText(errorMsg);
	        errorAlert.show();
	    }

	    return isValid;
	}
	
	private boolean isProductUnique(String product_name) {
		boolean isUnique = true;
		getProductData();
		for(Product product : productList) {
			if(product.getProductName().equals(product_name)) {
				isUnique = false;
				break;
			}
		}
		return isUnique;
	}
	  
	private void addProductToDatabase(String productID) {
		String product_name = productNameTF.getText();
		String product_price_str = productPriceTF.getText();
	    BigInteger product_price = new BigInteger(product_price_str);
		String product_des = productDesTA.getText();
		
		String query = "INSERT INTO product VALUES (?, ?, ?, ?)";
		PreparedStatement addQuery = connect.prepareStatement(query);
		try {
			addQuery.setString(1, productID);
			addQuery.setString(2, product_name);
			addQuery.setObject(3, product_price);
			addQuery.setString(4, product_des);
			
			addQuery.executeUpdate();
			
			Alert successAlert = new Alert(AlertType.INFORMATION);
			successAlert.setHeaderText("Add Product Success");
			successAlert.show();
		} catch (SQLException e) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Failed to Add Product");
			errorAlert.show();
			e.printStackTrace();
		}
	}
	
	// Update Product Functionality
	private void updateProduct() {
		boolean isValid = false;
		String errorMsg = "";
		String newProductPrice_str = newProductPriceTF.getText();
		if (newProductPrice_str.length() == 0) {
			errorMsg = "Price is empty";
		} else {
			try {
				BigInteger newProductPrice = new BigInteger(newProductPrice_str);
				if (newProductPrice.compareTo(BigInteger.ZERO) <= 0) {
	                errorMsg = "Price must be more than 0";
	            } else {
	            	isValid = true;
	            }
			} catch (NumberFormatException e) {
				errorMsg = "Invalid Price";
			}
		}
		
		if (isValid) {
			updateProductInDatabase();
			initialDisplay();
		} else {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText(errorMsg);
			errorAlert.show();
		}
	}
	
	private void updateProductInDatabase() {
		Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
		String query = "UPDATE product SET product_price = ? WHERE productID = ?";
		BigInteger newPrice = new BigInteger(newProductPriceTF.getText());
		PreparedStatement updateQuery = connect.prepareStatement(query);
		try {
			updateQuery.setObject(1, newPrice);
			updateQuery.setString(2, selectedProduct.getProductID());
			updateQuery.executeUpdate();
			Alert successAlert = new Alert(AlertType.INFORMATION);
			successAlert.setHeaderText("Update Product Success");
			successAlert.show();
		} catch (SQLException e) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Failed to Update Product");
			errorAlert.show();
			e.printStackTrace();
		}
	}
	
	// Remove Product Functionality
	private void removeProductFromDatabase() {
		Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
		String query = "DELETE FROM product WHERE productID = ?";
		PreparedStatement deleteQuery = connect.prepareStatement(query);
		try {
			deleteQuery.setString(1, selectedProduct.getProductID());
			deleteQuery.executeUpdate();
			Alert successAlert = new Alert(AlertType.INFORMATION);
			successAlert.setHeaderText("Update Product Success");
			successAlert.show();
			initialDisplay();
		} catch (SQLException e) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Failed to Update Product");
			errorAlert.show();
			e.printStackTrace();
		}
	}
}
