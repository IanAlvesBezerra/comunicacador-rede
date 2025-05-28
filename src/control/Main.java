package control;

import javax.swing.JFrame;

import view.ConectScreen;

public class Main {
	public static void main(String[] args) {
		// sets up frame
		JFrame frame = new JFrame();
		frame.setSize(600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		// starts up the conection screen
		ConectScreen conectScreen = new ConectScreen();
		new ConectScreenController(conectScreen, frame);
		frame.getContentPane().add(conectScreen);
		frame.repaint();
	}
}
