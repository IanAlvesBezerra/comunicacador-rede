package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import view.ConectScreen;

public class ConectScreenController implements ActionListener {
	
	ConectScreen conectScreen;
	
	public ConectScreenController(ConectScreen conectScreen) {
		this.conectScreen = conectScreen;
		addListeners();
	}
	
	private void addListeners() {
		conectScreen.getButtonServer().addActionListener(this);
		conectScreen.getButtonClient().addActionListener(this);
		conectScreen.getButtonConect().addActionListener(this);
		
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
		
		conectScreen.repaint();
	}
	
	private void serverMode() {
		conectScreen.getButtonServer().setVisible(false);
		conectScreen.getButtonClient().setVisible(false);
		conectScreen.getLabelCommand().setText("Aguarde a conexão");
		conectScreen.getLabelIPAddress().setVisible(true);
		
		String ip;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			conectScreen.getLabelIPAddress().setText("Seu endereço: " + ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Cria uma nova thread para não travar a interface
	    new Thread(() -> {
	        try (ServerSocket serverSocket = new ServerSocket(50018)) {
	            System.out.println("Aguardando cliente...");
	            Socket cliente = serverSocket.accept(); // Esta linha bloqueia!

	            // Quando o cliente conectar, atualizamos a interface com segurança:
	            SwingUtilities.invokeLater(() -> {
	                System.out.println("Cliente conectado: " + cliente.getInetAddress());
	                conectScreen.getLabelCommand().setText("Cliente conectado!");
	                // Aqui você pode abrir a tela de chat ou mudar de estado
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
            Socket socket = new Socket(serverIp, serverPort);
            conectScreen.getLabelCommand().setText("Conectado com sucesso ao servidor!");
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("IP inválido: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
	}
	
	public static void main(String[] args) {
		// sets up frame
		JFrame frame = new JFrame();
		frame.setSize(600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		// starts up the conection screen
		ConectScreen cs = new ConectScreen();
		new ConectScreenController(cs);
		frame.add(cs);
		frame.repaint();
	}

}
