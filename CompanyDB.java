package test3;

import java.sql.*;
import java.util.Set;

public class CompanyDB {
	private Connection con = null;  // connection ��ü
	private Statement stmt = null;  // Statement ��ü
	private PreparedStatement pstmt = null;  // PreparedStatement ��ü
	
	
	// ���� url �� �����, ��й�ȣ
	private String base_url="jdbc:mysql://localhost:3306/COMPANY?serverTimezon=UTC";
	private String base_user="root";
	private String base_pwd;
	
	// query ����
	private String queryWhere = " where";
	private String andText =" and";
	
	// �˻� ���ǿ�
	// GUI ���� table ���� �� String array�� ��ȯ�޾Ƽ� ���
	private Object[][] searchResult;
	// ResultSet ���� ���� �����ִ� method �� ��� �� ���� ���ϱ� ���� ������ ���� ��
	private String getCountQuery = "select count(*), concat(e.Fname, e.Minit, e.Lname) as Name, e.Ssn, e.Bdate, e.Address, e.Sex, e.Salary, concat(s.Fname, s.Minit, s.Lname) as Supervisor, d.Dname as Department"
			+ " from EMPLOYEE as e left outer join EMPLOYEE as s on e.Super_ssn = s.Ssn join DEPARTMENT as d on e.Dno = d.Dnumber";
	
	
	// �˻� ����, �˻� �׸� �޾ƿ��� �����ڷ� �̰� �������ֱ�
	private String whereQuery;
	private String selectQuery;
	private int select_columns;
	
	// insert�� �޾ƿ��� �����ڷ� �̰� �������ֱ�
	private String insertQuery;
	
	// delete�� �޾ƿ��� �����ڷ� �̰� �������ֱ�
	private String[] deleteESsn;  // ����� ���� ������ Ssn �迭
	private int cntEssn;  // ����� ���� ������ ��
	// delete �� query
	private String deleteQueryForm = "delete from EMPLOYEE where Ssn = ";  // �ڿ� ������ Ssn ���̱�
	
	private String updateQueryForm = "update EMPLOYEE set "; // ������ ATT�� where �� ���� ���̱�
	
	private String[] updateEssn;
	
	// �˻� ���� ������
	public CompanyDB(String selectQuery, String whereQuery, int select_columns, String password) {
		this.selectQuery = selectQuery;
		this.whereQuery = whereQuery;
		this.select_columns = select_columns;
		this.base_pwd = password;
	}
	
	// ���Թ� ������
	public CompanyDB(String insertQuery, String password) {
		this.insertQuery = insertQuery;
		this.base_pwd = password;
	}
	
	// ���� ���� ������ ����??
	
	// ������ ������
	public CompanyDB(String[] deleteEssn, int cntEssn, String password) {
		this.deleteESsn = deleteEssn;
		this.cntEssn = cntEssn;
		this.base_pwd = password;
	}
	
	// ���Ź� ������
	public CompanyDB(String[] updateEssn, int cntEssn, String password, String stat) {
		this.updateEssn = updateEssn;
		this.cntEssn = cntEssn;
		this.base_pwd = password;
	}
	
	
	// �˻� ���� �Լ�
	public Object[][] searchDB() {
		ResultSet rs;
		
		try {
			// ���� url �� �����, ��й�ȣ
			
			// url �� �����, ��й�ȣ�� Connection ��ü ����
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			
			int select_rows = 0;	// ���� getCountQuery �̿��ؼ� �� ����	
			int count_rows = 0;		// string array �� index �� ���
			int count_columns = 0;	// string array �� index �� ���
			
			// ���ڷ� �޾ƿ� whereQuery �� null �̸� �״��, null �� �ƴϸ� string ��ħ	
			if(whereQuery != null) {
				getCountQuery = getCountQuery + whereQuery;
				selectQuery = selectQuery + whereQuery;
			}
			
			// ��� �� ���� ���ϱ�
			stmt = con.createStatement();
			rs = stmt.executeQuery(getCountQuery);
			
			if(rs.next()) {
				select_rows = rs.getInt(1);
			}
			
			// �˻� ����� ���� string array ���� (�� �� ��� �� ���� ���� �� ���)
			searchResult = new Object[select_rows][select_columns];
			
			// �ϴ��� table ���� ������ ���� attribute �� ��� ��� (�˻����� ��ü, �˻� �׸� ��ü)
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectQuery);
			
			// �˻� ����� string array �� searchResult �� ����
			while(rs.next()) {
				
				while (count_columns < select_columns) {
					// "����" �� �ʱⰪ false
					if (count_columns == 0) {
						searchResult[count_rows][count_columns] = false;
					} else {
						searchResult[count_rows][count_columns] = rs.getString(count_columns);
					}
					count_columns++;
				}
				count_columns = 0;
				count_rows++;
			}
			
			
			// ����
			rs.close();
			stmt.close();
			
		
		} catch (SQLException e) {  // connection ��ü�� ������ �� ���� ��
			System.err.println("������ �� �����ϴ�.");
			e.printStackTrace();
			
			
		}
		
