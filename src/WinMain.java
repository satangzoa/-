import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WinMain extends JDialog {

	private final JPanel contentPanel = new JPanel();
	String sID, sName;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinMain dialog = new WinMain("test","홍길똥");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinMain(String strID, String strName) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		sID = strID;
		sName = strName;
		setTitle(sName + "님의 일기장");
		setBounds(100, 100, 686, 547);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Diary");
		mnNewMenu.setMnemonic('D');
		menuBar.add(mnNewMenu);
		
		JMenuItem mnInsertDiary = new JMenuItem("일기쓰기...");
		mnInsertDiary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinInsertDiary winInsertDiary = 
						new WinInsertDiary(sID,sName); // id/name 전달
				winInsertDiary.setModal(true);
				winInsertDiary.setVisible(true);
			}
		});
		mnNewMenu.add(mnInsertDiary);
		
		JMenuItem mnUpdateDiary = new JMenuItem("일기변경...");
		mnUpdateDiary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinDateSearch winDateSearch = 
						new WinDateSearch(sID, sName,"update");
				winDateSearch.setModal(true);
				winDateSearch.setVisible(true);
			}
		});
		mnNewMenu.add(mnUpdateDiary);
		
		JMenuItem mnDeleteDiary = new JMenuItem("일기삭제...");
		mnDeleteDiary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinDateSearch winDateSearch = 
						new WinDateSearch(sID, sName,"delete");
				winDateSearch.setModal(true);
				winDateSearch.setVisible(true);
			}
		});
		mnNewMenu.add(mnDeleteDiary);
	}

}
