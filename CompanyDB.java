package test3;

import java.sql.*;
import java.util.Set;

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
	public CompanyDB(String selectQuery, String whereQuery, int select_columns, String password) {
		this.selectQuery = selectQuery;
		this.whereQuery = whereQuery;
		this.select_columns = select_columns;
		this.base_pwd = password;
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
						searchResult[count_rows][count_columns] = rs.getString(count_columns);
					}
					count_columns++;
				}
				count_columns = 0;
				count_rows++;
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
	public void insertDB(String eString) {
		
		try {
			
			// 삽딥 단계에서 오류 있나 체크
			if ( eString != "") {
				if(eString == "Dno에 값을 입력하지 않아 default 값인 1로 적용됩니다.") { //구름
					ewin ew = new ewin(eString);
					ew.launch(eString);
				}
			}
            
			// url 과 사용자, 비밀번호로 Connection 객체 생성
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("정상적으로 연결되었습니다.");
			
			pstmt = con.prepareStatement(insertQuery);
			pstmt.executeUpdate();
			
			// 해제
			pstmt.close();
			
		} catch (SQLException e) {
			String err = e.getMessage();
			ewin ew = new ewin(err);
			ew.launch(err);
			
			e.printStackTrace();
		}
		
		// 해제 --> 물어보기
		/*
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
					
		}*/
		
		
	}  // 삽입 연산 함수 끝 

	
	// 테이블 내 체크박스 구현되면 갱신문, 삭제문 만들기
	
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
	public void updateDB(String att, String value) {
		try {
			con = DriverManager.getConnection(base_url,base_user,base_pwd);
			System.out.println("정상적으로 연결되었습니다.");
			
			String updateQuery;
			
			// salary가 들어온 경우 value가 숫자인가 확인하는 작업
			if (att == ("Salary")&&!value.matches("[+-]?\\d*(\\.\\d+)?")) {
				Exception e = new Exception("숫자만 입력해주세요!");
				
				throw e;
			}
			
			int cntUpdate = 0;
			while (cntUpdate < cntEssn) {
				updateQuery = updateQueryForm; // att 이름이랑 값 바꿈, 한번마다 초기화
				
				if(att != "Salary") {
					System.out.println(updateQuery);
					updateQuery = updateQuery + att + "= '" + value + "'" + " where " + "Ssn = '" + updateEssn[cntUpdate] + "';" ;
					System.out.println(updateQuery);
				}
				
				else {
					updateQuery = updateQuery + att + "= '" + value + "' " + " where " + "Ssn = " + updateEssn[cntUpdate] + ";" ; // 샐러리는 필요 없음.
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