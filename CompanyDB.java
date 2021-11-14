package test3;

import java.sql.*;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;

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
	private String[] attributes;
	static HashSet<String> ssnResult, works_onEssnResult, department_Mgr_ssnResult, employee_Super_ssnResult;
	
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
	public CompanyDB(String selectQuery, String whereQuery, int select_columns, String password, String[] attributes) {
		this.selectQuery = selectQuery;
		this.whereQuery = whereQuery;
		this.select_columns = select_columns;
		this.base_pwd = password;
		this.attributes = attributes;
	}
	
	public CompanyDB() {
		
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
    
    // �Ϲ� ������, ��� �ӱ� ���� ��
	public CompanyDB(String password) {
		this.base_pwd = password;
	}
	
	// �˻� ����ó���� ���� ssn �޾ƿ���
	public void getSsn() {
		ResultSet rs;
		try {
			// url �� �����, ��й�ȣ�� Connection ��ü ����
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			stmt = con.createStatement();
			rs = stmt.executeQuery("select Ssn from EMPLOYEE");
			
			ssnResult = new HashSet<>();
			while(rs.next()) {
				ssnResult.add(rs.getString("Ssn").toString());
			}
			
			// ����
			rs.close();
			stmt.close();
			
		}catch (SQLException e) {  // connection ��ü�� ������ �� ���� ��
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
			ssnResult = new HashSet<>();
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
						if (Arrays.asList(attributes).contains("Ssn")) {
							ssnResult.add(rs.getString("Ssn").toString());
						}
						searchResult[count_rows][count_columns] = rs.getString(count_columns);
					}
					count_columns++;
				}
				count_columns = 0;
				count_rows++;
			}
			
			// ����ó����
			// employee�� ssn�� �����ϰ� �ִ� �͵��� Ȯ���ϱ� (gui���� delete�� �� �̿��� ����
			works_onEssnResult = new HashSet<String>();
			rs = stmt.executeQuery("select Essn from WORKS_ON");
			while(rs.next()) {
				works_onEssnResult.add(rs.getString("Essn").toString());
				
			}
			
			department_Mgr_ssnResult = new HashSet<String>();
			rs = stmt.executeQuery("select Mgr_ssn from DEPARTMENT");
			while(rs.next()) {
				department_Mgr_ssnResult.add(rs.getString("Mgr_ssn").toString());
				
			}
			
			employee_Super_ssnResult = new HashSet<String>();
			rs = stmt.executeQuery("select Super_ssn from EMPLOYEE");
			while(rs.next()) {
				employee_Super_ssnResult.add(rs.getString("Super_ssn"));
				
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
	public String insertDB(String eString) { //����
		String eMessage = ""; //����
		ResultSet rs;
		
		try {
			// url �� �����, ��й�ȣ�� Connection ��ü ����
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			
			pstmt = con.prepareStatement(insertQuery);
			pstmt.executeUpdate();
			
			// ssnResult �ݿ�
			ssnResult = new HashSet<String>();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select Ssn from EMPLOYEE");
			
			while(rs.next()) {
				ssnResult.add(rs.getString("Ssn").toString());
			}
			
			
			// ����
			rs.close();
			stmt.close();
			pstmt.close();
			
			
			// ���� �ܰ迡�� ���� �ֳ� üũ �� ���� �޽��� ����
			if ( eString != "") {
				if(eString == "Dno�� ���� �Է����� �ʾ� default ���� 1�� ����˴ϴ�.") {
					eMessage = "���ԵǾ����ϴ�. " + eString;
					return eMessage; // ����
				}
			}
			
			else {
				String insertMessage = "���ԵǾ����ϴ�.";				
				return eMessage; // ����
			}
			
			return eMessage;
			
		} catch (SQLException e) {
			eMessage = e.getMessage();
			e.printStackTrace();

			return eMessage;
		}
	}  // ���� ���� �Լ� �� 

	

	
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
	public String updateDB(String att, String value) {
		String eString = "";
		
		try {
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			
			String updateQuery;
			
			int cntUpdate = 0;
			while (cntUpdate < cntEssn) {
				updateQuery = updateQueryForm; // att �̸��̶� �� �ٲ�, �ѹ����� �ʱ�ȭ
				
				if(att != "Salary") {
					if(value.equals("")) {
						updateQuery = updateQuery + att + " = null" + " where " + "Ssn = " + updateEssn[cntUpdate] + ";" ;
					}else {
						updateQuery = updateQuery + att + "= '" + value + "'" + " where " + "Ssn = '" + updateEssn[cntUpdate] + "';" ;
					}
				}
				
				else {
					if(value.equals("")) {
						updateQuery = updateQuery + att + " = null" + " where " + "Ssn = " + updateEssn[cntUpdate] + ";" ; // �������� �ʿ� ����.
					}
					else {
						updateQuery = updateQuery + att + "= '" + value + "' " + " where " + "Ssn = " + updateEssn[cntUpdate] + ";" ; // �������� �ʿ� ����.
					}
				}
				
				pstmt = con.prepareStatement(updateQuery);
				pstmt.executeUpdate();
				
				cntUpdate ++;
			}
			
			return eString;
		}
		catch (SQLException e) {
			eString = e.getMessage();
			e.printStackTrace();
			return eString;
		}
	}
    
    // ���õ� ���� ��� �ӱ� ����, sum ���� ���� �޾Ƽ� ����� ���̱�
    public float retAvgSal(Set<String> ssnSet) {
		float avg = 0;
		ResultSet rs;
        
        int cntEmp = ssnSet.size(); // ������ �ޱ�
		if (cntEmp == 0) {return avg;} // ���� üũ�� �� Ǯ�� size�� 0�̸� �׳� 0 ��ȯ
		
		try {
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			
			String avgQuery = "select SUM(Salary) as SUM from Employee where ";
			
            Iterator<String> iter_selectedSsn = ssnSet.iterator();
            
			int cnt = 0;
			while(iter_selectedSsn.hasNext()) {
				avgQuery = avgQuery+"ssn = " + iter_selectedSsn.next();
				
				if(iter_selectedSsn.hasNext()) {
					avgQuery = avgQuery + " or ";
				}
			}
			
			avgQuery = avgQuery + ";";
			
			
			stmt = con.createStatement();
			rs = stmt.executeQuery(avgQuery);
			
			if(rs.next()) {
				avg = rs.getFloat("SUM")/ssnSet.size(); // �ӱ����� ���õ� ���ڷ� ������ ���
			}
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return avg;
	}
    
    
    public ArrayList<Integer> getDnoList() {
    	ArrayList<Integer> arrInt = new ArrayList<Integer>();
    	
    	try {
    		con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			
			String getdnoQuery = "select Dnumber from department order by Dnumber ASC;";
			
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(getdnoQuery);
			
			while(rs.next()) {
				arrInt.add(rs.getInt(1));
			}
			
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	return arrInt;
    	
    }
    
    public float getSal(String supSsn) {
    	Float supSal = (float) 0;
    	
    	try {
    		con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			
			String getSupSalQuery = "select Salary from employee where ssn =" + supSsn + ";";
			
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(getSupSalQuery);
			
			if(rs.next()) {
				supSal = rs.getFloat(1);
			}
			
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	return supSal;
    	
    }
    // ��� Ssn �޾ƿ��� �Լ�
    public String getSupSsn(String Ssn) {
    	String supSsn = "";
    	
    	try {
    		con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			
			String getSupSsnQuery = "select Super_ssn from employee where ssn =" + Ssn + ";";
			
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(getSupSsnQuery);
			
			if(rs.next()) {
				supSsn = rs.getString(1);
			}
			
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	return supSsn;
    	
    }
    
    public Float getMaxSupvisedSal(String essn){
    	float maxSupervisedSal = 0;
    	
    	try {
    		con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			
			String getSupSsnQuery = "select salary from employee where Super_ssn =" + essn + ";";
			
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(getSupSsnQuery);
			
			while(rs.next()){
				String tmpSal = rs.getString(1);
				
				if (tmpSal == null) {
					return maxSupervisedSal;
				}
				
				else if(maxSupervisedSal < Float.parseFloat(tmpSal)) {
					maxSupervisedSal = rs.getFloat(1);
				}
			}
			
			return maxSupervisedSal;
			
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	return maxSupervisedSal;
    }
	
}