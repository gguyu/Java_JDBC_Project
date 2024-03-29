package test3;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.security.auth.callback.PasswordCallback;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Pattern;

public class testWB3 {

	protected JFrame frame, frameLogin;
	private JTextField textField_salary; // 연봉 검색창 객체
	private JTextField textField_sub; // 부하직원 검색창 객체
	private JComboBox comboBox_searchRange; // 검색 범위 콤보박스 객체
	private JTable dataTable; // DB table 객체
	private JPanel panel_1;


	private String selectStatement;
	private String fromStatement;
	private String whereClause; // null 이면 where절 없음
	private String[] selectAttribute;

	private JButton btnSearch;
	private JLabel selectedEmpLb, selectedCountLb, avgSalLb;
	private DefaultTableModel tableModel;
	private Set<String> selectedEmp, selectedSsn;
	
	// 검색 예외처리용
	private HashSet<String> ssnResult = CompanyDB.ssnResult;
	
	// 삭제 예외처리용
	private HashSet<String> employee_Super_ssnResult = CompanyDB.employee_Super_ssnResult;
	private HashSet<String> department_Mgr_ssnResult = CompanyDB.department_Mgr_ssnResult;
	private HashSet<String> works_onEssnResult = CompanyDB.works_onEssnResult;

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
	
