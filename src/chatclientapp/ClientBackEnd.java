/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class ClientBackEnd implements Runnable{
    
    private Socket clientSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private FXMLDocumentController controller;
    
    /**
     * 
     * @param controller 
     */
    public ClientBackEnd(FXMLDocumentController controller) {
        try {
            clientSocket = new Socket("localhost",3010);// localhost = 127.0.0.1 / Yrittää välittömästi ottaa yhteyden kyseiseen osoitteeseen
            this.controller = controller;
        } catch (IOException ex) {                      // Jos ei onnistu, tapahtuu poikkeus
            ex.printStackTrace();
        }
        
    }
    
    @Override
    public void run() {        //run-funktiota kutsutaan VAIN KERRAN! Säiettä ei voi käynnistää uudelleen!
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());    // Näiden järjestyksellä ON MERKITYS:
            input = new ObjectInputStream(clientSocket.getInputStream());       // ensin output, sitten input!
            } 
        catch (IOException ex) {
            ex.printStackTrace();
        }
        
        while(true){
            try {
                final ChatMessage m = (ChatMessage)input.readObject();    // Tämä toissijainen säie jää odottamaan, että jotain tapahtuu (tulee dataa)

                // Laitetaan seuraava EDT (Event Dispatcher Thread) jonoon ja ajetaan se kun on aikaa
                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        controller.updateTextArea(m.getUserName() + ": " + m.getChatMessage() + "\n", m.getMessageColor());
                    }
                } );
            }    
            catch (IOException | ClassNotFoundException ex) 
            {
                ex.printStackTrace();
            } 
        }
        
    }
    
    public void sendMessage(ChatMessage cm){
        
        try {
            output.writeObject(cm);
            output.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    
}
