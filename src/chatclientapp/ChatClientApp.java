/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class ChatClientApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        //ClientBackEnd backEnd = new ClientBackEnd();
        //Thread backThread = new Thread(backEnd);// Luodaan säie
        //backThread.setDaemon(true);             // 
        //backThread.start();                         // Tämä kutsuu minun tekemää omaa run-funktiota!
        //ChatMessage chatM = new ChatMessage();
        //chatM.setChatMessage("Hello there!");
        //backEnd.sendMessage(chatM);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
