
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI implements ActionListener{
        
        private JFrame chatWindow;
        private JTextArea chatField;
        private JButton connect;
        private JButton sendButton;
        private JTextField hostName;
        private JTextField nicknameField;
        private JTextField inputField;
        private String serverIP;
        private String nickname;
        private String actualMessage;
        private Client client;
        
        public GUI(){
                
                startWindow();
                client = new Client(this.chatField);
         }
         
         public void startWindow(){
                 
                 chatWindow = new JFrame("Chat");
                 chatWindow.setBounds(100, 100, 400, 500);
                 
                 chatWindow.setLayout(null);
                 
                 hostName = new JTextField("Server IP");
                 hostName.setBounds(100,50,200,30);
                 hostName.setVisible(true);
                 
                 
                 nicknameField = new JTextField("Nickname");
                 nicknameField.setBounds(100,150,200,30);
                 nicknameField.setVisible(true);
                 
                 
                 connect = new JButton("Connect");
                 connect.setBounds(125, 300, 150, 50);
                 connect.setVisible(true);                
                 connect.addActionListener(this);

                 
                 chatField = new JTextArea();
                 chatField.setBounds(20,30,355,340);
                 chatField.setVisible(false);
                 
                 inputField = new JTextField();
                 inputField.setBounds(20,400,270,30);
                 inputField.setVisible(false);
                 
                 sendButton = new JButton("Send");
                 sendButton.addActionListener(this);
                 sendButton.setBounds(300,400,80,30);
                 sendButton.setVisible(false);
                 
                 
                 chatWindow.add(hostName);
                 chatWindow.add(nicknameField);
                 chatWindow.add(connect);
                 chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                 chatWindow.setVisible(true);
                 
                 System.out.println("CHECK");
        	 
         }
         
         public void enterChat(){
                 System.out.println("TEEEST");
                 chatWindow.remove(connect);
                 chatWindow.remove(nicknameField);
                 chatWindow.remove(hostName);
                 

                 chatField.setVisible(true);
                 chatField.setEditable(false);
                 chatWindow.add(chatField);
                 
                 
                 inputField.setVisible(true);
                 chatWindow.add(inputField);
                 
                 
                 sendButton.setVisible(true);
                 chatWindow.add(sendButton);
                 
                 chatWindow.repaint();
         }
         
         public void showConnectionError(){
                 System.out.println("PROBLEMS WITH CONNECTION");
         }
         
         public void IOProblem(String type){
                 if(type.equals("output")){
                         chatField.append("\nProblem sending the message");
                 }
         }

        
        public void actionPerformed(ActionEvent e) {
                if(e.getSource().equals(connect)){
                        nickname = nicknameField.getText();
                        //TODO parse the hostName to find the port
                        if(/*client.connect(this.hostName.getText(), 8080)*/true){
                                this.enterChat();
                        }
                        else{
                                this.showConnectionError();
                        }
                }
                else if(e.getSource().equals(sendButton)){
                        System.out.println("SENDBUTTONPRESSED");
                        try{
                                actualMessage = inputField.getText();
                        }
                        catch (NullPointerException ne){
                                System.out.println(ne.getMessage());
                                actualMessage = " ";
                        }
                        if(/*!client.send(actualMessage)*/true){
                                this.IOProblem("output");
                        }
                }
        }
        
        public static void main(String[] args){
        	GUI gui = new GUI();
        }
                
}