package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import logic.HUnzipping;
import logic.HZipping;
import logic.LUnzipping;
import logic.LZipping;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class MainPageController implements Initializable {
    private File openedFile;
    private File anotherFile;
    private final Semaphore semaphore = new Semaphore(1);

    @FXML
    private Label fileNameLBL;

    @FXML
    private Label firstSizeLBL;

    @FXML
    private ImageView loadingIMG;

    @FXML
    private Label secondSizeLBL;

    @FXML
    private Button zip1BTN;

    @FXML
    private Button zip2BTN;

    @FXML
    private Button unzip1BTN;

    @FXML
    private Button unzip2BTN;

    @FXML
    private Button openBTN;


    @FXML
    void openFile(ActionEvent event) {
        zip1BTN.setDisable(true);
        zip2BTN.setDisable(true);
        unzip1BTN.setDisable(true);
        unzip2BTN.setDisable(true);
        openBTN.setDisable(true);
        FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter1 =
                    new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter1);
            FileChooser.ExtensionFilter extFilter2 =
                    new FileChooser.ExtensionFilter("Huffman zipped files (*.huffz)", "*.huffz");
            fileChooser.getExtensionFilters().add(extFilter2);
            FileChooser.ExtensionFilter extFilter3 =
                    new FileChooser.ExtensionFilter("Lun zipped files (*.LmZWp)", "*.LmZWp");
            fileChooser.getExtensionFilters().add(extFilter3);
            openedFile = fileChooser.showOpenDialog(null);
            loadingIMG.setVisible(true);
            if (openedFile != null) {
                fileNameLBL.setText(openedFile.getName());
                firstSizeLBL.setText(openedFile.length() + " bytes");
            }
            loadingIMG.setVisible(false);
            zip1BTN.setDisable(false);
            zip2BTN.setDisable(false);
            unzip1BTN.setDisable(false);
            unzip2BTN.setDisable(false);
            openBTN.setDisable(false);






    }

    @FXML
    void unzip1(ActionEvent event) {
        zip1BTN.setDisable(true);
        zip2BTN.setDisable(true);
        unzip1BTN.setDisable(true);
        unzip2BTN.setDisable(true);
        openBTN.setDisable(true);
        Thread thread = new Thread(() -> {
            if (openedFile == null){
                Platform.runLater(() ->{
                    Alert alert = new Alert(Alert.AlertType.ERROR , "Please select a file to unzip!");
                    alert.setTitle("No selected file");
                    alert.showAndWait();
                });

            }
            else if (!openedFile.getName().endsWith(".huffz")){
                Platform.runLater(() ->{
                    Alert alert = new Alert(Alert.AlertType.ERROR , "Please select a huffz file to unzip!");
                    alert.setTitle("format error");
                    alert.showAndWait();
                });

            }
            else {
                loadingIMG.setVisible(true);
                HUnzipping.beginHUnzipping(openedFile.getPath());
                String s = openedFile.getPath();
                s = s.substring(0, s.length() - 6);
                anotherFile = new File(s);

                loadingIMG.setVisible(false);
                Platform.runLater(() ->{
                    secondSizeLBL.setText(anotherFile.length() + " Bytes");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Unzipping done!" , ButtonType.OK);
                    alert.setTitle("Done");
                    alert.showAndWait();
                });



            }
        });
        thread.start();

        zip1BTN.setDisable(false);
        zip2BTN.setDisable(false);
        unzip1BTN.setDisable(false);
        unzip2BTN.setDisable(false);
        openBTN.setDisable(false);




    }

    @FXML
    void unzip2(ActionEvent event) {
        zip1BTN.setDisable(true);
        zip2BTN.setDisable(true);
        unzip1BTN.setDisable(true);
        unzip2BTN.setDisable(true);
        openBTN.setDisable(true);



        Thread thread = new Thread(() -> {
            if (openedFile == null){
                Platform.runLater(() ->{
                    Alert alert = new Alert(Alert.AlertType.ERROR , "Please select a file to unzip!");
                    alert.setTitle("No selected file");
                    alert.show();
                });

            }
            else if (!openedFile.getName().endsWith(".LmZWp")){
                Platform.runLater(() ->{
                    Alert alert = new Alert(Alert.AlertType.ERROR , "Please select a LmZWp file to unzip!");
                    alert.setTitle("format error");
                    alert.show();
                });

            }
            else {
                loadingIMG.setVisible(true);
                LUnzipping.beginLunzipping(openedFile.getPath());
                String s = openedFile.getPath();
                s = s.substring(0, s.length() - 6);
                anotherFile = new File(s);

                loadingIMG.setVisible(false);
                Platform.runLater(() ->{
                    secondSizeLBL.setText(anotherFile.length() + " Bytes");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Unzipping done!" , ButtonType.OK);
                    alert.setTitle("Done");
                    alert.show();
                });



            }
            zip1BTN.setDisable(false);
            zip2BTN.setDisable(false);
            unzip1BTN.setDisable(false);
            unzip2BTN.setDisable(false);
            openBTN.setDisable(false);
        });
        thread.start();
    }

    @FXML
    void zip1(ActionEvent event) {
        zip1BTN.setDisable(true);
        zip2BTN.setDisable(true);
        unzip1BTN.setDisable(true);
        unzip2BTN.setDisable(true);
        openBTN.setDisable(true);
        Thread thread = new Thread(() -> {
            if (openedFile == null){
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR , "Please select a file to zip!");
                    alert.setTitle("No selected file");
                    alert.show();
                });

            }
            else if (!openedFile.getName().endsWith(".txt")){
                Platform.runLater(() ->{
                    Alert alert = new Alert(Alert.AlertType.ERROR , "Please select a txt file to zip!");
                    alert.setTitle("format error");
                    alert.show();
                });

            }
            else {
                loadingIMG.setVisible(true);
                HZipping.beginHZipping(openedFile.getPath());
                String s = openedFile.getPath() + ".huffz";
                anotherFile = new File(s);

                loadingIMG.setVisible(false);
                Platform.runLater(() ->{
                    secondSizeLBL.setText(anotherFile.length() + " Bytes");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Zipping done!" , ButtonType.OK);
                    alert.setTitle("Done");
                    alert.show();
                });




            }
            zip1BTN.setDisable(false);
            zip2BTN.setDisable(false);
            unzip1BTN.setDisable(false);
            unzip2BTN.setDisable(false);
            openBTN.setDisable(false);
        });
        thread.start();

    }

    @FXML
    void zip2(ActionEvent event) {
        zip1BTN.setDisable(true);
        zip2BTN.setDisable(true);
        unzip1BTN.setDisable(true);
        unzip2BTN.setDisable(true);
        openBTN.setDisable(true);
        Thread thread = new Thread(() -> {
            if (openedFile == null){
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR , "Please select a file to zip!");
                    alert.setTitle("No selected file");
                    alert.showAndWait();
                });

            }
            else if (!openedFile.getName().endsWith(".txt")){
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.ERROR , "Please select a txt file to zip!");
                    alert.setTitle("format error");
                    alert.showAndWait();
                });

            }
            else {
                loadingIMG.setVisible(true);
                LZipping.beginLzipping(openedFile.getPath());
                String s = openedFile.getPath() + ".LmZWp";
                anotherFile = new File(s);

                loadingIMG.setVisible(false);
                Platform.runLater(() ->{
                    secondSizeLBL.setText(anotherFile.length() + " Bytes");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Zipping done!" , ButtonType.OK);
                    alert.setTitle("Done");
                    alert.show();
                });




            }
            zip1BTN.setDisable(false);
            zip2BTN.setDisable(false);
            unzip1BTN.setDisable(false);
            unzip2BTN.setDisable(false);
            openBTN.setDisable(false);
        });
        thread.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
