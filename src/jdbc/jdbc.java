package jdbc;
import java.sql.*;
import java.io.*;
import java.util.*;
public class jdbc {
	public static void main(String[] args) {
		Connection con= null;
		ArrayList<String> arr=new ArrayList<>();
		ArrayList<Integer> arr1=new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url="jdbc:mysql://localhost:3306/state?serverTimezone=UTC";
			String user="root";
			String pwd="0033220a";
			con =DriverManager.getConnection(url,user,pwd);
			String dname,mg_lname,ssn,address,sex;
			
			System.out.println("XXX COMPANY\n");
			java.sql.Statement st,st2;
			ResultSet rs,rs2;
			st=con.createStatement();
			rs = st.executeQuery("SELECT COUNT(*) AS count,SUM(salary) AS salary FROM EMPLOYEE");
			rs.next();
			int count=rs.getInt("count");
			System.out.println("Number of employees:"+ count+"\n");
			int salary=rs.getInt("salary");
			rs.close();
			st.close();
			System.out.println("Total salary = $"+ salary+"\n");
			st2=con.createStatement();
			rs2=st2.executeQuery("select dname,lname,COUNT(*) AS cnt,avg(salary) as salary1, dno as dno FROM employee as e left join department as d on d.mgr_ssn=e.ssn  GROUP BY dno order by cnt");
			while(rs2.next()) {
				dname=rs2.getString(1);
				mg_lname=rs2.getString(2);
				int count1=rs2.getInt("cnt");
				int salary1=rs2.getInt("salary1");
				int dno=rs2.getInt("dno");
				String a=dname+"  "+mg_lname+"  "+count1+"    "+salary1;
				arr.add(a);
				arr1.add(dno);
			}rs2.close();
			st2.close();
			for(int i=0;i<arr1.size();i++) {
			String stm3="select e.fname,e.lname,e.ssn,e.address,e.sex,e.salary,m.fname,m.lname from((employee as e left join employee as m on e.super_ssn=m.ssn) "					+ "join department on e.dno=dnumber) where e.dno=?";
			PreparedStatement p=con.prepareStatement(stm3);
			p.clearParameters();
			p.setInt(1, arr1.get(i));
			ResultSet rs3=p.executeQuery();
			System.out.println("------------------------------------------------------------------------------");
			System.out.println("Dname      Mgr_name No_Emps Avg-Salary");
			System.out.println("------------------------------------------------------------------------------");
			System.out.println("------------------------------------------------------------------------------");
			System.out.println(arr.get(i));
			System.out.println("------------------------------------------------------------------------------");
			System.out.println("Name             Ssn               Address          Sex     Salary      Supervisor");
			System.out.println("------------------------------------------------------------------------------");
			while(rs3.next()) {
				String fname=rs3.getString(1);
				String lname=rs3.getString(2);
				ssn =rs3.getString(3);
				address=rs3.getString(4);
				sex=rs3.getString(5);
				int salary2=rs3.getInt(6);
				String mg_fname=rs3.getString(7);
				String mg_lname1=rs3.getString(8);
				if(mg_fname==null) {
					mg_fname="";
				}
				
				System.out.println(fname+" "+lname+"   "+ssn+"   "+address+"   "+sex+"   "+salary2+"   "+mg_fname+" "+mg_lname1);
			}rs3.close();
			}
			
		}catch(ClassNotFoundException e){
            System.out.println("드라이버 로딩 실패");
        }
        catch(SQLException e){
            System.out.println("에러: " + e);
        }
        finally{
            try{
                if( con != null && !con.isClosed()){
                    con.close();
                }
            }
            catch( SQLException e){
                e.printStackTrace();
            }
        }
    }
	
}
