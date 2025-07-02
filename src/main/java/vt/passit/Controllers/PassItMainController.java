package vt.passit.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import vt.passit.Modules.SessionManager;
import vt.passit.Modules.Test;
import vt.passit.Modules.User;


public class PassItMainController {
    @FXML private BorderPane mainRoot;
    @FXML private HBox topBar;
    @FXML private Region topSpacer;
    @FXML private MenuButton accountButton;
    @FXML private MenuItem myTestsItem;
    @FXML private MenuItem settingsItem;
    @FXML private MenuItem logoutItem;

    @FXML private VBox centerBox;
    @FXML private TextField searchField;
    @FXML private Label sectionTitle;
    @FXML private FlowPane popularTestsPane;
    private User user;

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            accountButton.setText(user.getUsername());
            System.out.println("Головна панель завантажена для користувача: " + user.getUsername() + ", роль: " + user.getRole());

            if (SessionManager.getInstance().isAdmin()) {
                System.out.println("Користувач є адміністратором.");
            }
        } else {
            accountButton.setText("Гість");
            System.err.println("Помилка: Головна панель завантажена без авторизованого користувача.");
        }
    }

    @FXML
    public void initialize() {
        myTestsItem.setOnAction(event -> handleMyTests());
        settingsItem.setOnAction(event -> handleSettings());
        logoutItem.setOnAction(event -> handleLogout());

        searchField.setOnAction(event -> handleSearch());

        loadPopularTests();
    }

    private void handleSearch() {

    }

    private void handleMyTests() {

    }

    private void handleSettings() {

    }

    private void handleLogout() {

    }

    private void loadPopularTests() {
        popularTestsPane.getChildren().clear();

        Test test1 = new Test(1, "Test1", "Test test1.", 100);
        Test test2 = new Test(2, "Test2", "Test test2.", 90);
        Test test3 = new Test(3, "Test3", "Test test3.", 85);
        Test test4 = new Test(4, "Test4", "Test test4.", 75);

        popularTestsPane.getChildren().add(createTestCard(test1));
        popularTestsPane.getChildren().add(createTestCard(test2));
        popularTestsPane.getChildren().add(createTestCard(test3));
        popularTestsPane.getChildren().add(createTestCard(test4));

        System.out.println("Завантаження тестових тестів для перевірки UI.");
    }


    private VBox createTestCard(Test test) {
        VBox card = new VBox(5);
        card.setPrefSize(200, 150);
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.getStyleClass().add("test-card");
        card.setUserData(test.getId());

        Label titleLabel = new Label(test.getTitle());
        titleLabel.getStyleClass().add("test-title");
        titleLabel.setWrapText(true);

        Label descLabel = new Label(test.getDescription());
        descLabel.getStyleClass().add("test-desc");
        descLabel.setWrapText(true);

        card.setOnMouseClicked(event -> {
            int clickedTestId = (int) card.getUserData();
            System.out.println("Картка тесту ID: " + clickedTestId + ", Назва: " + test.getTitle() + " натиснута.");
        });

        card.getChildren().addAll(titleLabel, descLabel);
        return card;
    }
}
