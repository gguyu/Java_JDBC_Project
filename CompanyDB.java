package test3;

import java.sql.*;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;

public class CompanyDB {
	private Connection con = null;  // connection 객체
	private Statement stmt = null;  // Statement 객체
	private PreparedStatement pstmt = null;  // PreparedStatement 객체
	
	
	// 접속 url 과 사용자, 비밀번호
	private String base_url="jdbc:mysql://localhost:3306/COMPANY?serverTimezon=UTC";
	private String base_user="root";
	private String base_pwd;
	
	// query 조합
	private String queryWhere = " where";
	private String andText =" and";
	
	// 검색 질의용
	// GUI 에서 table 만들 때 String array를 반환받아서 사용
	private Object[][] searchResult;
	private String[] attributes;
	static HashSet<String> ssnResult, works_onEssnResult, department_Mgr_ssnResult, employee_Super_ssnResult;
	
	// ResultSet 에서 개수 구해주는 method 가 없어서 행 개수 구하기 위한 쿼리를 따로 둠
	private String getCountQuery = "select count(*), concat(e.Fname, e.Minit, e.Lname) as Name, e.Ssn, e.Bdate, e.Address, e.Sex, e.Salary, concat(s.Fname, s.Minit, s.Lname) as Supervisor, d.Dname as Department"
			+ " from EMPLOYEE as e left outer join EMPLOYEE as s on e.Super_ssn = s.Ssn join DEPARTMENT as d on e.Dno = d.Dnumber";
	
	
	// 검색 범위, 검색 항목 받아오면 생성자로 이거 설정해주기
	private String whereQuery;
	private String selectQuery;
	private int select_columns;
	
	// insert문 받아오면 생성자로 이거 설정해주기
	private String insertQuery;
	
	// delete문 받아오면 생성자로 이거 설정해주기
	private String[] deleteESsn;  // 지우고 싶은 직원의 Ssn 배열
	private int cntEssn;  // 지우고 싶은 직원의 수
	// delete 용 query
	private String deleteQueryForm = "delete from EMPLOYEE where Ssn = ";  // 뒤에 직원의 Ssn 붙이기
	
	private String updateQueryForm = "update EMPLOYEE set "; // 변경할 ATT랑 where 절 만들어서 붙이기
	
	private String[] updateEssn;
	
	// 검색 질의 생성자
	public CompanyDB(String selectQuery, String whereQuery, int select_columns, String password, String[] attributes) {
		this.selectQuery = selectQuery;
		this.whereQuery = whereQuery;
		this.select_columns = select_columns;
		this.base_pwd = password;
		this.attributes = attributes;
	}
	
	public CompanyDB() {
		
	}
	// 삽입문 생성자
	public CompanyDB(String insertQuery, String password) {
		this.insertQuery = insertQuery;
		this.base_pwd = password;
	}
	
	// 삽입 삭제 생성자 결합??
	
	// 삭제문 생성자
	public CompanyDB(String[] deleteEssn, int cntEssn, String password) {
		this.deleteESsn = deleteEssn;
		this.cntEssn = cntEssn;
		this.base_pwd = password;
	}
	
	// 갱신문 생성자
	public CompanyDB(String[] updateEssn, int cntEssn, String password, String stat) {
		this.updateEssn = updateEssn;
		this.cntEssn = cntEssn;
		this.base_pwd = password;
	}
    
    // 일반 생성자, 평균 임금 산출 용
	public CompanyDB(String password) {
		this.base_pwd = password;
	}
	
