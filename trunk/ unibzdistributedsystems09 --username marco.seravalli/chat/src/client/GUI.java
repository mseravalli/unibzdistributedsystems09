package client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class GUI extends JFrame {


	public GUI() {

		setTitle("BorderLayout");

		JPanel connectionSettings = new JPanel(new GridLayout(2,3));
		
		JLabel hostLabel = new JLabel("hostname");
		JLabel nicknameLabel = new JLabel("nickname");
		JLabel emptyLabel = new JLabel("");
       
       
		JTextField host = new JTextField("127.0.0.1");
		JTextField nickname = new JTextField("");
		JButton connect = new JButton("connect");
       
		connectionSettings.add(hostLabel);
		connectionSettings.add(nicknameLabel);
		connectionSettings.add(emptyLabel);		
       
		connectionSettings.add(host);
		connectionSettings.add(nickname);
		connectionSettings.add(connect);
		add(connectionSettings, BorderLayout.NORTH);
        
        
        JScrollPane scrollableTextArea = new JScrollPane();
        JTextArea area = new JTextArea(); 

        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        scrollableTextArea.getViewport().add(area);        

        add(scrollableTextArea, BorderLayout.CENTER);
        
        
        
        //statusbar.setPreferredSize(new Dimension(0, 22));

        //add(writeMessage, BorderLayout.SOUTH);
        
        
        


        JPanel sendPanel = new JPanel(new GridLayout(3,1));
        
        JLabel typeYourMessage = new JLabel("type your message here:");
        JTextField writeMessage = new JTextField();
		JButton sendMessage = new JButton("send");
       
		sendPanel.add(typeYourMessage);
		sendPanel.add(writeMessage);
		sendPanel.add(sendMessage);
		add(sendPanel, BorderLayout.SOUTH);
        
        
        
        
        
        
        
        
        
        

        setSize(300, 400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


    }

    public static void main(String[] args) {

    	GUI buttons = new GUI();
        buttons.setVisible(true);

    }
}
