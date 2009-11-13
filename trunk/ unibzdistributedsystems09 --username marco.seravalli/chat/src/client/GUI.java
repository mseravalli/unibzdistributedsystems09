package client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class GUI extends JFrame {

    private Toolkit toolkit;

    public GUI() {

        setTitle("Buttons");
        setSize(300, 200);

        toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation((size.width - getWidth())/2, (size.height - getHeight())/2);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        getContentPane().add(panel);

	panel.setLayout(null);

        JTextField beep = new JTextField("inserisci il testo qui");
        beep.setBounds(150, 60, 80, 30);
        beep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                toolkit.beep();
            }
        });

       JButton close = new JButton("Close");
       close.setBounds(50, 60, 80, 30);
       close.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent event) {
               System.exit(0);
          }
       });

        panel.add(beep);
        panel.add(close);

    }

    public static void main(String[] args) {

    	GUI buttons = new GUI();
        buttons.setVisible(true);

    }
}
