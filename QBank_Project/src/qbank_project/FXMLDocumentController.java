package qbank_project;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Hyperlink si_forgotPass;
    @FXML
    private Button si_loginBtn, side_CreateBtn, su_signupBtn, side_alreadyHave;
    @FXML
    private AnchorPane si_loginForm, su_signupForm, side_form;
    @FXML
    private PasswordField si_password, su_password;
    @FXML
    private TextField si_username, su_username, su_answer;
    @FXML
    private ComboBox<String> su_question;
    @FXML
    private Label AlreadyHave_text, create_accountText;

    @FXML
    private TextField fp_answer;

    @FXML
    private Button fp_back;

    @FXML
    private Button fp_preceedBtn;

    @FXML
    private ComboBox<String> fp_question;

    @FXML
    private AnchorPane fp_questionForm;

    @FXML
    private Button np_back;

    @FXML
    private Button np_changePassBtn;

    @FXML
    private PasswordField np_confirmPassword;

    @FXML
    private AnchorPane np_newPassForm;

    @FXML
    private PasswordField np_newPassword;

    @FXML
    private TextField fp_username;

    @FXML
    private ComboBox<String> Batch_drop;

    @FXML
    private ComboBox<String> CourseCode_drop;

    @FXML
    private ComboBox<String> Departmant_drop;

    @FXML
    private ComboBox<String> Type_drop;

    @FXML
    private Button signOutBtn;

    @FXML
    private Button welcomeBtn;

    @FXML
    private Label your_Name;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;

    private final String[] questionList = {
        "What is your Birth Date?",
        "What is your favorite food?",
        "What is your favorite color?",
        "What city were you born in?",
        "What is your pet’s name?"
    };

    // Department List - Completed with all departments from BAUST (you can customize as needed)
//    private final String[] departmentList = {
//        "CSE", "EEE", "ME", "CE",
//        "IPE", "BBA", "English",
//        "Architecture", "Physics",
//        "Chemistry", "Mathematics",
//        "Law", "Economics"
//    };

// Type List - Already good
    private final String[] typeList = {
        "CT",
        "LAB-CT",
        "Mid-Term",
        "LAB Mid",
        "Final",
        "LAB Final"
    };

// Batch List - Cleaned up formatting
//    private final String[] batchList = {
//        "Batch-10", "Batch-11",
//        "Batch-12", "Batch-13",
//        "Batch-14", "Batch-15",
//        "Batch-16", "Batch-17",
//        "Batch-18", "Batch-19",
//        "Batch-20", "Batch-21",
//        "Batch-22", "Batch-23",
//        "Batch-24"};

