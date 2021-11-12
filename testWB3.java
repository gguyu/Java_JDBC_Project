package test3;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.security.auth.callback.PasswordCallback;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class testWB3 {

	protected JFrame frame, frameLogin;
	private JTextField textField_salary; // 연봉 검색창 객체
	private JTextField textField_sub; // 부하직원 검색창 객체
	private JTable dataTable; // DB table 객체
	private JPanel panel_1;

	// 검색 항목: checkbox 에서 받아올 것임
	private String selectStatement;
	private String fromStatement;
	private String whereClause; // null 이면 where절 없음
	private String[] selectAttribute;
	// 검색 범위: combobox 나 textfield 에서 받아올 것임, default 는 전체이므로 초기값은 null
	private JButton btnSearch;
	private JLabel selectedEmpLb, selectedCountLb, avgSalLb;
	private DefaultTableModel tableModel;
	private Set<String> selectedEmp, selectedSsn;

	// MySQL 로그인 기능
	private String password = "root"; // 비밀번호 선택 창 입력안하고 끄면 우선 default 로 root
	private JTextField textField_Password;

	private JCheckBox chckbxSearchName;
	private JCheckBox chckbxSearchSsn;
	private JCheckBox chckbxSearchBdate;
	private JCheckBox chckbxSearchAddress;
	private JCheckBox chckbxSearchSex;
	private JCheckBox chckbxSearchSalary;
	private JCheckBox chckbxSearchSupervisor;
	private JCheckBox chckbxSearchDepartment;
	
	
	private void loginFrame() {
		frameLogin = new JFrame();
		frameLogin.setBounds(100, 100, 310, 180);
		frameLogin.getContentPane().setLayout(null);
		frameLogin.setLocationRelativeTo(null);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 이게 있으면 창닫았을 때 꺼짐.
		frameLogin.setVisible(true);
		frameLogin.setAlwaysOnTop(true); // 창을 맨 앞에 나오게 함

		JPanel panelPw = new JPanel();
		panelPw.setBounds(0, 0, 285, 115);
		frameLogin.getContentPane().add(panelPw);
		panelPw.setLayout(null);

		JLabel lblTitleLabel = new JLabel("MySQL Login");
		lblTitleLabel.setFont(new Font("굴림", Font.BOLD, 16));
		lblTitleLabel.setBounds(81, 10, 113, 24);
		panelPw.add(lblTitleLabel);

		JLabel lblIdLabel = new JLabel("ID");
		lblIdLabel.setBounds(29, 44, 41, 15);
		panelPw.add(lblIdLabel);

		JLabel lblIdRootLabel = new JLabel("root");
		lblIdRootLabel.setBounds(81, 44, 57, 15);
		panelPw.add(lblIdRootLabel);

		JLabel lblPwLabel = new JLabel("PW");
		lblPwLabel.setBounds(29, 69, 41, 15);
		panelPw.add(lblPwLabel);

		textField_Password = new JPasswordField();
		textField_Password.setBounds(78, 66, 116, 21);
		panelPw.add(textField_Password);
		textField_Password.setColumns(10);
		textField_Password.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String emptyPassword = "";
				if (!textField_Password.getText().equals(emptyPassword)) {
					password = textField_Password.getText();
					System.out.println(password);
					frameLogin.dispose();
				} else {
					JOptionPane.showMessageDialog(frameLogin, "password 를 입력해주세요.", "Login Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JLabel lblPwtelLabel = new JLabel("Password 를 정확히 입력하세요");
		lblPwtelLabel.setForeground(Color.RED);
		lblPwtelLabel.setBounds(52, 100, 250, 15);
		panelPw.add(lblPwtelLabel);

		// 로그인 버튼을 누르면 로그인 창 닫음 (비밀번호가 입력이 안되어있으면 입력할 때까지 안닫힘)
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(206, 44, 67, 43);
		panelPw.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String emptyPassword = "";
				if (!textField_Password.getText().equals(emptyPassword)) {
					password = textField_Password.getText();
					System.out.println(password);
					frameLogin.dispose();
				} else {
					JOptionPane.showMessageDialog(frameLogin, "password 를 입력해주세요.", "Login Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	} // Login 창 끝

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testWB3 window = new testWB3();
					window.loginFrame();
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
	public testWB3() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setForeground(SystemColor.desktop);
		frame.setBounds(100, 100, 1002, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);

		// Login 창 띄우기

		// 검색 조건 panel
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 986, 83);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("검색 범위");
		lblNewLabel.setBounds(12, 20, 57, 15);
		panel.add(lblNewLabel);

		// 검색 범위 구현

		// 부서
		JComboBox comboBox_department = new JComboBox();
		comboBox_department
				.setModel(new DefaultComboBoxModel(new String[] { "Research", "Administration", "Headquarters" }));
		comboBox_department.setBounds(150, 16, 107, 23);
		panel.add(comboBox_department);
		comboBox_department.setVisible(false);

		comboBox_department.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox_department.getSelectedItem() == "Research") {
					whereClause = " where e.Dno = 5";
				} else if (comboBox_department.getSelectedItem() == "Administration") {
					whereClause = " where e.Dno = 4";
				} else if (comboBox_department.getSelectedItem() == "Headquarters") {
					whereClause = " where e.Dno = 1";
				}
			}
		});

		// 성별
		JComboBox comboBox_sex = new JComboBox();
		comboBox_sex.setModel(new DefaultComboBoxModel(new String[] { "M", "F" }));
		comboBox_sex.setBounds(265, 16, 53, 23);
		panel.add(comboBox_sex);
		comboBox_sex.setVisible(false);

		comboBox_sex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox_sex.getSelectedItem() == "M") {
					whereClause = " where e.Sex = 'M'";
				} else if (comboBox_sex.getSelectedItem() == "F") {
					whereClause = " where e.Sex = 'F'";
				}
			}
		});

		// 연봉
		textField_salary = new JTextField();
		textField_salary.setBounds(330, 17, 116, 21);
		panel.add(textField_salary);
		textField_salary.setColumns(10);
		textField_salary.setVisible(false);

		// 생일
		JComboBox comboBox_bdate = new JComboBox();
		comboBox_bdate.setModel(new DefaultComboBoxModel(
				new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
		comboBox_bdate.setBounds(458, 16, 47, 23);
		panel.add(comboBox_bdate);
		comboBox_bdate.setVisible(false);

		comboBox_bdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int month = Integer.parseInt(comboBox_bdate.getSelectedItem().toString());
				switch (month) {
				case 1:
					whereClause = " where e.Bdate like '_____01%'";
					break;
				case 2:
					whereClause = " where e.Bdate like '_____02%'";
					break;
				case 3:
					whereClause = " where e.Bdate like '_____03%'";
					break;
				case 4:
					whereClause = " where e.Bdate like '_____04%'";
					break;
				case 5:
					whereClause = " where e.Bdate like '_____05%'";
					break;
				case 6:
					whereClause = " where e.Bdate like '_____06%'";
					break;
				case 7:
					whereClause = " where e.Bdate like '_____07%'";
					break;
				case 8:
					whereClause = " where e.Bdate like '_____08%'";
					break;
				case 9:
					whereClause = " where e.Bdate like '_____09%'";
					break;
				case 10:
					whereClause = " where e.Bdate like '_____10%'";
					break;
				case 11:
					whereClause = " where e.Bdate like '_____11%'";
					break;
				case 12:
					whereClause = " where e.Bdate like '_____12%'";
					break;

				} // swith 문 끝

			}
		});

		// 부하직원
		textField_sub = new JTextField();
		textField_sub.setBounds(517, 17, 123, 21);
		panel.add(textField_sub);
		textField_sub.setColumns(10);
		textField_sub.setVisible(false);

		// 상위 combobox 의 상태에 따라 하위 combobox 와 textfield 결정
		JComboBox comboBox_searchRange = new JComboBox();

		// 전체 => 콤보박스 X / 부서, 성별, 생일 => 콤보박스 O / 연봉, 부하직원 => 입력칸 O
		comboBox_searchRange.setModel(new DefaultComboBoxModel(new String[] { "전체", "부서", "성별", "연봉", "생일", "부하직원" }));
		comboBox_searchRange.setBounds(69, 16, 72, 23);
		panel.add(comboBox_searchRange);

		comboBox_searchRange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox_searchRange.getSelectedItem() == "전체") {
					comboBox_department.setVisible(false);
					comboBox_sex.setVisible(false);
					textField_salary.setVisible(false);
					comboBox_bdate.setVisible(false);
					textField_sub.setVisible(false);
					comboBox_department.setSelectedIndex(0);
					comboBox_sex.setSelectedIndex(0);
					textField_salary.setText(null);
					comboBox_bdate.setSelectedIndex(0);
					textField_sub.setText(null);
					whereClause = null;
				}

				else if (comboBox_searchRange.getSelectedItem() == "부서") {
					comboBox_sex.setVisible(false);
					textField_salary.setVisible(false);
					comboBox_bdate.setVisible(false);
					textField_sub.setVisible(false);
					comboBox_department.setVisible(true);
					comboBox_department.setSelectedIndex(0);
					comboBox_sex.setSelectedIndex(0);
					textField_salary.setText(null);
					comboBox_bdate.setSelectedIndex(0);
					textField_sub.setText(null);
					whereClause = " where e.Dno = 5";
				}

				else if (comboBox_searchRange.getSelectedItem() == "성별") {
					comboBox_department.setVisible(false);
					textField_salary.setVisible(false);
					comboBox_bdate.setVisible(false);
					textField_sub.setVisible(false);
					comboBox_sex.setVisible(true);
					comboBox_department.setSelectedIndex(0);
					comboBox_sex.setSelectedIndex(0);
					textField_salary.setText(null);
					comboBox_bdate.setSelectedIndex(0);
					textField_sub.setText(null);
					whereClause = " where e.Sex = 'M'";
				}

				else if (comboBox_searchRange.getSelectedItem() == "연봉") {
					comboBox_department.setVisible(false);
					comboBox_sex.setVisible(false);
					comboBox_bdate.setVisible(false);
					textField_sub.setVisible(false);
					textField_salary.setVisible(true);
					comboBox_department.setSelectedIndex(0);
					comboBox_sex.setSelectedIndex(0);
					comboBox_bdate.setSelectedIndex(0);
					textField_sub.setText(null);
					whereClause = null;
				}

				else if (comboBox_searchRange.getSelectedItem() == "생일") {
					comboBox_department.setVisible(false);
					comboBox_sex.setVisible(false);
					textField_salary.setVisible(false);
					textField_sub.setVisible(false);
					comboBox_bdate.setVisible(true);
					comboBox_department.setSelectedIndex(0);
					comboBox_sex.setSelectedIndex(0);
					textField_salary.setText(null);
					comboBox_bdate.setSelectedIndex(0);
					textField_sub.setText(null);
					whereClause = " where e.Bdate like '_____01%'";
				}

				else if (comboBox_searchRange.getSelectedItem() == "부하직원") {
					comboBox_department.setVisible(false);
					comboBox_sex.setVisible(false);
					textField_salary.setVisible(false);
					comboBox_bdate.setVisible(false);
					textField_sub.setVisible(true);
					comboBox_department.setSelectedIndex(0);
					comboBox_sex.setSelectedIndex(0);
					comboBox_bdate.setSelectedIndex(0);
					textField_sub.setText(null);
					whereClause = null;
				}

			}
		});

		// 검색 항목 구현
		JLabel lblNewLabel_1 = new JLabel("검색 항목");
		lblNewLabel_1.setBounds(12, 50, 57, 15);
		panel.add(lblNewLabel_1);

		chckbxSearchName = new JCheckBox("Name");
		chckbxSearchName.setSelected(true);
		chckbxSearchName.setBounds(69, 46, 59, 23);
		panel.add(chckbxSearchName);

		chckbxSearchSsn = new JCheckBox("Ssn");
		chckbxSearchSsn.setSelected(true);
		chckbxSearchSsn.setBounds(132, 46, 47, 23);
		panel.add(chckbxSearchSsn);

		chckbxSearchBdate = new JCheckBox("Bdate");
		chckbxSearchBdate.setSelected(true);
		chckbxSearchBdate.setBounds(183, 46, 59, 23);
		panel.add(chckbxSearchBdate);

		chckbxSearchAddress = new JCheckBox("Address");
		chckbxSearchAddress.setSelected(true);
		chckbxSearchAddress.setBounds(246, 46, 80, 23);
		panel.add(chckbxSearchAddress);

		chckbxSearchSex = new JCheckBox("Sex");
		chckbxSearchSex.setSelected(true);
		chckbxSearchSex.setBounds(330, 46, 47, 23);
		panel.add(chckbxSearchSex);

		chckbxSearchSalary = new JCheckBox("Salary");
		chckbxSearchSalary.setSelected(true);
		chckbxSearchSalary.setBounds(381, 46, 62, 23);
		panel.add(chckbxSearchSalary);

		chckbxSearchSupervisor = new JCheckBox("Supervisor");
		chckbxSearchSupervisor.setSelected(true);
		chckbxSearchSupervisor.setBounds(447, 46, 90, 23);
		panel.add(chckbxSearchSupervisor);

		chckbxSearchDepartment = new JCheckBox("Department");
		chckbxSearchDepartment.setSelected(true);
		chckbxSearchDepartment.setBounds(541, 46, 107, 23);
		panel.add(chckbxSearchDepartment);

		// 검색 버튼 구현
		btnSearch = new JButton("검색");
		btnSearch.setForeground(SystemColor.desktop);
		btnSearch.setBounds(837, 42, 107, 30);
		panel.add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searching(e);
			}
		});
		
		// table 생성
		tableModel = new DefaultTableModel();
		dataTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(dataTable);
		frame.getContentPane().add(scrollPane);

		scrollPane.setViewportView(dataTable);
		scrollPane.setBounds(10, 96, 964, 277);

		selectStatement = null;
		fromStatement = null;
		whereClause = null;

		// table 보이기 끝

		// 삽입, 삭제, 갱신 panel
		panel_1 = new JPanel();
		panel_1.setBounds(0, 376, 986, 97);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		selectedEmpLb = new JLabel();
		selectedEmpLb.setText("선택한 직원 :");
		selectedEmpLb.setBounds(14, 12, 1000, 18);
		panel_1.add(selectedEmpLb);
		selectedEmpLb.setVisible(false);

		selectedCountLb = new JLabel();
		selectedCountLb.setText("인원 수 :");
		selectedCountLb.setBounds(14, 42, 100, 18);
		panel_1.add(selectedCountLb);

		avgSalLb = new JLabel(); // 평균 임금 label
		avgSalLb.setText("선택한 직원 평균 임금 : ");
		avgSalLb.setBounds(14, 70, 545, 15);
		panel_1.add(avgSalLb);

		// 삽입 버튼
		JButton btnInsertDisp = new JButton("새로운 데이터 삽입"); // 버튼 누르면 삽입하는 새로운 레이아웃 보여줌
		btnInsertDisp.setBounds(815, 10, 145, 23);
		panel_1.add(btnInsertDisp);
		btnInsertDisp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				InsertSetting insertSetting = new InsertSetting(password); // 삽입 레이아웃 객체 생성해서 창 띄움
				insertSetting.launch();
			}
		});

		// 삭제 버튼
		JButton btnDeleteDisp = new JButton("선택한 데이터 삭제"); // 버튼 누르면 선택한 직원의 정보를 DB에서 삭제
		btnDeleteDisp.setBounds(815, 40, 145, 23);
		panel_1.add(btnDeleteDisp);
		btnDeleteDisp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cntDeleteSsn = selectedSsn.size(); // 선택된 직원 수

				if (selectedSsn.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "선택된 직원이 없습니다. 선택되어 있다면 검색 항목에 'Ssn'이 선택되어있는지 확인하세요.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				} else {
					Iterator<String> iter_selectedSsn = selectedSsn.iterator();
					String[] deleteSsn = new String[cntDeleteSsn]; // 배열 사이즈는 선택된 직원의 수
					int idxSsn = 0;
					while (iter_selectedSsn.hasNext()) {
						deleteSsn[idxSsn] = iter_selectedSsn.next();
						idxSsn++;
					}

					
					// 만약 부하직원이 있는 직원은 삭제 불가 (super_ssn 이 다른 ssn 들 중에 같은게 있으면 삭제 불가)
					// 만약 ssn 이 department 의 mgr_ssn 과 같으면 삭제 불가
					// 만약 ssn 이 works_on 의 essn 과 같으면 삭제 불가
					
					
					
					
					
					// sql 객체 생성 (생성자에 deleteSsn array 와 cntDeleteSsn 넘겨주기 / cntDelete 만큼
					// deleteQuery[i] 삭제 반복 수행

					CompanyDB companyDB = new CompanyDB(deleteSsn, cntDeleteSsn, password);
					companyDB.deleteDB();
					System.out.println(selectedSsn.size());
					JOptionPane.showMessageDialog(frame, "데이터가 삭제되었습니다.");
					searching(e);
				}

				// 예외처리되면 에러메세지만 출력, 정상적으로 작동되면 delete 연산 수행 완료

			}
		});

		// 갱신 버튼
		JLabel lblNewLabel_6 = new JLabel("수정 :");
		lblNewLabel_6.setBounds(311, 40, 45, 18);
		panel_1.add(lblNewLabel_6);

		JComboBox comboBox_update = new JComboBox();
		comboBox_update.setModel(new DefaultComboBoxModel(new String[] { "Address", "Sex", "Salary" }));
		comboBox_update.setBounds(359, 40, 82, 24);
		panel_1.add(comboBox_update);

		JTextField textField = new JTextField();
		textField.setBounds(443, 40, 116, 24);
		panel_1.add(textField);
		textField.setColumns(10);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(560, 40, 80, 24);
		panel_1.add(btnUpdate);
		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cntUpdateSsn = selectedSsn.size();
				String stat = "up";
                String eMessage = "";
                
				if (selectedSsn.isEmpty()) {
					System.out.println("에러메시지 출력, 선택된 직원이 없습니다. 선택되어있다면 검색 항목에 'Ssn' 이 선택되어있는지 확인하세요.");
				} else {
					Iterator<String> iter_selectedSsn = selectedSsn.iterator();
					String[] updateSsn = new String[cntUpdateSsn]; // 배열 사이즈는 선택된 직원의 수
					int idxSsn = 0;
					while (iter_selectedSsn.hasNext()) {
						updateSsn[idxSsn] = iter_selectedSsn.next();
						idxSsn++;
					}
					//구름
					if (comboBox_update.getSelectedItem() == "Address") {
						// update employee set address = textField.getText()
						String uData = textField.getText();
						String att = (String) comboBox_update.getSelectedItem();

						CompanyDB companyDB = new CompanyDB(updateSsn, cntUpdateSsn, password, stat);
						eMessage = companyDB.updateDB(att, uData);
						
						if(eMessage != "") {
							JOptionPane.showMessageDialog(frame, eMessage, "ERROR",
									JOptionPane.ERROR_MESSAGE);
						}

					} else if (comboBox_update.getSelectedItem() == "Sex") {
						// update employee set sex = textFiled.getText()
						String uData = textField.getText();
						String att = (String) comboBox_update.getSelectedItem();

						CompanyDB companyDB = new CompanyDB(updateSsn, cntUpdateSsn, password, stat);
						eMessage = companyDB.updateDB(att, uData);
						
						if(eMessage != "") {
							JOptionPane.showMessageDialog(frame, eMessage, "ERROR",
									JOptionPane.ERROR_MESSAGE);
						}

					} else if (comboBox_update.getSelectedItem() == "Salary") {
						String uData = textField.getText();
						String att = (String) comboBox_update.getSelectedItem();

						CompanyDB companyDB = new CompanyDB(updateSsn, cntUpdateSsn, password, stat);
						eMessage = companyDB.updateDB(att, uData);
						
						if(eMessage != "") {
							JOptionPane.showMessageDialog(frame, eMessage, "ERROR",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}

				// 객체 생성 + 수정연산

			}
		});
		

	} // initialize() 함수 끝

	// 테이블의 checkbox 에서 선택된 row의 Ssn 을 찾기 위한 함수, 만약에 Ssn이 없으면 -1을 반환. -1을 반환받으면
	// 에러메시지 출력하기
	private int getColumnIndex(JTable table, String header) {
		for (int i = 0; i < table.getColumnCount(); i++) {
			if (table.getColumnName(i).equals(header)) {
				return i;
			}
		}
		return -1;
	}

	// 테이블에 checkbox 를 넣기 위함.
	DefaultTableCellRenderer dcr = new DefaultTableCellRenderer() {
		public Component getTableCellRendererComponent // 셀렌더러
		(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,int column){JCheckBox box=new JCheckBox();box.setSelected(((Boolean)value).booleanValue());box.setHorizontalAlignment(JLabel.CENTER);

	box.addMouseListener(new MouseAdapter(){@Override public void mouseClicked(MouseEvent e){System.out.println("s");}});

	return box;}};
	
	
	public void searching(ActionEvent e) {
		String emptyText = ""; // textfield 가 비어있는지 비교 용도
		// 검색 범위에서 textField 로 받아오는 경우
		if (!textField_salary.getText().equals(emptyText)) { // salary textfield 에 값이 있으면 where 절 작성
			String salaryText = textField_salary.getText();
			whereClause = " where e.Salary > " + salaryText;
		} else if (!textField_sub.getText().equals(emptyText)) { // sub textfield 에 값이 있으면 where 절 작성
			String subText = textField_sub.getText();
			whereClause = " where e.Super_ssn = '" + subText + "'";
		}

		System.out.println("검색");

		// 검색 항목 받아오는거 구현하기
		int cntSelect = 0; // 체크박스 개수 (0일 때에는 select + query 에 그냥 추가하고, 0이 아니면 " , " 도 추가해야됨

		if (chckbxSearchName.isSelected()) { // Name 이 체크되면 실행
			if (cntSelect != 0) {
				selectStatement += ", ";
			} else {
				selectStatement = "select ";
			}
			cntSelect++;
			selectStatement += "concat(e.Fname, IFNULL(e.Minit, \"\"), e.Lname) as Name";
		}

		if (chckbxSearchSsn.isSelected()) { // Ssn 이 체크되면 실행
			if (cntSelect != 0) {
				selectStatement += ", ";
			} else {
				selectStatement = "select ";
			}
			cntSelect++;
			selectStatement += "e.Ssn";
		}

		if (chckbxSearchBdate.isSelected()) { // Bdate 가 체크되면 실행
			if (cntSelect != 0) {
				selectStatement += ", ";
			} else {
				selectStatement = "select ";
			}
			cntSelect++;
			selectStatement += "e.Bdate";
		}

		if (chckbxSearchAddress.isSelected()) { // Address 가 체크되면 실행
			if (cntSelect != 0) {
				selectStatement += ", ";
			} else {
				selectStatement = "select ";
			}
			cntSelect++;
			selectStatement += "e.Address";
		}

		if (chckbxSearchSex.isSelected()) { // Sex 가 체크되면 실행
			if (cntSelect != 0) {
				selectStatement += ", ";
			} else {
				selectStatement = "select ";
			}
			cntSelect++;
			selectStatement += "e.Sex";
		}

		if (chckbxSearchSalary.isSelected()) { // Salary 가 체크되면 실행
			if (cntSelect != 0) {
				selectStatement += ", ";
			} else {
				selectStatement = "select ";
			}
			cntSelect++;
			selectStatement += "e.Salary";
		}

		if (chckbxSearchSupervisor.isSelected()) { // Supervisor 가 체크되면 실행
			if (cntSelect != 0) {
				selectStatement += ", ";
			} else {
				selectStatement = "select ";
			}
			cntSelect++;
			selectStatement += "concat(s.Fname, IFNULL(s.Minit, \"\"), s.Lname) as Supervisor";
		}

		if (chckbxSearchDepartment.isSelected()) { // Department 가 체크되면 실행
			if (cntSelect != 0) {
				selectStatement += ", ";
			} else {
				selectStatement = "select ";
			}
			cntSelect++;
			selectStatement += "d.Dname as Department";
		}

		// 체크박스에 따른 from 절 설정

		if (chckbxSearchSupervisor.isSelected() && chckbxSearchDepartment.isSelected()) { // 검색 항목에 supervisor,
																							// department 둘 다 있을
																							// 때
			fromStatement = " from EMPLOYEE as e left outer join EMPLOYEE as s on e.Super_ssn = s.Ssn join DEPARTMENT as d on e.Dno = d.Dnumber";
		} else if (chckbxSearchSupervisor.isSelected()) { // 검색 항목에 supervisor 가 있을 때
			fromStatement = " from EMPLOYEE as e left outer join EMPLOYEE as s on e.Super_ssn = s.Ssn";
		} else if (chckbxSearchDepartment.isSelected()) { // 검색 항목에 department 가 있을 때
			fromStatement = " from EMPLOYEE as e join DEPARTMENT as d on e.Dno = d.Dnumber";
		} else { // 검색 항목에 supervisor, department 둘 다 없을 때
			fromStatement = " from EMPLOYEE as e";
		}

		// 체크박스에 따른 string[] columns 설정 (table 만들 때 column 헤더로 사용)
		int columnCnt = 0;
		selectAttribute = new String[cntSelect + 1];
		selectAttribute[0] = "선택";
		if (chckbxSearchName.isSelected()) {
			columnCnt++;
			selectAttribute[columnCnt] = "Name";
		}
		if (chckbxSearchSsn.isSelected()) {
			columnCnt++;
			selectAttribute[columnCnt] = "Ssn";
		}
		if (chckbxSearchBdate.isSelected()) {
			columnCnt++;
			selectAttribute[columnCnt] = "Bdate";
		}
		if (chckbxSearchAddress.isSelected()) {
			columnCnt++;
			selectAttribute[columnCnt] = "Address";
		}
		if (chckbxSearchSex.isSelected()) {
			columnCnt++;
			selectAttribute[columnCnt] = "Sex";
		}
		if (chckbxSearchSalary.isSelected()) {
			columnCnt++;
			selectAttribute[columnCnt] = "Salary";
		}
		if (chckbxSearchSupervisor.isSelected()) {
			columnCnt++;
			selectAttribute[columnCnt] = "Supervisor";
		}
		if (chckbxSearchDepartment.isSelected()) {
			columnCnt++;
			selectAttribute[columnCnt] = "Department";
		}

		selectStatement = selectStatement + fromStatement;

		// 검색을 다시하면 선택한 직원과 인원 수 표시해주는 라벨도 초기화
		selectedEmpLb.setVisible(false);
		if (Arrays.asList(selectAttribute).contains("Name")) {
			selectedEmpLb.setVisible(true);
		}
		selectedEmpLb.setText("선택한 직원 :");
		selectedCountLb.setText("인원 수 :");
		avgSalLb.setText("선택한 직원 평균 임금 : ");

		// table 만들기
		CompanyDB companyDB = new CompanyDB(selectStatement, whereClause, columnCnt + 1, password, selectAttribute);

		int cntRowModel = tableModel.getRowCount();
		int cntRowM = 0;
		while (cntRowM < cntRowModel) {
			tableModel.removeRow(0);
			cntRowM++;
		}

		tableModel.setDataVector(companyDB.searchDB(), selectAttribute);

		// checkBox 생성
		dataTable.getColumn("선택").setCellRenderer(dcr);
		JCheckBox checkBox = new JCheckBox();
		checkBox.setHorizontalAlignment(JLabel.CENTER);

		// 선택한 직원, 인원 수
		selectedEmp = new LinkedHashSet<>(); // 선택된 직원 정보 라벨에 사용
		selectedSsn = new LinkedHashSet<>(); // delete, update 연산 시에 사용.
		dataTable.getColumn("선택").setCellEditor(new DefaultCellEditor(checkBox));
		checkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 선택한 행의 첫번째 attribute 받기
				String selected = dataTable.getValueAt(dataTable.getSelectedRow(), 1).toString();

				// delete 연산에 인자로 넘겨줄 Ssn 이 있으면 실행 (없으면 무시하고 delete 버튼을 누르면 에러메세지 출력하게 함)
				if (getColumnIndex(dataTable, "Ssn") != -1) {
					// getColumnIndex(): column header 에 Ssn 이 없으면 -1 반환 --> exception 발생으로
					// selectedEssn 은 정의되지 않고 selectedSsn hashset 은 비어있게됨 (.isEmpty() == true)
					String selectedEssn = dataTable
							.getValueAt(dataTable.getSelectedRow(), getColumnIndex(dataTable, "Ssn"))
							.toString();
					if (selectedSsn.contains(selectedEssn)) {
						selectedSsn.remove(selectedEssn);
						System.out.println(selectedSsn.size());
					} else {
						selectedSsn.add(selectedEssn);
						System.out.println(selectedSsn.size());
					}
				}

				// 선택한 직원이 누구인지 라벨에 표시함
				if (selectedEmp.contains(selected)) {
					selectedEmp.remove(selected);
				} else {
					selectedEmp.add(selected);
				}

				// 평균 임금 연산
				
				CompanyDB companyDB = new CompanyDB(password);
				float avg = companyDB.retAvgSal(selectedSsn);

				if (checkBox.isSelected()) {

				}

				// 평균임금 값 label에 집어 넣기
				avgSalLb.setText("선택한 직원 평균 임금 : + avg ");

				if (selectedEmp.isEmpty()) {
					selectedEmpLb.setText("선택한 직원 : ");
					selectedCountLb.setText("인원 수 : ");
					avgSalLb.setText("선택한 직원 평균 임금 : ");
					panel_1.revalidate();
				} else {
					selectedEmpLb.setText("선택한 직원 : " + selectedEmp);
					selectedCountLb.setText("인원 수 :" + selectedEmp.size());
					if (chckbxSearchSsn.isSelected()) {
						avgSalLb.setText("선택한 직원 평균 임금 : " + avg);
					} else {
						avgSalLb.setText("선택한 직원 평균 임금 : 검색 결과 표에 Ssn이 포함되어 있지 않으면 표기되지 않습니다.");
					}
					panel_1.revalidate();
				}

			}
		});

	}

}