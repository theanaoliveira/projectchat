package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Cliente extends JFrame {

	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu = new JMenu("Menu");
	private JMenuItem itemImagem = new JMenuItem("Enviar Imagem");
	private JMenuItem itemSalvarconv = new JMenuItem("Salvar Conversa");
	private JMenuItem itemAbrirConv = new JMenuItem("Abrir Conversa");
	private JMenuItem itemSair = new JMenuItem("Sair");

	private JTextArea txtRecebe = new JTextArea();
	private JScrollPane scRecebe = new JScrollPane(txtRecebe);
	private JTextField txtEnvia = new JTextField();
	//private JScrollPane scEnvia = new JScrollPane(txtEnvia);
	private ImageIcon iconeEnviar = new ImageIcon("imagens/iconeEnviar.png");
	private JButton btnEnviar = new JButton(iconeEnviar);
	private Container painel1 = new JPanel();
	private Font fonte1 = new Font("Estrangelo Edessa", Font.PLAIN, 15);

	Scanner leitor;
	private String ip;
	private int porta;
	private static String nome;
	private Socket cliente;

	private Random random1 = new Random();
	private Random random2 = new Random();
	private Random random3 = new Random();
	
	public Cliente(String ip, int porta, String nome) throws UnknownHostException, IOException {
		this.ip = ip;
		this.porta = porta;
		Cliente.nome = nome;
		janela();
		menu();				
	}

	public void janela() throws UnknownHostException, IOException {
		setTitle(nome);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(540, 500);
		setLocationRelativeTo(null);
		setVisible(true);
		
		Color cor = new Color(random1.nextInt(255), random1.nextInt(255), random1.nextInt(255));
	
		txtRecebe.setLineWrap(true);
		txtRecebe.setEditable(false);
		txtRecebe.setBackground(new Color(230, 230, 230));
		//txtRecebe.setForeground(cor);
		//txtEnvia.setBackground(new Color(230, 230, 230));
		txtRecebe.setFont(fonte1);
		txtEnvia.requestFocus();

		painel1.setLayout(new BorderLayout());
		painel1.add(BorderLayout.CENTER, txtEnvia);
		painel1.add(BorderLayout.EAST, btnEnviar);

		getContentPane().add(BorderLayout.CENTER, scRecebe);
		getContentPane().add(BorderLayout.SOUTH, painel1);

		btnEnviar.setFocusable(false);
		btnEnviar.setContentAreaFilled(false);
		btnEnviar.setPreferredSize(new Dimension(37, 25));
	}

	public void menu() {
		menu.add(itemImagem);
		menu.add(itemSalvarconv);
		menu.add(itemAbrirConv);
		menu.add(itemSair);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		System.out.println("Teste: " + txtEnvia.getText());
	}

	public void executa() throws UnknownHostException, IOException {
		cliente = new Socket(this.ip, this.porta);
		System.out.println("O cliente se conectou ao servidor!");
		// thread para receber mensagens do servidor
		Recebedor r = new Recebedor(cliente.getInputStream());
		new Thread(r).start();
		// lê msgs do teclado e manda pro servidor
		btnEnviar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!txtEnvia.getText().equals("")) {
					try {
						PrintStream saida = new PrintStream(cliente.getOutputStream());
						saida.println(nome + ": " + txtEnvia.getText());
						txtEnvia.setText("");
						txtEnvia.requestFocus();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Erro ao enviar mensagem: " + e1.getMessage());
					}
				}
			}
		});
		txtEnvia.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if ((!txtEnvia.getText().equals(""))) {
						try {
							PrintStream saida = new PrintStream(cliente.getOutputStream());
							saida.println(nome + ": " + txtEnvia.getText());
							txtEnvia.setText("");
							txtEnvia.requestFocus();

						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "Erro ao enviar mensagem: " + e2.getMessage());
						}
					}
				}
			}
		});
	}

	public class Recebedor implements Runnable {
		private InputStream cliente;

		public Recebedor(InputStream cliente) {
			this.cliente = cliente;
		}

		public void run() {
			try {
				Scanner s = new Scanner(cliente);
				while (s.hasNextLine()) {
					txtRecebe.append(s.nextLine() + "\n");
					txtRecebe.setCaretPosition(txtRecebe.getText().length());
				}
			} catch (Exception a) {
			}
		}
	}
}
