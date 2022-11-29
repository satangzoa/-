import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WinCalendar extends JDialog {

	private final JPanel contentPanel = new JPanel();
	JPanel panelCalendar;
	JComboBox cbYear;
	JComboBox cbMonth;
	private JButton btnNextMonth;
	private JButton btnPrevMonth;
	private JButton btnPrevYear;
	private JButton btnNextYear;
	String retDate;
	
	public String getDate() {
		return retDate;
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinCalendar dialog = new WinCalendar();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinCalendar() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				// 기존 달력 지우고
				Component compList[] = panelCalendar.getComponents();
				for(Component c : compList)
					if(c instanceof JButton)
						panelCalendar.remove(c);
				panelCalendar.revalidate();
				panelCalendar.repaint();
				
				// 해당하는 년/월 달력을 보인다
				String yoil[]= {"일","월","화","수","목","금","토"};
				int Months[] = {31,28,31,30,31,30,31,31,30,31,30,31};
				int year = (int)cbYear.getSelectedItem();
				int month = (int)cbMonth.getSelectedItem();
				for(int i=0;i<7;i++) {
					JButton btn = new JButton(yoil[i] + "요일");
					btn.setBackground(Color.YELLOW);
					panelCalendar.add(btn);
				}				
				retDate = year + "-" + month + "-" + 1234567; // test용				
				// 빈 버튼을 추가 1922년 1월 1일 일요일(공백)
				int index = 0;
				int sum = 0;
				
				for(int i=1922;i<year;i++) //1922년부터 이전 해까지의 합
					if(i%4==0 && i%100!=0 || i%400==0)
						sum = sum + 366;
					else
						sum = sum + 365;
				
				int lastDay = 0; 
				for(int i=0; i<month-1;i++)  // 1월부터 이전 달까지의 합
					if(i==1 && (year%4==0 && year%100!=0 || year%400==0)) 
						sum = sum + ++Months[i];
					else 
						sum = sum + Months[i];						
				if(month==2 && (year%4==0 && year%100!=0 || year%400==0)) 
					lastDay = ++Months[month-1];
				else
					lastDay = Months[month-1];
				
				index = (index + sum) % 7;						
				for(int i=1;i<=index;i++) {
					JButton btn = new JButton("");
					panelCalendar.add(btn);		
					btn.setVisible(false);
				}						
				
				// 해당 월의 1일부터 마지막 날짜까지 버튼 생성
				for(int i=1;i<=lastDay;i++) {
					JButton btn = new JButton(Integer.toString(i));
					panelCalendar.add(btn);	
					
					btn.addActionListener(new ActionListener() {								
						@Override
						public void actionPerformed(ActionEvent e) {
							JButton btn1 = (JButton)e.getSource();
							retDate = cbYear.getSelectedItem()+"-";
							retDate = retDate + cbMonth.getSelectedItem()+"-";
							retDate = retDate + btn1.getText();
							setVisible(false);
						}
					});			
				}
				panelCalendar.revalidate();
			}
		});
		setTitle("Calendar");
		setBounds(100, 100, 617, 385);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				cbYear = new JComboBox();
				cbYear.setEditable(true);

				for(int i=1922;i<=2100;i++)
					cbYear.addItem(i);				
				Calendar now = Calendar.getInstance();
				{
					btnPrevMonth = new JButton("<");
					btnPrevMonth.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int year = (int)cbYear.getSelectedItem();
							int month = (int)cbMonth.getSelectedItem();
							if(month==1) {
								year = year - 1;
								month = 12;
							}else
								month--;
							cbYear.setSelectedItem(year);
							cbMonth.setSelectedItem(month);
						}
					});
					{
						btnPrevYear = new JButton("<<");
						btnPrevYear.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								int year = (int)cbYear.getSelectedItem();
								int month = (int)cbMonth.getSelectedItem();
								if(year==1922) 
									year = 1922;
								else
									year--;
								cbYear.setSelectedItem(year);
								cbMonth.setSelectedItem(month);
							}
						});
						panel.add(btnPrevYear);
					}
					panel.add(btnPrevMonth);
				}
				cbYear.setSelectedItem(now.get(Calendar.YEAR));				
				panel.add(cbYear);
				cbYear.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						// 기존 달력 지우고
						Component compList[] = panelCalendar.getComponents();
						for(Component c : compList)
							if(c instanceof JButton)
								panelCalendar.remove(c);
						panelCalendar.revalidate();
						panelCalendar.repaint();
						
						// 해당하는 년/월 달력을 보인다
						String yoil[]= {"일","월","화","수","목","금","토"};
						int Months[] = {31,28,31,30,31,30,31,31,30,31,30,31};
						int year = (int)cbYear.getSelectedItem();
						int month = (int)cbMonth.getSelectedItem();
						for(int i=0;i<7;i++) {
							JButton btn = new JButton(yoil[i] + "요일");
							btn.setBackground(Color.YELLOW);
							panelCalendar.add(btn);
						}
						// 빈 버튼을 추가 1922년 1월 1일 일요일(공백)
						int index = 0;
						int sum = 0;
						
						for(int i=1922;i<year;i++) //1922년부터 이전 해까지의 합
							if(i%4==0 && i%100!=0 || i%400==0)
								sum = sum + 366;
							else
								sum = sum + 365;
						
						int lastDay = 0; 
						for(int i=0; i<month-1;i++)  // 1월부터 이전 달까지의 합
							if(i==1 && (year%4==0 && year%100!=0 || year%400==0)) 
								sum = sum + ++Months[i];
							else 
								sum = sum + Months[i];						
						if(month==2 && (year%4==0 && year%100!=0 || year%400==0)) 
							lastDay = ++Months[month-1];
						else
							lastDay = Months[month-1];
						
						index = (index + sum) % 7;						
						for(int i=1;i<=index;i++) {
							JButton btn = new JButton("");
							panelCalendar.add(btn);		
							btn.setVisible(false);
						}						
						
						// 해당 월의 1일부터 마지막 날짜까지 버튼 생성
						for(int i=1;i<=lastDay;i++) {
							JButton btn = new JButton(Integer.toString(i));
							panelCalendar.add(btn);	
							
							btn.addActionListener(new ActionListener() {								
								@Override
								public void actionPerformed(ActionEvent e) {
									JButton btn1 = (JButton)e.getSource();
									retDate = cbYear.getSelectedItem()+"-";
									retDate = retDate + cbMonth.getSelectedItem()+"-";
									retDate = retDate + btn1.getText();
									setVisible(false);
								}
							});			
						}
						panelCalendar.revalidate();
					}
				});
			}
			{
				cbMonth = new JComboBox();	
				cbMonth.setEditable(true);
				for(int i=1;i<=12;i++)
					cbMonth.addItem(i);				
				Calendar now = Calendar.getInstance();
				cbMonth.setSelectedItem(now.get(Calendar.MONTH)+1);				
				panel.add(cbMonth);
				{
					btnNextMonth = new JButton(">");
					btnNextMonth.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int year = (int)cbYear.getSelectedItem();
							int month = (int)cbMonth.getSelectedItem();
							if(month==12) {
								year = year + 1;
								month = 1;
							}else
								month++;
							cbYear.setSelectedItem(year);
							cbMonth.setSelectedItem(month);
						}
					});
					panel.add(btnNextMonth);
				}
				{
					btnNextYear = new JButton(">>");
					btnNextYear.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							int year = (int)cbYear.getSelectedItem();
							int month = (int)cbMonth.getSelectedItem();
							if(year==2100) 
								year = 2100;
							else
								year++;
							cbYear.setSelectedItem(year);
							cbMonth.setSelectedItem(month);
						}
					});
					panel.add(btnNextYear);
				}
				
				cbMonth.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// 기존 달력 지우고
						Component compList[] = panelCalendar.getComponents();
						for(Component c : compList)
							if(c instanceof JButton)
								panelCalendar.remove(c);
						panelCalendar.revalidate();
						panelCalendar.repaint();
						
						// 해당하는 년/월 달력을 보인다
						String yoil[]= {"일","월","화","수","목","금","토"};
						int Months[] = {31,28,31,30,31,30,31,31,30,31,30,31};
						int year = (int)cbYear.getSelectedItem();
						int month = (int)cbMonth.getSelectedItem();
						for(int i=0;i<7;i++) {
							JButton btn = new JButton(yoil[i] + "요일");
							btn.setBackground(Color.YELLOW);
							panelCalendar.add(btn);
						}
						// 빈 버튼을 추가 1922년 1월 1일 일요일(공백)
						int index = 0;
						int sum = 0;
						
						for(int i=1922;i<year;i++) //1922년부터 이전 해까지의 합
							if(i%4==0 && i%100!=0 || i%400==0)
								sum = sum + 366;
							else
								sum = sum + 365;
						
						int lastDay = 0; 
						for(int i=0; i<month-1;i++)  // 1월부터 이전 달까지의 합
							if(i==1 && (year%4==0 && year%100!=0 || year%400==0)) 
								sum = sum + ++Months[i];
							else 
								sum = sum + Months[i];						
						if(month==2 && (year%4==0 && year%100!=0 || year%400==0)) 
							lastDay = ++Months[month-1];
						else
							lastDay = Months[month-1];
						
						index = (index + sum) % 7;						
						for(int i=1;i<=index;i++) {
							JButton btn = new JButton("");
							panelCalendar.add(btn);		
							btn.setVisible(false);
						}						
						
						// 해당 월의 1일부터 마지막 날짜까지 버튼 생성
						for(int i=1;i<=lastDay;i++) {
							JButton btn = new JButton(Integer.toString(i));
							panelCalendar.add(btn);	
							
							btn.addActionListener(new ActionListener() {								
								@Override
								public void actionPerformed(ActionEvent e) {
									JButton btn1 = (JButton)e.getSource();
									retDate = cbYear.getSelectedItem()+"-";
									retDate = retDate + cbMonth.getSelectedItem()+"-";
									retDate = retDate + btn1.getText();
									setVisible(false);
								}
							});							
						}
						panelCalendar.revalidate();
					}
				});
			}
		}
		{
			panelCalendar = new JPanel();
			contentPanel.add(panelCalendar, BorderLayout.CENTER);
			panelCalendar.setLayout(new GridLayout(0, 7, 0, 0));
		}
	}

}
