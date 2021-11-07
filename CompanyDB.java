package test3;

import java.sql.*;

public class CompanyDB {
	private Connection con = null;  // connection 객체
	private Statement stmt = null;  // Statement 객체
	private PreparedStatement pstmt = null;  // PreparedStatement 객체
	
	// 검색 질의용
	// GUI 에서 table 만들 때 String array를 반환받아서 사용
	private Object[][] searchResult;
	// ResultSet 에서 개수 구해주는 method 가 없어서 행 개수 구하기 위한 쿼리를 따로 둠
	private String getCountQuery = "select count(*), concat(e.Fname, e.Minit, e.Lname) as Name, e.Ssn, e.Bdate, e.Address, e.Sex, e.Salary, concat(s.Fname, s.Minit, s.Lname) as Supervisor, d.Dname as Department"
			+ " from EMPLOYEE as e left outer join EMPLOYEE as s on e.Super_ssn = s.Ssn join DEPARTMENT as d on e.Dno = d.Dnumber";
	private String queryWhere = " where";
	private String andText =" and";
	
	// 검색 범위, 검색 항목 받아오면 생성자로 이거 설정해주기
	private String whereQuery;
	private String selectQuery;
	private int select_columns;
	
	// insert문 받아오면 생성자로 이거 설정해주기
	private String insertQuery;
	
	// 검색 질의 생성자
	public CompanyDB(String selectQuery, String whereQuery, int select_columns) {
		this.selectQuery = selectQuery;
		this.whereQuery = whereQuery;
		this.select_columns = select_columns;
	}
	
	// 삽입문 생성자
	public CompanyDB(String insertQuery) {
		this.insertQuery = insertQuery;
	}
	
	
	
	// 검색 질의 함수
	public Object[][] searchDB() {
		ResultSet rs;
		
		try {
			// 접속 url 과 사용자, 비밀번호
			String url="jdbc:mysql://localhost:3306/COMPANY?serverTimezon=UTC";
			String user="root";
			String pwd="root";
			
			// url 과 사용자, 비밀번호로 Connection 객체 생성
			con = DriverManager.getConnection(url,user,pwd);
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
	public void insertDB() {
		
		try {
			// 접속 url 과 사용자, 비밀번호
			String url="jdbc:mysql://localhost:3306/COMPANY?serverTimezon=UTC";
			String user="root";
			String pwd="root";
			
			// url 과 사용자, 비밀번호로 Connection 객체 생성
			con = DriverManager.getConnection(url,user,pwd);
			System.out.println("정상적으로 연결되었습니다.");
			
			pstmt = con.prepareStatement(insertQuery);
			pstmt.executeUpdate();
			
			
			
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
		
		
	}  // 삽입 연산 함수 끝 

	
	// 테이블 내 체크박스 구현되면 갱신문, 삭제문 만들기
	
	
	
	
	

}
