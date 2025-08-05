package ma.enst.iibdcc.chatClient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

public class ChatClientController {

    @FXML private VBox messageContainer;
    @FXML private ScrollPane scrollPane;
    @FXML private TextField inputField;

    private PrintWriter out;
    private String username;

    @FXML
    public void initialize() {
        // Demander le nom de l'utilisateur
        TextInputDialog dialog = new TextInputDialog("Utilisateur");
        dialog.setTitle("Connexion");
        dialog.setHeaderText("Entrez votre nom d'utilisateur :");
        Optional<String> result = dialog.showAndWait();
        username = result.orElse("Anonyme");

        // Thread de réception des messages
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 9090);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Annonce de présence
                out.println(username + " a rejoint le chat.");

                String line;
                while ((line = in.readLine()) != null) {
                    String finalLine = line;
                    Platform.runLater(() -> displayMessage(finalLine));
                }

            } catch (IOException e) {
                Platform.runLater(() -> showError("Erreur de connexion au serveur."));
            }
        }).start();
    }

    @FXML
    public void handleSend() {
        String msg = inputField.getText();
        if (!msg.isEmpty() && out != null) {
            out.println(username + ": " + msg);
            inputField.clear();
        }
    }

    private void displayMessage(String message) {
        HBox messageBox = new HBox();
        messageBox.setPadding(new Insets(5, 10, 5, 10));
        messageBox.setSpacing(10);

        Label avatar = new Label();
        avatar.setMinSize(32, 32);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color: #bbbbbb; -fx-text-fill: white; -fx-background-radius: 50%; -fx-font-weight: bold;");
        avatar.setText(message.split(":")[0].substring(0, 1).toUpperCase());

        Label content = new Label(message);
        content.setWrapText(true);
        content.setMaxWidth(400);
        content.setStyle("-fx-padding: 10; -fx-background-radius: 10;");

        // Message de l'utilisateur => à droite
        if (message.startsWith(username + ":")) {
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            content.setStyle(content.getStyle() +
                    "-fx-background-color: #dcf8c6;"); // vert clair
            messageBox.getChildren().addAll(content, avatar);
        } else {
            messageBox.setAlignment(Pos.CENTER_LEFT);
            content.setStyle(content.getStyle() +
                    "-fx-background-color: #ffffff;"); // blanc
            messageBox.getChildren().addAll(avatar, content);
        }

        messageContainer.getChildren().add(messageBox);

        // Scroll auto vers le bas
        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(msg);
        alert.show();
    }
}
