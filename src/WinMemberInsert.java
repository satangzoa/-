import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPasswordField;

public class WinMemberInsert extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfID;
	private JTextField tfName;
	private JTextField tfTel;
	private JTextField tfBirthday;

	private String filePath = null;
	private JPasswordField tfPW;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinMemberInsert dialog = new WinMemberInsert();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinMemberInsert() {
		setTitle("회원 가입창");
		setBounds(100, 100, 549, 298);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblID = new JLabel("ID:");
		lblID.setBounds(169, 28, 57, 15);
		contentPanel.add(lblID);
		
		tfID = new JTextField();
		tfID.setBounds(237, 25, 116, 21);
		contentPanel.add(tfID);
		tfID.setColumns(10);
		
		JButton btnDup = new JButton("\uC911\uBCF5\uD655\uC778");
		btnDup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// DB load, connect, stmt -> select
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = 
							DriverManager.getConnection(
									"jdbc:mysql://localhost:3306/sqlDB",
									"root",
									"12345");
					Statement stmt = con.createStatement();
					//====== 이 위는 복사해서 붙여넣기 함
					String sql = "select count(*) from memberTBL ";
					sql = sql + "where id='" + tfID.getText() +"'";
					
					ResultSet rs = stmt.executeQuery(sql);
					if(rs.next()) {
						if(rs.getInt(1) > 0) {
							JOptionPane.showMessageDialog(null, "이미 ID가 존재합니다");
							tfID.requestFocus();
							tfID.setSelectionStart(0);
							tfID.setSelectionEnd(tfID.getText().length());
						}else {
							JOptionPane.showMessageDialog(null, "사용 가능합니다");
							tfPW.requestFocus();
						}
							
					}
					System.out.println(sql);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnDup.setBounds(365, 25, 97, 23);
		contentPanel.add(btnDup);
		
		JLabel lblPW = new JLabel("PW:");
		lblPW.setBounds(169, 59, 57, 15);
		contentPanel.add(lblPW);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(169, 91, 57, 15);
		contentPanel.add(lblName);
		
		tfName = new JTextField();
		tfName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					tfTel.requestFocus();
			}
		});
		tfName.setColumns(10);
		tfName.setBounds(237, 88, 116, 21);
		contentPanel.add(tfName);
		
		JLabel lblTel = new JLabel("Tel:");
		lblTel.setBounds(169, 119, 57, 15);
		contentPanel.add(lblTel);
		
		tfTel = new JTextField();
		tfTel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					tfBirthday.requestFocus();
			}
		});
		tfTel.setColumns(10);
		tfTel.setBounds(237, 116, 116, 21);
		contentPanel.add(tfTel);
		
		JLabel lblBirthday = new JLabel("Birthday");
		lblBirthday.setBounds(169, 147, 57, 15);
		contentPanel.add(lblBirthday);
		
		tfBirthday = new JTextField();
		tfBirthday.setColumns(10);
		tfBirthday.setBounds(237, 144, 116, 21);
		contentPanel.add(tfBirthday);
		
		JButton btnCalendar = new JButton("Calendar...");
		btnCalendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinCalendar winCalendar = new WinCalendar();
				winCalendar.setModal(true);
				winCalendar.setVisible(true);
			}
		});
		btnCalendar.setBounds(365, 144, 97, 23);
		contentPanel.add(btnCalendar);
		
		JLabel lblPic = new JLabel("");
		lblPic.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = 
							new FileNameExtensionFilter("이미지 파일","jpg","gif","png");
					chooser.addChoosableFileFilter(filter);
					int ret = chooser.showOpenDialog(null);
					if(ret == JFileChooser.APPROVE_OPTION) {
						filePath = chooser.getSelectedFile().getPath();
						filePath = filePath.replaceAll("\\\\", "/");
						System.out.println(filePath);
						ImageIcon image = new ImageIcon(filePath);
						Image img = image.getImage();
						img = img.getScaledInstance(120, 150, Image.SCALE_SMOOTH);
						ImageIcon pic = new ImageIcon(img);
						lblPic.setIcon(pic);
					}
				}
			}
		});
		lblPic.setToolTipText("이 곳을 더블클릭 한 후 선택하시오.");
		lblPic.setBackground(new Color(255, 255, 0));
		lblPic.setBounds(25, 23, 120, 150);
		lblPic.setOpaque(true);
		contentPanel.add(lblPic);
		
		JButton btnInsert = new JButton("가입");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = 
							DriverManager.getConnection(
									"jdbc:mysql://localhost:3306/sqlDB",
									"root",
									"12345");
					Statement stmt = con.createStatement();
					String sql = "Insert into memberTBL values('";
					sql = sql + tfID.getText()   + "','";
					sql = sql + tfPW.getPassword()   + "','";
					sql = sql + tfName.getText().replace("'", "@") + "','";
					sql = sql + tfTel.getText().replaceAll("-", "")  + "','";
					sql = sql + tfBirthday.getText() + "','";
					sql = sql + filePath + "')";
					
					//System.out.println(String.valueOf(tfPW.getPassword()));
					System.out.println(sql);
					
					if(stmt.executeUpdate(sql) > 0)
						JOptionPane.showMessageDialog(null, "회원 가입 완료!!!");
					else
						JOptionPane.showMessageDialog(null, "회원 가입 오류!!!");
					
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnInsert.setBounds(237, 191, 116, 58);
		contentPanel.add(btnInsert);
		
		tfPW = new JPasswordField();
		tfPW.setEchoChar('@');
		tfPW.setBounds(237, 56, 116, 21);
		contentPanel.add(tfPW);
	}
}
