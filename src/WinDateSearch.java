import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WinDateSearch extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfDate;
	String sID, sName;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinDateSearch dialog = new WinDateSearch("","","");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinDateSearch(String strID, String strName, String strType) {
		sID = strID;
		sName = strName;
		setTitle("날짜 선택");
		setBounds(100, 100, 344, 88);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblDate = new JLabel("날짜:");
			contentPanel.add(lblDate);
		}
		{
			tfDate = new JTextField();
			contentPanel.add(tfDate);
			tfDate.setColumns(10);
		}
		{
			JButton btnCalendar = new JButton("...");
			btnCalendar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WinCalendar winCalendar = new WinCalendar();	
					winCalendar.setModal(true);
					winCalendar.setVisible(true);				
					tfDate.setText(winCalendar.getDate());
				}
			});
			contentPanel.add(btnCalendar);
		}
		{
			JButton btnChoice = new JButton("선택");
			btnChoice.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(strType.equals("update")) {
						WinUpDateDiary winUpdateDiary = 
								new WinUpDateDiary(sID, sName, tfDate.getText());
						winUpdateDiary.setModal(true);
						winUpdateDiary.setVisible(true);
					}else {  //delete
						WinDeleteDiary winDeleteDiary = 
								new WinDeleteDiary(sID, sName, tfDate.getText());
						winDeleteDiary.setModal(true);
						winDeleteDiary.setVisible(true);
					}
				}
			});
			contentPanel.add(btnChoice);
		}
	}

}
