package test3;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class testWB3 {

	private JFrame frame;
	
	private JTextField textField_salary;  // 연봉 검색창 객체
	private JTextField textField_sub; // 부하직원 검색창 객체
	private JTable dataTable;  // DB table 객체
	
	// 검색 항목: checkbox 에서 받아올 것임
	private String selectStatement;
	private String fromStatement;
	private String whereClause;  // null 이면 where절 없음
	private String[] selectAttribute;
	// 검색 범위: combobox 나 textfield 에서 받아올 것임, default 는 전체이므로 초기값은 null
	private JButton btnSearch;
	
	// JTable 만들 때 쓸 model
	private DefaultTableModel tableModel;
	

	/**
	 * 앱 실행
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testWB3 window = new testWB3();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * 앱 생성
	 */
	public testWB3() {
		initialize();
	}

	/**
	 * 초기 설정
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setForeground(SystemColor.desktop);
		frame.setBounds(100, 100, 1002, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
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
		comboBox_department.setModel(new DefaultComboBoxModel(new String[] {"Research", "Administration", "Headquarters"}));
		comboBox_department.setBounds(150, 16, 107, 23);
		panel.add(comboBox_department);
		comboBox_department.setVisible(false);
		
		comboBox_department.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox_department.getSelectedItem() == "Research") {
					whereClause = " where e.Dno = 5";
				} else if (comboBox_department.getSelectedItem() == "Administration") {
					whereClause = " where e.Dno = 4";
				} else if (comboBox_department.getSelectedItem() == "Headquarters"){
					whereClause = " where e.Dno = 1";
				}
			}
		});
		
		// 성별
		JComboBox comboBox_sex = new JComboBox();
		comboBox_sex.setModel(new DefaultComboBoxModel(new String[] {"M", "F"}));
		comboBox_sex.setBounds(265, 16, 53, 23);
		panel.add(comboBox_sex);
		comboBox_sex.setVisible(false);
		
		comboBox_sex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox_sex.getSelectedItem() == "M") {
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
		comboBox_bdate.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));
		comboBox_bdate.setBounds(458, 16, 47, 23);
		panel.add(comboBox_bdate);
		comboBox_bdate.setVisible(false);
		
		comboBox_bdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int month = Integer.parseInt(comboBox_bdate.getSelectedItem().toString());
				switch(month) {
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
		comboBox_searchRange.setModel(new DefaultComboBoxModel(new String[] {"전체", "부서", "성별", "연봉", "생일", "부하직원"}));
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
				
				else if (comboBox_searchRange.getSelectedItem() == "부서" ) {
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
				
				else if (comboBox_searchRange.getSelectedItem() == "성별" ) {
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
		
		JCheckBox chckbxSearchName = new JCheckBox("Name");
		chckbxSearchName.setSelected(true);
		chckbxSearchName.setBounds(69, 46, 59, 23);
		panel.add(chckbxSearchName);
		
		JCheckBox chckbxSearchSsn = new JCheckBox("Ssn");
		chckbxSearchSsn.setSelected(true);
		chckbxSearchSsn.setBounds(132, 46, 47, 23);
		panel.add(chckbxSearchSsn);
		
		JCheckBox chckbxSearchBdate = new JCheckBox("Bdate");
		chckbxSearchBdate.setSelected(true);
		chckbxSearchBdate.setBounds(183, 46, 59, 23);
		panel.add(chckbxSearchBdate);
		
		JCheckBox chckbxSearchAddress = new JCheckBox("Address");
		chckbxSearchAddress.setSelected(true);
		chckbxSearchAddress.setBounds(246, 46, 80, 23);
		panel.add(chckbxSearchAddress);
		
		JCheckBox chckbxSearchSex = new JCheckBox("Sex");
		chckbxSearchSex.setSelected(true);
		chckbxSearchSex.setBounds(330, 46, 47, 23);
		panel.add(chckbxSearchSex);
		
		JCheckBox chckbxSearchSalary = new JCheckBox("Salary");
		chckbxSearchSalary.setSelected(true);
		chckbxSearchSalary.setBounds(381, 46, 62, 23);
		panel.add(chckbxSearchSalary);
		
		JCheckBox chckbxSearchSupervisor = new JCheckBox("Supervisor");
		chckbxSearchSupervisor.setSelected(true);
		chckbxSearchSupervisor.setBounds(447, 46, 90, 23);
		panel.add(chckbxSearchSupervisor);
		
		JCheckBox chckbxSearchDepartment = new JCheckBox("Department");
		chckbxSearchDepartment.setSelected(true);
		chckbxSearchDepartment.setBounds(541, 46, 107, 23);
		panel.add(chckbxSearchDepartment);
		
		// 검색 버튼 구현
		btnSearch = new JButton("\uAC80\uC0C9");
		btnSearch.setForeground(SystemColor.desktop);
		btnSearch.setBounds(837, 42, 107, 30);
		panel.add(btnSearch);
		
		
		// table 생성
		tableModel = new DefaultTableModel();
		dataTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(dataTable);
		frame.getContentPane().add(scrollPane);
		
		scrollPane.setViewportView(dataTable);
		scrollPane.setBounds(10, 96, 964, 277);
		
		
		
		
		
		btnSearch.addMouseListener(new MouseAdapter() {
			String emptyText = "";  // textfield 가 비어있는지 비교 용도
			@Override
			public void mouseClicked(MouseEvent e) {
				// 검색 범위에서 textField 로 받아오는 경우
				if(!textField_salary.getText().equals(emptyText)) {  // salary textfield 에 값이 있으면 where 절 작성
					String salaryText = textField_salary.getText();
					whereClause = " where e.Salary > " + salaryText;
				} else if(!textField_sub.getText().equals(emptyText)) {  // sub textfield 에 값이 있으면 where 절 작성
					String subText = textField_sub.getText();
					whereClause = " where e.Super_ssn = '" + subText + "'";
				}
				
				System.out.println(whereClause);
				
				// 검색 항목 받아오는거 구현하기
				int cntSelect = 0;  // 체크박스 개수 (0일 때에는 select +  query 에 그냥 추가하고, 0이 아니면 " , " 도 추가해야됨
				
				if(chckbxSearchName.isSelected()) {  // Name 이 체크되면 실행
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "concat(e.Fname, e.Minit, e.Lname) as Name";	
				}
				
				if(chckbxSearchSsn.isSelected()) {  // Ssn 이 체크되면 실행
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "e.Ssn";	
				}
				
				if(chckbxSearchBdate.isSelected()) {  // Bdate 가 체크되면 실행
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "e.Bdate";
				}
				
				if(chckbxSearchAddress.isSelected()) {  // Address 가 체크되면 실행
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "e.Address";
				}
				
				if(chckbxSearchSex.isSelected()) {  // Sex 가 체크되면 실행
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "e.Sex";
				}
				
				if(chckbxSearchSalary.isSelected()) {  // Salary 가 체크되면 실행
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "e.Salary";
				}
				
				if(chckbxSearchSupervisor.isSelected()) {  // Supervisor 가 체크되면 실행
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "concat(s.Fname, s.Minit, s.Lname) as Supervisor";
				}
				
				if(chckbxSearchDepartment.isSelected()) {  // Department 가 체크되면 실행
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "d.Dname as Department";
				}
				
				// 체크박스에 따른 from 절 설정
				
				
				if (chckbxSearchSupervisor.isSelected() && chckbxSearchDepartment.isSelected()) {  // 검색 항목에 supervisor, department 둘 다 있을 때
					fromStatement = " from EMPLOYEE as e left outer join EMPLOYEE as s on e.Super_ssn = s.Ssn join DEPARTMENT as d on e.Dno = d.Dnumber";
				} else if (chckbxSearchSupervisor.isSelected()) {  // 검색 항목에 supervisor 가 있을 때
					fromStatement = " from EMPLOYEE as e left outer join EMPLOYEE as s on e.Super_ssn = s.Ssn";
				} else if (chckbxSearchDepartment.isSelected()) {  // 검색 항목에 department 가 있을 때
					fromStatement = " from EMPLOYEE as e join DEPARTMENT as d on e.Dno = d.Dnumber";
				} else {  // 검색 항목에 supervisor, department 둘 다 없을 때
					fromStatement =" from EMPLOYEE as e";
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
				
				/*
				// 체크박스 실험
				Class[] columnTypes = new Class[columnCnt];
				int type_Cnt = 1;
				columnTypes[0] = Boolean.class;
				while (type_Cnt < columnCnt) {
					columnTypes[type_Cnt] =Object.class;
					type_Cnt++;
				}
				*/
				
				// table 만들기
				CompanyDB companyDB = new CompanyDB(selectStatement, whereClause, columnCnt + 1);
				
				int cntRowModel = tableModel.getRowCount();
				int cntRowM = 0;
				while (cntRowM < cntRowModel) {
					tableModel.removeRow(0);
					cntRowM++;
				}
				
				tableModel.setDataVector(companyDB.searchDB(), selectAttribute);
				
	
				// companyDB.searchDB(), selectAttribute
				
				
				// System.out.println(dataTable.getColumnClass(0));
				
			}
		});
		
		selectStatement = null;
		fromStatement = null;
		whereClause = null;
		
		
		// table 내에 check 박스 구현해야함.
		
		
		// table 보이기 끝
		
		
		
		
		
		
		
		
		
		
		// 삽입, 삭제, 갱신 panel
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 376, 986, 97);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		// 삽입 버튼
		JButton btnInsertDisp = new JButton("새로운 데이터 삽입");  // 버튼 누르면 삽입하는 새로운 레이아웃 보여줌
		btnInsertDisp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InsertSetting insertSetting = new InsertSetting();  // 삽입 레이아웃 객체 생성해서 창 띄움
				insertSetting.launch();
			}
		});
		btnInsertDisp.setBounds(815, 10, 145, 23);
		panel_1.add(btnInsertDisp);
		
		// 삭제 버튼
		// 갱신 버튼
		
		
		
		
		
	}  // initialize() 함수 끝
}












