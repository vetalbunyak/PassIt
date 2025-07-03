package vt.passit.Controllers;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import vt.passit.Animations.TestCardAnimator;
import vt.passit.Modules.SessionManager;
import vt.passit.Modules.Test;
import vt.passit.Modules.User;
import javafx.util.Duration;
import javafx.application.Platform;
import vt.passit.Views.FXMLCustomLoader;

import java.io.IOException;
import java.util.Objects;

public class PassItMainController extends BaseController{
    @FXML private MenuButton accountButton;
    @FXML private MenuItem profileItem;
    @FXML private MenuItem myTestsItem;
    @FXML private MenuItem settingsItem;
    @FXML private MenuItem logoutItem;

    @FXML private TextField searchField;
    @FXML private FlowPane popularTestsPane;

    public void setUser(User user) {
        if (user != null) {
            accountButton.setText(user.getName());
            System.out.println("Головна панель завантажена для користувача: " + user.getName() + ", роль: " + user.getRole());

            String userProfilePicturePath = user.getProfilePicturePath();

            try {
                Image userIcon;
                if (userProfilePicturePath != null && !userProfilePicturePath.isEmpty()) {
                    userIcon = new Image("file:" + userProfilePicturePath);
                    if (userIcon.isError()) {
                        throw new Exception("Не вдалося завантажити зображення з шляху: " + userProfilePicturePath);
                    }
                } else {

                    userIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/vt/passit/Images/default_user.png"))); // Or your generic user-icon.png
                    if (userIcon.isError()) {
                        throw new Exception("Не вдалося завантажити стандартну іконку користувача.");
                    }
                }

                ImageView iconView = new ImageView(userIcon);
                iconView.setFitWidth(20);
                iconView.setFitHeight(20);
                accountButton.setGraphic(iconView);

            } catch (Exception e) {
                System.err.println("Помилка завантаження іконки профілю: " + e.getMessage());
                try {
                    Image fallbackIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/vt/passit/Images/default_user.png"))); // Ensure this path is correct
                    ImageView fallbackIconView = new ImageView(fallbackIcon);
                    fallbackIconView.setFitWidth(20);
                    fallbackIconView.setFitHeight(20);
                    accountButton.setGraphic(fallbackIconView);
                } catch (Exception fallbackE) {
                    System.err.println("Помилка завантаження резервної іконки: " + fallbackE.getMessage());
                    accountButton.setGraphic(null);
                }
            }
        } else {
            accountButton.setText("Гість");
            accountButton.setGraphic(null);
            System.err.println("Помилка: Головна панель завантажена без авторизованого користувача.");
        }
    }

    @FXML
    public void initialize() {
        profileItem.setOnAction(_ -> {
            try {
                handleProfile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        myTestsItem.setOnAction(_ -> handleMyTests());
        settingsItem.setOnAction(_ -> {
            try {
                handleSettings();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        logoutItem.setOnAction(_ -> handleLogout());

        Platform.runLater(() -> {
            final double initialPrefWidth = searchField.prefWidthProperty().get();

            final double expandedPrefWidth = initialPrefWidth * 1.5;

            searchField.focusedProperty().addListener((_, _, newVal) -> {
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

            accountButton.pressedProperty().addListener((_, _, isPressed) -> {
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

    private void handleProfile() throws IOException {
        FXMLCustomLoader loader = new FXMLCustomLoader("Profile-view.fxml");
        Stage newStage = loader.getStage();

        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(this.stage);
        newStage.setTitle("Профіль");
        newStage.setMinWidth(700);
        newStage.setMinHeight(600);
        newStage.setWidth(700);
        newStage.setHeight(600);
        newStage.centerOnScreen();
        newStage.show();
    }

    private void handleMyTests() {

    }

    private void handleSettings() throws IOException {
        FXMLCustomLoader loader = new FXMLCustomLoader("ProfileSettings-view.fxml");
        Stage newStage = loader.getStage();

        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(this.stage);
        newStage.setTitle("Налаштування");
        newStage.setMinWidth(700);
        newStage.setMinHeight(900);
        newStage.setWidth(700);
        newStage.setHeight(900);
        newStage.centerOnScreen();
        newStage.show();
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
        } catch(IOException _){

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
