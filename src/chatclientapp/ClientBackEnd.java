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
import java.util.logging.Level;
import java.util.logging.Logger;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class ClientBackEnd implements Runnable{
    
    private Socket clientSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ClientBackEnd() {
        try {
            clientSocket = new Socket("localhost",3010); //localhost = 127.0.0.1
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    @Override
    public void run() {        //run-funktiota kutsutaan VAIN KERRAN! Säiettä ei voi käynnistää uudelleen!
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());    // Näiden järjestyksellä ON MERKITYS:
            input = new ObjectInputStream(clientSocket.getInputStream());       // ensin output(kirjoitus), sitten input(luku) !
            } 
        catch (IOException ex) {
            ex.printStackTrace();
        }
        
        while(true){
            try {
                ChatMessage m = (ChatMessage)input.readObject();
                System.out.println(m.getChatMessage());             //Tulostetaan chatMessage
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
