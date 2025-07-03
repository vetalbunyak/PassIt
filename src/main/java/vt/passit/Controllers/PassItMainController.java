package vt.passit.Controllers;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import vt.passit.Animations.TestCardAnimator;
import vt.passit.Modules.SessionManager;
import vt.passit.Modules.Test;
import vt.passit.Modules.User;
import javafx.util.Duration;
import javafx.application.Platform;
import vt.passit.Views.FXMLCustomLoader;

import java.io.IOException;

public class PassItMainController extends BaseController{
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
            accountButton.setText(user.getName());
            System.out.println("Головна панель завантажена для користувача: " + user.getName() + ", роль: " + user.getRole());

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

        Platform.runLater(() -> {
            final double initialPrefWidth = searchField.prefWidthProperty().get();

            final double expandedPrefWidth = initialPrefWidth * 1.5;

            searchField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                ParallelTransition animIn = new ParallelTransition();
                ParallelTransition animOut = new ParallelTransition();

                KeyValue kvWidthIn = new KeyValue(searchField.prefWidthProperty(), expandedPrefWidth, Interpolator.EASE_BOTH);
                KeyFrame kfWidthIn = new KeyFrame(Duration.millis(300), kvWidthIn);
                Timeline timelineWidthIn = new Timeline(kfWidthIn);

                KeyValue kvWidthOut = new KeyValue(searchField.prefWidthProperty(), initialPrefWidth, Interpolator.EASE_BOTH);
                KeyFrame kfWidthOut = new KeyFrame(Duration.millis(300), kvWidthOut);
                Timeline timelineWidthOut = new Timeline(kfWidthOut);

                if (newVal) {
                    animIn.getChildren().addAll(timelineWidthIn);
                    animIn.play();
                } else {
                    animOut.getChildren().addAll(timelineWidthOut);
                    animOut.play();
                }
            });

            accountButton.pressedProperty().addListener((obs, wasPressed, isPressed) -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(100), accountButton);
                if (isPressed) {
                    st.setToX(0.95);
                    st.setToY(0.95);
                } else {
                    st.setToX(1.0);
                    st.setToY(1.0);
                }
                st.play();
            });
        });

        loadPopularTests();
    }

    private void handleSearch() {

    }

    private void handleMyTests() {

    }

    private void handleSettings() {

    }

    private void handleLogout() {
        SessionManager.getInstance().logout();

        try{
            FXMLCustomLoader loader = new FXMLCustomLoader("AuthWindow-view.fxml", 400, 300);
            Stage newStage = loader.getStage();

            newStage.setTitle("Авторизація");
            newStage.centerOnScreen();
            this.stage.close();
            newStage.show();
        } catch(IOException e){

        }
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
            System.out.println("Картка тесту ID: " + clickedTestId + ", Назва: " + test.getTitle() + " клікнута.");
            // TODO: логіка початку тесту при кліку на картку
        });

        card.getChildren().addAll(titleLabel, descLabel);

        TestCardAnimator.applyHoverAnimation(card);

        return card;
    }
}
