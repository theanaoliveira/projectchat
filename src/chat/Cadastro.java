package chat;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Cadastro extends JFrame {

	private static Connection con = null;
	private PreparedStatement pst;
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu = new JMenu("Menu");
	private JMenuItem itemSair = new JMenuItem("Sair");

	private JLabel lblCadastro = new JLabel("CADASTRO DE USUÁRIOS");
	private JLabel lblNome = new JLabel("Nome.....................:");
	private JTextField txtNome = new JTextField(12);
	private JLabel lblSobrenome = new JLabel("Sobrenome..........:");
	private JTextField txtSobrenome = new JTextField(12);
	private JLabel lblUsuario = new JLabel("Usuário.................: ");
	private JTextField txtUsuario = new JTextField(10);
	private JLabel lblEmail = new JLabel("E-mail....................:");
	private JTextField txtEmail = new JTextField(12);
	private JLabel lblSenha = new JLabel("Senha...................:");
	private JPasswordField txtSenha = new JPasswordField(10);
	private JLabel lblCSenha = new JLabel("Confirmar Senha:");
	private JPasswordField txtCSenha = new JPasswordField(10);
	private ImageIcon iconeCancelar = new ImageIcon("imagens/iconeCancelar.png");
	private JButton btnCancelar = new JButton(iconeCancelar);
	private ImageIcon iconeCadastro = new ImageIcon("imagens/iconeConfirmar.png");
	private JButton btnCadastro = new JButton(iconeCadastro);

	private Font fonte1 = new Font("Arial", Font.BOLD, 18);

	public Cadastro() {
		select();
		menu();
		janela();
		acoesMenu();
		acoesBotoes();
		setVisible(true);
	}

	public Connection getConexao() {
		return con;
	}

	public static Connection conexao() {
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:C:/sqlite/Bancos/DB_CHAT.db3");
			System.out.println("conectado");
			return con;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}

	public void inserir(String nome, String sobrenome, String email, String usuario, String senha) {
		String sql = "INSERT INTO LOGIN(USUARIO, SENHA, NOME, SOBRENOME, EMAIL) VALUES (?,?,?,?,?)";
		conexao();
		try {
			pst = getConexao().prepareStatement(sql);
			pst.setString(1, usuario);
			pst.setString(2, senha);
			pst.setString(3, nome);
			pst.setString(4, sobrenome);
			pst.setString(5, email);
			pst.executeUpdate();
			// System.out.println("Dados inseridos");
			JOptionPane.showMessageDialog(null, "Usuário cadastrado com exito!");
			dispose();
		} catch (Exception e) {
			System.out.println();
			JOptionPane.showMessageDialog(null, "Usuário já existe");
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

	public void select() {
		String sql = "SELECT * FROM LOGIN";
		conexao();

		try {
			pst = getConexao().prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				System.out.println("USUARIO: " + rs.getString(1) + " |" + " SENHA: " + rs.getString(2) + " |"
						+ " NOME: " + rs.getString(3) + " |" + " SOBRENOME: " + rs.getString(4) + " |" + " EMAIL: "
						+ rs.getString(5));

			}
		} catch (SQLException e) {
			System.out.println("Erro ao se conectar: " + e.getMessage());
		}

		fecha();
	}

	public void excluir(String senha) {
		String sql = "DELETE FROM LOGIN WHERE USUARIO = " + " " + senha;
		conexao();
		try {
			pst = getConexao().prepareStatement(sql);
			pst.executeUpdate();
			System.out.println("Item excluido...");
		} catch (Exception e) {
			System.out.println("Erro ao deletar: " + e.getMessage());
		}
		fecha();
	}

	public void janela() {
		setTitle("CADASTRO DE USUÁRIO");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(330, 365);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);

		lblCadastro.setBorder(new LineBorder(Color.BLACK));

		int x = 0;
		int y = 0;

		lblCadastro.setBounds(x + 45, y + 20, 235, 28);
		lblNome.setBounds(x + 10, y + 95, 140, 10);
		txtNome.setBounds(x + 115, y + 90, 200, 20);
		lblSobrenome.setBounds(x + 10, y + 125, 140, 10);
		txtSobrenome.setBounds(x + 115, y + 120, 200, 20);
		lblEmail.setBounds(x + 10, y + 155, 140, 10);
		txtEmail.setBounds(x + 115, y + 150, 200, 20);
		lblUsuario.setBounds(x + 10, y + 185, 140, 10);
		txtUsuario.setBounds(x + 115, y + 180, 200, 20);
		lblSenha.setBounds(x + 10, y + 215, 140, 10);
		txtSenha.setBounds(x + 115, y + 210, 200, 20);
		lblCSenha.setBounds(x + 10, y + 245, 140, 10);
		txtCSenha.setBounds(x + 115, y + 240, 200, 20);
		btnCancelar.setBounds(x + 240, y + 270, 35, 35);
		btnCadastro.setBounds(x + 280, y + 270, 35, 35);

		lblCadastro.setFont(fonte1);

		add(lblCadastro);
		add(lblNome);
		add(txtNome);
		add(lblSobrenome);
		add(txtSobrenome);
		add(lblEmail);
		add(txtEmail);
		add(lblUsuario);
		add(txtUsuario);
		add(lblSenha);
		add(txtSenha);
		add(lblCSenha);
		add(txtCSenha);
		add(btnCancelar);
		add(btnCadastro);
	}
	
	public void menu() {
		menu.add(itemSair);
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	public void acoesMenu() {
		itemSair.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	public void acoesBotoes() {
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

		btnCadastro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if ((!(txtNome.getText().equals("") || txtSobrenome.getText().equals("")
						|| txtEmail.getText().equals("") || txtUsuario.getText().equals("")
						|| txtSenha.getText().equals("") || txtCSenha.getText().equals("")))) {

					int nSenha = txtSenha.getText().length();
					int nCSenha = txtCSenha.getText().length();

					if ((nSenha > 5) || (nCSenha > 5)) {

						if (txtSenha.getText().equals(txtCSenha.getText())) {
							inserir(txtNome.getText(), txtSobrenome.getText(), txtEmail.getText(), txtUsuario.getText(),
									txtSenha.getText());
							// txtNome.setText("");
							// txtSobrenome.setText("");
							// txtEmail.setText("");
							txtUsuario.setText("");
							txtSenha.setText("");
							txtCSenha.setText("");
						} else {
							JOptionPane.showMessageDialog(null, "Erro|Senha");
							txtSenha.setText("");
							txtCSenha.setText("");
							txtSenha.setBorder(new LineBorder(Color.red));
							txtCSenha.setBorder(new LineBorder(Color.red));
						}

					} else {
						JOptionPane.showMessageDialog(null, "A senha precisa ter mais de 5 caracteres");
					}

				} else {
					JOptionPane.showMessageDialog(null, "Preencha os campos para efetuar o Cadastro!");
				}

			}
		});
	}

	public static void main(String[] args) {
		new Cadastro();
	}
}
