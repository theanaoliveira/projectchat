package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class JanelaPrincipal extends JFrame {

	private Connection con = null;
	private PreparedStatement pst;

	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu = new JMenu("Menu");
	private JMenuItem itemCadastro = new JMenuItem("Cadastro");
	private JMenuItem itemSair = new JMenuItem("Sair");
	private JLabel lblIp = new JLabel("IP:");
	private static JTextField txtIp = new JTextField(10);
	private JLabel lblPorta = new JLabel("PORTA:");
	private static JTextField txtPorta = new JTextField(5);
	private JLabel lblUsuario = new JLabel("Usuário:");
	private static JTextField txtUsuario = new JTextField(19);
	private JLabel lblSenha = new JLabel("Senha:");
	private JPasswordField txtSenha = new JPasswordField(20);
	private JButton btnEntrar = new JButton("Entrar");
	private JPanel painel1 = new JPanel();
	private JPanel painel2 = new JPanel();
	private JPanel painel3 = new JPanel();	
	
	private String usuario;
	private String senha;
	private String nome;
	private String nomeFrame;
				

	public void janela() {
		setTitle("LOGIN");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(295, 190);
		setLocationRelativeTo(null);
		setResizable(false);
		
		txtIp.setDocument(new LimitaNroCaracteres(14));
		txtPorta.setDocument(new LimitaNroCaracteres(6));
		txtUsuario.setDocument(new LimitaNroCaracteres(20));
		txtSenha.setDocument(new LimitaNroCaracteres(20));
		
		painel1.setBackground(new Color(200, 200, 200));

		getContentPane().add(BorderLayout.NORTH, painel1);
		painel1.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 5));
		getContentPane().add(BorderLayout.CENTER, painel2);
		painel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
		getContentPane().add(BorderLayout.SOUTH, painel3);
		painel3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
		
		txtIp.setText("127.0.0.1");
		txtPorta.setText("5000");

		painel1.add(lblIp);
		painel1.add(txtIp);
		painel1.add(lblPorta);
		painel1.add(txtPorta);
		painel2.add(lblUsuario);
		painel2.add(txtUsuario);
		painel2.add(lblSenha);
		painel2.add(txtSenha);
		painel3.add(btnEntrar);		

		acoesBotoes();
		acoesMenu();
		menu();
	}
	

	public void menu() {
		menu.add(itemCadastro);
		menu.add(itemSair);
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	public Connection getConexao() {
		return con;
	}

	public Connection conexao() {
		try {
			// Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:C:/sqlite/Bancos/DB_CHAT.db3");
			System.out.println("conectado");
			return con;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	public void verificarUsuario(String usuario1, String senha1) throws SQLException, NumberFormatException, UnknownHostException, IOException {
		String sql = "SELECT usuario, senha, nome FROM LOGIN WHERE usuario = " + "'" + usuario1 + "'" + " AND " + "senha = "
				+ "'" + senha1 + "'";
		conexao();
		
			pst = getConexao().prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				usuario = rs.getString(1);
				senha = rs.getString(2);
				nome = rs.getString(3);
				nomeFrame = nome;
							
			}
			if ((usuario1.equals(usuario)) && (senha1.equals(senha))) {
				System.out.println("Acesso liberado!");
				Cliente cliente = new Cliente(txtIp.getText(), Integer.parseInt(txtPorta.getText()), nomeFrame);
				cliente.executa();
				dispose();
				
			}
			else {
				JOptionPane.showMessageDialog(null, "Senha ou usuário incorreto!");
				txtSenha.setBorder(new LineBorder(Color.red));
				txtUsuario.setBorder(new LineBorder(Color.red));
				txtSenha.setText("");
			}
		fecha();
	}

	public void fecha() {
		try {
			con.close();
			System.out.println("Conexão fechada...");
		} catch (SQLException e) {
			System.out.println("Erro ao fechar a conexão!");
		}
	}


	public void acoesBotoes() {
		btnEntrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					verificarUsuario(txtUsuario.getText(), txtSenha.getText());
				} catch (NumberFormatException | SQLException | IOException e1) {

				}

			}
		});
		
		txtSenha.addKeyListener(new KeyAdapter() {  
		      public void keyPressed(KeyEvent e) {  
		    	  if (e.getKeyCode() == KeyEvent.VK_ENTER) { 
		    	  try {
						verificarUsuario(txtUsuario.getText(), txtSenha.getText());
					} catch (NumberFormatException | SQLException | IOException e1) {

					} 
		    	  }
		      }  
		});
		
		
		
	}
	
	public void acoesMenu() {
		
		itemCadastro.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Cadastro();
			}
		});
		
		itemSair.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		JanelaPrincipal janela = new JanelaPrincipal();
		janela.janela();
		janela.setVisible(true);
	}
}
