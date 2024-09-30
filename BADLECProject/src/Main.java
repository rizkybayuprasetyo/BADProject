import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.Cart;
import models.Items;
import Util.Connect;

public class Main extends Application{
	
	Scene regisScene;
	Scene loginScene;
	Scene userHome;
	Scene adminHome;
	Scene cartPage;
	Scene transactPage;
	
	//setelah logged in, di jadiin temp data
	private String loggedInAs;
	private String loggedInAsName;
	
	//StoreHomePage
	TableView<Items> items;
	Spinner<Integer> quantity;
	Label itemName;
	Label price;
	int total;
	
	//untuk cart page
		ArrayList<Items> itemlisArrayList; 
		TableView<Cart> cartTable;	
		ComboBox<String> courierComboBox;
		int updatedPrice;
		int netPrice;
		int beforeInsurancePrice;
		String getSelectedCourier;
		int courierPrice;
		int insuranceFee;
		
	//untuk transaction page
		int outputSelectedCourier;
		int outputInsurancePrice;
		int outputFinalCheckoutPrice;
		
	//untuk halaman admin
		ObservableList<Items> itemManagementData;
	
	//instance untuk bawain connect
	private Connect connect = Connect.getInstance();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	public void alert(Alert.AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	private EventHandler<MouseEvent> onClickItems(){
		return new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent args0) {
				// TODO Auto-generated method stub
				TableSelectionModel<Items> tableSelectionModelItems = items.getSelectionModel();
				tableSelectionModelItems.setSelectionMode(SelectionMode.SINGLE);				
				Items selectedItem = tableSelectionModelItems.getSelectedItem();
				
				if (selectedItem != null) {
					
					quantity.valueProperty().addListener((obs, oldValue, newValue) -> {
						if (!oldValue.equals(newValue)) {
							itemName.setText(selectedItem.getItemName());
							total = selectedItem.getItemPrice() * quantity.getValue();
							price.setText("Total price: " +(String.valueOf(total)));
						}
					});
					
					itemName.setText(selectedItem.getItemName());
					total = selectedItem.getItemPrice() * quantity.getValue();
					price.setText("Total price: " +(String.valueOf(total)));
					
				}
				
			}
		};
	}
	
	public void login(Stage primaryStage) {
	    
	    BorderPane loginBp;
	    GridPane formContainer;
	    TextField nameField;
	    PasswordField passField;
	    Label nameLabel, passLabel, loginAlert, titleLabel;
	    Hyperlink registerLink;
	    Button loginButton;
	    
	    
	    ////
	    
	    loginBp = new BorderPane();
        
        loginScene = new Scene(loginBp, 1600, 900);

        formContainer = new GridPane();

        // Login
        titleLabel = new Label("Login");
        titleLabel.setFont(Font.font("Arial", 40));
        nameLabel = new Label("Username	");
        nameField = new TextField("");
        passLabel = new Label("Password	");
        passField = new PasswordField();
        loginButton = new Button("Login");
        registerLink = new Hyperlink("Don't have an account yet? Register Here!");
        loginAlert = new Label();
        
        //positioning
        
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setVgap(10);
        
        formContainer.add(nameLabel, 0, 0);
        formContainer.add(nameField, 0, 1);
        nameField.setPrefWidth(400);
        
        formContainer.add(passLabel, 0, 2);
        formContainer.add(passField, 0, 3);
        
        formContainer.add(loginAlert, 0, 4);

        loginButton.setPrefSize(100, 40);
        
        	
        loginButton.setOnAction(e -> {
        		 
                String username = nameField.getText();
                String password = passField.getText();

                if (username.isEmpty()) {
                    alert(AlertType.ERROR, "Error", "Login Error", "Please fill out your username");
                    return;
                }
                if(password.isEmpty()){
                	alert(AlertType.ERROR, "Error", "Login Error", "Please fill out your password");
                	return;
                }
                
                String queryCredicentials = String.format("SELECT * FROM msuser WHERE Username = '%s' AND UserPassword = '%s'", username, password);
                ResultSet matchCredidentials = connect.execute(queryCredicentials);
                try {
                	
					if (!matchCredidentials.next()) {
						alert(AlertType.ERROR, "Error", "Login Error", "credentials does not match");
						return;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
                try {
					String role = matchCredidentials.getString("UserRole");
					loggedInAs = matchCredidentials.getString("UserID");
					loggedInAsName = username;
					
					if (role.equals("Admin")) {
						
						
						primaryStage.setScene(adminHome);
						
					}else {
						primaryStage.setScene(userHome);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//                
//        		adminHome(primaryStage);
//        		cartPage(primaryStage);
        });
        
        registerLink.setOnAction(e -> primaryStage.setScene(regisScene));
        	
        // positioning
        VBox vBox = new VBox(30); 
        vBox.getChildren().addAll(titleLabel, formContainer,loginButton,registerLink);
        vBox.setAlignment(Pos.CENTER);
        loginBp.setCenter(vBox);
        
	}
	
	public void regis(Stage primaryStage) {
		BorderPane bp;
		GridPane formContainer;
		FlowPane genderContainer;
		
		TextField nameField;
		TextField emailField;
		PasswordField passField;
		
		Label nameLabel, emailLabel, passLabel, genderLabel, titleLabel;
		
		RadioButton maleButton,femaleButton;
		ToggleGroup genderGroup;
		
		Button registerButton;
		
		Hyperlink backToLogin;
		
		//// Declarations
		
		bp = new BorderPane();
		regisScene = new Scene(bp,1600,900);
		formContainer = new GridPane();
		genderContainer = new FlowPane();
		
		nameField = new TextField();
		emailField = new TextField();
		passField = new PasswordField();
		
		nameLabel = new Label("Name");
		emailLabel= new Label("Email");
		passLabel = new Label("Password");
		genderLabel = new Label("Gender");
		genderLabel.setFont(Font.font("Arial", 20));
		titleLabel = new Label("Register");
		titleLabel.setFont(Font.font("Arial", 40));
		
		maleButton = new RadioButton("Male");  
	    femaleButton = new RadioButton("Female");  
	    genderGroup = new ToggleGroup();
		
		registerButton = new Button("Register");
		
		backToLogin = new Hyperlink("Already have an account? Click here to login!");
		
		
		///// Validations 
		
		
		registerButton.setOnAction(e -> {
		String name = nameField.getText();
		String email = emailField.getText().toLowerCase();
        String password = passField.getText();
        RadioButton selectedGender = (RadioButton) genderGroup.getSelectedToggle();
        String gender = selectedGender != null ? selectedGender.getText() : null;
        String role;
        
        
		// Username, Email, Password and Gender cannot be empty
        if (name.isEmpty()) {
            alert(AlertType.ERROR, "Error", "Register Error", "All fields are required.");
            return; 
        }
        if (email.isEmpty()) {
        	alert(AlertType.ERROR, "Error", "Register Error", "All fields are required.");
            return; 
		}
        if (password.isEmpty() ) {
        	alert(AlertType.ERROR, "Error", "Register Error", "All fields are required.");
            return; 
		}
        if (gender == null) {
        	alert(AlertType.ERROR, "Error", "Register Error", "All fields are required.");
        	return;
		}

        //Password must have a length of 8 – 15 characters inclusively (cannot be less than 8 or more than 15 characters)
        if (password.length()<8 || password.length()>15) {
        	alert(AlertType.ERROR, "Error", "Register Error", "Make sure your password cannot be less than 8 or more than 15 characters");
        	return;
		}
        
        //Email must end with @gmail.com
        if (!email.toLowerCase().endsWith("@gmail.com")) {
        	alert(AlertType.ERROR, "Error", "Register Error", "Make sure your email ends with '@gmail.com'");
			return;
		}
        
        //Password must be Alphanumeric
        if (!password.matches(".*\\d.*") || !password.matches(".*[a-zA-Z].*")) {
        	alert(AlertType.ERROR, "Error", "Register Error", "Make sure your password is alphanumeric");
        	return;
		}
        
        //Username must not exist in the database (must be unique)
		try {
			ResultSet duplicateNameCheck = connect.execute("SELECT * FROM msuser WHERE Username = '" + name + "'");
			if (duplicateNameCheck.next()) {
				alert(AlertType.ERROR, "Error", "Register Error", "Please choose another username");
				return;
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
		//Email must not exist in the database (must be unique)
        try {
        	ResultSet duplicateEmailCheck = connect.execute("SELECT * FROM msuser WHERE UserEmail = '" + email + "'");
			if (duplicateEmailCheck.next()) {
				alert(AlertType.ERROR, "Error", "Register Error", "Please choose another email");
				return;
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
        
        
        if (name.contains("admin")) {
			// -> direct ke page admin
        	role = "Admin";
        	primaryStage.setScene(loginScene);
		}else {
			primaryStage.setScene(loginScene);
			role = "Users";
		}
        // input data ke dalam query
        
        Connect connect = Connect.getInstance();
        
        String getMaxUser = "SELECT COUNT(*) AS TotalUsers FROM msuser";
        ResultSet countUsers = connect.execute(getMaxUser);
        
        
        int totalUsers = 0;
        
        try {
			if (countUsers.next()) {
				totalUsers = countUsers.getInt("TotalUsers");
			} 
		} catch (SQLException e1) {
			// TODO: handle exception
			e1.printStackTrace();
		}
        
        int newUserId = totalUsers+ 1;
   
        
        String ID = String.format("US%03d", newUserId);
        
        String insertUser = String.format("INSERT INTO msuser (UserID, Username, UserEmail, UserPassword, UserGender, UserRole) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')", ID, name, email, password, gender, role);
        System.out.println(insertUser);
        
        nameField.clear();
        emailField.clear();
        passField.clear();
        selectedGender.setSelected(false);
        
        connect.executeUpdate(insertUser);
		});
		
		///// positioning
		
		formContainer.setAlignment(Pos.CENTER);
		formContainer.setVgap(10);
		maleButton.setToggleGroup(genderGroup);
		femaleButton.setToggleGroup(genderGroup);
		
		genderContainer.getChildren().add(maleButton);
		genderContainer.getChildren().add(femaleButton);
		
		formContainer.add(nameLabel, 0, 0);
		formContainer.add(nameField, 0, 1);
		
		formContainer.add(passLabel, 0, 2);
		formContainer.add(passField, 0, 3);
		
		formContainer.add(emailLabel, 0, 4);
		formContainer.add(emailField, 0, 5);
		
		formContainer.add(genderLabel, 0, 6);
		formContainer.add(genderContainer, 0, 7);
		
		registerButton.setPrefSize(100, 40);
		
	    backToLogin.setOnAction(e -> primaryStage.setScene(loginScene));
	    
		
		VBox vBox = new VBox(30); 
        vBox.getChildren().addAll(titleLabel, formContainer, registerButton,backToLogin);
        vBox.setAlignment(Pos.CENTER);

        bp.setCenter(vBox);
	}
	
	public void userhome(Stage primaryStage) {

		BorderPane bp = new BorderPane();
		userHome = new Scene(bp,1600,900);
		GridPane contentContainer = new GridPane();
		//navigation bar
		
		Menu menu = new Menu("Menu");
		MenuItem home = new MenuItem("Home");
		MenuItem cart = new MenuItem("Cart");
		MenuItem out = new MenuItem("Log Out");
		MenuBar naviBar = new MenuBar(menu);
		menu.getItems().addAll(home, cart, out);
		cart.setOnAction(e -> {
			cartPage(primaryStage);
			getSelectedCourier = "";
			primaryStage.setScene(cartPage);
		});
		out.setOnAction(e -> primaryStage.setScene(loginScene));
		
		//Everything for table 
		String queryReadItems = "SELECT * FROM msitem";
		
		ResultSet readItems = connect.execute(queryReadItems);
		
		itemlisArrayList = new ArrayList<>();
		
		try {
			while (readItems.next()) {
				String itemID = readItems.getString("ItemID");
				String itemName = readItems.getString("ItemName");
				int itemPrice = readItems.getInt("ItemPrice");
				
				Items itemInstances = new Items(itemID, itemName, itemPrice);
				itemlisArrayList.add(itemInstances);

			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ObservableList<Items> itemDatas = FXCollections.observableArrayList(itemlisArrayList);
		
		items = new TableView<>(itemDatas);
		
		
		TableColumn<Items, String> cnames = new TableColumn<>("Item Name");
		cnames.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		cnames.setMinWidth(250);
		cnames.setMaxWidth(250);
		cnames.setReorderable(false);
		cnames.setResizable(false);
		
		TableColumn<Items, Integer> cprices = new TableColumn<>("Item Price");
		cprices.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
		cprices.setMinWidth(250);
		cprices.setMaxWidth(250);
		cprices.setReorderable(false);
		cprices.setResizable(false);
		
		cnames.setPrefWidth(250);
		cprices.setPrefWidth(250);
		
		items.getColumns().add(cnames);
		items.getColumns().add(cprices);
		
		items.setOnMouseClicked(onClickItems()); // menjalankan event handler
		
		
		
		//home attributes
		itemName = new Label("Item Name");
		itemName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		quantity = new Spinner<>(1, 20, 1);
		
		price = new Label("price");
		price.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		Button addCart = new Button("Add to Cart");
		
		addCart.setOnAction(e -> {
			TableSelectionModel<Items> tableSelectionModel = items.getSelectionModel();
			Items selectedItem = tableSelectionModel.getSelectedItem();
			
			int amountBought = quantity.getValue();
			
			//Condition 1: If the item is not inside the cart yet, make a new cart item with the selected item name and quantity
			if (selectedItem == null) {
				alert(AlertType.ERROR, "Error", "Cart Error", "Please select a item to be added");
			}
			
			selectedItem.getItemID();
			
			Connect connect = Connect.getInstance();
			
			//Condition 2: If the item is already inside the cart, add the current quantity to the old quantity inside the cart 
			String checkCartInfoQuery ="SELECT * FROM cart WHERE UserID = '" + loggedInAs + "' AND ItemID = '" + selectedItem.getItemID() + "'";
			ResultSet cartInfoExist = connect.execute(checkCartInfoQuery);
			
			try {
				if (cartInfoExist.next()) {
					int getExistingQuantity = cartInfoExist.getInt("Quantity");
					int setNewQuantity = getExistingQuantity + amountBought;
					String updatequantity = String.format("UPDATE cart SET Quantity = '%d' WHERE UserID = '%s' AND ItemID = '%s'", setNewQuantity, loggedInAs, selectedItem.getItemID());
					connect.executeUpdate(updatequantity);
					System.out.println(updatequantity);
				}else {
					String cartInsertQuery = String.format("INSERT INTO cart (UserID, ItemID, Quantity) VALUES ('%s', '%s', '%d')", loggedInAs, selectedItem.getItemID(), amountBought);
					connect.executeUpdate(cartInsertQuery);
					System.out.println(cartInsertQuery);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			alert(AlertType.INFORMATION, "Message", "Cart Info", "Item Succesfully added to cart!");
			
			cartPage(primaryStage);
			
		});
		
		items.setMaxHeight(600);

		
		contentContainer.setAlignment(Pos.CENTER);
		contentContainer.setVgap(20);
		contentContainer.add(itemName, 0, 0);
		contentContainer.add(price, 0, 1);
		contentContainer.add(quantity, 0, 2);
		contentContainer.add(addCart, 0, 3);
		quantity.setMinWidth(250);
		
		
		
		bp.setTop(naviBar);
		
		
		HBox hb = new HBox(30);
		
		hb.getChildren().addAll(contentContainer, items);
		hb.setAlignment(Pos.CENTER);
		
		bp.setCenter(hb);
		
		
	}
	
	public void cartPage (Stage primaryStage) {
		BorderPane bp = new BorderPane();
		cartPage = new Scene(bp,1600,900);
		GridPane contentContainer = new GridPane();
		GridPane tableAndNameContainer = new GridPane();
		
		ArrayList<Cart> cartList = new ArrayList<>();
		
		int finalPrice = 0;
		//navigation bar
		
		Menu menu = new Menu("Menu");
		MenuItem home = new MenuItem("Home");
		MenuItem cart = new MenuItem("Cart");
		MenuItem out = new MenuItem("Log Out");
		MenuBar naviBar = new MenuBar(menu);
		menu.getItems().addAll(home, cart, out);
		home.setOnAction(e -> primaryStage.setScene(userHome));
		out.setOnAction(e -> {
			updatedPrice = 0;
			primaryStage.setScene(loginScene);
		});
		
		//table cart
	String readCartQuery = String.format("SELECT cart.UserID, cart.ItemID, cart.Quantity, msitem.ItemName, msitem.ItemPrice FROM cart INNER JOIN msitem ON cart.ItemID = msitem.ItemID WHERE cart.UserID = '%s'", loggedInAs);
		ResultSet cartOfUser = connect.execute(readCartQuery);
		System.out.println(readCartQuery);
		try {
			while (cartOfUser.next()) {
			String itemId = cartOfUser.getString("ItemID");
			String itemName = cartOfUser.getString("ItemName");
			int itemPrice = cartOfUser.getInt("ItemPrice");
			int quantity = cartOfUser.getInt("Quantity");
			int totalPrice = cartOfUser.getInt("ItemPrice") * cartOfUser.getInt("Quantity");
			
			finalPrice = finalPrice + totalPrice;
			netPrice = finalPrice;
			Cart cartInstances = new Cart(itemId, itemName, itemPrice, quantity, totalPrice);
			cartList.add(cartInstances);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ObservableList<Cart> cartDatas = FXCollections.observableArrayList(cartList);
		
		cartTable = new TableView<>(cartDatas);
		TableColumn<Cart, String> name = new TableColumn<>("Item name");
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		name.setMinWidth(150);
		name.setReorderable(false);
		name.setResizable(false);
		
		TableColumn<Cart, Integer> itemPrice = new TableColumn<>("Price");
		itemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		itemPrice.setMinWidth(100);
		itemPrice.setReorderable(false);
		itemPrice.setResizable(false);
		
		TableColumn<Cart, Integer> quantity = new TableColumn<>("Quantity");
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantity.setMinWidth(100);
		quantity.setReorderable(false);
		quantity.setResizable(false);
		
		TableColumn<Cart, Integer> total = new TableColumn<>("Total");
		total.setCellValueFactory(new PropertyValueFactory<>("total"));
		total.setMinWidth(100);
		total.setReorderable(false);
		total.setResizable(false);
		
		cartTable.getColumns().add(name);
		cartTable.getColumns().add(itemPrice);
		cartTable.getColumns().add(quantity);
		cartTable.getColumns().add(total);
		cartTable.setMinHeight(600);
		cartTable.setMaxHeight(600);
		
		
		System.out.println(finalPrice);
		
		Label cartName = new Label(loggedInAsName + "'s Cart");
		cartName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		Label deleteLabel = new Label("Delete Item");
		deleteLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		Button deleteBtn = new Button("Delete Item");
		deleteBtn.setPrefSize(100, 40);
		
		Label courierLabel = new Label("Courier");
		courierLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		courierComboBox = new ComboBox<>();
		courierComboBox.setPromptText("Select a courier");
		ObservableList<String> couriers = courierComboBox.getItems();
		couriers.addAll("HubertExprss", "Speed", "FetEx", "GoGrabUberSend", "Magically Appear");
		
		Label CourierPrice = new Label("Courier Price: ");
		CourierPrice.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		GridPane InsuranceLine = new GridPane();
		InsuranceLine.setHgap(8);
		CheckBox insurance = new CheckBox();
		Label checkBoxLabel = new Label("Use Delivery Insurance");
		InsuranceLine.add(insurance, 0, 0);
		InsuranceLine.add(checkBoxLabel, 1, 0);
		
		Label totalPrice = new Label("TotalPrice: " + finalPrice);
		totalPrice.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		Button checkOut = new Button("Checkout");
		checkOut.setPrefSize(100, 40);
		
		
		deleteBtn.setOnAction(e -> {
			TableSelectionModel<Cart> tableSelectionModel = cartTable.getSelectionModel();
			Cart selectedCart = tableSelectionModel.getSelectedItem(); 
			if (selectedCart == null) {
				
				alert(AlertType.ERROR, "Error", "Deletion Error", "Please select the item to be deleted");
				
			}else {
				cartTable.getItems().removeAll(cartTable.getSelectionModel().getSelectedItem());
				
				String deleteItemQuery = String.format("DELETE FROM cart WHERE UserID = '%s' AND ItemID = '%s'", loggedInAs, selectedCart.getId());
				connect.executeUpdate(deleteItemQuery);
				
				String reCountTotalQuery = String.format("SELECT SUM(cart.Quantity * msitem.ItemPrice) AS TotalPrice FROM cart INNER JOIN msitem ON cart.ItemID = msitem.ItemID WHERE cart.UserID = '%s'", loggedInAs);
				ResultSet reCountTotal = connect.execute(reCountTotalQuery);
				try {
					if (reCountTotal.next()) {
						int newTotal = reCountTotal.getInt("TotalPrice");
						updatedPrice = newTotal;
						netPrice = newTotal;
						beforeInsurancePrice = newTotal;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (insurance.isSelected()) {
					updatedPrice += 2000;
				}
				
				if (courierComboBox != null) {
					getSelectedCourier = courierComboBox.getValue();
					String getCourierInfoQuery = String.format("SELECT * FROM mscourier WHERE CourierName = '%s'", getSelectedCourier);
					ResultSet CourierInfo = connect.execute(getCourierInfoQuery);
					try {
						if (CourierInfo.next()) {
							int courierPrice = CourierInfo.getInt("CourierPrice");
							updatedPrice += courierPrice;
							beforeInsurancePrice += courierPrice;
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				totalPrice.setText("TotalPrice: " + updatedPrice);
				
				alert(AlertType.INFORMATION, "Message", "Deletion Information", "Cart Deleted Succesfully");
			}
			
		});
		
		courierComboBox.setOnAction(e -> {
			getSelectedCourier = courierComboBox.getValue();
			System.out.println(getSelectedCourier);
			int newTotal = 0;
			int courierPrice = 0;
			
			try {
				String reCountTotalQuery = String.format("SELECT SUM(cart.Quantity * msitem.ItemPrice) AS TotalPrice FROM cart INNER JOIN msitem ON cart.ItemID = msitem.ItemID WHERE cart.UserID = '%s'", loggedInAs);
				ResultSet reCountTotal = connect.execute(reCountTotalQuery);
				if (reCountTotal.next()) {
				    newTotal = reCountTotal.getInt("TotalPrice");
				}
				String getCourierInfoQuery = String.format("SELECT * FROM mscourier WHERE CourierName = '%s'", getSelectedCourier);
				ResultSet CourierInfo = connect.execute(getCourierInfoQuery);
				if (CourierInfo.next()) {
					courierPrice = CourierInfo.getInt("CourierPrice");
				}
				updatedPrice = newTotal + courierPrice;
				
				beforeInsurancePrice = updatedPrice;
				
				if (insurance.isSelected()) {
					updatedPrice += 2000;
					totalPrice.setText("TotalPrice: " + updatedPrice);
				}else {
					totalPrice.setText("TotalPrice: " + updatedPrice);
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		});
		
		insurance.setOnAction(e -> {
			String reCountTotalQuery = String.format("SELECT SUM(cart.Quantity * msitem.ItemPrice) AS TotalPrice FROM cart INNER JOIN msitem ON cart.ItemID = msitem.ItemID WHERE cart.UserID = '%s'", loggedInAs);
			ResultSet reCountTotal = connect.execute(reCountTotalQuery);
			
			
			if (updatedPrice == 0) {
				try {
					if (reCountTotal.next()) {
					    int newTotal = reCountTotal.getInt("TotalPrice");
					    if (insurance.isSelected()) {
							int newTotalWithInsurance = newTotal + 2000;
							totalPrice.setText("TotalPrice: " + newTotalWithInsurance);
						}else {
							totalPrice.setText("TotalPrice: " + newTotal);
						}
					}
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else {
				if (insurance.isSelected()) {
					int afterInsurance = beforeInsurancePrice + 2000;
					totalPrice.setText("TotalPrice: " + afterInsurance);
				}else if(!insurance.isSelected()){
					
					totalPrice.setText("TotalPrice: " + beforeInsurancePrice);
				}
			}
			
		});
		
		checkOut.setOnAction(e -> {
			outputInsurancePrice = 0;
			if (getSelectedCourier.isEmpty()) {
				alert(AlertType.ERROR, "Error", "Courier Error", "Select a courier to proceed to checkout");
				return;
			}
			if (cartDatas.isEmpty()) {
				alert(AlertType.ERROR, "Error", "Checkout Error", "No item to check out");
				return;
			}
			
			int checkoutPrice = netPrice;
			System.out.println("netprice: " + netPrice);
			
			if (insurance.isSelected()) {
				insuranceFee = checkoutPrice + 2000;
				checkoutPrice = insuranceFee;
				outputInsurancePrice = insuranceFee;
			}
			
			if (courierComboBox != null) {
				getSelectedCourier = courierComboBox.getValue();
				String getCourierInfoQuery = String.format("SELECT * FROM mscourier WHERE CourierName = '%s'", getSelectedCourier);
				ResultSet CourierInfo = connect.execute(getCourierInfoQuery);
				try {
					if (CourierInfo.next()) {
						courierPrice = CourierInfo.getInt("CourierPrice");
						checkoutPrice += courierPrice;
						outputSelectedCourier = courierPrice;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
			System.out.println("Checkout Price: "+ checkoutPrice);
			outputFinalCheckoutPrice = checkoutPrice;
			transactionPage(primaryStage);
			primaryStage.setScene(transactPage);
			
		});
		tableAndNameContainer.setAlignment(Pos.CENTER);
		tableAndNameContainer.setVgap(10);
		tableAndNameContainer.add(cartName, 0, 0);
		tableAndNameContainer.add(cartTable, 0, 1);
		
		contentContainer.setAlignment(Pos.CENTER);
		contentContainer.setVgap(15);
		
		contentContainer.add(deleteLabel, 0, 0);
		contentContainer.add(deleteBtn, 0, 1);
		contentContainer.add(courierLabel, 0, 2);
		contentContainer.add(courierComboBox, 0, 3);
		contentContainer.add(CourierPrice, 0, 4);
		contentContainer.add(InsuranceLine, 0, 5);
		contentContainer.add(totalPrice, 0, 6);
		contentContainer.add(checkOut, 0, 7);
		
		HBox hb = new HBox(30);
		hb.getChildren().addAll(contentContainer, tableAndNameContainer);
		hb.setAlignment(Pos.CENTER);
		
		bp.setTop(naviBar);
		bp.setCenter(hb);
	}
	
	public void transactionPage(Stage primaryStage) {
		BorderPane bp = new BorderPane();
		transactPage = new Scene(bp, 1600, 900);
		GridPane checkoutInfo = new GridPane();
		GridPane buttonLayout = new GridPane();
		
		Label netPriceLabel = new Label("Net Price: "+ netPrice); 
		netPriceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		Label courierPriceLabel =new Label("Courier Price: "+ courierPrice);
		courierPriceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		Label insurancePrieLabel = new Label("Insurance Price: " + insuranceFee);
		insurancePrieLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		int checkoutPrice = netPrice + courierPrice + insuranceFee;
		Label finalCheckoutLabel = new Label("Checkout Price: "+ checkoutPrice);
		finalCheckoutLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		
		Button cancelButton = new Button("Cancel Checkout");
		cancelButton.setMinSize(100, 40);
		Button confirmButton = new Button("Confirm Checkout");
		confirmButton.setMinSize(100, 40);
		cancelButton.setOnAction(e ->{
			primaryStage.setScene(cartPage);
		});
		
		confirmButton.setOnAction(e -> {
			alert(AlertType.CONFIRMATION, "Checkout", "Checkout Confirm", "Checkout Success");
			String finishCheckoutQuery = String.format("DELETE FROM cart WHERE UserID = '%s'", loggedInAs);
			connect.executeUpdate(finishCheckoutQuery);
			itemlisArrayList.clear();
			cartPage(primaryStage);
			primaryStage.setScene(cartPage);
		});
		checkoutInfo.setAlignment(Pos.CENTER);
		checkoutInfo.setVgap(10);
		checkoutInfo.add(netPriceLabel, 0, 0);
		checkoutInfo.add(courierPriceLabel, 0, 1);
		checkoutInfo.add(insurancePrieLabel, 0, 2);
		checkoutInfo.add(finalCheckoutLabel, 0, 3);
		buttonLayout.setAlignment(Pos.CENTER);
		buttonLayout.setHgap(30);
		buttonLayout.add(cancelButton, 0, 0);
		buttonLayout.add(confirmButton, 1, 0);
		
		VBox vbox = new VBox(30);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(checkoutInfo, buttonLayout);
		
		bp.setCenter(vbox);
		
	}
	
 	public void adminHome (Stage primaryStage) {
		
		BorderPane bp = new BorderPane();
		adminHome = new Scene(bp,1600,900);
		GridPane contentContainer = new GridPane();
		GridPane titleAndTableContainer = new GridPane();
			
			
		// navi bar
		Menu menu = new Menu("Menu");
		MenuItem itemManagement = new MenuItem("Item Management");
		MenuItem logOut = new MenuItem("Log Out");
		MenuBar  naviBar = new MenuBar(menu);
		menu.getItems().addAll(itemManagement, logOut);
		
		logOut.setOnAction(e -> primaryStage.setScene(loginScene));
		
		Label titleItemManagement = new Label("Item Management");
		titleItemManagement.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		Label nameLabel = new Label("Item Name");
		nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		TextField nameField = new TextField();
			
		Label priceLabel = new Label("Item Price");
		priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		TextField priceField = new TextField();
		priceField.setPrefWidth(300);
		
		Button add = new Button("Add Item");
		add.setPrefSize(200, 50);
		
		Button upd = new Button("Update Item");
		upd.setPrefSize(200, 50);
		Button rmv = new Button("Remove Item");
		rmv.setPrefSize(200, 50);
			
			
		//Everything for table
				
		ArrayList<Items> itemManagementArray = new ArrayList<>();
			
		String getItemListQuery = "SELECT * FROM msitem";
		ResultSet getItemList = connect.execute(getItemListQuery);
				
		try {
			while (getItemList.next()) {
				String itemId = getItemList.getString("ItemID");
				String itemName = getItemList.getString("ItemName");
				int itemPrice = getItemList.getInt("ItemPrice");
						
				Items itemsInstancesItems = new Items(itemId, itemName, itemPrice);
				itemManagementArray.add(itemsInstancesItems);
						
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		itemManagementData = FXCollections.observableArrayList(itemManagementArray);
				
		TableView<Items> items = new TableView<>(itemManagementData);
				
		TableColumn<Items, String> cnames = new TableColumn<>("Item Name");
		cnames.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		cnames.setResizable(false);
		cnames.setReorderable(false);
		cnames.setMinWidth(250);
				
		TableColumn<Items, Integer> cprices = new TableColumn<>("Item Price");
		cprices.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
		cprices.setResizable(false);
		cprices.setReorderable(false);
		cprices.setMinWidth(250);
				
		items.getColumns().add(cnames);
		items.getColumns().add(cprices);
				
		items.setMinHeight(600);
		items.setMaxHeight(600);

			
		//button functionality
		
		
		add.setOnAction(e -> {
			String newItemName = nameField.getText();
			String getItemPriceFromField = priceField.getText();
			int newItemPrice;
			String newItemID;
			
			// Validate that the input fields must not be empty
			if (newItemName.isEmpty()) {
				alert(AlertType.ERROR, "Error", "Item Management Error", "name cannot be empty");
				return;
			}
			
			if (getItemPriceFromField.isEmpty()) {
				alert(AlertType.ERROR, "Error", "Item Management Error", "price cannot be empty");
				return;
			}
			
			if (!getItemPriceFromField.matches("\\d*")) {
				alert(AlertType.ERROR, "Error", "Item Management Error", "Price must be in integer");
				return;
			}else {
				newItemPrice = Integer.parseInt(getItemPriceFromField);
			}
			
			String checkUniqueItemNameQuery = String.format("SELECT * FROM msitem WHERE ItemName = '%s'", newItemName);
			ResultSet checkUniqueItemName = connect.execute(checkUniqueItemNameQuery);
			
			try {
				if (checkUniqueItemName.next()) {
					alert(AlertType.ERROR, "Error", "Item Management Error", "Name must be unique");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (newItemPrice < 5000 || newItemPrice > 1000000) {
				alert(AlertType.ERROR, "Error", "Item Management Error", "price must range from 5000 - 1000000");
				return;
			}
			
			String CheckItemIDQuery = "SELECT COUNT(*) AS TotalItem FROM msitem";
			ResultSet getTotalID = connect.execute(CheckItemIDQuery);
			
			int numberItemID = 0;
			
			try {
				if (getTotalID.next()) {
					numberItemID = getTotalID.getInt("TotalItem");
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			while (true) { // menamabah nomor ID jika ada duplicate ID
				numberItemID += 1;
				
				newItemID = String.format("I%04d", numberItemID);
				
				String CheckDuplicateIDQuery = String.format("SELECT * FROM msitem WHERE ItemID = '%s'", newItemID);
				ResultSet CheckDuplicateID = connect.execute(CheckDuplicateIDQuery);
				
				try {
					if (!CheckDuplicateID.next()) {
						break;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			String insertItemDatabaseQuery = String.format("INSERT INTO msitem (ItemID, ItemName, ItemPrice) VALUES ('%s', '%s', '%d')", newItemID, newItemName, newItemPrice);
			
			System.out.println(insertItemDatabaseQuery);

			connect.executeUpdate(insertItemDatabaseQuery);
			
			Items addItem = new Items(newItemID, newItemName, newItemPrice);
			
			itemManagementArray.add(addItem);
			
			items.setItems(FXCollections.observableArrayList(itemManagementArray));
			
			nameField.clear();
			priceField.clear();
			
		});
		
		upd.setOnAction(e -> {
			String newItemName = nameField.getText();
			String getItemPriceFromField = priceField.getText();
			int newItemPrice;
			
			TableSelectionModel<Items> tableSelectionModel = items.getSelectionModel();
			Items selectedItem = tableSelectionModel.getSelectedItem();
			
			if (selectedItem == null) {
				alert(AlertType.ERROR, "Error", "Item Management Error", "Please select a item to update");
			}else {

				// Validate that the input fields must not be empty
				if (newItemName.isEmpty()) {
					alert(AlertType.ERROR, "Error", "Item Management Error", "name cannot be empty");
					return;
				}
				
				if (getItemPriceFromField.isEmpty()) {
					alert(AlertType.ERROR, "Error", "Item Management Error", "price cannot be empty");
					return;
				}
				
				if (!getItemPriceFromField.matches("\\d*")) {
					alert(AlertType.ERROR, "Error", "Item Management Error", "Price must be in integer");
					return;
				}else {
					newItemPrice = Integer.parseInt(getItemPriceFromField);
				}
				
				String checkUniqueItemNameQuery = String.format("SELECT * FROM msitem WHERE ItemName = '%s'", newItemName);
				ResultSet checkUniqueItemName = connect.execute(checkUniqueItemNameQuery);
				
				try {
					if (checkUniqueItemName.next()) {
						alert(AlertType.ERROR, "Error", "Item Management Error", "Name must be unique");
						return;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (newItemPrice < 5000 || newItemPrice > 1000000) {
					alert(AlertType.ERROR, "Error", "Item Management Error", "price must range from 5000 - 1000000");
					return;
				}
				
				String newItemID = selectedItem.getItemID();
				
				String updateItemDatabaseQuery = String.format("UPDATE msitem SET ItemName = '%s', ItemPrice = '%d' WHERE ItemID = '%s'", newItemName, newItemPrice, newItemID);
				
				System.out.println(updateItemDatabaseQuery);
				
				connect.executeUpdate(updateItemDatabaseQuery);
				
				selectedItem.setItemName(newItemName);
				selectedItem.setItemPrice(newItemPrice);
				
				items.refresh();
				
				nameField.clear();
				priceField.clear();
				
			}
		});
		
		rmv.setOnAction(e -> {
			TableSelectionModel<Items> tableSelectionModel = items.getSelectionModel();
			Items selectedItem = tableSelectionModel.getSelectedItem();
			
			if (selectedItem == null) {
				alert(AlertType.ERROR, "Error", "Item Management Error", "Please select a item to delete");
			}else {
				items.getItems().removeAll(selectedItem);
				itemManagementArray.remove(selectedItem);
				String removeItemObjectQuery = String.format("DELETE FROM msitem WHERE ItemID = '%s'", selectedItem.getItemID());
				connect.executeUpdate(removeItemObjectQuery);
			}
			
			for (Items items2 : itemManagementArray) {
				System.out.println(items2.getItemID());
				System.out.println(items2.getItemName());
				System.out.println(items2.getItemPrice());
			}
			
		});
		
		
		
		//positioning
		titleAndTableContainer.setAlignment(Pos.CENTER);
		titleAndTableContainer.setVgap(10);
		titleAndTableContainer.add(titleItemManagement, 0, 0);
		titleAndTableContainer.add(items, 0, 1);
		
		contentContainer.setAlignment(Pos.CENTER);
		contentContainer.setVgap(15);
		contentContainer.add(nameLabel, 0, 0);
		contentContainer.add(nameField, 0, 1);
		contentContainer.add(priceLabel, 0, 2);
		contentContainer.add(priceField, 0, 3);
		contentContainer.add(add, 0, 4);
		contentContainer.add(upd, 0, 5);
		contentContainer.add(rmv, 0, 6);
			
		bp.setTop(naviBar);
		
		HBox hb = new HBox(30);
		hb.getChildren().addAll(titleAndTableContainer, contentContainer);
		hb.setAlignment(Pos.CENTER);
		
		bp.setCenter(hb);
	}

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		login(primaryStage);
		regis(primaryStage);
		userhome(primaryStage);
		adminHome(primaryStage);
		
		primaryStage.setTitle("GoGame!");
		primaryStage.setScene(loginScene);
		primaryStage.show();
		
	}

}
