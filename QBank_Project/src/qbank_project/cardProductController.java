/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qbank_project;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author WINDOWS 10
 */
public class cardProductController implements Initializable {

    @FXML
    private Label dep;
    @FXML
    private Label tpye_;
    @FXML
    private Label code_;
    @FXML
    private Label batch_;


    @FXML
    private ImageView Question;


    private QuestionData QuestionData;
    private Image image;

    private String QuestionID;
    private String type;
    private String Question_date;
    private String Question_img;

    private SpinnerValueFactory<Integer> spin;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    public void setData(QuestionData QuestionData) {
        this.QuestionData = QuestionData;

        Question_img = QuestionData.getImage();
        Question_date = String.valueOf(QuestionData.getDate());
        type = QuestionData.getType();
        QuestionID = QuestionData.getQuestionId();

        dep.setText(QuestionData.getQuestionId());
        tpye_.setText(QuestionData.getType());
        int priceAsInt = (int) QuestionData.getCode().doubleValue();
        code_.setText(String.valueOf(priceAsInt));
        batch_.setText(String.valueOf(QuestionData.getQuantity()));

        String path = "File:" + QuestionData.getImage();
        image = new Image(path, 190, 94, false, true);
        Question.setImage(image);
        pr = QuestionData.getCode();

    }
    private int qty;
    private double totalP;
    private double pr;

    public void viewBtn() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Question show");

            File file = new File(Question_img);
            Image image = new Image(file.toURI().toString());

            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);

            // Bind image size to window size
            imageView.fitWidthProperty().bind(stage.widthProperty());
            imageView.fitHeightProperty().bind(stage.heightProperty());

            StackPane root = new StackPane(imageView);
            Scene scene = new Scene(root, 800, 600); // initial size
            stage.setScene(scene);

            // Make it full screen or maximized
            stage.setMaximized(true);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to display image.");
            alert.showAndWait();
        }
    }


    public void downloadBtn() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            // Suggest a default file name
            fileChooser.setInitialFileName(QuestionID + "_image");

            // Let user pick a location
            File destFile = fileChooser.showSaveDialog(null);
            if (destFile != null) {
                // Source image file
                File sourceFile = new File(Question_img);

                // Copy the file
                java.nio.file.Files.copy(
                        sourceFile.toPath(),
                        destFile.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Download Successful");
                alert.setHeaderText(null);
                alert.setContentText("Image downloaded to: " + destFile.getAbsolutePath());
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Download Failed");
            alert.setHeaderText(null);
            alert.setContentText("Could not download the image.");
            alert.showAndWait();
        }
    }


    public void addBtn() {

        mainFormController mForm = new mainFormController();
        mForm.customerID();

        qty = 0;
        String check = "";
        String checkAvailable = "SELECT status FROM product WHERE prod_id = '"
                + QuestionID + "'";

        connect = Database.connectDB();

        try {
            int checkStck = 0;
            String checkStock = "SELECT stock FROM product WHERE prod_id = '"
                    + QuestionID + "'";

            prepare = connect.prepareStatement(checkStock);
            result = prepare.executeQuery();

            if (result.next()) {
                checkStck = result.getInt("stock");
            }

            if (checkStck == 0) {

                String updateStock = "UPDATE product SET prod_name = '"
                        + dep.getText() + "', type = '"
                        + type + "', stock = 0, price = " + pr
                        + ", status = 'Unavailable', image = '"
                        + Question_img + "', date = '"
                        + Question_date + "' WHERE prod_id = '"
                        + QuestionID + "'";
                prepare = connect.prepareStatement(updateStock);
                prepare.executeUpdate();

            }

            prepare = connect.prepareStatement(checkAvailable);
            result = prepare.executeQuery();

            if (result.next()) {
                check = result.getString("status");
            }

                    Question_img = Question_img.replace("\\", "\\\\");

                    String insertData = "INSERT INTO customer "
                            + "(customer_id, prod_id, prod_name, type, quantity, price, date, image, em_username) "
                            + "VALUES(?,?,?,?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, String.valueOf(data.cID));
            prepare.setString(2, QuestionID);
            prepare.setString(3, dep.getText());
                    prepare.setString(4, type);
                    prepare.setString(5, String.valueOf(qty));

                    totalP = (qty * pr);
                    prepare.setString(6, String.valueOf(totalP));

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(7, String.valueOf(sqlDate));

            prepare.setString(8, Question_img);
                    prepare.setString(9, data.username);

                    prepare.executeUpdate();

                    int upStock = checkStck - qty;

            System.out.println("Date: " + Question_date);
            System.out.println("Image: " + Question_img);

                    String updateStock = "UPDATE product SET prod_name = '"
                            + dep.getText() + "', type = '"
                            + type + "', stock = " + upStock + ", price = " + pr
                            + ", status = '"
                            + check + "', image = '"
                            + Question_img + "', date = '"
                            + Question_date + "' WHERE prod_id = '"
                            + QuestionID + "'";

                    prepare = connect.prepareStatement(updateStock);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    mForm.menuGetTotal();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        setQuantity();

    }

}
