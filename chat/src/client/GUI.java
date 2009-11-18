
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
                 
                 hostName = new JTextField("127.0.0.1:8080");
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
                 inputField.addActionListener(this);
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
                         chatField.append("Problem sending the message\n");
                 }
         }

        
        public void actionPerformed(ActionEvent e) {
        		
        		//if the "connect" button was pressed
                if(e.getSource().equals(connect)){
                        nickname = nicknameField.getText() + ": ";
                        String delims = "[:]";
                        String[] tokens = hostName.getText().split(delims);
                        try{
                        	//System.out.println(tokens[0] + " " + tokens[1]);
                        	if(client.connect(tokens[0], Integer.parseInt(tokens[1]))){
                        			this.enterChat();
                        	}
                        	else{
                        			this.showConnectionError();
                        	}
                        }
                        catch(Exception ex){
                        	this.showConnectionError();
                        }
                }
                
                //if the "Send" button was pressed
                else if(e.getSource().equals(sendButton)){
                        System.out.println("SENDBUTTONPRESSED");
                        try{
                                actualMessage = inputField.getText();
                        }
                        catch (NullPointerException ne){
                                System.out.println(ne.getMessage());
                                actualMessage = " ";
                        }
                        if(!client.sendMessage(nickname+actualMessage)){
                                this.IOProblem("output");
                        }
                        
                        inputField.setText("");
                        
                }
                //
                else if(e.getSource().equals(inputField)){
                    System.out.println("SENDBUTTONPRESSED");
                    try{
                            actualMessage = inputField.getText();
                    }
                    catch (NullPointerException ne){
                            System.out.println(ne.getMessage());
                            actualMessage = " ";
                    }
                    if(!client.sendMessage(nickname+actualMessage)){
                            this.IOProblem("output");
                    }
                    
                    inputField.setText("");
                    
            }
               
                
                
        }
        
        public static void main(String[] args){
        	GUI gui = new GUI();
        }
                
}