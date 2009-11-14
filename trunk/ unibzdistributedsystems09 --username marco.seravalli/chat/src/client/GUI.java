package client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public JTextArea area;

	public GUI() {
		
		/*
		 * panel with all the settings for the connection
		 */
		JPanel connectionSettings = new JPanel(new GridLayout(2,3));
		
		JLabel hostLabel = new JLabel("hostname");
		JLabel nicknameLabel = new JLabel("nickname");       
       
		JTextField host = new JTextField("127.0.0.1");
		JTextField nickname = new JTextField("");
		JButton connect = new JButton("connect");
       
		connectionSettings.add(hostLabel);
		connectionSettings.add(nicknameLabel);
		connectionSettings.add(new JLabel(""));		
       
		connectionSettings.add(host);
		connectionSettings.add(nickname);
		connectionSettings.add(connect);
		add(connectionSettings, BorderLayout.NORTH);
        
        
		/*
		 * text area with scrollbar
		 */
        JScrollPane scrollableTextArea = new JScrollPane();
        area = new JTextArea(); 

        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        scrollableTextArea.getViewport().add(area);        

        add(scrollableTextArea, BorderLayout.CENTER);
        
        

        /*
         * this part creates the panel under the text area for the writing of user's messages
         */
        JPanel sendPanel = new JPanel(new GridLayout(3,1));
        
        JLabel typeYourMessage = new JLabel("type your message here:");
        JTextField writeMessage = new JTextField();
		JButton sendMessage = new JButton("send");
       
		sendPanel.add(typeYourMessage);
		sendPanel.add(writeMessage);
		sendPanel.add(sendMessage);
		
		
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
