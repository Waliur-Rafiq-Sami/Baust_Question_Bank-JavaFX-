package qbank_project;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class mainFormController implements Initializable {

    @FXML
    private Button About_btn;

    @FXML
    private AnchorPane customers_form;

    @FXML
    private Button HomePage_btn;

    @FXML
    private AnchorPane HomePage;


    @FXML
    private Button AddQuestion_btn;


    @FXML
    private TableColumn<QuestionData, String> Add_question_Date;

    @FXML
    private TableColumn<QuestionData, String> Add_question_Code;

    @FXML
    private TableColumn<QuestionData, String> Add_question_depertment;

    @FXML
    private TableColumn<QuestionData, String> Add_question_CourseTittle;

    @FXML
    private TableColumn<QuestionData, String> Add_question_Role;

    @FXML
    private TableColumn<QuestionData, String> Add_question_Batch;

    @FXML
    private TableColumn<QuestionData, String> Add_question_Type;

    @FXML
    private AnchorPane AddQuestionForm;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button input_Image;

    @FXML
    private TextField input_Code;

    @FXML
    private TextField inpit_Depertment;

    @FXML
    private TextField input_CourseTittle;

    @FXML
    private ComboBox<?> Question_status;

    @FXML
    private TextField input_Batch;

    @FXML
    private TableView<QuestionData> QuestionTable;

    @FXML
    private ComboBox<String> input_QuestionType;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button viewAllQuestion_btn;

    @FXML
    private TableColumn<QuestionData, String> ViewPageBatch;

    @FXML
    private TableColumn<QuestionData, String> ViewPageTittle;

    @FXML
    private TableColumn<QuestionData, String> ViewPageType;

    @FXML
    private AnchorPane ViewFrom;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private ScrollPane ViewQuestion_scrollPane;

    @FXML
    private TableView<QuestionData> ViewPageTablle;

    @FXML
    private Label username;

    private Alert alert;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;

    private ObservableList<QuestionData> cardListData = FXCollections.observableArrayList();

    public void viewRemoveBtn() {

        if (getid == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select the order you want to remove");
            alert.showAndWait();
        } else {
            String deleteData = "DELETE FROM customer WHERE id = " + getid;
            connect = Database.connectDB();
            try {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this order?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                }

                viewPageShowData();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void AddQuestionBtn() {
        // Check if any field is empty
        if (inpit_Depertment.getText().isEmpty()
                || input_CourseTittle.getText().isEmpty()
                || input_QuestionType.getSelectionModel().getSelectedItem() == null
                || input_Batch.getText().isEmpty()
                || input_Code.getText().isEmpty()
                || Question_status.getSelectionModel().getSelectedItem() == null
                || data.path == null) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            return;
        }

        // Validate numeric fields
        int batch;
        double code;
        try {
            batch = Integer.parseInt(input_Batch.getText());
        } catch (NumberFormatException e) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Batch");
            alert.setHeaderText(null);
            alert.setContentText("Batch must be a whole number.");
            alert.showAndWait();
            return;
        }

        try {
            code = Double.parseDouble(input_Code.getText());
        } catch (NumberFormatException e) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Code");
            alert.setHeaderText(null);
            alert.setContentText("Code must be a number.");
            alert.showAndWait();
            return;
        }

        // Database insert
        String insertData = "INSERT INTO product (prod_id, prod_name, type, stock, price, status, image, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        connect = Database.connectDB();
        try {
            prepare = connect.prepareStatement(insertData);
            prepare.setString(1, inpit_Depertment.getText());
            prepare.setString(2, input_CourseTittle.getText());
            prepare.setString(3, input_QuestionType.getSelectionModel().getSelectedItem().toString());
            prepare.setInt(4, batch); // stock
            prepare.setDouble(5, code); // price
            prepare.setString(6, Question_status.getSelectionModel().getSelectedItem().toString());

            String path = data.path.replace("\\", "\\\\"); // Escape slashes for DB
            prepare.setString(7, path);

            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            prepare.setString(8, sqlDate.toString());

            prepare.executeUpdate();

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Question successfully added!");
            alert.showAndWait();

            ShowQuestions();      // Refresh table
            clearQuestionBtn();   // Clear input fields

        } catch (Exception e) {
            e.printStackTrace();
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to insert question.");
            alert.showAndWait();
        }
    }

    public void UpdateQuestionBtn() {

        if (inpit_Depertment.getText().isEmpty()
                || input_CourseTittle.getText().isEmpty()
                || input_QuestionType.getSelectionModel().getSelectedItem() == null
                || input_Batch.getText().isEmpty()
                || input_Code.getText().isEmpty()
                || Question_status.getSelectionModel().getSelectedItem() == null
                || data.path == null || data.id == 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else {

            String path = data.path;
            path = path.replace("\\", "\\\\");

            String updateData = "UPDATE product SET "
                    + "prod_id = '" + inpit_Depertment.getText() + "', prod_name = '"
                    + input_CourseTittle.getText() + "', type = '"
                    + input_QuestionType.getSelectionModel().getSelectedItem() + "', stock = '"
                    + input_Batch.getText() + "', price = '"
                    + input_Code.getText() + "', status = '"
                    + Question_status.getSelectionModel().getSelectedItem() + "', image = '"
                    + path + "', date = '"
                    + data.date + "' WHERE id = " + data.id;

            connect = Database.connectDB();

            try {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE PRoduct ID: " + inpit_Depertment.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    ShowQuestions();
                    clearQuestionBtn();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void DeleteQuestionBtn() {
        if (data.id == 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE Product ID: " + inpit_Depertment.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM product WHERE id = " + data.id;
                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("successfully Deleted!");
                    alert.showAndWait();

                    ShowQuestions();
                    clearQuestionBtn();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }
        }
    }

    public void clearQuestionBtn() {

        inpit_Depertment.setText("");
        input_CourseTittle.setText("");
        input_QuestionType.getSelectionModel().clearSelection();
        input_Batch.setText("");
        input_Code.setText("");
        Question_status.getSelectionModel().clearSelection();
        data.path = "";
        data.id = 0;
        inventory_imageView.setImage(null);

    }

    public void QuestionimputBtn() {

        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg", "*pdf"));

        File file = openFile.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {

            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 120, 127, false, true);

            inventory_imageView.setImage(image);
        }
    }

    public ObservableList<QuestionData> inventoryDataList() {

        ObservableList<QuestionData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM product";

        connect = Database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            QuestionData prodData;

            while (result.next()) {

                prodData = new QuestionData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(prodData);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<QuestionData> inventoryListData;

    public void ShowQuestions() {
        inventoryListData = inventoryDataList();

        Add_question_depertment.setCellValueFactory(new PropertyValueFactory<>("questionId"));
        Add_question_CourseTittle.setCellValueFactory(new PropertyValueFactory<>("questionTitle"));
        Add_question_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        Add_question_Batch.setCellValueFactory(new PropertyValueFactory<>("batch"));

        Add_question_Code.setCellValueFactory(cellData -> {
            double price = cellData.getValue().getCode();
            int intPrice = (int) price;
            return new javafx.beans.property.SimpleStringProperty(String.valueOf(intPrice));
        });
        Add_question_Role.setCellValueFactory(new PropertyValueFactory<>("status"));
        Add_question_Date.setCellValueFactory(new PropertyValueFactory<>("date"));

        QuestionTable.setItems(inventoryListData);

    }

    // this name can't change
    public void inventorySelectData() {

        QuestionData prodData = QuestionTable.getSelectionModel().getSelectedItem();
        int num = QuestionTable.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        inpit_Depertment.setText(prodData.getQuestionId());
        input_CourseTittle.setText(prodData.getQuestionTitle());
        input_Batch.setText(String.valueOf(prodData.getBatch()));
        input_Code.setText(String.valueOf(prodData.getCode()));

        data.path = prodData.getImage();

        String path = "File:" + prodData.getImage();
        data.date = String.valueOf(prodData.getDate());
        data.id = prodData.getId();

        image = new Image(path, 120, 127, false, true);
        inventory_imageView.setImage(image);
    }

    private final String[] questionList = {
        "What is your Birth Date?",
        "What is your favorite food?",
        "What is your favorite color?",
        "What city were you born in?",
        "What is your pet’s name?"
    };

    private final String[] typeList = {
        "CT-1(A)",
        "CT-1(B)",
        "CT-1(C)",
        "CT-2(A)",
        "CT-2(B)",
        "CT-2(C)",
        "CT-3(A)",
        "CT-3(B)",
        "CT-3(C)",
        "LAB-CT-1",
        "LAB-CT-2",
        "LAB-CT-3",
        "Mid-Term",
        "LAB Mid",
        "Final",
        "LAB Final"
    };

    private final String[] statusList = {
        "Class Representative CR",
        "Student",
        "Teacher",};

    public void QuestionTypeList() {

        List<String> typeL = new ArrayList<>();
        for (String data : typeList) {
            typeL.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(typeL);
        input_QuestionType.setItems(listData);
    }

    public void QuestionStatus() {

        List<String> statusL = new ArrayList<>();
        for (String data : statusList) {
            statusL.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(statusL);
        Question_status.setItems(listData);

    }

    public ObservableList<QuestionData> QuestionGet() {

        String sql = "SELECT * FROM product";

        ObservableList<QuestionData> listData = FXCollections.observableArrayList();
        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            QuestionData Ques;

            while (result.next()) {
                Ques = new QuestionData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(Ques);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    public void QuestionDisplay() {

        cardListData.clear();
        cardListData.addAll(QuestionGet());

        int row = 0;
        int column = 0;

        menu_gridPane.getChildren().clear();
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();

        for (int q = 0; q < cardListData.size(); q++) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("QuestionBox.fxml"));
                AnchorPane pane = load.load();
                cardProductController cardC = load.getController();
                cardC.setData(cardListData.get(q));

                if (column == 3) {
                    column = 0;
                    row += 1;
                }

                menu_gridPane.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(10));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<QuestionData> ViewpageQuestion() {
        customerID();
        ObservableList<QuestionData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM customer WHERE customer_id = " + cID;

        connect = Database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            QuestionData prod;

            while (result.next()) {
                prod = new QuestionData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"));
                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private ObservableList<QuestionData> ViewQuestions;

    public void viewPageShowData() {
        ViewQuestions = ViewpageQuestion();
        ViewPageTittle.setCellValueFactory(new PropertyValueFactory<>("questionTitle"));
        ViewPageType.setCellValueFactory(new PropertyValueFactory<>("type"));
        ViewPageBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));

        ViewPageTablle.setItems(ViewQuestions);
    }
    private int getid;

    public void menuSelectOrder() {
        QuestionData prod = (QuestionData) ViewPageTablle.getSelectionModel().getSelectedItem();
        int num = ViewPageTablle.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        getid = prod.getId();

    }

    private double totalP;

    public void menuGetTotal() {
        customerID();
        String total = "SELECT SUM(price) FROM customer WHERE customer_id = " + cID;

        connect = Database.connectDB();

        try {

            prepare = connect.prepareStatement(total);
            result = prepare.executeQuery();

            if (result.next()) {
                totalP = result.getDouble("SUM(price)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private double amount;
    private double change;

    private int cID;

    public void customerID() {

        String sql = "SELECT MAX(customer_id) FROM customer";
        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                cID = result.getInt("MAX(customer_id)");
            }

            String checkCID = "SELECT MAX(customer_id) FROM receipt";
            prepare = connect.prepareStatement(checkCID);
            result = prepare.executeQuery();
            int checkID = 0;
            if (result.next()) {
                checkID = result.getInt("MAX(customer_id)");
            }

            if (cID == 0) {
                cID += 1;
            } else if (cID == checkID) {
                cID += 1;
            }

            data.cID = cID;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<UsersData> customersDataList() {

        ObservableList<UsersData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM receipt";

        connect = Database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            UsersData cData;

            while (result.next()) {
                cData = new UsersData(result.getInt("id"),
                        result.getInt("customer_id"),
                        result.getDouble("total"),
                        result.getDate("date"),
                        result.getString("em_username"));

                listData.add(cData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == HomePage_btn) {
            HomePage.setVisible(true);
            AddQuestionForm.setVisible(false);
            ViewFrom.setVisible(false);
            customers_form.setVisible(false);

        } else if (event.getSource() == AddQuestion_btn) {
            HomePage.setVisible(false);
            AddQuestionForm.setVisible(true);
            ViewFrom.setVisible(false);
            customers_form.setVisible(false);

            QuestionTypeList();
            QuestionStatus();
            ShowQuestions();
        } else if (event.getSource() == viewAllQuestion_btn) {
            HomePage.setVisible(false);
            AddQuestionForm.setVisible(false);
            ViewFrom.setVisible(true);
            customers_form.setVisible(false);

            QuestionDisplay();
            viewPageShowData();
        } else if (event.getSource() == About_btn) {
            HomePage.setVisible(false);
            AddQuestionForm.setVisible(false);
            ViewFrom.setVisible(false);
            customers_form.setVisible(true);

        }

    }


    public void logout() {

        try {

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                logout_btn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("Baust Question Bank");

                stage.setScene(scene);
                stage.show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayUsername() {

        String user = data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);

        username.setText(user);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        displayUsername();

        QuestionTypeList();
        QuestionStatus();
        ShowQuestions();

        QuestionDisplay();
        ViewpageQuestion();
        viewPageShowData();
    }

}