	// Login 창 띄우기
	private void loginFrame() {
		frameLogin = new JFrame();
		frameLogin.setBounds(100, 100, 310, 180);
		frameLogin.getContentPane().setLayout(null);
		frameLogin.setLocationRelativeTo(null);

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
		comboBox_department.setBounds(183, 16, 107, 23);
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
		comboBox_sex.setBounds(183, 16, 53, 23);
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
		textField_salary.setBounds(183, 17, 116, 21);
		panel.add(textField_salary);
		textField_salary.setColumns(10);
		textField_salary.setVisible(false);

		// 생일
		JComboBox comboBox_bdate = new JComboBox();
		comboBox_bdate.setModel(new DefaultComboBoxModel(
				new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
		comboBox_bdate.setBounds(183, 16, 47, 23);
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
		textField_sub.setBounds(183, 17, 123, 21);
		panel.add(textField_sub);
		textField_sub.setColumns(10);
		textField_sub.setVisible(false);

		// 상위 combobox 의 상태에 따라 하위 combobox 와 textfield 결정
		comboBox_searchRange = new JComboBox();

		// 전체 => 콤보박스 X / 부서, 성별, 생일 => 콤보박스 O / 연봉, 부하직원 => 입력칸 O
		comboBox_searchRange.setModel(new DefaultComboBoxModel(new String[] { "전체", "부서", "성별", "연봉", "생일", "부하직원" }));
		comboBox_searchRange.setBounds(69, 16, 102, 23);
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
					whereClause = " where e.Salary > 0";
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
		selectedCountLb.setBounds(14, 42, 449, 18);
		panel_1.add(selectedCountLb);

		avgSalLb = new JLabel(); // 평균 임금 label
		avgSalLb.setText("선택한 직원 평균 임금 : ");
		avgSalLb.setBounds(14, 70, 449, 15);
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
					
					// 예외처리
					employee_Super_ssnResult = CompanyDB.employee_Super_ssnResult;
					department_Mgr_ssnResult = CompanyDB.department_Mgr_ssnResult;
					works_onEssnResult = CompanyDB.works_onEssnResult;
					
					int exceptnum = 0;
					
					// 만약 WOKRS_ON 테이블의 Essn 에게 참조되고 있는 직원은 삭제 불가
					iter_selectedSsn = selectedSsn.iterator();
					while (iter_selectedSsn.hasNext()) {
						if (works_onEssnResult.contains(iter_selectedSsn.next())) {
							exceptnum = 1;  // WORKS_ON 의 Essn 에 겹치면 exception = 1 로 변경
						}
					}
					// 만약 DEPARTMENT 테이블의 Mgr_ssn 에게 참조되고 있는 직원은 삭제 불가
					iter_selectedSsn = selectedSsn.iterator();
					while (iter_selectedSsn.hasNext()) {
						if (department_Mgr_ssnResult.contains(iter_selectedSsn.next())) {
							exceptnum = 2;  // DEPARTMENT 의 Mgr_ssn 에 겹치면 exception = 2 로 변경
						}
					}
					// 만약 부하직원이 있는 직원은 삭제 불가 (super_ssn 이 다른 ssn 들 중에 같은게 있으면 삭제 불가)
					iter_selectedSsn = selectedSsn.iterator();
					while (iter_selectedSsn.hasNext()) {
						if (employee_Super_ssnResult.contains(iter_selectedSsn.next())) {
							exceptnum = 3;  // EMPLOYEE의 super_Ssn 에 겹치면 exception = 1 로 변경
						}
					}
					
					if (exceptnum == 1) {
						JOptionPane.showMessageDialog(frame, "일한 기록이 남아있는 직원은 삭제할 수 없습니다. 확인해주세요. (WORKS_ON.Essn)", "ERROR",
								JOptionPane.ERROR_MESSAGE);
					} else if (exceptnum == 2) {
						JOptionPane.showMessageDialog(frame, "부서의 매니저인 직원은 삭제할 수 없습니다. 확인해주세요. (DEPARTMENT.Mgr_ssn)", "ERROR",
								JOptionPane.ERROR_MESSAGE);
					} else if (exceptnum == 3) {
						JOptionPane.showMessageDialog(frame, "다른 직원의 상사인 직원은 삭제할 수 없습니다. 확인해주세요. (Supervisor or EMPLOYEE.Super_ssn)", "ERROR",
								JOptionPane.ERROR_MESSAGE);
					} else {
						// sql 객체 생성 (생성자에 deleteSsn array 와 cntDeleteSsn 넘겨주기 / cntDelete 만큼
						// deleteQuery[i] 삭제 반복 수행
						CompanyDB companyDB = new CompanyDB(deleteSsn, cntDeleteSsn, password);
						companyDB.deleteDB();
						JOptionPane.showMessageDialog(frame, "데이터가 삭제되었습니다.");
						searching(e);
					}
					
				}
				// 예외처리되면 에러메세지만 출력, 정상적으로 작동되면 delete 연산 수행 완료
			}
		});

		// 갱신 버튼
		JLabel lblNewLabel_6 = new JLabel("수정 :");
		lblNewLabel_6.setBounds(475, 42, 45, 18);
		panel_1.add(lblNewLabel_6);

		JComboBox comboBox_update = new JComboBox();
		comboBox_update.setModel(new DefaultComboBoxModel(new String[] { "Address", "Sex", "Salary" }));
		comboBox_update.setBounds(522, 40, 82, 24);
		panel_1.add(comboBox_update);

		JTextField textField = new JTextField();
		textField.setBounds(606, 40, 116, 24);
		panel_1.add(textField);
		textField.setColumns(10);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(723, 40, 80, 24);
		panel_1.add(btnUpdate);
		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cntUpdateSsn = selectedSsn.size();
				String stat = "up";
                String eMessage = "";
                
				if (selectedSsn.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "선택된 직원이 없습니다. 선택되어있다면 검색 항목에 'Ssn' 이 선택되어있는지 확인하세요.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				} else {
					Iterator<String> iter_selectedSsn = selectedSsn.iterator();
					ArrayList<String> updateSsn = new ArrayList<String>(); // 배열 사이즈는 선택된 직원의 수
					while (iter_selectedSsn.hasNext()) {
						updateSsn.add(iter_selectedSsn.next());
					}
					
					if (comboBox_update.getSelectedItem() == "Address") {

						String uData = textField.getText();
						String att = (String) comboBox_update.getSelectedItem();
						
						if(uData.length() > 30) {
							JOptionPane.showMessageDialog(frame, "주소가 너무 깁니다. 30자 이하로 입력하세요.(Address)", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						}else {
							CompanyDB companyDB = new CompanyDB(updateSsn.toArray(new String[updateSsn.size()]), cntUpdateSsn, password, stat);
							eMessage = companyDB.updateDB(att, uData);
						}
						
						if(eMessage == "") {
							JOptionPane.showMessageDialog(frame, "데이터가 갱신되었습니다. 적용된 결과는 새로 검색 시 나타납니다.");
							textField.setText("");
						}
						
					} else if (comboBox_update.getSelectedItem() == "Sex") {

						String uData = textField.getText();
						String att = (String) comboBox_update.getSelectedItem();
						
						String[] sexArr = {"M","F"};
						
						if(!Arrays.asList(sexArr).contains(uData)) {
							JOptionPane.showMessageDialog(frame, "성별은 \'M\' 또는 \'F\'로만 입력해주세요. (Sex)", "ERROR",
									JOptionPane.ERROR_MESSAGE);
							
							eMessage = "Doesn't meet the condition. (Sex)";
						}else {
							CompanyDB companyDB = new CompanyDB(updateSsn.toArray(new String[updateSsn.size()]), cntUpdateSsn, password, stat);
							eMessage = companyDB.updateDB(att, uData);
						}
						
						if(eMessage == "") {
							JOptionPane.showMessageDialog(frame, "데이터가 갱신되었습니다. 적용된 결과는 새로 검색 시 나타납니다.");
							textField.setText("");
						}

					}else if (comboBox_update.getSelectedItem() == "Salary") {
						String uData = textField.getText();
						String att = (String) comboBox_update.getSelectedItem();
						
						if (!uData.equals("") && !uData.matches("[+-]?\\d*(\\.\\d+)?")) {
							eMessage = "연봉을 숫자로 입력하세요. 10자 이하(+ 소수점 둘째자리까지 가능)로 입력하세요.(Salary)";
							JOptionPane.showMessageDialog(frame, "연봉을 숫자로 입력하세요. 10자 이하(+ 소수점 둘째자리까지 가능)로 입력하세요.(Salary)", "ERROR",
									JOptionPane.ERROR_MESSAGE);
						}
						CompanyDB companyDBSal = new CompanyDB(password);
						
						ArrayList<String> tmpSsn = (ArrayList<String>) updateSsn.clone();
						
						float maxSupervisedSal = 0;
						
						for(String Ssn : updateSsn) {
							maxSupervisedSal = companyDBSal.getMaxSupvisedSal(Ssn);
							
							if(updateSsn.contains(companyDBSal.getSupSsn(Ssn))) {
								eMessage = "부하직원과 직속 상사의 연봉을 동시에 같은 값으로 설정할 수 없습니다. 다른 직원을 선택해주세요.";
								tmpSsn.remove(Ssn);
							}
							
							if(!uData.equals("") && companyDBSal.getSupSsn(Ssn) != null) {
								if(Float.parseFloat(uData) >= companyDBSal.getSal(companyDBSal.getSupSsn(Ssn)) || Float.parseFloat(uData) < maxSupervisedSal) {
									tmpSsn.remove(Ssn);
								}
								
							}else if(!uData.equals("") && companyDBSal.getSupSsn(Ssn) == null) {
								if(Float.parseFloat(uData) < maxSupervisedSal) {
									tmpSsn.remove(Ssn);
								}
							}else {
								continue;
							}
							
						
						}
						
						if(tmpSsn.size() != updateSsn.size()) {
							if (tmpSsn.size() ==0) {
								eMessage = "직원의 연봉은 부하직원보다는 높고, 상사보다는 낮아야 합니다. 값을 다시 입력해주세요.";
							}else {
								if(eMessage == "부하직원과 직속 상사의 연봉을 동시에 같은 값으로 설정할 수 없습니다. 다른 직원을 선택해주세요.") {
									JOptionPane.showMessageDialog(frame, "부하직원과 직속 상사의 연봉을 동시에 같은 값으로 설정할 수 없습니다. 다음 직원(SSN : " + tmpSsn + ") 만 연봉이 갱신됩니다.", "ERROR",
											JOptionPane.ERROR_MESSAGE);
								}
								else {
									JOptionPane.showMessageDialog(frame, "직원의 연봉은 부하직원보다는 높고, 상사보다는 낮아야 합니다. 해당 조건을 만족하는 직원(SSN : " + tmpSsn + ") 만 연봉이 갱신됩니다.", "ERROR",
											JOptionPane.ERROR_MESSAGE);
                                    searching(e);
								}
								CompanyDB companyDB = new CompanyDB(tmpSsn.toArray(new String[tmpSsn.size()]), tmpSsn.size(), password, stat);
								eMessage = companyDB.updateDB(att, uData);
							}	
						}else {
							CompanyDB companyDB = new CompanyDB(tmpSsn.toArray(new String[tmpSsn.size()]), tmpSsn.size(), password, stat);
							eMessage = companyDB.updateDB(att, uData);
						}
						
						if(eMessage != "") {
							JOptionPane.showMessageDialog(frame, eMessage, "ERROR",
									JOptionPane.ERROR_MESSAGE);
							textField.setText("");	
						}else {
							JOptionPane.showMessageDialog(frame, "데이터가 갱신되었습니다.");
							textField.setText("");
                            searching(e);
						}
                    }
				}
				// 객체 생성 + 수정연산
			}
		});
		
		// 검색 예외처리를 위한 초기객체생성
		CompanyDB initcompanyDB = new CompanyDB(password);
		initcompanyDB.getSsn();

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
		CompanyDB initcompanyDB = new CompanyDB(password);
		initcompanyDB.getSsn();
		ssnResult = CompanyDB.ssnResult;
		// 검색 범위에서 textField 로 받아오는 경우
		if (!textField_salary.getText().equals(emptyText)) { // salary textfield 에 값이 있으면 where 절 작성
			if(!textField_salary.getText().equals(emptyText) && !Pattern.matches("^(\\d){1,10}(\\.(\\d)(\\d)?)?$", textField_salary.getText())) {
				JOptionPane.showMessageDialog(frame, "연봉을 숫자로 입력하세요. 10자 이하(+ 소수점 둘째자리까지 가능)로 입력하세요.(연봉)", "ERROR",
						JOptionPane.ERROR_MESSAGE);  // Salary 는 Decimal(10, 2);
				return;
				
			}
			String salaryText = textField_salary.getText();
			whereClause = " where e.Salary > " + salaryText;
			
		}else if(comboBox_searchRange.getSelectedItem() == "연봉" && textField_salary.getText().equals(emptyText)) {
			whereClause = " where e.Salary > 0";
			
		}
		
		if (!textField_sub.getText().equals(emptyText)) { // sub textfield 에 값이 있으면 where 절 작성
			if (!Pattern.matches("^[0-9]*$", textField_sub.getText())) {
				JOptionPane.showMessageDialog(frame, "9자리 숫자를 입력하세요. (부하직원)", "ERROR",
						JOptionPane.ERROR_MESSAGE);  // Ssn은 숫자만 가능
				return;
				
			}else if(textField_sub.getText().toString().length() != 9) {
				JOptionPane.showMessageDialog(frame, "9자리 숫자를 입력하세요. (부하직원)", "ERROR",
						JOptionPane.ERROR_MESSAGE);  // Ssn은 CHAR(9)
				return;
				
			}else if(!ssnResult.contains(textField_sub.getText())) {
				JOptionPane.showMessageDialog(frame, "입력한 Ssn 을 갖는 직원이 없습니다. (부하직원)", "ERROR",
						JOptionPane.ERROR_MESSAGE);  // 해당 Ssn을 갖는 직원의 부하직원을 검색하는데 해당 Ssn을 갖는 직원 자체가 없는 경우
				return;
			}
			
			String subText = textField_sub.getText();
			whereClause = " where e.Super_ssn = '" + subText + "'";	
			
		}
		
		if(comboBox_searchRange.getSelectedItem() == "부하직원" && textField_sub.getText().equals(emptyText)) { // textfield 입력하지 않았을 때, 상사가 없는 직원 table 출력.
			whereClause = " where e.Super_ssn is NULL";	
		}


		// 검색 항목 받아오는거 구현하기
		int cntSelect = 0; // 체크박스 개수 (0일 때에는 select + query 에 그냥 추가하고, 0이 아니면 " , " 도 추가해야됨

		if (chckbxSearchName.isSelected()) { // Name 이 체크되면 실행
			if (cntSelect != 0) {
				selectStatement += ", ";
			} else {
				selectStatement = "select ";
			}
			cntSelect++;
			selectStatement += "concat(e.Fname, \" \", IFNULL(e.Minit, \"\"), \" \", e.Lname) as Name";
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
			selectStatement += "concat(s.Fname, \" \", IFNULL(s.Minit, \"\"), \" \", s.Lname) as Supervisor";
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
		
		// 검색 항목이 없을 때는 에러 메세지 띄우고 검색 실행 X
		if(!(chckbxSearchName.isSelected() || chckbxSearchSsn.isSelected() || chckbxSearchBdate.isSelected() || chckbxSearchAddress.isSelected() 
				|| chckbxSearchSex.isSelected() || chckbxSearchSalary.isSelected() || chckbxSearchSupervisor.isSelected() || chckbxSearchDepartment.isSelected())) {
			JOptionPane.showMessageDialog(frame, "검색 항목에 아무것도 체크되지 않았습니다. 다시 확인해주세요! (검색 항목)", "ERROR",
					JOptionPane.ERROR_MESSAGE);  // 검색 항목에 아무것도 체크되지 않았을 때
			return;
		} 
		
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
				String selected = "";
				
				if(dataTable.getValueAt(dataTable.getSelectedRow(), 1) != null) {
					selected = dataTable.getValueAt(dataTable.getSelectedRow(), 1).toString();
				}

				String selectedEssn = "";

				// delete 연산에 인자로 넘겨줄 Ssn 이 있으면 실행 (없으면 무시하고 delete 버튼을 누르면 에러메세지 출력하게 함)
				if (getColumnIndex(dataTable, "Ssn") != -1) {
					selectedEssn = dataTable.getValueAt(dataTable.getSelectedRow(), getColumnIndex(dataTable, "Ssn")).toString();					

					if (selectedSsn.contains(selectedEssn)) {
						selectedSsn.remove(selectedEssn);
						selectedEmp.remove(selected);
					} else {
						selectedSsn.add(selectedEssn);
						selectedEmp.add(selected);
					}
				}
				
				boolean ssnIncluded = Arrays.asList(selectAttribute).contains("Ssn"); // 검색 항목에 SSN이 빠졌는지 여부 판단;
				
				if (!ssnIncluded) {
					avgSalLb.setText("선택한 직원 평균 임금 : 검색 결과 표에 Ssn이 포함되어 있지 않으면 표기되지 않습니다.");
					selectedCountLb.setText("인원 수 : 검색 결과 표에 Ssn이 포함되어 있지 않으면 표기되지 않습니다.");	
					panel_1.revalidate();
				} else {
					selectedEmpLb.setText("선택한 직원 : " + selectedEmp);
					selectedCountLb.setText("인원 수 :" + selectedSsn.size());
					if(ssnIncluded) { // Ssn이 검색 항목에 표함되어 있으면 평균 연산
						// 평균 임금 연산
						CompanyDB companyDB = new CompanyDB(password);
						float avg = companyDB.retAvgSal(selectedSsn);
						avgSalLb.setText("선택한 직원 평균 임금 : " + avg);
					} else if(!ssnIncluded){
						avgSalLb.setText("선택한 직원 평균 임금 : 검색 결과 표에 Ssn이 포함되어 있지 않으면 표기되지 않습니다.");
					}
					panel_1.revalidate();
				}

			}
		});

	}

}