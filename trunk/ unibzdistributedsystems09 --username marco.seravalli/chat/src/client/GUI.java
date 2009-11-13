package client;

public class GUI extends javax.swing.JFrame {
	
	public GUI (){
		this.setSize(300, 400);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setVisible(true);
	}
	
	public static void main (String args[]) { 
		
		new GUI();
		
		
	}

}
