package test3;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

import java.util.regex.Pattern;

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
	private HashSet<String> ssnResult = CompanyDB.ssnResult;
	
	private String insertQuery = "insert into EMPLOYEE value("; // insert 문 저장, 안에 다 넣고 마지막에 sql 실행전에 ')' 더해주기
	private String insertedSex = "M";  // combobox의 default 값이 M이므로
	
	private String password;

	/**
	 * Launch the application.
	 */
	
	
	public void launch() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InsertSetting window = new InsertSetting(password);
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
	public InsertSetting(String password) {
		this.password = password;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 480, 397);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  <-- 이게 있으면 창닫으면 프로그램이 종료되서 창닫아도 기존 창은 유지되게 하려면 이 줄이 없어야됨
		frame.getContentPane().setLayout(null);
        
        frame.setLocationRelativeTo(null); // 가운데 뜨기
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 523, 358);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\uC0C8\uB85C\uC6B4 \uC9C1\uC6D0 \uC815\uBCF4 \uCD94\uAC00");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 15));
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
		
		
		// 입력창
		
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
		
		
		// 데이터 추가하기 버튼 (누르면 값들 받아와서 insertQuery 만들고 데이터 추가)
		JButton btnInsertButton = new JButton("데이터 추가하기");
		btnInsertButton.setBounds(199, 313, 140, 23);
		panel.add(btnInsertButton);
		
		btnInsertButton.addMouseListener(new MouseAdapter() {
			String emptyText = "";  	// textfield 가 비어있는지 비교 용도
			String spaceWord = ", ";	// insertQuery 에 추가하기 전에 한 칸 띄어쓰기 해야.
			String stringWord = "'"; 	// insertQuery 에 string 삽입하면 ' 를 양 옆에 붙여줘야함.
			String endQuery = ")"; 		// insert 문 마지막은 value 의 괄호를 닫아줘야함
			String nullText = "NULL";   // 값을 따로 입력안하면 NULL 로 집어넣음
            
            String eString = "";        // 에러 메시지 출력용
            
			@Override
			public void mouseClicked(MouseEvent e) {
				// textField.getText() 는 textField가 empty 이면 "" 반환해줌
				// Fname (not null)
				
				if (textField_insertFname.getText().equals(emptyText)) {
					// 에러발생 , 창띄우고 실행 x
					insertQuery = insertQuery + nullText; // 이름에 null을 넣어 sqlexception 트리거 용
				} else {
					insertQuery = insertQuery + stringWord + textField_insertFname.getText() + stringWord;
				}
				
				// Minit
				if (textField_insertMinit.getText().equals(emptyText)) {
					insertQuery = insertQuery + spaceWord + nullText; 
				} else {
					insertQuery = insertQuery + spaceWord + stringWord + textField_insertMinit.getText() + stringWord;
				}
				
				// Lname (not null)
				if (textField_insertLname.getText().equals(emptyText)) {
					insertQuery = insertQuery + spaceWord + nullText; // 구름// 이름에 null을 넣어 sqlexception 트리거 용
					
				} else {
					insertQuery = insertQuery + spaceWord + stringWord + textField_insertLname.getText() + stringWord;
				}
				
				// Ssn (not null, primary key)
				if(textField_insertSsn.getText().toString().length() == 9) {
					if (textField_insertSsn.getText().equals(emptyText)) {
						insertQuery = insertQuery + spaceWord + nullText;
					} else {
						if(ssnResult != null) {
							if(!ssnResult.contains(textField_insertSsn.getText())) {
								insertQuery = insertQuery + spaceWord + stringWord + textField_insertSsn.getText() + stringWord;
							}
						}else {
							insertQuery = insertQuery + spaceWord + stringWord + textField_insertSsn.getText() + stringWord;
						}
					}
				}
				
				
				// Bdate
				if (textField_insertBdate.getText().equals(emptyText)) {
					insertQuery = insertQuery + spaceWord + nullText;
				} else {
					insertQuery = insertQuery + spaceWord + stringWord + textField_insertBdate.getText() + stringWord;
				}
				
				// Address
				if (textField_insertAddress.getText().equals(emptyText)) {
					insertQuery = insertQuery + spaceWord + nullText;
				} else {
					insertQuery = insertQuery + spaceWord + stringWord + textField_insertAddress.getText() + stringWord;
				}
				
				// Sex
				insertQuery = insertQuery + spaceWord + stringWord + insertedSex + stringWord;
				
				// Salary
				if (textField_insertSalary.getText().equals(emptyText)) {
					insertQuery = insertQuery + spaceWord + nullText;
				} else {
					insertQuery = insertQuery + spaceWord + textField_insertSalary.getText();
				}
				
				// Super_ssn
				if (textField_insertSup_ssn.getText().equals(emptyText)) {
					insertQuery = insertQuery + spaceWord + nullText;
				} else {
					insertQuery = insertQuery + spaceWord + stringWord + textField_insertSup_ssn.getText() + stringWord;
				}
				
				// Dno (not null default 1)
				if (textField_insertDno.getText().equals(emptyText)) {
                   insertQuery = insertQuery + spaceWord + "1" + endQuery;
					
				} else {
					insertQuery = insertQuery + spaceWord + textField_insertDno.getText() + endQuery;
				}
				
				// test
				System.out.println(insertQuery);
				
				/*
				// CompanyDB 객체 생성해서 생성자로 insertQuery 넘겨줘서 삽입 쿼리 실행하기
				CompanyDB companyDB_insert = new CompanyDB(insertQuery, password);
				companyDB_insert.insertDB(eString);
				*/
				
				// 예외처리
				ssnResult = CompanyDB.ssnResult;
				// 작동 O
				if(textField_insertFname.getText().equals(emptyText)) {
					JOptionPane.showMessageDialog(frame, "필수 입력항목입니다.(First Name)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Fname not null
					
				} 
				// 작동 O
				else if(!Pattern.matches("^[a-zA-Z|가-힣]*$", textField_insertFname.getText())) {
					JOptionPane.showMessageDialog(frame, "한글이나 영어로된 이름을 입력하세요.(First Name)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Fname은 한글이나 영어만 가능
					
				} 
				// 작동 O
				else if(textField_insertFname.getText().length() > 15) {
					JOptionPane.showMessageDialog(frame, "이름이 너무 깁니다. 15자 이하로 입력하세요.(First Name)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Fname 은 VARCHAR(15)
					
				}
				// 작동 O
				else if (!textField_insertMinit.getText().equals(emptyText) && !Pattern.matches("^[a-z|A-Z|가-힣]*$", textField_insertMinit.getText())) {
					JOptionPane.showMessageDialog(frame, "한글이나 영어로된 이름을 입력하세요.(Minit)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Minit은 한글이나 영어만 가능
					
				} 
				// 작동 O
				else if(textField_insertMinit.getText().length() > 1){
					JOptionPane.showMessageDialog(frame, "한 글자만 입력하세요.(Minit)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Minit은 CHAR
					
				} 
				// 작동 O
				else if(textField_insertLname.getText().equals(emptyText)) {
					JOptionPane.showMessageDialog(frame, "필수 입력항목입니다.(Last Name)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Lname not null
					
				} 
				// 작동 O
				else if(!Pattern.matches("^[a-z|A-Z|가-힣]*$", textField_insertLname.getText())) {
					JOptionPane.showMessageDialog(frame, "한글이나 영어로된 이름을 입력하세요.(Last Name)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Lname은 한글이나 영어만 가능
					
				}
				// 작동 O
				else if(textField_insertLname.getText().length() > 15) {
					JOptionPane.showMessageDialog(frame, "이름이 너무 깁니다. 15자 이하로 입력하세요.(Last Name)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Lname 은 VARCHAR(15)
					
				}
				// 작동 O
				else if(textField_insertSsn.getText().equals(emptyText)) {
					JOptionPane.showMessageDialog(frame, "필수 입력항목입니다.(ssn)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Ssn not null
					
				} 
				// 작동 O
				else if(!Pattern.matches("^[0-9]*$", textField_insertSsn.getText())) {
					JOptionPane.showMessageDialog(frame, "9자리 숫자를 입력하세요.(ssn)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Ssn은 숫자만 가능
					
				} 
				// 작동 O
				else if(ssnResult.contains(textField_insertSsn.getText())){
					JOptionPane.showMessageDialog(frame, "중복되는 값이 존재합니다.(Ssn)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Ssn primary key
					
				} 
				// 작동 O
				else if(textField_insertSsn.getText().length() != 9){
					JOptionPane.showMessageDialog(frame, "9자리 숫자를 입력하세요.(Ssn)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Ssn은 CHAR(9)
					
				} 
				// 작동 O
				else if(!textField_insertBdate.getText().equals(emptyText) && textField_insertBdate.getText().length() != 10) {
					JOptionPane.showMessageDialog(frame, "yyyy-mm-dd 형식에 맞게 정확한 생일을 입력하세요.(Bdate)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Bdate 는 date 타입 (yyyy-mm-dd) (총 10자)
				}
				// 작동 O
				else if(!textField_insertBdate.getText().equals(emptyText) && !Pattern.matches("^((19\\d{2})|(20([01][0-9]|2[01])))\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", textField_insertBdate.getText())) {
					JOptionPane.showMessageDialog(frame, "yyyy-mm-dd 형식에 맞게 정확한 생일을 입력하세요.(Bdate)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Bdate 는 date 타입 (yyyy-mm-dd)
				} 
				
				// 작동 O
				else if(textField_insertAddress.getText().length() > 30) {
					JOptionPane.showMessageDialog(frame, "주소가 너무 깁니다. 30자 이하로 입력하세요.(Address)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Address 는 VARCHAR(30)
					
				} 
				// 작동O
				else if(!textField_insertSalary.getText().equals(emptyText) && !Pattern.matches("^(\\d)*(\\.(\\d)?(\\d)?)?$", textField_insertSalary.getText())) {
					JOptionPane.showMessageDialog(frame, "연봉을 숫자로 입력하세요. 10자 이하(+ 소수점 둘째자리까지 가능)로 입력하세요.(Salary)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Salary 는 Decimal(10, 2);
					
				}
				// 작동O
				else if(!textField_insertSup_ssn.getText().equals(emptyText) && textField_insertSup_ssn.getText().toString().length() != 9) {  // Super_ssn 이 빈칸이면 상관X
					JOptionPane.showMessageDialog(frame, "9자리 숫자를 입력하세요.(Super_ssn)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Super_ssn은 CHAR(9)
				
				}
				// 작동O
				else if(!textField_insertSup_ssn.getText().equals(emptyText) && !Pattern.matches("^[0-9]*$", textField_insertSup_ssn.getText())) {
					JOptionPane.showMessageDialog(frame, "9자리 숫자를 입력하세요.(Super_ssn)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Super_ssn은 숫자만 가능
					
				} 
				// 작동O
				else if(textField_insertSup_ssn.getText().equals(textField_insertSsn.getText())) {
					JOptionPane.showMessageDialog(frame, "자기 자신의 상사가 자기 자신이 될 수 없습니다. 상사의 Ssn을 맞게 입력해주세요.(Super_ssn)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Super_ssn은 자기 자신의 ssn 이 될 수 없음
					
				}
				// 작동O
				else if(!textField_insertSup_ssn.getText().equals(emptyText) && !ssnResult.contains(textField_insertSup_ssn.getText())) {
					JOptionPane.showMessageDialog(frame, "Super_ssn 에 해당하는 직원이 없습니다. 상사의 Ssn을 맞게 입력해주세요.(Super_ssn)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // FK (Super_ssn) - PK (Ssn)
					
				} 
				// 작동 O
				else if(!textField_insertDno.getText().equals(emptyText) && !Pattern.matches("^(1|4|5)*$", textField_insertDno.getText())) {
					JOptionPane.showMessageDialog(frame, "정확한 부서 번호 숫자를 입력하세요.(Dno)", "ERROR",
							JOptionPane.ERROR_MESSAGE);  // Dno 는 INT, 기존에 있는 부서 번호만 선택 가능 (1, 4, 5)
					
				}
				else {
					
					// CompanyDB 객체 생성해서 생성자로 insertQuery 넘겨줘서 삽입 쿼리 실행하기
					CompanyDB companyDB_insert = new CompanyDB(insertQuery, password);
					companyDB_insert.insertDB(eString);
					JOptionPane.showMessageDialog(frame, "데이터가 삽입되었습니다.");
				}
				
				// 쿼리문 초기화
				insertQuery = "insert into EMPLOYEE value(";
			}
		});

		// insertQuery   <---- insert 문 저장할 string 변수 위에 class 변수로 선언해놓음
		
		
	}  // initialize 함수 끝

	
}
