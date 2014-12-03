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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    RadioMenuItem fontSize14;
    @FXML
    RadioMenuItem fontSize16;
    @FXML
    RadioMenuItem fontSize18;
    @FXML
    RadioMenuItem fontSize20;
    @FXML
    RadioMenuItem fontColorBlack;
    @FXML
    RadioMenuItem fontColorBlue;
    @FXML
    RadioMenuItem fontColorRed;
    @FXML
    RadioMenuItem fontColorGreen;
    @FXML
    ColorPicker fontColor;
    @FXML
    ChoiceBox fontSize;
    @FXML
    Menu editMenu;
    
    ClientBackEnd backEnd;
    Thread backThread;
    
    private String nimi;
    //public Font font = new Font("Verdana", Font.BOLD, 18);
    private final ToggleGroup colorGroup = new ToggleGroup();
    private final ToggleGroup sizeGroup = new ToggleGroup();
    
    @FXML
    public void handleEnterKey (KeyEvent k){
        if (k.getCode() == KeyCode.ENTER)  {
            System.out.println("ENTER");
            sendChatMessage();
        }
    }
    
    @FXML
    public void handleClose(){
        Platform.exit();
    }

    @FXML
    public void handleSendButton (ActionEvent ae) {
        sendChatMessage();
    }
    
    public void sendChatMessage (){
        ChatMessage cm = new ChatMessage();
        //cm.getMessageColor();
        if(nimi==null) {
            handleUserName(cm); // Jos ei vielä nimeä, ajaetaan sille funktio
        }
        else {
            cm.setUserName(nimi);
            cm.setChatMessage(chatMessage.getText());
            cm.setMessageColor(handleFontColor());
            cm.setFontSize(handleFontSize());
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
                            updateTextArea("*** Please give the user name ***\n", "#ff0000", 12); // Jos ei ole vielä annettu nimeä, niin pyydetään sitä
                        }
                    }
                } );
        
        backEnd = new ClientBackEnd(this);
        backThread = new Thread(backEnd);       // Luodaan säie
        backThread.setDaemon(true);             // 
        backThread.start();                     // Tämä kutsuu minun tekemää omaa run-funktiota!
        ToggleFontMenu();
        ToggleColorMenu();
        fontColor.setValue(Color.BLACK);        // Set default color value
    }    
    
    public void updateTextArea(String message, String color, double size){
        Text msg = new Text(message);
        msg.setFill(Color.web(color));
        msg.setFont(Font.font(size));
        chatMessageArea.getChildren().add(msg);
        chatMessage.clear();
        chatMessage.requestFocus();
    }    

    public void handleUserName(ChatMessage cm){
        nimi = chatMessage.getText();
        cm.setUserName(nimi);
        chatMessage.clear();
        chatMessage.requestFocus();
        updateTextArea("*** Welcome to the chat, " + nimi + " ***\n", "#008000", 12);
        editMenu.setDisable(false); // Edit menu enbled
    }
    
    public String handleFontColor(){
        String color = "#000000";
//        if(fontColorBlack.isSelected()) color = "#000000";
//        else if(fontColorBlue.isSelected()) color = "#0000ff";
//        else if(fontColorRed.isSelected()) color = "#ff0000";
//        else if(fontColorGreen.isSelected()) color = "#00ff00";
        color = fontColor.getValue().toString();
        System.out.println(color);
        return color;
    }
    
    public double handleFontSize(){
        double size=12;
        if(fontSize12.isSelected()) size = 12;
        else if(fontSize14.isSelected()) size = 14;
        else if(fontSize16.isSelected()) size = 16;
        else if(fontSize18.isSelected()) size = 18;
        else if(fontSize20.isSelected()) size = 20;
        return size;
    }
    
    private void ToggleColorMenu(){
        fontColorBlack.setToggleGroup(colorGroup);
        fontColorBlue.setToggleGroup(colorGroup);
        fontColorRed.setToggleGroup(colorGroup);
        fontColorGreen.setToggleGroup(colorGroup);
    }

    private void ToggleFontMenu(){
        fontSize12.setToggleGroup(sizeGroup);
        fontSize14.setToggleGroup(sizeGroup);
        fontSize16.setToggleGroup(sizeGroup);
        fontSize18.setToggleGroup(sizeGroup);
        fontSize20.setToggleGroup(sizeGroup);
    }
}
