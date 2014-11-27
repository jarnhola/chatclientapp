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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        //String name = cm.getUserName();
        
        if(nimi==null) {
            handleUserName(cm); // Jos ei vielä nimeä, ajaetaan sille funktio
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
                        if(name==null){
                            updateTextArea("***\nPlease give the user name"); // Jos ei ole vielä annettu nimeä, niin pyydetään sitä
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
        chatMessage.clear();
    }
    
    /**
     * 
     * @param cm 
     */
    public void handleUserName(ChatMessage cm){
        nimi = chatMessage.getText();
        cm.setUserName(nimi);
        chatMessage.clear();
        updateTextArea("Welcome to the chat, " + nimi + "!\n***");
    }
    
    /*public void keyHandler() {
        // handler for enter key press / release events, other keys are
        // handled by the parent (keyboard) node handler
        final EventHandler<KeyEvent> keyEventHandler =
                new EventHandler<KeyEvent>() {
                    public void handle(final KeyEvent keyEvent) {
                        if (keyEvent.getCode() == KeyCode.ENTER) {

                        }
                    }
                };
    }*/
    
    public void handleClose(){
        Platform.exit();
    }
    public void handleFontSize12(){
        
    }
    public void handleFontSize16(){
        
    }
    public void handleBlackFontColor(){
        
    }
    public void handleRedFontColor(){
        
    }
}
