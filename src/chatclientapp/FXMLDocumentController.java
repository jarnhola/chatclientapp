/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientapp;

//import java.awt.Font;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    TextField chatMessage;
    @FXML
    TextFlow chatMessageArea;
    @FXML
    ListView userListView;
    @FXML
    RadioMenuItem fontSize12;
    @FXML
    RadioMenuItem fontSize16;
    @FXML
    RadioMenuItem fontColorBlack;
    @FXML
    RadioMenuItem fontColorBlue;
    @FXML
    RadioMenuItem fontColorRed;
    @FXML
    RadioMenuItem fontColorGreen;
    
    ClientBackEnd backEnd;
    Thread backThread;
    
    private String nimi;
    //public Font font = new Font("Verdana", Font.BOLD, 18);
    private final ToggleGroup group = new ToggleGroup();
    
    @FXML
    public void handleSendButton (ActionEvent ae) {
        sendChatMessage();
    }
    
    @FXML
    public void handleEnterKey (KeyEvent k){
        if (k.getCode() == KeyCode.ENTER)  {
            sendChatMessage();
        }
    }
    
    @FXML
    public void handleClose(){
        Platform.exit();
    }

    public void sendChatMessage (){
        ChatMessage cm = new ChatMessage();
        cm.getMessageColor();
        String color = handleFontColor();//"#000000";//Color.BLACK.toString();
        if(nimi==null) {
            handleUserName(cm); // Jos ei vielä nimeä, ajaetaan sille funktio
        }
        else {
            cm.setUserName(nimi);
            cm.setChatMessage(chatMessage.getText());
            cm.setMessageColor(color);
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
                            updateTextArea("*** Please give the user name ***\n", "#ff0000"); // Jos ei ole vielä annettu nimeä, niin pyydetään sitä
                        }
                    }
                } );
        
        backEnd = new ClientBackEnd(this);
        backThread = new Thread(backEnd);       // Luodaan säie
        backThread.setDaemon(true);             // 
        backThread.start();                     // Tämä kutsuu minun tekemää omaa run-funktiota!
    }    
    
    public void updateTextArea(String message, String color){
        Text msg = new Text(message);
        msg.setFill(Color.web(color));
        //msg.setFont(font);
        chatMessageArea.getChildren().add(msg);
        chatMessage.clear();
        chatMessage.requestFocus();
    }
    
    /**
     * 
     * @param cm 
     */
    public void handleUserName(ChatMessage cm){
        nimi = chatMessage.getText();
        cm.setUserName(nimi);
        chatMessage.clear();
        chatMessage.requestFocus();
        updateTextArea("*** Welcome to the chat, " + nimi + " ***\n", "#008000");
    }
    
    public String handleFontColor(){
        ToggleColorMenu();
        String color = "";
        if(fontColorBlack.isSelected()) color = "#000000";
        else if(fontColorBlue.isSelected()) color = "#0000ff";
        else if(fontColorRed.isSelected()) color = "#ff0000";
        else if(fontColorGreen.isSelected()) color = "#00ff00";
        return color;
    }
    
    private void ToggleColorMenu(){
        fontColorBlack.setToggleGroup(group);
        fontColorBlue.setToggleGroup(group);
        fontColorRed.setToggleGroup(group);
        fontColorGreen.setToggleGroup(group);
    }
        
}
