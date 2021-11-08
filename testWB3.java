package test3;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class testWB3 {

   private JFrame frame;
   
   private JTextField textField_salary;  // ���� �˻�â ��ü
   private JTextField textField_sub; // �������� �˻�â ��ü
   private JTable dataTable;  // DB table ��ü
   private JPanel panel_1;
   
   // �˻� �׸�: checkbox ���� �޾ƿ� ����
   private String selectStatement;
   private String fromStatement;
   private String whereClause;  // null �̸� where�� ����
   private String[] selectAttribute;
   // �˻� ����: combobox �� textfield ���� �޾ƿ� ����, default �� ��ü�̹Ƿ� �ʱⰪ�� null
   private JButton btnSearch;
   private JLabel selectedEmpLb, selectedCountLb;
   private DefaultTableModel tableModel;
   private Set<String> selectedEmp;
   
   
   
   

   /**
    * Launch the application.
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
                    
            // table �����
            CompanyDB companyDB = new CompanyDB(selectStatement, whereClause, columnCnt + 1);
            
            int cntRowModel = tableModel.getRowCount();
            int cntRowM = 0;
            while (cntRowM < cntRowModel) {
               tableModel.removeRow(0);
               cntRowM++;
            }
            
            tableModel.setDataVector(companyDB.searchDB(), selectAttribute);
            
            // checkBox ����
            dataTable.getColumn("����").setCellRenderer(dcr);
            JCheckBox checkBox = new JCheckBox();
            checkBox.setHorizontalAlignment(JLabel.CENTER);
            
            // ������ ����, �ο� ��
            selectedEmp = new HashSet<>();
            dataTable.getColumn("����").setCellEditor(new DefaultCellEditor(checkBox));
            checkBox.addActionListener(new ActionListener() {
            	@Override
            	public void actionPerformed(ActionEvent e) {
            		String selected = dataTable.getValueAt(dataTable.getSelectedRow(), 1).toString();
            		if(selectedEmp.contains(selected)) {
            			selectedEmp.remove(selected);
            		} else {
            			selectedEmp.add(selected);
            		}
            		selectedEmpLb.setText("������ ���� : " + selectedEmp);
            		selectedCountLb.setText("�ο� �� :" + selectedEmp.size());
            		panel_1.revalidate();
            	}
            });
            
            
         }
      });
      
      selectStatement = null;
      fromStatement = null;
      whereClause = null;
      
      // table ���̱� ��

      // ����, ����, ���� panel
      panel_1 = new JPanel();
      panel_1.setBounds(0, 376, 986, 97);
      frame.getContentPane().add(panel_1);
      panel_1.setLayout(null);
      
      selectedEmpLb = new JLabel();
      selectedEmpLb.setText("������ ���� :");
      selectedEmpLb.setBounds(14, 12, 1000, 18);
      panel_1.add(selectedEmpLb);
      
      selectedCountLb = new JLabel();
      selectedCountLb.setText("�ο� �� :");
      selectedCountLb.setBounds(14, 42, 100, 18);
      panel_1.add(selectedCountLb);
      
      
      
      
      // ���� ��ư
      JButton btnInsertDisp = new JButton("���ο� ������ ����");  // ��ư ������ �����ϴ� ���ο� ���̾ƿ� ������
      btnInsertDisp.setBounds(815, 10, 145, 23);
      panel_1.add(btnInsertDisp);
      btnInsertDisp.addActionListener(new ActionListener() {
    	 @Override
         public void actionPerformed(ActionEvent e) {
        	InsertSetting insertSetting = new InsertSetting();  // ���� ���̾ƿ� ��ü �����ؼ� â ���
            insertSetting.launch();
         }
      });
      
      // ���� ��ư
      JButton btnDeleteDisp = new JButton("������ ������ ����"); // ��ư ������ ������ ������ ������ DB���� ����
      btnDeleteDisp.setBounds(815, 40, 145, 23);
      panel_1.add(btnDeleteDisp);
      btnDeleteDisp.addActionListener(new ActionListener() {
    	 @Override
         public void actionPerformed(ActionEvent e) {
    		 // delete From employee where selectedEmp�� ���� ���
         }
      });
      

      
      // ���� ��ư
      JLabel lblNewLabel_6 = new JLabel("���� :");
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
    		  if (comboBox_update.getSelectedItem() == "Address") {
    			  // update employee set address = textField.getText()
    		  } else if (comboBox_update.getSelectedItem() == "Sex") {
    			  // update employee set sex = textFiled.getText()
    		  } else if (comboBox_update.getSelectedItem() == "Salary") {
    			  
    		  }
    		  
    		  // ��ü ���� + ��������
    		  
    	  }
      });
      
      
      
      
   }  // initialize() �Լ� ��
   
   DefaultTableCellRenderer dcr = new DefaultTableCellRenderer() {
      public Component getTableCellRendererComponent // ��������
      (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         JCheckBox box = new JCheckBox();
         box.setSelected(((Boolean) value).booleanValue());
         box.setHorizontalAlignment(JLabel.CENTER);
         
         box.addMouseListener(new MouseAdapter() {
        	 @Override
             public void mouseClicked(MouseEvent e) {
        		 System.out.println("s");
        	 }
         });
         
         return box;
      }
   };
}