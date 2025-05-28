package control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import utils.FontLoader;
import view.ChatScreen;
import view.ConectScreen;

public class ChatScreenController implements ActionListener {
    
	JFrame frame;
    ChatScreen chatScreen;

    String localUserName;
    String targetUserName;
    
    private Color remoteColor = new Color(230, 240, 250);  // Light blue color for remote messages
    private Color localColor = new Color(46, 125, 50);     // Dark green color for local messages
    
    // For reading incoming and sending outgoing messages
    private BufferedReader reader;
    private PrintWriter writer;                            
    
    public ChatScreenController(JFrame frame, ChatScreen chatScreen, String localUserName, String targetUserName, Socket socket) {
        this.frame = frame;
    	this.chatScreen = chatScreen;
        this.localUserName = localUserName;
        this.targetUserName = targetUserName;
        this.chatScreen.getLabelRemoteUsername().setText(targetUserName);  // Set remote username label
        
        try {
            // Initialize network communication streams
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        addListeners();
        startReceiver();  // Start thread to listen for incoming messages
    }

    private void addListeners() {
        // Add action listeners to buttons
        chatScreen.getButtonSendMessage().addActionListener(this);
        chatScreen.getButtonSendFile().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chatScreen.getButtonSendMessage()) {
            String message = chatScreen.getTextFieldMessageInput().getText().trim();
            if (!message.isEmpty()) {
                writer.println(message);  // Send message through network
                addMessage(localUserName, message, true);  // Add to UI as local message
                chatScreen.getTextFieldMessageInput().setText("");  // Clear input field
            }
        }
    }
    
    private void startReceiver() {
        new Thread(() -> {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    final String receivedMessage = message;
                    addMessage(targetUserName, receivedMessage, false);  // Add received message to UI
                }
            } catch (IOException e) {
            	JOptionPane.showMessageDialog(chatScreen, targetUserName+" saiu do chat, você será redirecionado para a tela inicial!",targetUserName+" Abandonou a Conversa",JOptionPane.INFORMATION_MESSAGE);
            	// starts up the conection screen
        		ConectScreen conectScreen = new ConectScreen();
        		new ConectScreenController(conectScreen, frame);
        		frame.getContentPane().removeAll();
        		frame.getContentPane().add(conectScreen);
        		frame.revalidate();     
        		frame.repaint();
            }
        }).start();
    }
    
    private void addMessage(String sender, String message, boolean isLocal) {
        SwingUtilities.invokeLater(() -> {
            // Panel to contain the message
            JPanel messagePanel = new JPanel();
            messagePanel.setLayout(new BorderLayout());
            messagePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            messagePanel.setBackground(Color.WHITE);

            // Configure message label
            JLabel messageLabel = new JLabel("<html><body style='width: 200px'>" + message + "</body></html>");
            messageLabel.setOpaque(true);
            messageLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            messageLabel.setFont(FontLoader.loadCustomFont(12f));
            messageLabel.setForeground(Color.BLACK);

            // Alignment and color based on message origin
            if (isLocal) {
                messagePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));  // Right-align local messages
                messageLabel.setBackground(localColor);
                messageLabel.setForeground(Color.WHITE);
            } else {
                messagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));   // Left-align remote messages
                messageLabel.setBackground(remoteColor);
            }
                
            // Add message to chat panel
            messagePanel.add(messageLabel);
            chatScreen.getPanelChat().add(messagePanel);
        
            // Auto-scroll to bottom
            chatScreen.getPanelChat().revalidate();
            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = chatScreen.getScrollPane().getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());  
            });
        });
    }
}