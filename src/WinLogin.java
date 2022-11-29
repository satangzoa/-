import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WinLogin extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfID;
	private JTextField tfPW;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinLogin dialog = new WinLogin();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinLogin() {
		setTitle("Login...");
		setBounds(100, 100, 362, 183);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblID = new JLabel("ID:");
		lblID.setBounds(29, 50, 57, 15);
		contentPanel.add(lblID);
		
		tfID = new JTextField();
		tfID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					tfPW.requestFocus();
			}
		});
		tfID.setBounds(92, 47, 116, 21);
		contentPanel.add(tfID);
		tfID.setColumns(10);
		
		tfPW = new JTextField();
		tfPW.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) 
					Go();
				
			}
		});
		tfPW.setColumns(10);
		tfPW.setBounds(92, 89, 116, 21);
		contentPanel.add(tfPW);
		
		JLabel lblPW = new JLabel("PW:");
		lblPW.setBounds(29, 92, 57, 15);
		contentPanel.add(lblPW);
		
		JButton btnLogin = new JButton("Login...");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Go();
			}
		});
		btnLogin.setBounds(220, 46, 97, 64);
		contentPanel.add(btnLogin);
	}

	protected void Go() {
		// DB 연동
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = 
					DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/sqlDB",
							"root",
							"12345");
			Statement stmt = con.createStatement();
			String sql = "SELECT name FROM logintbl ";
			sql = sql + "where id='" + tfID.getText();
			sql = sql + "' and pw='" + tfPW.getText() + "'";	
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				setVisible(false);
				String strName, strID;
				strID = tfID.getText();
				strName = rs.getString("name");
				
				WinMain winMain = new WinMain(strID,strName);
				winMain.setModal(true);
				winMain.setVisible(true);	
				System.exit(DISPOSE_ON_CLOSE);
			}
			else {
				JOptionPane.showMessageDialog(null, "ID/PW 확인 요망");
				tfID.requestFocus();
			}					
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