// Course Code List - Expanded to 30 course codes
//    private final String[] courseCodeList = {
//        "CSE101", "CSE102", "CSE103", "CSE104",
//        "CSE105", "CSE201", "CSE202",
//        "CSE203", "CSE204", "CSE205", "EEE101",
//        "EEE102", "EEE103", "EEE104", "EEE105",
//        "ME101", "ME102", "ME103", "ME104",
//        "ME105", "CE101", "CE102", "CE103",
//        "CE104", "CE105", "BBA101", "BBA102",
//        "ENG101", "ENG102", "MATH101", "PHY101"};

    public void loginBtn() {

        if (si_username.getText().isEmpty() || si_password.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Incorrect Username/Password");
        } else {
            String selctData = "SELECT username, password FROM users WHERE username = ? and password = ?";
            connect = Database.connectDB();

            try {
                prepare = connect.prepareStatement(selctData);
                prepare.setString(1, si_username.getText());
                prepare.setString(2, si_password.getText());

                result = prepare.executeQuery();

                if (result.next()) {
                    showAlert(Alert.AlertType.INFORMATION, "Successfully Logged In!");

                    // TO GET THE USERNAME THAT USER USED
                    data.username = si_username.getText();
                    // LINK YOUR MAIN FORM
                    var root = FXMLLoader.load(getClass().getResource("main2Form.fxml"));

                    Stage stage = new Stage();
                    Scene scene = new Scene((Parent) root);

                    stage.setTitle("Baust Question Bank");
                    stage.getIcons().add(
                            new Image(getClass().getResourceAsStream("/qbank_project/img/icon.png"))
                    );
                    stage.setMinWidth(1100);
                    stage.setMinHeight(600);

                    stage.setScene(scene);
                    stage.show();

                    ((Stage) si_loginBtn.getScene().getWindow()).close();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Incorrect Username/Password");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public void loadDepartmentList() {
//        ObservableList<String> listData = FXCollections.observableArrayList(Arrays.asList(departmentList));
//        Departmant_drop.setItems(listData);
//    }

    public void regBtn() {
        if (su_username.getText().isEmpty() || su_password.getText().isEmpty()
                || su_question.getSelectionModel().getSelectedItem() == null
                || su_answer.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Please fill all blank fields.");
        } else {
            connect = Database.connectDB();

            try {
                String checkUsername = "SELECT username FROM users WHERE username = ?";
                prepare = connect.prepareStatement(checkUsername);
                prepare.setString(1, su_username.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    showAlert(Alert.AlertType.WARNING, su_username.getText() + " is already taken");
                } else if (su_password.getText().length() < 5) {
                    showAlert(AlertType.ERROR, "Password must be at least 5 characters");
                } else {
                    String regData = "INSERT INTO users (username, password, question, "
                            + "answer, date) VALUES (?, ?, ?, ?, ?)";
                    prepare = connect.prepareStatement(regData);
                    prepare.setString(1, su_username.getText());
                    prepare.setString(2, su_password.getText());
                    prepare.setString(3, su_question.getSelectionModel().getSelectedItem());
                    prepare.setString(4, su_answer.getText());

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setDate(5, sqlDate);

                    prepare.executeUpdate();

                    showAlert(AlertType.INFORMATION, "Successfully registered account!");

                    // Clear form
                    su_username.clear();
                    su_password.clear();
                    su_answer.clear();
                    //su_question.getSelectionModel().clearSelection();

                    slideToLoginForm();
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Registration failed.");
            }
        }
    }

    public void loadQuestionList() {
        ObservableList<String> listData = FXCollections.observableArrayList(Arrays.asList(questionList));
        su_question.setItems(listData);
    }

//    public void click() {
//        loadDepartmentList();
//        loadTypeList();
//        loadBatchList();
//        loadCourseCodeList();
//    }

    public void loadTypeList() {
        ObservableList<String> listData = FXCollections.observableArrayList(Arrays.asList(typeList));
        Type_drop.setItems(listData);
    }

//    public void loadBatchList() {
//        ObservableList<String> listData = FXCollections.observableArrayList(Arrays.asList(batchList));
//        Batch_drop.setItems(listData);
//    }

//    public void loadCourseCodeList() {
//        ObservableList<String> listData = FXCollections.observableArrayList(Arrays.asList(courseCodeList));
//        CourseCode_drop.setItems(listData);
//    }

    public void switchForgotPass() {
        fp_questionForm.setVisible(true);
        si_loginForm.setVisible(false);
        forgotPassQuestionList();
    }

    public void proceedBtn() {
        if (fp_username.getText().isEmpty() || fp_question.getSelectionModel().getSelectedItem() == null
                || fp_answer.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String selectData = "SELECT username, question, answer FROM users WHERE username = ? AND question = ? AND answer = ?";
            connect = Database.connectDB();
            try {

                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, fp_username.getText());
                prepare.setString(2, (String) fp_question.getSelectionModel().getSelectedItem());
                prepare.setString(3, fp_answer.getText());

                result = prepare.executeQuery();

                if (result.next()) {
                    np_newPassForm.setVisible(true);
                    fp_questionForm.setVisible(false);
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Information");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void changePassBtn() {
        if (np_newPassword.getText().isEmpty() || np_confirmPassword.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            if (np_newPassword.getText().equals(np_confirmPassword.getText())) {
                String getDate = "SELECT date FROM users WHERE username = '"
                        + fp_username.getText() + "'";

                connect = Database.connectDB();

                try {
                    prepare = connect.prepareStatement(getDate);
                    result = prepare.executeQuery();
                    String date = "";
                    if (result.next()) {
                        date = result.getString("date");
                    }
                    String updatePass = "UPDATE users SET password = '"
                            + np_newPassword.getText() + "', question = '"
                            + fp_question.getSelectionModel().getSelectedItem() + "', answer = '"
                            + fp_answer.getText() + "', date = '"
                            + date + "' WHERE username = '"
                            + fp_username.getText() + "'";

                    prepare = connect.prepareStatement(updatePass);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully changed Password!");
                    alert.showAndWait();

                    si_loginForm.setVisible(true);
                    np_newPassForm.setVisible(false);

                    np_confirmPassword.setText("");
                    np_newPassword.setText("");
                    fp_question.getSelectionModel().clearSelection();
                    fp_answer.setText("");
                    fp_username.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Sorry! not match.");
                alert.showAndWait();
            }
        }
    }

    public void forgotPassQuestionList() {
        List<String> listQ = new ArrayList<>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listQ);
        fp_question.setItems(listData);
    }

    public void backToLoginForm() {
        si_loginForm.setVisible(true);
        fp_questionForm.setVisible(false);
    }

    public void backToQuestionForm() {
        fp_questionForm.setVisible(true);
        np_newPassForm.setVisible(false);
    }

    public void switchForm(ActionEvent event) {
        TranslateTransition slider = new TranslateTransition();
        slider.setNode(side_form);
        slider.setDuration(Duration.seconds(0.7));

        if (event.getSource() == side_CreateBtn) {
            slider.setToX(400);
            slider.setOnFinished(e -> {
                side_alreadyHave.setVisible(true);
                side_CreateBtn.setVisible(false);

                create_accountText.setVisible(false);
                AlreadyHave_text.setVisible(true);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);

                loadQuestionList();
            });
        } else if (event.getSource() == side_alreadyHave) {
            slider.setToX(0);
            slider.setOnFinished(e -> {
                side_alreadyHave.setVisible(false);
                side_CreateBtn.setVisible(true);
                create_accountText.setVisible(true);
                AlreadyHave_text.setVisible(false);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
            });
        }

        slider.play();
    }

    private void slideToLoginForm() {
        TranslateTransition slider = new TranslateTransition();
        slider.setNode(side_form);
        slider.setToX(0);
        slider.setDuration(Duration.seconds(0.7));
        slider.setOnFinished(e -> {
            side_alreadyHave.setVisible(false);
            side_CreateBtn.setVisible(true);
            create_accountText.setVisible(true);
            AlreadyHave_text.setVisible(false);
        });
        slider.play();
    }

    private void showAlert(Alert.AlertType type, String message) {
        alert = new Alert(type);
        alert.setTitle(type == AlertType.ERROR ? "Error Message" : "Information Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize logic here if needed
    }
}
