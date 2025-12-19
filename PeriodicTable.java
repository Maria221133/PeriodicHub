package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PeriodicTable extends Application {
	private Element elements;
	private GridPane periodicTableGrid;
	private String currentUser;
	private String userRole;
	private Database db;
	private MenuItem uploadResearchItem;
	private MenuItem profileItem;
	private Menu researchMenu;
	private MenuItem researchItem;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		stage.setTitle("Periodic Table");

		try {
			db = new Database();
			db.openConnection();
			MenuBar menuBar = new MenuBar();
			menuBar.setPrefWidth(20);

			Menu userMenu = new Menu("Login/Create Account");
			MenuItem loginItem = new MenuItem("Login");
			MenuItem createAccountItem = new MenuItem("Create Account");
			userMenu.getItems().addAll(loginItem, createAccountItem);
			menuBar.getMenus().add(userMenu);

			researchMenu = new Menu("Research");
			researchMenu.setDisable(true);
			researchItem = new MenuItem("Research Page");

			uploadResearchItem = new MenuItem("Upload Research");
			uploadResearchItem.setVisible(false);

			researchMenu.getItems().addAll(researchItem, uploadResearchItem);
			menuBar.getMenus().add(researchMenu);

			profileItem = new MenuItem("Profile");
			profileItem.setVisible(false);
			userMenu.getItems().add(profileItem);

			MenuItem viewResearchItem = new MenuItem("View Research");
			researchMenu.getItems().add(viewResearchItem);

			periodicTableGrid = createPeriodicTableGrid();

			Label titleLabel = new Label("Periodic Table");
			titleLabel.setStyle(
					"-fx-background-color: #FFE4E1; -fx-font-size: 30px; -fx-font-family: 'Comic Sans MS'; -fx-font-weight: bold; -fx-text-fill: Black;");
			VBox.setVgrow(titleLabel, Priority.ALWAYS);
			titleLabel.setMaxWidth(Double.MAX_VALUE);
			titleLabel.setAlignment(Pos.CENTER);

			VBox mainContainer = new VBox(10);
			mainContainer.getChildren().addAll(menuBar, titleLabel, periodicTableGrid);
			mainContainer.setPadding(new Insets(10));

			Scene scene = new Scene(mainContainer);
			stage.setScene(scene);

			loginItem.setOnAction(e -> showLoginDialog());
			createAccountItem.setOnAction(e -> showCreateAccountDialog());
			researchItem.setOnAction(e -> showResearchPage());
			profileItem.setOnAction(e -> showProfile());
			viewResearchItem.setOnAction(e -> viewResearch());
			uploadResearchItem.setOnAction(e -> uploadResearch());

		} catch (SQLException e) {
			System.err.println("Error connecting to database: " + e.getMessage());
			showErrorAlert("Error connecting to database.", "view periodic Table");
		}

		stage.show();
	}

	private GridPane createPeriodicTableGrid() {
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(1);
		gridPane.setVgap(1);
		gridPane.setPadding(new Insets(10));
		gridPane.setStyle("-fx-background-color: #FFE4E1;");

		try {
			List<Element> elements = db.getElements();
			System.out.println("Number of elements retrieved: " + elements.size());

			int row = 0;
			int col = 0;
			for (Element element : elements) {
				Button button = new Button(element.getSymbol());
				System.out.println("Element symbol: " + element.getSymbol());

				button.setPrefSize(40, 40);
				button.setOnAction(event -> {
					if (currentUser == null) {
						showLoginOrCreateAccountAlert();
					} else {
						showElementDetails(element);
					}
				});

				int period = element.getPeriod();
				int group = element.getGroup();

				row = period;
				col = group;

				int elementTypeId = element.getElementTypeId();
				String elementTypeName = db.getElementTypeName(elementTypeId);
				System.out.println("ElementTypeId: " + elementTypeId);

				if (elementTypeName != null) {
					switch (elementTypeName) {
					case "Alkali Metal":
						button.setStyle("-fx-background-color: #FFC0CB; -fx-border-color: black;");
						break;
					case "Alkaline Earth Metal":
						button.setStyle("-fx-background-color: #FFA07A; -fx-border-color: black;");
						break;
					case "Transition Metal":
						button.setStyle("-fx-background-color: #C71585; -fx-border-color: black;");
						break;
					case "Metalloid":
						button.setStyle("-fx-background-color: #FFDAB9; -fx-border-color: black;");
						break;
					case "Nonmetal":
						button.setStyle("-fx-background-color: #FAEBD7; -fx-border-color: black;");
						break;
					case "Halogen":
						button.setStyle("-fx-background-color: #E6E6FA; -fx-border-color: black;");
						break;
					case "Noble Gas":
						button.setStyle("-fx-background-color: #DDA0DD; -fx-border-color: black;");
						break;
					case "Lanthanide":
						button.setStyle("-fx-background-color: #F08080; -fx-border-color: black;");
						break;
					case "Actinide":
						button.setStyle("-fx-background-color: #F5DEB3; -fx-border-color: black;");
						break;
					case "Post-transition Metal":
						button.setStyle("-fx-background-color: #F0FFF0; -fx-border-color: black;");
						break;
					default:
						button.setStyle("-fx-background-color: #FFF0F5; -fx-border-color: black;");
					}
				} else {
					button.setStyle("-fx-background-color: gray;");
					System.err.println("Element type not found for elementTypeId: " + elementTypeId);
				}
				gridPane.add(button, col, row);
			}

		} catch (SQLException e) {
			System.err.println("Error getting elements from database: " + e.getMessage());
			showErrorAlert("Error loading periodic table.", "view periodic Table");
		}
		return gridPane;
	}

	private void showLoginDialog() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Login");

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		TextField usernameField = new TextField();
		usernameField.setPromptText("Username");
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		
		grid.add(new Label("Username:"), 0, 0);
		grid.add(usernameField, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(passwordField, 1, 1);

		dialog.getDialogPane().setContent(grid);
		ButtonType loginButton = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButton, ButtonType.CANCEL);
		dialog.setResultConverter(button -> {

			if (button == loginButton) {
				try {
					String username = usernameField.getText();
					String password = passwordField.getText();

					if (username.isEmpty() || password.isEmpty()) {
						showErrorAlert("Please enter both username and password.", "login");
						return null;
					}
					
					User user = db.getUser(username, password);
					if (user != null) {
						currentUser = username;
						userRole = user.getRole();
						
						researchMenu.setDisable(false);

						if (userRole.equals("2")) {
							profileItem.setVisible(true);
							uploadResearchItem.setVisible(true);
						} else {
							profileItem.setVisible(true);
							uploadResearchItem.setVisible(false);
						}
					} else {
						showErrorAlert("Incorrect username or password.", "login");
					}
				} catch (SQLException e) {
					System.err.println("Error during login: " + e.getMessage());
					showErrorAlert("Error during login.", "login");
				}
			}
			return null;
		});
		
		dialog.showAndWait();
	}

	private void showCreateAccountDialog() {
		Dialog<ButtonType> dialog = new Dialog<>();
		
		dialog.setTitle("Create Account");
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));

		TextField usernameField = new TextField();
		usernameField.setPromptText("Username");
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		TextField emailField = new TextField();
		emailField.setPromptText("Email");
		ComboBox<String> roleComboBox = new ComboBox<>();

		roleComboBox.getItems().addAll("student", "researcher");
		roleComboBox.setValue("student");
		grid.add(new Label("Role:"), 0, 2);
		grid.add(roleComboBox, 1, 2);
		grid.add(new Label("Username:"), 0, 0);
		grid.add(usernameField, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(passwordField, 1, 1);
		grid.add(new Label("Email:"), 0, 3);
		grid.add(emailField, 1, 3);

		dialog.getDialogPane().setContent(grid);
		ButtonType createButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(createButton, ButtonType.CANCEL);
		dialog.setResultConverter(button -> {
			if (button == createButton) {
				try {
					String username = usernameField.getText();
					String password = passwordField.getText();

					if (username.isEmpty() || password.isEmpty()) {
						showErrorAlert("Please enter both username and password.", "create account");
						return null;
					}

					if (db.usernameExists(username)) {
						showErrorAlert("Username already exists. Please choose a different one.", "create account");
						return null;
					}
					
					String email = emailField.getText();
					String role = roleComboBox.getValue();
					db.createUser(username, password, email, role);
					currentUser = username;
					userRole = role;

				} catch (SQLException e) {
					System.err.println("Error creating account: " + e.getMessage());
					showErrorAlert("Error creating account.", "create account");
				}
			}
			return null;
		});

		dialog.showAndWait();
	}

	private void showElementDetails(Element element) {
		Dialog<Void> dialog = new Dialog<>();
		dialog.setTitle(element.getName() + " Details");

		Label atomicNumberLabel = new Label("Atomic Number: " + element.getAtomicNumber());
		Label nameLabel = new Label("Name: " + element.getName());
		Label symbolLabel = new Label("Symbol: " + element.getSymbol());
		Label atomicMassLabel = new Label("Atomic Mass: " + element.getAtomicMass());
		Label densityLabel = new Label("Density: " + element.getDensity());
		Label meltingPointLabel = new Label("Melting Point: " + element.getMeltingPoint());
		Label boilingPointLabel = new Label("Boiling Point: " + element.getBoilingPoint());
		Label stateOfMatterLabel = new Label("State of Matter: " + element.getStateOfMatter());
		Label electronConfigurationLabel = new Label("Electron Configuration: " + element.getElectronConfiguration());

		VBox content = new VBox(10);
		content.getChildren().addAll(
				atomicNumberLabel,
				nameLabel,
				symbolLabel,
				atomicMassLabel,
				densityLabel,
				meltingPointLabel,
				boilingPointLabel,
				stateOfMatterLabel,
				electronConfigurationLabel
				);

		content.setPadding(new Insets(20));
		dialog.getDialogPane().setContent(content);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		dialog.showAndWait();
	}

	private void showErrorAlert(String message, String returnPage) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.setContentText(message + "\n\nReturn to " + returnPage + " page.");

		VBox content = new VBox();
		Label messageLabel = new Label(message);
		Hyperlink link = new Hyperlink("Click here to return to " + returnPage + " page.");
		content.getChildren().addAll(messageLabel, link);
		alert.getDialogPane().setContent(content);

		link.setOnAction(e -> {
			if (returnPage.equals("login")) {
				showLoginDialog();
			} else if (returnPage.equals("create account")) {
				showCreateAccountDialog();
			} else if (returnPage.equals("view album")) {
				System.out.println("Returning to view album page...");
				alert.close();
			}
		});

		alert.showAndWait();
	}

	private void showLoginOrCreateAccountAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Login or Create Account");
		alert.setHeaderText(null);
		alert.setContentText("You need to be logged in to view element details.");
		ButtonType loginButton = new ButtonType("Login");
		ButtonType createAccountButton = new ButtonType("Create Account");
		ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(loginButton, createAccountButton, cancelButton);
		alert.showAndWait().ifPresent(response -> {
			
			if (response == loginButton) {
				showLoginDialog();
			} else if (response == createAccountButton) {
				showCreateAccountDialog();
			}
		});
	}

	private void showResearchPage() {
		try {
			Stage researchStage = new Stage();
			researchStage.setTitle("Research Papers");
			List<ResearchPaper> papers = db.getResearchPapers();
			ListView<ResearchPaper> paperListView = new ListView<>();
			paperListView.setCellFactory(param -> new ListCell<ResearchPaper>() {

				@Override
				protected void updateItem(ResearchPaper paper, boolean empty) {
					super.updateItem(paper, empty);

					if (empty || paper == null) {
						setText(null);
						setGraphic(null);

					} else {
						VBox vbox = new VBox();
						Label titleLabel = new Label(paper.getTitle());
						
						titleLabel.setStyle("-fx-font-weight: bold;");
						Label authorLabel = new Label("By " + paper.getAuthor());
						Label dateLabel = new Label("Published: " + paper.getPublicationDate().toString());
						Hyperlink urlLink = new Hyperlink(paper.getUrl());
						
						urlLink.setOnAction(e -> {
							try {
								Desktop.getDesktop().browse(new URI(paper.getUrl()));
							} catch (IOException | URISyntaxException ex) {
								((Throwable) ex).printStackTrace();
							}
						});
						
						vbox.getChildren().addAll(titleLabel, authorLabel, dateLabel, urlLink);
						setGraphic(vbox);
					}
				}
			});
			paperListView.getItems().addAll(papers);
			Scene researchScene = new Scene(paperListView, 800, 600);
			researchStage.setScene(researchScene);
			researchStage.show();
		} catch (SQLException e) {
			System.err.println("Error loading research papers: " + e.getMessage());
			showErrorAlert("Error loading research papers.", "main page");
		}
	}

	private void showProfile() {
		try {
			User researcher = db.getUserDetails(currentUser);
			
			if (researcher != null) {
				Dialog<Void> dialog = new Dialog<>();
				dialog.setTitle("Researcher Profile");

				Label nameLabel = new Label("Name: " + researcher.getUsername());
				Label emailLabel = new Label("Email: " + researcher.getEmail());

				VBox content = new VBox(10);
				content.getChildren().addAll(nameLabel, emailLabel);
				content.setPadding(new Insets(20));
				
				dialog.getDialogPane().setContent(content);
				dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
				dialog.showAndWait();

			} else {
				showErrorAlert("Error loading profile.", "profile");
			}
		} catch (SQLException e) {
			System.err.println("Error getting researcher details: " + e.getMessage());
			showErrorAlert("Error loading profile.", "profile");
		}
	}

	private void uploadResearch() {
		Dialog<ButtonType> dialog = new Dialog<>();

		dialog.setTitle("Upload Research Paper");
		TextField titleField = new TextField();
		titleField.setPromptText("Title");
		TextField authorField = new TextField();
		authorField.setPromptText("Author(s)");
		DatePicker publicationDatePicker = new DatePicker();
		TextArea abstractArea = new TextArea();
		abstractArea.setPromptText("Abstract");
		TextField urlField = new TextField();
		urlField.setPromptText("URL (if applicable)");
		GridPane grid = new GridPane();

		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20));
		grid.add(new Label("Title:"), 0, 0);
		grid.add(titleField, 1, 0);
		grid.add(new Label("Author(s):"), 0, 1);
		grid.add(authorField, 1, 1);
		grid.add(new Label("Publication Date:"), 0, 2);
		grid.add(publicationDatePicker, 1, 2);
		grid.add(new Label("Abstract:"), 0, 3);
		grid.add(abstractArea, 1, 3);
		grid.add(new Label("URL:"), 0, 4);
		grid.add(urlField, 1, 4);
		
		dialog.getDialogPane().setContent(grid);
		ButtonType uploadButton = new ButtonType("Upload", ButtonBar.ButtonData.OK_DONE);

		dialog.getDialogPane().getButtonTypes().addAll(uploadButton, ButtonType.CANCEL);
		dialog.setResultConverter(buttonType -> {

			if (buttonType == uploadButton) {
				try {
					String title = titleField.getText();
					String authors = authorField.getText();
					LocalDate publicationDate = publicationDatePicker.getValue();
					String abstractText = abstractArea.getText();
					String url = urlField.getText();
					Element selectedElement = getSelectedElement();
					
					if (selectedElement != null) {
						int atomicNumber = selectedElement.getAtomicNumber();
						db.insertResearchPaper(atomicNumber, currentUser, title, authors, publicationDate, abstractText,
								url);
					} else {
						showErrorAlert("Please select an element first.", "periodic table");
					}

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Success");
					alert.setHeaderText(null);
					alert.setContentText("Research paper uploaded successfully.");
					alert.showAndWait();
				} catch (SQLException e) {
					System.err.println("Error uploading research paper: " + e.getMessage());
					showErrorAlert("Error uploading research paper.", "upload research");
				}
			}
			return null;
		});
		dialog.showAndWait();
	}

	private Element getSelectedElement() {
		for (javafx.scene.Node node : periodicTableGrid.getChildren()) {
			if (node instanceof Button) {
				Button button = (Button) node;
				if (button.isFocused()) {
					return (Element) button.getUserData();
				}
			}
		}
		return null;
	}

	private void viewResearch() {
		try {
			List<ResearchPaper> papers = db.getResearchPapers();

			if (papers != null && !papers.isEmpty()) {
				Dialog<Void> dialog = new Dialog<>();
				dialog.setTitle("Research Papers");
				ListView<ResearchPaper> listView = new ListView<>();

				listView.getItems().addAll(papers);
				listView.setCellFactory(param -> new ListCell<>() {
					@Override
					protected void updateItem(ResearchPaper paper, boolean empty) {
						super.updateItem(paper, empty);

						if (empty || paper == null) {
							setText(null);
						} else {
							setText(paper.getTitle());
						}
					}
				});

				listView.setOnMouseClicked(event -> {
					ResearchPaper selectedPaper = listView.getSelectionModel().getSelectedItem();

					if (selectedPaper != null) {
						showResearchPaperDetails(selectedPaper);
					}
				});

				VBox content = new VBox(10);
				content.getChildren().add(listView);
				content.setPadding(new Insets(20));

				dialog.getDialogPane().setContent(content);
				dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
				dialog.showAndWait();
			} else {
				showErrorAlert("No research papers found.", "view research");
			}
		} catch (SQLException e) {
			System.err.println("Error getting research papers: " + e.getMessage());
			showErrorAlert("Error loading research papers.", "view research");
		}
	}

	private void showResearchPaperDetails(ResearchPaper paper) {
		Dialog<Void> dialog = new Dialog<>();
		dialog.setTitle("Research Paper Details");

		Label titleLabel = new Label("Title: " + paper.getTitle());
		Label authorsLabel = new Label("Authors: " + paper.getAuthor());
		Label publicationDateLabel = new Label("Publication Date: " + paper.getPublicationDate());
		Label abstractLabel = new Label("Abstract: " + paper.getAbstractText());
		
		VBox content = new VBox(10);
		content.getChildren().addAll(titleLabel, authorsLabel, publicationDateLabel, abstractLabel);
		content.setPadding(new Insets(20));
		dialog.getDialogPane().setContent(content);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		dialog.showAndWait();

	}
}