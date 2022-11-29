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

public class WinUpDateDiary extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTextField tfMDate;
	private JTextField tfTitle;
	private JTextField tfPW;
	JLabel lblPic;
	JButton okButton;
	String sID, sName, sDate;
	String sWeather, sTitle, sContents, sCurPW;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinUpDateDiary dialog = new WinUpDateDiary("test","안녕하세요","");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinUpDateDiary(String strID, String strName, String strDate) {//생성자 변경(인수 0개-> 2개로)
		sID = strID;
		sName = strName;
		sDate = strDate;
		
		// 함수 호출하여 값 얻어오기
		ShowDialog(sID, sDate);
		
		setTitle("[" + sName + "]의 일기장 변경");
		setBounds(100, 100, 547, 466);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblMDate = new JLabel("날짜:");
		lblMDate.setBounds(12, 30, 57, 15);
		contentPanel.add(lblMDate);
		
		tfMDate = new JTextField(strDate);
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
		
		lblPic.setBackground(new Color(255, 255, 0));
		lblPic.setOpaque(true);
		lblPic.setBounds(320, 25, 64, 64);
		contentPanel.add(lblPic);
		
		cbWeather.setSelectedItem(sWeather);
		
		JLabel lblTitle = new JLabel("제목:");
		lblTitle.setBounds(12, 102, 57, 15);
		contentPanel.add(lblTitle);
		
		tfTitle = new JTextField(sTitle);
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
		
		JTextArea taContents = new JTextArea(sContents);
		scrollPane.setViewportView(taContents);
		
		JLabel lblPW = new JLabel("비밀번호:");
		lblPW.setBounds(12, 363, 57, 15);
		contentPanel.add(lblPW);
		
		tfPW = new JTextField();
		tfPW.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					if(tfPW.getText().equals(sCurPW))
						okButton.setEnabled(true);
					else
						JOptionPane.showMessageDialog(null, "비밀번호 오류");
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
							String sql = "update diaryTBL set ";
							sql = sql + "mDate='" + tfMDate.getText()+"',";
							sql = sql + "weather='" + cbWeather.getSelectedItem()+"',";
							sql = sql + "title='" + tfTitle.getText()+"',";
							sql = sql + "contents='" + taContents.getText()+"' ";
							sql = sql + "where id='" + sID + "' and ";
							sql = sql + "mDate='" + sDate + "'";							
							System.out.println(sql);
							if(stmt.executeUpdate(sql) > 0)
								JOptionPane.showMessageDialog(null, "정상 변경 완료");
							else
								JOptionPane.showMessageDialog(null, "변경 오류");
							
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

	private void ShowDialog(String strID, String strDate) {
		// DB 연동
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = 
					DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/sqlDB",
							"root",
							"12345");
			Statement stmt = con.createStatement();
			String sql = "select * from diaryTBL ";
			sql = sql + "where id='"+ strID + "' and mDate='";
			sql = sql + strDate + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				sWeather = rs.getString("weather");
				sTitle = rs.getString("title");
				sContents = rs.getString("contents");
				sCurPW = rs.getString("curpw");				
			}else{
				JOptionPane.showMessageDialog(null, "일기 없음");
			}
						
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
}
