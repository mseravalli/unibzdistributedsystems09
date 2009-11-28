
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI implements ActionListener, WindowListener{
        
        private JFrame chatWindow;
        private JTextArea chatField;
        private JScrollPane scroll;
        private JButton connect;
        private JButton sendButton;
        private JButton disconnectButton;
	private JButton quitButton;
        private JTextField hostName;
        private JTextField nicknameField;
        private JTextField inputField;
        private JLabel errorLabel;
        
        private String nickname;
        private String actualMessage;
        private Client client;

	/**
	 *
	 */
        public GUI(){

            
            	chatWindow = new JFrame("Chat");
            	chatWindow.setBounds(100, 100, 400, 500);
            	chatWindow.setLayout(null);
            	hostName = new JTextField("127.0.0.1:8080");
            	nicknameField = new JTextField("Nickname");
                connect = new JButton("Connect");                
                connect.addActionListener(this);
                scroll = new JScrollPane();
                chatField = new JTextArea();         
		scroll.setViewportView(chatField);

                inputField = new JTextField();

		sendButton = new JButton("Send");
                sendButton.addActionListener(this);

                disconnectButton = new JButton("Back to menu");
                disconnectButton.addActionListener(this);

		quitButton = new JButton("Quit");
                quitButton.addActionListener(this);

                inputField.addActionListener(this);
                errorLabel = new JLabel("Connection Failed");
                
                chatWindow.add(hostName);
                chatWindow.add(nicknameField);
                chatWindow.add(connect);
                chatWindow.add(errorLabel);
                
                chatWindow.add(scroll);
                chatWindow.add(inputField);
                chatWindow.add(sendButton);
                chatWindow.add(disconnectButton);
		chatWindow.add(quitButton);

                

                chatWindow.addWindowListener(this);
                
                startWindow();
                client = new Client(this.chatField);
         }

	/**
	 * 
	 */
	public void startWindow(){
                 
                 
                hostName.setBounds(100,50,200,30);
                hostName.setVisible(true);
                 
                 
                nicknameField.setBounds(100,120,200,30);
                nicknameField.setVisible(true);
                 
                 
                connect.setBounds(125, 250, 150, 50);
                connect.setVisible(true);

                errorLabel.setVisible(false);
                errorLabel.setBounds(100, 350, 200, 30);
                 
                scroll.setBounds(5,5,390,390);
                scroll.setVisible(false);
                 
                //chatField.setBounds(5,5,300,300);
                chatField.setVisible(false);
                chatField.setLineWrap(true);
                chatField.setWrapStyleWord(true);


                inputField.setBounds(5,400,390,30);
                inputField.setVisible(false);
                 
                sendButton.setBounds(5,435,80,30);
                sendButton.setVisible(false);
                 
                disconnectButton.setBounds(140, 435, 120, 30);
                disconnectButton.setVisible(false);

		quitButton.setBounds(125, 310, 150, 50);
		quitButton.setVisible(true);
                 
                 
                 //chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                chatWindow.setResizable(false);
                chatWindow.setVisible(true);
                 
                chatWindow.repaint();
                 
        	 
	}

	 /**
	  * 
	  */
         public void enterChat(){
                 
                 connect.setVisible(false);
                 nicknameField.setVisible(false);
                 hostName.setVisible(false);
                 

                 chatField.setVisible(true);
                 chatField.setEditable(false);
                 scroll.setVisible(true);
                 
                 
                 inputField.setVisible(true);
                 
                 
                 sendButton.setVisible(true);
                 
                 disconnectButton.setVisible(true);
		 
		 quitButton.setBounds(315, 435, 80, 30);

                 errorLabel.setVisible(false);
                 
                 chatWindow.repaint();
         }
         
         public void showConnectionError(){
                 errorLabel.setVisible(true);
         }
         
         public void disconnect(){
        	 	
        	 this.startWindow();
         }
         
         public void IOProblem(String type){
                 if(type.equals("output")){
                         chatField.append("Problem sending the message\n");
                 }
         }

        /**
	 * 
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
        		
        		//if the "connect" button was pressed
                if(e.getSource().equals(connect)){
                	
                        nickname = nicknameField.getText() + ": ";
                        String delims = "[:]";
                        String[] tokens = hostName.getText().split(delims);
                        try{
                        	//System.out.println(tokens[0] + " " + tokens[1]);
                        	if(client.connect(tokens[0], Integer.parseInt(tokens[1]), this.nickname)){
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
                
                //if the enter is pressed while typing in the textField
                else if(e.getSource().equals(inputField)){

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
                
                //if the disconnect-button was pressed
                else if(e.getSource().equals(disconnectButton)){
                	client.disconnect();
                	this.disconnect();
                }

		else if(e.getSource().equals(quitButton)){
                	client.disconnect();
                	System.exit(0);
		}
                
                
        }

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			//this.client.disconnect();
			System.exit(0);
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			//this.client.disconnect();
			System.exit(0);			
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
        public static void main(String[] args){
        	GUI gui = new GUI();
        }
                
}