
package client;

import java.awt.Color;
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
import javax.swing.SwingConstants;

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
        
        private String actualMessage;
        private Client client;

	/**
	 * Constructs a new GUI
	 */
        public GUI(){

            
            	chatWindow = new JFrame("Chat");
            	chatWindow.setBounds(100, 100, 400, 500);
            	chatWindow.setLayout(null);
            	
            	hostName = new JTextField("127.0.0.1:8080");
            	hostName.addActionListener(this);
            	
            	nicknameField = new JTextField("Nickname");
            	nicknameField.addActionListener(this);
            	
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
                errorLabel.setForeground(Color.RED);
                
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
	 * Initializes some components to be visible and to be located in a determined
	 * part of the window
	 */
	public void startWindow(){
                
				chatWindow.setTitle("Chat");
                 
                hostName.setBounds(100,50,200,30);
                hostName.setVisible(true);
                 
                 
                nicknameField.setBounds(100,120,200,30);
                nicknameField.setVisible(true);
                 
                 
                connect.setBounds(125, 250, 150, 50);
                connect.setVisible(true);
                 
                scroll.setBounds(5,5,390,390);
                scroll.setVisible(false);
                 
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
		
				errorLabel.setVisible(false);
				errorLabel.setBounds(125, 370, 150, 50);
				errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
                 
                chatWindow.setResizable(false);
                chatWindow.setVisible(true);
                 
                chatWindow.repaint();
                 
        	 
	}

	 /**
	 * Initializes some components to be visible and to be located in a determined
	 * part of the window
	 */
         public void enterChat(){
        	 
        	 chatWindow.setTitle("Chat - " + this.nicknameField.getText());
        	 
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

		 /**
		  * The errorLabel becomes visible
		  */
         public void showConnectionError(){
                 errorLabel.setVisible(true);
         }

		 /**
		  * Prints the type of error on the chatField
		  *
		  * @param type
		  */
         public void IOProblem(String type){
                 if(type.equals("output")){
                         chatField.append("Problem sending the message\n");
                 }
         }

    /**
	 * Handles all the actions performed by all the components within the
	 * chatWindow
	 * 
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
        		
                if(e.getSource().equals(this.connect)){
                	this.connectToServer();
                }
                
                else if(e.getSource().equals(hostName)){
                	System.out.println("shoene da");
                	this.connectToServer();
                }
                
                else if(e.getSource().equals(nicknameField)){
                	this.connectToServer();
                }
                
                /*
                 * if the "Send" button was pressed the message present in the 
                 * input field is sent to the server
                 */
                else if(e.getSource().equals(sendButton)){

                        this.sendMessage();
                        
                }
                
                /*
                 * if the enter is pressed while typing in the textField the message
                 * present in the input field is sent to the server
                 */
                else if(e.getSource().equals(inputField)){

                    this.sendMessage();
                    
                }
                
                /*
                 * if the disconnect-button was pressed the client disconnects and
                 * shows the main menu
                 */
                else if(e.getSource().equals(disconnectButton)){
                	client.disconnect();
                	this.startWindow();
                }

                /*
                 * if the quit-button was pressed the client disconnects and the
                 * program exits
                 */
				else if(e.getSource().equals(quitButton)){
		                	client.disconnect();
		                	System.exit(0);
				}
                
                
        }
	
		private void sendMessage(){
			try{
                actualMessage = inputField.getText();
	        }
	        catch (NullPointerException ne){
	                System.out.println(ne.getMessage());
	                actualMessage = " ";
	        }
	        if(!client.sendMessage(actualMessage)){
	                this.IOProblem("output");
	        }
	        
	        inputField.setText("");
		}
		
		
		/*
		 * the method parses the server field 
		 * in order to obtain the ip address and the port, then if the 
		 * client successfully connects to the server another screen is
		 * displayed in the window otherwise an error message
		 */
		private void connectToServer(){
			String delims = "[:]";
            String[] tokens = hostName.getText().split(delims);
            try{
            	if(client.connect(tokens[0], Integer.parseInt(tokens[1]), this.nicknameField.getText())){
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

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			//this.client.disconnect();
			System.exit(0);
		}

		/**
		 * Disconnects the client and stops the main, when the window
		 * is closed
		 *
		 * @param arg0
		 */
		@Override
		public void windowClosing(WindowEvent arg0) {
			this.client.disconnect();
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