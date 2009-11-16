package client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	JPanel connectionSettings;
	
	JLabel hostLabel;
	JLabel nicknameLabel;
	JTextField host;
	JTextField nickname;
	JButton connect;
	
	JScrollPane scrollableTextArea;
	public JTextArea area;
	
	JPanel sendPanel;
	JLabel typeYourMessage;
	JTextField writeMessage;
	JButton sendMessage;

	public GUI() {
		
		/*
		 * panel with all the settings for the connection
		 */
		connectionSettings = new JPanel(new GridLayout(2,3));
		
		hostLabel = new JLabel("hostname");
		nicknameLabel = new JLabel("nickname");       
       
		host = new JTextField("127.0.0.1");
		nickname = new JTextField("");
		connect = new JButton("connect");
       
		connectionSettings.add(hostLabel);
		connectionSettings.add(nicknameLabel);
		connectionSettings.add(new JLabel(""));		
       
		connectionSettings.add(host);
		connectionSettings.add(nickname);
		connectionSettings.add(connect);
        
        
		/*
		 * text area with scrollbar
		 */
        scrollableTextArea = new JScrollPane();
        area = new JTextArea(); 

        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        scrollableTextArea.getViewport().add(area);            
        

        /*
         * this part creates the panel under the text area for the writing of user's messages
         */
        sendPanel = new JPanel(new GridLayout(3,1));
        
        typeYourMessage = new JLabel("type your message here:");
        writeMessage = new JTextField();
		sendMessage = new JButton("send");
       
		sendPanel.add(typeYourMessage);
		sendPanel.add(writeMessage);
		sendPanel.add(sendMessage);
		
		
		/*
		 * all the panels above are added to the main window
		 */
		this.add(connectionSettings, BorderLayout.NORTH);
		this.add(scrollableTextArea, BorderLayout.CENTER);
		this.add(sendPanel, BorderLayout.SOUTH);       
                
		this.setTitle("chat room");
		
		this.setSize(300, 450);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
        
        
    }

    public static void main(String[] args) {

    	GUI gui = new GUI();
    	
    	Client c = new Client (gui.area);
        
        c.connect();

    }
}
