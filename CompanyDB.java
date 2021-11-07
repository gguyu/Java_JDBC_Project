package test3;

import java.sql.*;

public class CompanyDB {
	private Connection con = null;  // connection ��ü
	private Statement stmt = null;  // Statement ��ü
	private PreparedStatement pstmt = null;  // PreparedStatement ��ü
	
	// �˻� ���ǿ�
	// GUI ���� table ���� �� String array�� ��ȯ�޾Ƽ� ���
	private Object[][] searchResult;
	// ResultSet ���� ���� �����ִ� method �� ��� �� ���� ���ϱ� ���� ������ ���� ��
	private String getCountQuery = "select count(*), concat(e.Fname, e.Minit, e.Lname) as Name, e.Ssn, e.Bdate, e.Address, e.Sex, e.Salary, concat(s.Fname, s.Minit, s.Lname) as Supervisor, d.Dname as Department"
			+ " from EMPLOYEE as e left outer join EMPLOYEE as s on e.Super_ssn = s.Ssn join DEPARTMENT as d on e.Dno = d.Dnumber";
	private String queryWhere = " where";
	private String andText =" and";
	
	// �˻� ����, �˻� �׸� �޾ƿ��� �����ڷ� �̰� �������ֱ�
	private String whereQuery;
	private String selectQuery;
	private int select_columns;
	
	// insert�� �޾ƿ��� �����ڷ� �̰� �������ֱ�
	private String insertQuery;
	
	// �˻� ���� ������
	public CompanyDB(String selectQuery, String whereQuery, int select_columns) {
		this.selectQuery = selectQuery;
		this.whereQuery = whereQuery;
		this.select_columns = select_columns;
	}
	
	// ���Թ� ������
	public CompanyDB(String insertQuery) {
		this.insertQuery = insertQuery;
	}
	
	
	
	// �˻� ���� �Լ�
	public Object[][] searchDB() {
		ResultSet rs;
		
		try {
			// ���� url �� �����, ��й�ȣ
			String url="jdbc:mysql://localhost:3306/COMPANY?serverTimezon=UTC";
			String user="root";
			String pwd="root";
			
			// url �� �����, ��й�ȣ�� Connection ��ü ����
			con = DriverManager.getConnection(url,user,pwd);
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
	public void insertDB() {
		
		try {
			// ���� url �� �����, ��й�ȣ
			String url="jdbc:mysql://localhost:3306/COMPANY?serverTimezon=UTC";
			String user="root";
			String pwd="root";
			
			// url �� �����, ��й�ȣ�� Connection ��ü ����
			con = DriverManager.getConnection(url,user,pwd);
			System.out.println("���������� ����Ǿ����ϴ�.");
			
			pstmt = con.prepareStatement(insertQuery);
			pstmt.executeUpdate();
			
			
			
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

	
	// ���̺� �� üũ�ڽ� �����Ǹ� ���Ź�, ������ �����
	
	
	
	
	

}