		// ����
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return searchResult;
		
	} // �˻� ���� �Լ� ��
	

	
	// ���� ���� �Լ�
	public void insertDB(String eString) {
		
		try {
			
			// ��� �ܰ迡�� ���� �ֳ� üũ
			if ( eString != "") {
				if(eString == "Dno�� ���� �Է����� �ʾ� default ���� 1�� ����˴ϴ�.") { //����
					ewin ew = new ewin(eString);
					ew.launch(eString);
				}
			}
            
			// url �� �����, ��й�ȣ�� Connection ��ü ����
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			
			pstmt = con.prepareStatement(insertQuery);
			pstmt.executeUpdate();
			
			// ����
			pstmt.close();
			
		} catch (SQLException e) {
			String err = e.getMessage();
			ewin ew = new ewin(err);
			ew.launch(err);
			
			e.printStackTrace();
		}
		
		// ���� --> �����
		/*
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
					
		}*/
		
		
	}  // ���� ���� �Լ� �� 

	
	// ���̺� �� üũ�ڽ� �����Ǹ� ���Ź�, ������ �����
	
	// ���� ���� �Լ�
	public void deleteDB() {
		
		try {
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			
			// ������ �°� �����ϱ� , deleteESsn, deleteQueryForm, cntEssn
			String deleteQuery;
			int cntDelete = 0;
			while (cntDelete < cntEssn) {
				deleteQuery = deleteQueryForm + deleteESsn[cntDelete];
				
				pstmt = con.prepareStatement(deleteQuery);
				pstmt.executeUpdate();
				
				cntDelete ++;
			}
			
			// ����
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// ����
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}  // ���� ���� �Լ� ��
	
	// ���� ���� �Լ�
	public void updateDB(String att, String value) {
		try {
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			
			String updateQuery;
			
			// salary�� ���� ��� value�� �����ΰ� Ȯ���ϴ� �۾�
			if (att == ("Salary")&&!value.matches("[+-]?\\d*(\\.\\d+)?")) {
				Exception e = new Exception("���ڸ� �Է����ּ���!");
				
				throw e;
			}
			
			int cntUpdate = 0;
			while (cntUpdate < cntEssn) {
				updateQuery = updateQueryForm; // att �̸��̶� �� �ٲ�, �ѹ����� �ʱ�ȭ
				
				if(att != "Salary") {
					System.out.println(updateQuery);
					updateQuery = updateQuery + att + "= '" + value + "'" + " where " + "Ssn = '" + updateEssn[cntUpdate] + "';" ;
					System.out.println(updateQuery);
				}
				
				else {
					updateQuery = updateQuery + att + "= '" + value + "' " + " where " + "Ssn = " + updateEssn[cntUpdate] + ";" ; // �������� �ʿ� ����.
					System.out.println(updateQuery);
				}
				
				pstmt = con.prepareStatement(updateQuery);
				pstmt.executeUpdate();
				
				cntUpdate ++;
			}
			
		}
		catch (SQLException e) {
			String err = e.getMessage();
			ewin ew = new ewin(err);
			ew.launch(err);
			e.printStackTrace();
		}
		
		catch (Exception e) {
			String err = e.getMessage();
			ewin ew = new ewin(err);
			ew.launch(err);
			
			e.printStackTrace();
		}
	}		
	
}