	// 검색 예외처리를 위한 ssn 받아오기
	public void getSsn() {
		ResultSet rs;
		try {
			// url 과 사용자, 비밀번호로 Connection 객체 생성
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("정상적으로 연결되었습니다.");
			stmt = con.createStatement();
			rs = stmt.executeQuery("select Ssn from EMPLOYEE");
			
			ssnResult = new HashSet<>();
			while(rs.next()) {
				ssnResult.add(rs.getString("Ssn").toString());
			}
			
			// 해제
			rs.close();
			stmt.close();
			
		}catch (SQLException e) {  // connection 객체를 생성할 수 없을 때
			System.err.println("연결할 수 없습니다.");
			e.printStackTrace();
			
			
		}
		
		// 해제
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// 검색 질의 함수
	public Object[][] searchDB() {
		ResultSet rs;
		try {
			// 접속 url 과 사용자, 비밀번호
			
			// url 과 사용자, 비밀번호로 Connection 객체 생성
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("정상적으로 연결되었습니다.");
			
			int select_rows = 0;	// 위의 getCountQuery 이용해서 값 받음	
			int count_rows = 0;		// string array 의 index 로 사용
			int count_columns = 0;	// string array 의 index 로 사용
			
			// 인자로 받아온 whereQuery 가 null 이면 그대로, null 이 아니면 string 합침	
			if(whereQuery != null) {
				getCountQuery = getCountQuery + whereQuery;
				selectQuery = selectQuery + whereQuery;
			}
			
			// 결과 행 개수 구하기
			stmt = con.createStatement();
			rs = stmt.executeQuery(getCountQuery);
			
			if(rs.next()) {
				select_rows = rs.getInt(1);
			}
			
			// 검색 결과를 담을 string array 선언 (이 때 결과 행 개수 구한 것 사용)
			searchResult = new Object[select_rows][select_columns];
			ssnResult = new HashSet<>();
			// 일단은 table 구동 시험을 위한 attribute 들 모두 출력 (검색범위 전체, 검색 항목 전체)
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectQuery);
			// 검색 결과를 string array 인 searchResult 에 저장
			
			while(rs.next()) {
				
				while (count_columns < select_columns) {
					// "선택" 열 초기값 false
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
			
			// 예외처리용
			// employee의 ssn을 참조하고 있는 것들을 확인하기 (gui에서 delete할 때 이용할 목적
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
			
			// 해제
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {  // connection 객체를 생성할 수 없을 때
			System.err.println("연결할 수 없습니다.");
			e.printStackTrace();
			
			
		}
		
		// 해제
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return searchResult;
		
	} // 검색 질의 함수 끝
	

	
	// 삽입 연산 함수
	public String insertDB(String eString) { //구름
		String eMessage = ""; //구름
		ResultSet rs;
		
		try {
			// url 과 사용자, 비밀번호로 Connection 객체 생성
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("정상적으로 연결되었습니다.");
			
			pstmt = con.prepareStatement(insertQuery);
			pstmt.executeUpdate();
			
			// ssnResult 반영
			ssnResult = new HashSet<String>();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select Ssn from EMPLOYEE");
			
			while(rs.next()) {
				ssnResult.add(rs.getString("Ssn").toString());
			}
			
			
			// 해제
			rs.close();
			stmt.close();
			pstmt.close();
			
			
			// 삽입 단계에서 오류 있나 체크 및 성공 메시지 띄우기
			if ( eString != "") {
				if(eString == "Dno에 값을 입력하지 않아 default 값인 1로 적용됩니다.") {
					eMessage = "삽입되었습니다. " + eString;
					return eMessage; // 구름
				}
			}
			
			else {
				String insertMessage = "삽입되었습니다.";				
				return eMessage; // 구름
			}
			
			return eMessage;
			
		} catch (SQLException e) {
			eMessage = e.getMessage();
			e.printStackTrace();

			return eMessage;
		}
	}  // 삽입 연산 함수 끝 

	

	
	// 삭제 연산 함수
	public void deleteDB() {
		
		try {
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("정상적으로 연결되었습니다.");
			
			// 삭제에 맞게 수정하기 , deleteESsn, deleteQueryForm, cntEssn
			String deleteQuery;
			int cntDelete = 0;
			while (cntDelete < cntEssn) {
				deleteQuery = deleteQueryForm + deleteESsn[cntDelete];
				
				pstmt = con.prepareStatement(deleteQuery);
				pstmt.executeUpdate();
				
				cntDelete ++;
			}
			
			// 해제
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// 해제
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}  // 삭제 연산 함수 끝
	
	// 갱신 연산 함수
	public String updateDB(String att, String value) {
		String eString = "";
		
		try {
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("정상적으로 연결되었습니다.");
			
			String updateQuery;
			
			int cntUpdate = 0;
			while (cntUpdate < cntEssn) {
				updateQuery = updateQueryForm; // att 이름이랑 값 바꿈, 한번마다 초기화
				
				if(att != "Salary") {
					if(value.equals("")) {
						updateQuery = updateQuery + att + " = null" + " where " + "Ssn = " + updateEssn[cntUpdate] + ";" ;
					}else {
						updateQuery = updateQuery + att + "= '" + value + "'" + " where " + "Ssn = '" + updateEssn[cntUpdate] + "';" ;
					}
				}
				
				else {
					if(value.equals("")) {
						updateQuery = updateQuery + att + " = null" + " where " + "Ssn = " + updateEssn[cntUpdate] + ";" ; // 샐러리는 필요 없음.
					}
					else {
						updateQuery = updateQuery + att + "= '" + value + "' " + " where " + "Ssn = " + updateEssn[cntUpdate] + ";" ; // 샐러리는 필요 없음.
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
    
    // 선택된 직원 평균 임금 연산, sum 값을 쿼리 받아서 사이즈를 줄이기
    public float retAvgSal(Set<String> ssnSet) {
		float avg = 0;
		ResultSet rs;
        
        int cntEmp = ssnSet.size(); // 사이즈 받기
		if (cntEmp == 0) {return avg;} // 만약 체크가 다 풀려 size가 0이면 그냥 0 반환
		
		try {
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("정상적으로 연결되었습니다.");
			
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
				avg = rs.getFloat("SUM")/ssnSet.size(); // 임금합을 선택된 숫자로 나누어 평균
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
			System.out.println("정상적으로 연결되었습니다.");
			
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
			System.out.println("정상적으로 연결되었습니다.");
			
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
    // 상사 Ssn 받아오는 함수
    public String getSupSsn(String Ssn) {
    	String supSsn = "";
    	
    	try {
    		con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("정상적으로 연결되었습니다.");
			
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
			System.out.println("정상적으로 연결되었습니다.");
			
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