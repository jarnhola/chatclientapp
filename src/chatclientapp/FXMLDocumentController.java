/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientapp;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    TextField chatMessage;
    @FXML
    TextArea chatMessageArea;
    
    ClientBackEnd backEnd;
    Thread backThread;
    
    private String nimi;
        
    @FXML
    public void sendChatMessage (ActionEvent ae) {
        ChatMessage cm = new ChatMessage();
        String name = cm.getUserName();
        
        // Tsekataan onko nimeä vielä annettu.
        if(nimi==null) {
            handleUserName(cm);
        }
        else{
            cm.setUserName(nimi);
            cm.setChatMessage(chatMessage.getText());
            backEnd.sendMessage(cm);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        chatMessage.requestFocus();         // Asetetaan focus chatMessage -fieldiin
                        ChatMessage cm = new ChatMessage();
                        String name = cm.getUserName();
                        // Jos ei ole vielä annettu nimeä, niin pyydetään sitä
                        if(name==null){
                            updateTextArea("Please give the user name");
                        }
                    }
                } );
        backEnd = new ClientBackEnd(this);
        backThread = new Thread(backEnd);       // Luodaan säie
        backThread.setDaemon(true);             // 
        backThread.start();                     // Tämä kutsuu minun tekemää omaa run-funktiota!
    }    
    
    public void updateTextArea(String message){
        chatMessageArea.appendText(message + "\n");
    }
    
    public void handleUserName(ChatMessage cm){
        nimi = chatMessage.getText();
        cm.setUserName(nimi);
        chatMessage.clear();
        updateTextArea("Welcome to the chat, " + nimi + "!");
    }
}
