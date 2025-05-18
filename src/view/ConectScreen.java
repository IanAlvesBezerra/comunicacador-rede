package view;

import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import utils.FontLoader;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

public class ConectScreen extends JPanel{
	private JPanel panelTitle;
	private JLabel labelTitle;
	private JButton buttonServer;
	private JButton buttonClient;
	private JLabel labelCommand;
	private JLabel labelIPAddress;
	private JFormattedTextField textFieldIPInput;
	private JButton buttonConect;
	
	public ConectScreen() {
		setSize(600, 500);
		setLayout(null);
		add(getPanelTitle());
		add(getButtonServer());
		add(getButtonClient());
		add(getLabelCommand());
		add(getLabelIPAddress());
		add(getIpInput());
		add(getButtonConect());
	}
	
	public JPanel getPanelTitle() {
		if (panelTitle == null) {
			panelTitle = new JPanel();
			panelTitle.setBackground(new Color(43, 87, 154));
			panelTitle.setBounds(0, 0, 600, 100);
			panelTitle.setLayout(null);
			panelTitle.add(getLabelTitle());
		}
		return panelTitle;
	}
	public JLabel getLabelTitle() {
		if (labelTitle == null) {
			labelTitle = new JLabel("Web Chat");
			labelTitle.setForeground(new Color(255, 255, 255));
			labelTitle.setFont(FontLoader.loadCustomFont(30f));
			labelTitle.setBounds(231, 29, 138, 42);
		}
		return labelTitle;
	}
	public JButton getButtonServer() {
		if (buttonServer == null) {
			buttonServer = new JButton("Aguardar Conexão");
			buttonServer.setToolTipText("Aguarda a conxão como servidor");
			buttonServer.setForeground(new Color(255, 255, 255));
			buttonServer.setFont(FontLoader.loadCustomFont(25f));
			buttonServer.setBackground(new Color(43, 87, 154));
			buttonServer.setBounds(137, 213, 323, 65);
//			buttonServer.setVisible(false);
		}
		return buttonServer;
	}
	public JButton getButtonClient() {
		if (buttonClient == null) {
			buttonClient = new JButton("Conectar");
			buttonClient.setToolTipText("Tenta conectar em outro computador como cliente");
			buttonClient.setForeground(Color.WHITE);
			buttonClient.setFont(FontLoader.loadCustomFont(25f));
			buttonClient.setBackground(new Color(43, 87, 154));
			buttonClient.setBounds(137, 312, 323, 65);
//			buttonClient.setVisible(false);
		}
		return buttonClient;
	}
	public JLabel getLabelCommand() {
		if (labelCommand == null) {
			labelCommand = new JLabel("Selecione uma opção");
			labelCommand.setFont(FontLoader.loadCustomFont(30f));
			labelCommand.setBounds(0, 135, 600, 42);
			labelCommand.setHorizontalAlignment(JLabel.CENTER);
		}
		return labelCommand;
	}
	public JLabel getLabelIPAddress() {
		if (labelIPAddress == null) {
			labelIPAddress = new JLabel("Seu endereço: ");
			labelIPAddress.setFont(FontLoader.loadCustomFont(30f));
			labelIPAddress.setBounds(0, 225, 600, 49);
			labelIPAddress.setHorizontalAlignment(JLabel.CENTER);
			labelIPAddress.setVisible(false);
		}
		return labelIPAddress;
	}
	public JFormattedTextField getIpInput() {
		if(textFieldIPInput == null) {
			MaskFormatter mask = null;
			try {
				mask = new MaskFormatter("###.###.###.###");
				mask.setPlaceholderCharacter('_'); // Caractere de placeholder
	            mask.setValidCharacters("0123456789"); // Aceita apenas números
	            mask.setValueClass(String.class);
	            mask.setOverwriteMode(true);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			textFieldIPInput = new JFormattedTextField(mask);
			textFieldIPInput.setFont(FontLoader.loadCustomFont(25f));
			textFieldIPInput.setBounds(137, 213, 323, 65);
			textFieldIPInput.setHorizontalAlignment(JFormattedTextField.CENTER);
			textFieldIPInput.setVisible(false);
		}
		return textFieldIPInput;
	}
	public JButton getButtonConect() {
		if(buttonConect == null) {
			buttonConect = new JButton("Iniciar conexão");
			buttonConect.setToolTipText("Tenta conectar em outro computador como cliente");
			buttonConect.setForeground(Color.WHITE);
			buttonConect.setFont(FontLoader.loadCustomFont(30f));
			buttonConect.setBackground(new Color(76, 175, 80));
			buttonConect.setBounds(137, 312, 323, 65);
			buttonConect.setEnabled(false);
			buttonConect.setVisible(false);
		}
		return buttonConect;
	}
}
