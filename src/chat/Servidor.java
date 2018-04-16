package chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Servidor {

	private ServerSocket servidor;
	private List<PrintStream> clientes;

	public Servidor(int porta) throws IOException {		
		servidor = new ServerSocket(porta);
		clientes = new ArrayList<PrintStream>();
		JOptionPane.showMessageDialog(null, "Porta " + porta + " foi aberta!");
	}

	public void executar() throws IOException {

		while (true) {
			Socket cliente = servidor.accept();
			System.out.println("O cliente " + cliente.getInetAddress().getHostAddress() + " acaba de entrar");
			// adiciona saida do client à lists
			PrintStream ps = new PrintStream(cliente.getOutputStream());
			this.clientes.add(ps);
			// cria tratador de cliente numa nova thread
			TrataCliente tc = new TrataCliente(cliente.getInputStream(), this);
			new Thread(tc).start();
		}
	}

	public void distribuiMensagem(String msg) {
		// envia msg para todo mundo
		for (PrintStream cliente : this.clientes) {
			cliente.println(msg);
		}
	}

	public class TrataCliente implements Runnable {
		private InputStream cliente;
		private Servidor servidor;

		public TrataCliente(InputStream cliente, Servidor servidor) {
			this.cliente = cliente;
			this.servidor = servidor;
		}

		public void run() {
			// quando chegar uma msg, distribui pra todos
			Scanner s = new Scanner(this.cliente);
			while (s.hasNextLine()) {
				servidor.distribuiMensagem(s.nextLine());
			}
			s.close();
		}
	}
	
	public static void main(String[] args) throws IOException {
		new Servidor(5000).executar();
	}
}
