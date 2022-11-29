import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WinInsertDiary extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfMDate;
	private JTextField tfTitle;
	private JTextField tfPW;
	JLabel lblPic;
	JButton okButton;
	String sID, sName;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinInsertDiary dialog = new WinInsertDiary("test","안녕하세요");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinInsertDiary(String strID, String strName) {//생성자 변경(인수 0개-> 2개로)
		sID = strID;
		sName = strName;
		setTitle("[" + sName + "]의 일기장");
		setBounds(100, 100, 547, 466);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblMDate = new JLabel("날짜:");
		lblMDate.setBounds(12, 30, 57, 15);
		contentPanel.add(lblMDate);
		
		tfMDate = new JTextField();
		tfMDate.setBounds(81, 27, 156, 21);
		contentPanel.add(tfMDate);
		tfMDate.setColumns(10);
		
		JButton btnCalendar = new JButton("...");
		btnCalendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinCalendar winCalendar = new WinCalendar();	
				winCalendar.setModal(true);
				winCalendar.setVisible(true);				
				tfMDate.setText(winCalendar.getDate());
			}
		});
		btnCalendar.setBounds(249, 26, 57, 23);
		contentPanel.add(btnCalendar);
		
		JLabel lblWeather = new JLabel("날씨:");
		lblWeather.setBounds(12, 70, 57, 15);
		contentPanel.add(lblWeather);
		
		JComboBox cbWeather = new JComboBox();
		cbWeather.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String imgPath = "images/";
				imgPath = imgPath + cbWeather.getSelectedItem()+".png";
				ImageIcon img = new ImageIcon(imgPath);
				Image image = img.getImage();
				image = image.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
				ImageIcon img2 = new ImageIcon(image);
				lblPic.setIcon(img2);
			}
		});
		cbWeather.setModel(new DefaultComboBoxModel(new String[] {"맑음","흐림","바람","눈","바람","번개"}));
		cbWeather.setBounds(81, 66, 116, 23);
		contentPanel.add(cbWeather);
		
		lblPic = new JLabel("");
		String imgPath = "images/맑음.png";
		ImageIcon img = new ImageIcon(imgPath);
		Image image = img.getImage();
		image = image.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
		ImageIcon img2 = new ImageIcon(image);
		lblPic.setIcon(img2);
		
		lblPic.setBackground(new Color(255, 255, 0));
		lblPic.setOpaque(true);
		lblPic.setBounds(320, 25, 64, 64);
		contentPanel.add(lblPic);
		
		JLabel lblTitle = new JLabel("제목:");
		lblTitle.setBounds(12, 102, 57, 15);
		contentPanel.add(lblTitle);
		
		tfTitle = new JTextField();
		tfTitle.setColumns(10);
		tfTitle.setBounds(81, 99, 426, 21);
		contentPanel.add(tfTitle);
		
		JLabel lblContents = new JLabel("내용:");
		lblContents.setBounds(12, 144, 57, 15);
		contentPanel.add(lblContents);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(81, 146, 426, 192);
		contentPanel.add(scrollPane);
		
		JTextArea taContents = new JTextArea();
		scrollPane.setViewportView(taContents);
		
		JLabel lblPW = new JLabel("비밀번호:");
		lblPW.setBounds(12, 363, 57, 15);
		contentPanel.add(lblPW);
		
		tfPW = new JTextField();
		tfPW.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					int length = tfPW.getText().length();
					if(length >= 4) {
						String value = 
								JOptionPane.showInputDialog("비밀번호 입력(확인)");
						if(tfPW.getText().equals(value)) {
							JOptionPane.showMessageDialog(null, "일치합니다");
							okButton.setEnabled(true);
						}						
					}
					else {
						JOptionPane.showMessageDialog(null, "4글자 이상 비밀번호 입력해야 합니다");
						tfPW.setText("");
						tfPW.requestFocus();
					}
				}
			}
		});
		tfPW.setColumns(10);
		tfPW.setBounds(81, 360, 116, 21);
		contentPanel.add(tfPW);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("완료");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							Connection con = 
									DriverManager.getConnection(
											"jdbc:mysql://localhost:3306/sqlDB",
											"root",
											"12345");
							Statement stmt = con.createStatement();
							String sql = "insert into diaryTBL values(null,'";
							sql = sql + tfMDate.getText() + "','";
							sql = sql + cbWeather.getSelectedItem() + "','images/";
							sql = sql + cbWeather.getSelectedItem()+".png','";
							sql = sql + tfTitle.getText() + "','";
							sql = sql + taContents.getText() + "','";
							sql = sql + sID + "','" + tfPW.getText()+"')"; //test 삭제 후, sID로 교체  
							//System.out.println(sql);
							if(stmt.executeUpdate(sql) > 0)
								JOptionPane.showMessageDialog(null, "정상 입력 완료");
							else
								JOptionPane.showMessageDialog(null, "오류입니다");
							
						} catch (ClassNotFoundException | SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				okButton.setEnabled(false);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				//getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("취소");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
