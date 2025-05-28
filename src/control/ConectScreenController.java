package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import view.ChatScreen;
import view.ConectScreen;

public class ConectScreenController implements ActionListener {
	
	JFrame frame;
	ConectScreen conectScreen;
	String localIP;
	String targetIP;
	String localUserName;
	String targetUserName;
	Socket socket;
	BufferedReader reader;
	PrintWriter writer;

	
	public ConectScreenController(ConectScreen conectScreen, JFrame frame) {
		this.conectScreen = conectScreen;
		this.frame = frame;
		addListeners();
	}
	
	private void addListeners() {
		conectScreen.getButtonServer().addActionListener(this);
		conectScreen.getButtonClient().addActionListener(this);
		conectScreen.getButtonConect().addActionListener(this);
		conectScreen.getButtonStartChat().addActionListener(this);
		
		conectScreen.getIpInput().getDocument().addDocumentListener(new DocumentListener() {
		    private void checkIPComplete() {
		        String ipText = conectScreen.getIpInput().getText();
		        boolean isComplete = !ipText.contains("_");

		        conectScreen.getButtonConect().setEnabled(isComplete);
		    }

		    @Override
		    public void insertUpdate(DocumentEvent e) {
		        checkIPComplete();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
		        checkIPComplete();
		    }

		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        checkIPComplete();
		    }
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == conectScreen.getButtonServer()) {
			serverMode();
		}
		else if(e.getSource() == conectScreen.getButtonClient()) {
			clientMode();
		}
		else if(e.getSource() == conectScreen.getButtonConect()) {
			conect();
		}
		else if (e.getSource() == conectScreen.getButtonStartChat()) {
		    getUserNames();
		}

		
		conectScreen.repaint();
	}
	
	private void serverMode() {
		conectScreen.getButtonServer().setVisible(false);
		conectScreen.getButtonClient().setVisible(false);
		conectScreen.getLabelCommand().setText("Aguarde a conexão");

		// Cria uma nova thread para não travar a interface
	    new Thread(() -> {
	        try (ServerSocket serverSocket = new ServerSocket(50018)) {
	            System.out.println("Aguardando cliente...");
	            socket = serverSocket.accept(); // Esta linha bloqueia!

	            SwingUtilities.invokeLater(() -> {
	                System.out.println("Cliente conectado: " + socket.getInetAddress());
	                conectScreen.getLabelCommand().setText("Cliente conectado!");
	                targetIP = socket.getInetAddress().getHostAddress();
	                localIP = socket.getLocalAddress().getHostAddress();
	                System.out.println("ip local: "+localIP);
	                System.out.println("ip alvo: "+targetIP);
	                
	                requestUsername();
	            });

	        } catch (IOException e) {
	            e.printStackTrace();
	            SwingUtilities.invokeLater(() -> {
	                conectScreen.getLabelCommand().setText("Erro na conexão.");
	            });
	        }
	    }).start(); // Inicia a thread
	}
	
	private void clientMode() {
		conectScreen.getButtonServer().setVisible(false);
		conectScreen.getButtonClient().setVisible(false);
		conectScreen.getLabelCommand().setText("Digite o IP do outro computador");
		conectScreen.getButtonConect().setVisible(true);
		conectScreen.getIpInput().setVisible(true);
	}
	
	private void conect() {
		String serverIp = conectScreen.getIpInput().getText();
		int serverPort = 50018;
		
		try {
            conectScreen.getLabelCommand().setText("Tentando conectar ao servidor em " + serverIp + ":" + serverPort);
            socket = new Socket(serverIp, serverPort);
            conectScreen.getLabelCommand().setText("Conectado com sucesso ao servidor!");
            targetIP = socket.getInetAddress().getHostAddress();
            localIP = socket.getLocalAddress().getHostAddress();
            System.out.println("ip local: "+localIP);
            System.out.println("ip alvo: "+targetIP);
            
            requestUsername();
        } catch (UnknownHostException e) {
            System.err.println("IP inválido: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
	}
	
	private void requestUsername() {
		conectScreen.getIpInput().setVisible(false);
		conectScreen.getButtonConect().setVisible(false);
		conectScreen.getLabelCommand().setText("Digite o seu nome para iniciar a conversa!");
		conectScreen.getButtonStartChat().setVisible(true);
		conectScreen.getNameInput().setVisible(true);
	}
	
	private void getUserNames() {
	    if (conectScreen.getNameInput().getText().isBlank()) {
	        JOptionPane.showMessageDialog(null, "Você precisa digitar um nome!", "Erro, nome em branco", JOptionPane.WARNING_MESSAGE);
	    }
	    else {
	    	localUserName = conectScreen.getNameInput().getText();
	    	conectScreen.getButtonStartChat().setVisible(false);
	    	conectScreen.getNameInput().setVisible(false);
	    	conectScreen.getLabelCommand().setText("Aguardanado o nome do outro usuário...");
	    	
	        try {
	            writer = new PrintWriter(socket.getOutputStream(), true);
	            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	            // Sends local user's name
	            writer.println(localUserName);

	            new Thread(() -> {
	                try {
	                	// Receives the name of the other user
	                    targetUserName = reader.readLine();

	                    System.out.println("Meu nome: " + localUserName);
	                    System.out.println("Nome do outro usuário: " + targetUserName);

	                    SwingUtilities.invokeLater(() -> {
	                        conectScreen.getLabelCommand().setText("Conectado com " + targetUserName + "!");
	                    });
	                    
	                    try {
							Thread.sleep(2500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	                    
	                    //Esperando o código da tela de chat...
	                    ChatScreen chatScreen = new ChatScreen();
	                    new ChatScreenController(frame, chatScreen, localUserName, targetUserName, socket);
	                    frame.setContentPane(chatScreen);

	                } catch (IOException e) {
	                    e.printStackTrace();
	                    SwingUtilities.invokeLater(() -> {
	                        conectScreen.getLabelCommand().setText("Erro ao receber o nome do usuário.");
	                    });
	                }
	            }).start();

	        } catch (IOException e) {
	            e.printStackTrace();
	            conectScreen.getLabelCommand().setText("Erro ao trocar nomes.");
	        }
	    }
	}
}
