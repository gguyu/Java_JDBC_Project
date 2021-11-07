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
	
	private JTextField textField_salary;  // ���� �˻�â ��ü
	private JTextField textField_sub; // �������� �˻�â ��ü
	private JTable dataTable;  // DB table ��ü
	
	// �˻� �׸�: checkbox ���� �޾ƿ� ����
	private String selectStatement;
	private String fromStatement;
	private String whereClause;  // null �̸� where�� ����
	private String[] selectAttribute;
	// �˻� ����: combobox �� textfield ���� �޾ƿ� ����, default �� ��ü�̹Ƿ� �ʱⰪ�� null
	private JButton btnSearch;
	
	// JTable ���� �� �� model
	private DefaultTableModel tableModel;
	

	/**
	 * �� ����
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
	 * �� ����
	 */
	public testWB3() {
		initialize();
	}

	/**
	 * �ʱ� ����
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setForeground(SystemColor.desktop);
		frame.setBounds(100, 100, 1002, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// �˻� ���� panel
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 986, 83);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("�˻� ����");
		lblNewLabel.setBounds(12, 20, 57, 15);
		panel.add(lblNewLabel);
		
		
		// �˻� ���� ����
		
		// �μ�
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
		
		// ����
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
		
		// ����
		textField_salary = new JTextField();
		textField_salary.setBounds(330, 17, 116, 21);
		panel.add(textField_salary);
		textField_salary.setColumns(10);
		textField_salary.setVisible(false);
		
		
		// ����
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
					
				} // swith �� ��
				
			}
		});
		
		// ��������
		textField_sub = new JTextField();
		textField_sub.setBounds(517, 17, 123, 21);
		panel.add(textField_sub);
		textField_sub.setColumns(10);
		textField_sub.setVisible(false);
		
		// ���� combobox �� ���¿� ���� ���� combobox �� textfield ����
		JComboBox comboBox_searchRange = new JComboBox();
		
		// ��ü => �޺��ڽ� X / �μ�, ����, ���� => �޺��ڽ� O / ����, �������� => �Է�ĭ O
		comboBox_searchRange.setModel(new DefaultComboBoxModel(new String[] {"��ü", "�μ�", "����", "����", "����", "��������"}));
		comboBox_searchRange.setBounds(69, 16, 72, 23);
		panel.add(comboBox_searchRange);
		
		comboBox_searchRange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBox_searchRange.getSelectedItem() == "��ü") {
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
				
				else if (comboBox_searchRange.getSelectedItem() == "�μ�" ) {
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
				
				else if (comboBox_searchRange.getSelectedItem() == "����" ) {
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
				
				else if (comboBox_searchRange.getSelectedItem() == "����") {
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
				
				else if (comboBox_searchRange.getSelectedItem() == "����") {
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
				
				else if (comboBox_searchRange.getSelectedItem() == "��������") {
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
		
		
		
		
		// �˻� �׸� ����
		JLabel lblNewLabel_1 = new JLabel("�˻� �׸�");
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
		
		// �˻� ��ư ����
		btnSearch = new JButton("\uAC80\uC0C9");
		btnSearch.setForeground(SystemColor.desktop);
		btnSearch.setBounds(837, 42, 107, 30);
		panel.add(btnSearch);
		
		
		// table ����
		tableModel = new DefaultTableModel();
		dataTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(dataTable);
		frame.getContentPane().add(scrollPane);
		
		scrollPane.setViewportView(dataTable);
		scrollPane.setBounds(10, 96, 964, 277);
		
		
		
		
		
		btnSearch.addMouseListener(new MouseAdapter() {
			String emptyText = "";  // textfield �� ����ִ��� �� �뵵
			@Override
			public void mouseClicked(MouseEvent e) {
				// �˻� �������� textField �� �޾ƿ��� ���
				if(!textField_salary.getText().equals(emptyText)) {  // salary textfield �� ���� ������ where �� �ۼ�
					String salaryText = textField_salary.getText();
					whereClause = " where e.Salary > " + salaryText;
				} else if(!textField_sub.getText().equals(emptyText)) {  // sub textfield �� ���� ������ where �� �ۼ�
					String subText = textField_sub.getText();
					whereClause = " where e.Super_ssn = '" + subText + "'";
				}
				
				System.out.println(whereClause);
				
				// �˻� �׸� �޾ƿ��°� �����ϱ�
				int cntSelect = 0;  // üũ�ڽ� ���� (0�� ������ select +  query �� �׳� �߰��ϰ�, 0�� �ƴϸ� " , " �� �߰��ؾߵ�
				
				if(chckbxSearchName.isSelected()) {  // Name �� üũ�Ǹ� ����
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "concat(e.Fname, e.Minit, e.Lname) as Name";	
				}
				
				if(chckbxSearchSsn.isSelected()) {  // Ssn �� üũ�Ǹ� ����
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "e.Ssn";	
				}
				
				if(chckbxSearchBdate.isSelected()) {  // Bdate �� üũ�Ǹ� ����
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "e.Bdate";
				}
				
				if(chckbxSearchAddress.isSelected()) {  // Address �� üũ�Ǹ� ����
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "e.Address";
				}
				
				if(chckbxSearchSex.isSelected()) {  // Sex �� üũ�Ǹ� ����
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "e.Sex";
				}
				
				if(chckbxSearchSalary.isSelected()) {  // Salary �� üũ�Ǹ� ����
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "e.Salary";
				}
				
				if(chckbxSearchSupervisor.isSelected()) {  // Supervisor �� üũ�Ǹ� ����
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "concat(s.Fname, s.Minit, s.Lname) as Supervisor";
				}
				
				if(chckbxSearchDepartment.isSelected()) {  // Department �� üũ�Ǹ� ����
					if (cntSelect != 0) {
						selectStatement += ", ";
					} else {
						selectStatement = "select ";
					}
					cntSelect++;
					selectStatement += "d.Dname as Department";
				}
				
				// üũ�ڽ��� ���� from �� ����
				
				
				if (chckbxSearchSupervisor.isSelected() && chckbxSearchDepartment.isSelected()) {  // �˻� �׸� supervisor, department �� �� ���� ��
					fromStatement = " from EMPLOYEE as e left outer join EMPLOYEE as s on e.Super_ssn = s.Ssn join DEPARTMENT as d on e.Dno = d.Dnumber";
				} else if (chckbxSearchSupervisor.isSelected()) {  // �˻� �׸� supervisor �� ���� ��
					fromStatement = " from EMPLOYEE as e left outer join EMPLOYEE as s on e.Super_ssn = s.Ssn";
				} else if (chckbxSearchDepartment.isSelected()) {  // �˻� �׸� department �� ���� ��
					fromStatement = " from EMPLOYEE as e join DEPARTMENT as d on e.Dno = d.Dnumber";
				} else {  // �˻� �׸� supervisor, department �� �� ���� ��
					fromStatement =" from EMPLOYEE as e";
				} 
				
				
				// üũ�ڽ��� ���� string[] columns ���� (table ���� �� column ����� ���)
				int columnCnt = 0;
				selectAttribute = new String[cntSelect + 1];
				selectAttribute[0] = "����";
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
				// üũ�ڽ� ����
				Class[] columnTypes = new Class[columnCnt];
				int type_Cnt = 1;
				columnTypes[0] = Boolean.class;
				while (type_Cnt < columnCnt) {
					columnTypes[type_Cnt] =Object.class;
					type_Cnt++;
				}
				*/
				
				// table �����
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
		
		
		// table ���� check �ڽ� �����ؾ���.
		
		
		// table ���̱� ��
		
		
		
		
		
		
		
		
		
		
		// ����, ����, ���� panel
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 376, 986, 97);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		// ���� ��ư
		JButton btnInsertDisp = new JButton("���ο� ������ ����");  // ��ư ������ �����ϴ� ���ο� ���̾ƿ� ������
		btnInsertDisp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InsertSetting insertSetting = new InsertSetting();  // ���� ���̾ƿ� ��ü �����ؼ� â ���
				insertSetting.launch();
			}
		});
		btnInsertDisp.setBounds(815, 10, 145, 23);
		panel_1.add(btnInsertDisp);
		
		// ���� ��ư
		// ���� ��ư
		
		
		
		
		
	}  // initialize() �Լ� ��
}












