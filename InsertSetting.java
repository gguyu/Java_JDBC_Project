package test3;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InsertSetting {

	private JFrame frame;
	private JTextField textField_insertFname;
	private JTextField textField_insertMinit;
	private JTextField textField_insertLname;
	private JTextField textField_insertSsn;
	private JTextField textField_insertBdate;
	private JTextField textField_insertAddress;
	private JTextField textField_insertSalary;
	private JTextField textField_insertSup_ssn;
	private JTextField textField_insertDno;
	
	private String insertQuery = "insert into EMPLOYEE value("; // insert �� ����, �ȿ� �� �ְ� �������� sql �������� ')' �����ֱ�
	private String insertedSex = "M";  // combobox�� default ���� M�̹Ƿ�

	/**
	 * Launch the application.
	 */
	
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InsertSetting window = new InsertSetting();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	
	public void launch() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InsertSetting window = new InsertSetting();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	/**
	 * Create the application.
	 */
	public InsertSetting() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 539, 397);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  <-- �̰� ������ â������ ���α׷��� ����Ǽ� â�ݾƵ� ���� â�� �����ǰ� �Ϸ��� �� ���� ����ߵ�
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 523, 358);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\uC0C8\uB85C\uC6B4 \uC9C1\uC6D0 \uC815\uBCF4 \uCD94\uAC00");
		lblNewLabel.setFont(new Font("����", Font.BOLD, 15));
		lblNewLabel.setBounds(180, 0, 159, 30);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("First Name:");
		lblNewLabel_1.setBounds(12, 49, 76, 15);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Middle Init.:");
		lblNewLabel_1_1.setBounds(12, 74, 76, 15);
		panel.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("Last Name:");
		lblNewLabel_1_2.setBounds(12, 99, 76, 15);
		panel.add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("Ssn:");
		lblNewLabel_1_3.setBounds(12, 124, 76, 15);
		panel.add(lblNewLabel_1_3);
		
		JLabel lblNewLabel_1_4 = new JLabel("Birthdate:");
		lblNewLabel_1_4.setBounds(12, 149, 76, 15);
		panel.add(lblNewLabel_1_4);
		
		JLabel lblNewLabel_1_5 = new JLabel("Address:");
		lblNewLabel_1_5.setBounds(12, 174, 76, 15);
		panel.add(lblNewLabel_1_5);
		
		JLabel lblNewLabel_1_6 = new JLabel("Sex:");
		lblNewLabel_1_6.setBounds(12, 199, 76, 15);
		panel.add(lblNewLabel_1_6);
		
		JLabel lblNewLabel_1_7 = new JLabel("Salary:");
		lblNewLabel_1_7.setBounds(12, 224, 76, 15);
		panel.add(lblNewLabel_1_7);
		
		JLabel lblNewLabel_1_8 = new JLabel("Super_ssn:");
		lblNewLabel_1_8.setBounds(12, 249, 76, 15);
		panel.add(lblNewLabel_1_8);
		
		JLabel lblNewLabel_1_9 = new JLabel("Dno:");
		lblNewLabel_1_9.setBounds(12, 274, 76, 15);
		panel.add(lblNewLabel_1_9);
		
		
		// �Է�â
		
		textField_insertFname = new JTextField();
		textField_insertFname.setBounds(87, 46, 356, 21);
		panel.add(textField_insertFname);
		textField_insertFname.setColumns(10);
		
		textField_insertMinit = new JTextField();
		textField_insertMinit.setColumns(10);
		textField_insertMinit.setBounds(87, 71, 356, 21);
		panel.add(textField_insertMinit);
		
		textField_insertLname = new JTextField();
		textField_insertLname.setColumns(10);
		textField_insertLname.setBounds(87, 96, 356, 21);
		panel.add(textField_insertLname);
		
		textField_insertSsn = new JTextField();
		textField_insertSsn.setColumns(10);
		textField_insertSsn.setBounds(87, 121, 356, 21);
		panel.add(textField_insertSsn);
		
		textField_insertBdate = new JTextField();
		textField_insertBdate.setColumns(10);
		textField_insertBdate.setBounds(87, 146, 356, 21);
		panel.add(textField_insertBdate);
		
		textField_insertAddress = new JTextField();
		textField_insertAddress.setColumns(10);
		textField_insertAddress.setBounds(87, 171, 356, 21);
		panel.add(textField_insertAddress);
		
		textField_insertSalary = new JTextField();
		textField_insertSalary.setColumns(10);
		textField_insertSalary.setBounds(87, 221, 356, 21);
		panel.add(textField_insertSalary);
		
		textField_insertSup_ssn = new JTextField();
		textField_insertSup_ssn.setColumns(10);
		textField_insertSup_ssn.setBounds(87, 246, 356, 21);
		panel.add(textField_insertSup_ssn);
		
		textField_insertDno = new JTextField();
		textField_insertDno.setColumns(10);
		textField_insertDno.setBounds(87, 271, 356, 21);
		panel.add(textField_insertDno);
		
		JComboBox comboBox_insertSex = new JComboBox();
		comboBox_insertSex.setModel(new DefaultComboBoxModel(new String[] {"M", "F"}));
		comboBox_insertSex.setBounds(87, 195, 356, 23);
		panel.add(comboBox_insertSex); 
		
		comboBox_insertSex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox_insertSex.getSelectedItem() == "M") {
					insertedSex = "M";
				} else if (comboBox_insertSex.getSelectedItem() =="F") {
					insertedSex = "F";
				}
			}
		});
		
		
		// ������ �߰��ϱ� ��ư (������ ���� �޾ƿͼ� insertQuery ����� ������ �߰�)
		JButton btnInsertButton = new JButton("������ �߰��ϱ�");
		btnInsertButton.setBounds(199, 313, 121, 23);
		panel.add(btnInsertButton);
		
		btnInsertButton.addMouseListener(new MouseAdapter() {
			String emptyText = "";  	// textfield �� ����ִ��� �� �뵵
			String spaceWord = ", ";	// insertQuery �� �߰��ϱ� ���� �� ĭ ���� �ؾ߉�.
			String stringWord = "'"; 	// insertQuery �� string �����ϸ� ' �� �� ���� �ٿ������.
			String endQuery = ")"; 		// insert �� �������� value �� ��ȣ�� �ݾ������
			@Override
			public void mouseClicked(MouseEvent e) {
				// textField.getText() �� textField�� empty �̸� "" ��ȯ����
				// Fname (not null)
				if (textField_insertFname.getText().equals(emptyText)) {
					// �����߻� , â���� ���� x
					System.out.println("Fname has not null constraints");
				}
				insertQuery = insertQuery + stringWord + textField_insertFname.getText() + stringWord;
				
				// Minit
				insertQuery = insertQuery + spaceWord + stringWord + textField_insertMinit.getText() + stringWord;
				
				// Lname (not null)
				if (textField_insertLname.getText().equals(emptyText)) {
					// �����߻� , â���� ���� x
					System.out.println("Lname has not null constraints");
				}
				insertQuery = insertQuery + spaceWord + stringWord + textField_insertLname.getText() + stringWord;
				
				// Ssn (not null, primary key)
				if (textField_insertSsn.getText().equals(emptyText)) {
					// �����߻� , â���� ���� x
					System.out.println("Ssn has not null constraints (primary key)");
				}
				insertQuery = insertQuery + spaceWord + stringWord + textField_insertSsn.getText() + stringWord;
				
				// Bdate
				insertQuery = insertQuery + spaceWord + stringWord + textField_insertBdate.getText() + stringWord;
				
				// Address
				insertQuery = insertQuery + spaceWord + stringWord + textField_insertAddress.getText() + stringWord;
				
				// Sex
				insertQuery = insertQuery + spaceWord + stringWord + insertedSex + stringWord;
				
				// Salary
				insertQuery = insertQuery + spaceWord + textField_insertSalary.getText();
				
				// Super_ssn
				insertQuery = insertQuery + spaceWord + stringWord + textField_insertSup_ssn.getText() + stringWord;
				
				// Dno (not null default 1)
				insertQuery = insertQuery + spaceWord + textField_insertDno.getText() + endQuery;
				
				// test
				System.out.println(insertQuery);
				
				// CompanyDB ��ü �����ؼ� �����ڷ� insertQuery �Ѱ��༭ ���� ���� �����ϱ�
				CompanyDB companyDB_insert = new CompanyDB(insertQuery);
				companyDB_insert.insertDB();
				
				// ������ �ʱ�ȭ
				insertQuery = "insert into EMPLOYEE value(";
				
			
			}
		});
		
		
		
		// insertQuery   <---- insert �� ������ string ���� ���� class ������ �����س���
		
		
		
		
		
		
		
		
		
	}  // initialize �Լ� ��
}
