package view;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.FontLoader;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JButton;

public class ChatScreen extends JPanel {
	
	private JPanel panelTitle;
	private JLabel labelTitle;
	private JPanel panelChat;
	private JScrollPane scrollPane;
	private JTextField textFieldMessageInput;
	private JLabel labelRemoteUsername;
	private JButton buttonSendMessage;
	private JButton buttonSendFile;
	
	public ChatScreen() {
		setSize(600, 500);
		setLayout(null);
		add(getPanelTitle());
		add(getScrollPane());
		add(getTextFieldMessageInput());
		add(getButtonSendMessage());
		add(getButtonSendFile());
	}
	
	public JPanel getPanelTitle() {
		if (panelTitle == null) {
			panelTitle = new JPanel();
			panelTitle.setBackground(new Color(43, 87, 154));
			panelTitle.setBounds(0, 0, 600, 100);
			panelTitle.setLayout(null);
			panelTitle.add(getLabelTitle());
			panelTitle.add(getLabelRemoteUsername());
		}
		return panelTitle;
	}
	public JLabel getLabelTitle() {
		if (labelTitle == null) {
			labelTitle = new JLabel("LAN Chat");
			labelTitle.setForeground(new Color(255, 255, 255));
			labelTitle.setFont(FontLoader.loadCustomFont(30f));
			labelTitle.setBounds(415, 29, 132, 42);
		}
		return labelTitle;
	}
	public JPanel getPanelChat() {
		if (panelChat == null) {
			panelChat = new JPanel();
			panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.Y_AXIS));
			panelChat.setBounds(10, 111, 564, 295);
			panelChat.setBackground(Color.WHITE);
		}
		return panelChat;
	}
	public JScrollPane getScrollPane() {
		if(scrollPane == null) {
			scrollPane = new JScrollPane(getPanelChat());
			scrollPane.setBounds(10, 111, 564, 295);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return scrollPane;
	}
	public JTextField getTextFieldMessageInput() {
		if (textFieldMessageInput == null) {
			textFieldMessageInput = new JTextField();
			textFieldMessageInput.setBounds(57, 413, 437, 37);
			textFieldMessageInput.setFont(FontLoader.loadCustomFont(15f));
			textFieldMessageInput.setColumns(10);
		}
		return textFieldMessageInput;
	}
	public JLabel getLabelRemoteUsername() {
		if (labelRemoteUsername == null) {
			labelRemoteUsername = new JLabel("Remote Username");
			labelRemoteUsername.setForeground(Color.WHITE);
			labelRemoteUsername.setFont(FontLoader.loadCustomFont(30f));
			labelRemoteUsername.setBounds(25, 29, 398, 42);
		}
		return labelRemoteUsername;
	}
	public JButton getButtonSendMessage() {
		if (buttonSendMessage == null) {
			buttonSendMessage = new JButton("Enviar");
			buttonSendMessage.setBackground(new Color(76, 175, 80));
			buttonSendMessage.setFont(FontLoader.loadCustomFont(10f));
			buttonSendMessage.setForeground(Color.WHITE);
			buttonSendMessage.setBounds(504, 413, 70, 37);
		}
		return buttonSendMessage;
	}
	public JButton getButtonSendFile() {
		if (buttonSendFile == null) {
			buttonSendFile = new JButton("+");
			buttonSendFile.setBackground(new Color(43, 87, 154));
			buttonSendFile.setForeground(Color.WHITE);
			buttonSendFile.setFont(FontLoader.loadCustomFont(5f));
			buttonSendFile.setBounds(10, 413, 37, 37);
		}
		return buttonSendFile;
	}
}
