package bank;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Bank {
		
	@SuppressWarnings("deprecation")
	public static void main(String[] args)
	{
		long time = System.currentTimeMillis(); 

		SimpleDateFormat dayTime1 = new SimpleDateFormat("yyyy-mm-dd");
		SimpleDateFormat dayTime2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		String StartDate1 = dayTime1.format(new Date(time));
		String StartDate2 = dayTime2.format(new Date(time));
		//System.out.println(StartDate2);
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// 1.Load JDBC Driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// 2. Open DBMS connection
			String url = "jdbc:mysql://localhost:3306/bank?characterEncoding=UTF-8&serverTimezone=UTC";
			String user = "root";
			String psw = "5692389";
			con = DriverManager.getConnection(url, user, psw);
			
			// 3.Execute SQL query
			//	Using executeUpdate method
			stmt = con.createStatement();
			String cmd;
			rs=null;
			int num= -1;
			Scanner Key = new Scanner(System.in);
			boolean EXIT = false;
			
			while(!EXIT) {
				
				System.out.println("=============================================");
				System.out.println("(0)  :  Exit Program");
				System.out.println("(1)  :  Manager Mode");
				System.out.println("(2)  :  User Mode");
				System.out.println("=============================================");
				
				num = Key.nextInt();
				String table;
				int selectNum;
				
				switch(num) 
				{
				case 0:
					System.out.println("Exit the program");
					EXIT = true;
					break;
					
				case 1:
					System.out.println("=============================================");
					System.out.println("Choose TABLE which you want");
					System.out.println("=============================================");
					Key.nextLine();
					table = Key.nextLine();
					
					boolean exit1 = false;
				
					while(!exit1) {
						
						System.out.println("=============================================");
						System.out.println("Choose operation which you want");
						System.out.println("=============================================");
						System.out.println("(0)  :  Exit operation");
						System.out.println("(1)  :  Insertion");
						System.out.println("(2)  :  Deletion");
						System.out.println("(3)  :  Update");								
						System.out.println("(4)  :  Selection");						
						System.out.println("(5)  :  Show All Tuples");		
						System.out.println("=============================================");
						
						cmd = Key.nextLine();
						
						if(cmd.equals("exit") ||cmd.equals("EXIT") || cmd.equals("0")) {
							
							System.out.println("Exit the program");
							exit1 = true;
							
						}
						else if(cmd.contains("SELECT")||cmd.contains("select")|| cmd.equals("4")) {
							
							//if(cmd.equals("4")) cmd=Key.nextLine();
							
							ArrayList<String> arr = new ArrayList<String>();
							int fromPos;
							
							if(cmd.contains("SELECT") || cmd.contains("select")) {
								if(cmd.contains("FROM")) fromPos = cmd.indexOf("FROM");
								else fromPos = cmd.indexOf("from");
								String str = cmd.substring(0,fromPos-1);
								int beginIndex = 7;
								int comPos = 0;
								while(true) {
									comPos = str.indexOf(",",beginIndex);
									if(comPos<0) comPos = str.length();
									arr.add(cmd.substring(beginIndex,comPos));
									if(comPos == str.length()) break;
									beginIndex = comPos+1;
								}
								rs = stmt.executeQuery(cmd);
							}
							else if(cmd.equals("4")) {
								
								System.out.println("ENTER ATTRIBUTES :");
								String attributes = Key.nextLine();
								
								rs = stmt.executeQuery("SELECT " + attributes + " FROM " + table);
								int beginIndex = 0;
								int comPos = 0;
								while(true) {
									comPos = attributes.indexOf(",",beginIndex);
									if(comPos<0) comPos = attributes.length();
									arr.add(attributes.substring(beginIndex,comPos));
									if(comPos == attributes.length()) break;
									beginIndex = comPos+1;
								}
							}
							
							while(rs.next()) {
								for(int i=0;i<arr.size();i++) {
									
									System.out.print(arr.get(i) + " : " + rs.getString(i+1) + " ");
								}
								System.out.println();
							}
						}
						else if(cmd.equals("5")) {
							
							rs = stmt.executeQuery("SELECT * FROM " + table);
							ResultSetMetaData rsmd = rs.getMetaData();
							int columnCount = rsmd.getColumnCount();
							
							while(rs.next()) {
								for(int i=1;i<=columnCount;i++) {
									System.out.print(rs.getString(i) + " ");
								}
								System.out.println();
							}
						}
						else if(cmd.equals("1") || cmd.equals("2") || cmd.equals("3") || cmd.contains("INSERT") || cmd.contains("DELETE") || cmd.contains("UPDATE")) 
						{
							if(cmd.equals("1") || cmd.equals("2") || cmd.equals("3")) {
								//cmd=Key.nextLine();
								int userID,SocialID,UManagerID,accountNum,UserID,ManagerID;
								String Name, Sex, BirthDate, Address,AccountStartDate,condition,setValue;
								
								if(cmd.equals("1")) {
									if(table.equals("user") || table.equals("USER")) {
										System.out.println("Enter USER INFORMATION : (userID, Name, Sex, BirthDate, Address, SocialID, ManagerID)");
										userID = Key.nextInt();
										Name =  Key.next();
										Sex = Key.next();
										BirthDate=Key.next();
										Address = Key.next();
										SocialID = Key.nextInt();
										UManagerID = Key.nextInt();
										stmt.executeUpdate("INSERT into "+ table + " values ('"+ userID +"','"+ Name +"','"+Sex 
												+ "','" + BirthDate + "','" + Address + "','" + SocialID + "','" + UManagerID+"')");
									}
									else if(table.equals("account")|| table.equals("ACCOUNT")) {
										System.out.println("Enter ACCOUNT INFORMATION : (AccountNumber, AccountStartDate, UserID");
										accountNum = Key.nextInt();
										AccountStartDate = Key.next();
										UserID = Key.nextInt();
										stmt.executeUpdate("INSERT into "+ table + " values ('"+ accountNum +"','"+ AccountStartDate
												+"','"+ UserID + "')");
									}
									else if(table.equals("manager")|| table.equals("MANAGER")) {
										System.out.println("Enter MANAGER INFORMATION : (ManagerID, Name, BirthDate, Address, Sex)");
										ManagerID = Key.nextInt();
										Name = Key.next();
										BirthDate = Key.next();
										Address = Key.next();
										Sex = Key.next();
										stmt.executeUpdate("INSERT into "+ table + " values ('"+ ManagerID +"','"+ Name
												+"','"+ BirthDate + "','" + Address + "','" + Sex + "')");
									}
								}
								else if(cmd.equals("2")) {
									
									System.out.println("Enter Condition :");
									condition = Key.nextLine();
									if(condition.equals(null)) stmt.executeUpdate("DELETE FROM " + table);
									else stmt.executeUpdate("DELETE FROM " + table + " WHERE " + condition);
								}
								else if(cmd.equals("3")) {
									System.out.println("Enter SET VALUE :");
									setValue = Key.nextLine();
									System.out.println("Enter Condition :");
									condition = Key.nextLine();
									stmt.executeUpdate("UPDATE " + table + " SET " + setValue + " WHERE " + condition);
								}
							}
							else
								stmt.executeUpdate(cmd);
						}
					}
					 break;
					 
				case 2:
					
					boolean exit2 = false;
					while(!exit2) {
					System.out.println("=============================================");
					System.out.println("Choose operation which you want");
					System.out.println("=============================================");
					System.out.println("(0)  :  Exit Operation");
					System.out.println("(1)  :  Manage User");
					System.out.println("(2)  :  Manage Account");
					System.out.println("(3)  :  Manage Card");
					System.out.println("(4)  :  Manage Money");
					System.out.println("=============================================");
					
					selectNum = Key.nextInt();
					int userID,SocialID,ManagerID,accountNum,columnCount,cardNum,deposit,withdraw,money=0;
					String Name, Sex, BirthDate, Address;
					ResultSetMetaData rsmd;
					
						switch(selectNum) 
						{
						case 0:
							System.out.println("Exit the program");
							exit2 = true;
							break;
						case 1:
							boolean exit3 =false;
							while(!exit3) {
								System.out.println("=============================================");
								System.out.println("Choose operation which you want");
								System.out.println("=============================================");
								System.out.println("(0)  :  Exit Operation");
								System.out.println("(1)  :  Register");
								System.out.println("(2)  :  Show User's Manager Number");
								System.out.println("(3)  :  Change Manager");
								System.out.println("=============================================");
							
							int selectNum1 = Key.nextInt();
							switch(selectNum1) 
							{
							case 0:
								System.out.println("Exit the program");
								exit3 = true;
								break;
							case 1:
								System.out.println("Enter PERSONAL INFORMATION : (userID, Name, Sex, BirthDate, Address, SocialID, ManagerID)");
								userID = Key.nextInt();
								Name =  Key.next();
								Sex = Key.next();
								BirthDate=Key.next();
								Address = Key.next();
								SocialID = Key.nextInt();
								ManagerID = Key.nextInt();
								stmt.executeUpdate("INSERT into USER values ('"+ userID +"','"+ Name +"','"+Sex 
										+ "','" + BirthDate + "','" + Address + "','" + SocialID + "','" + ManagerID+"')");
								break;
							case 2:
								System.out.println("Enter UserID :");
								userID = Key.nextInt();
								rs = stmt.executeQuery("SELECT ManagerID FROM USER WHERE UserID='" + userID + "'");
								
								while(rs.next()) {
										System.out.print("ManagerID : " + rs.getString(1));
												}
								break;
							case 3:
								System.out.println("Enter UserID :");
								userID = Key.nextInt();
								System.out.println("Enter ManagerID :");
								ManagerID = Key.nextInt();
								stmt.executeUpdate("UPDATE USER SET ManagerID = " + ManagerID + " WHERE UserID = " + userID);
								break;
							}
						}
							break;
						case 2:
							boolean exit4 =false;
							while(!exit4) {
								System.out.println("=============================================");
								System.out.println("Choose operation which you want");
								System.out.println("=============================================");
								System.out.println("(0)  :  Exit Operation");
								System.out.println("(1)  :  Create Account");
								System.out.println("(2)  :  Show Account Information");
								System.out.println("=============================================");
							
							int selectNum2 = Key.nextInt();
							switch(selectNum2)
							{
							case 0:
								System.out.println("Exit the program");
								exit4 = true;
								break;
							case 1:
								System.out.println("Enter ACCOUNT INFORMATION : (AccountNumber, UserID)");
								accountNum = Key.nextInt();
								userID = Key.nextInt();
								stmt.executeUpdate("INSERT into ACCOUNT values ('"+ accountNum +"','"+ StartDate1
										+"','"+ userID + "')");
								stmt.executeUpdate("INSERT into Money values ('"+ StartDate2 +"','0','0','" + accountNum + 
										 "','0')");
								break;
							case 2:
								System.out.println("Enter ACCOUNT NUMBER :");
								accountNum = Key.nextInt();
								rs = stmt.executeQuery("SELECT AccountStartDate, UserID FROM ACCOUNT WHERE AccountNum='" + accountNum + "'");
								
								while(rs.next()) {
										System.out.println("AccountStartDate : " + rs.getString(1) + " UserID : " + rs.getString(2));
								}
								break;
							}
						}
							break;
						case 3:
							boolean exit5 =false;
							while(!exit5) {
								System.out.println("=============================================");
								System.out.println("Choose operation which you want");
								System.out.println("=============================================");
								System.out.println("(0)  :  Exit Operation");
								System.out.println("(1)  :  Create Card");
								System.out.println("=============================================");
							
							int selectNum3 = Key.nextInt();
							switch(selectNum3)
							{
							case 0:
								System.out.println("Exit the program");
								exit5 = true;
								break;
							case 1:
								System.out.println("Enter UserID : ");
								userID = Key.nextInt();
								System.out.println("Enter ACCOUNT NUMBER :");
								accountNum = Key.nextInt();
								System.out.println("Enter CARD NUMBER : ");
								cardNum = Key.nextInt();
								stmt.executeUpdate("INSERT into CARD values ('" + cardNum + "')");
								stmt.executeUpdate("INSERT into CREATECARD values ('" + userID +"','"+ accountNum
										+"','"+ cardNum + "','" + StartDate1 + "')");
								break;
							}
						}
							break;
						case 4:
							boolean exit6 =false;
							while(!exit6) {
								System.out.println("=============================================");
								System.out.println("Choose operation which you want");
								System.out.println("=============================================");
								System.out.println("(0)  :  Exit Operation");
								System.out.println("(1)  :  Deposit Money");
								System.out.println("(2)  :  Withdraw Money");
								System.out.println("(3)  :  Show Money Table");
								System.out.println("=============================================");
							
							int selectNum4 = Key.nextInt();
							switch(selectNum4)
							{
							case 0:
								System.out.println("Exit the program");
								exit6 = true;
								break;
							case 1:
								System.out.println("Enter ACCOUNT NUMBER :");
								accountNum = Key.nextInt();
								System.out.println("How much will you deposit :");
								deposit = Key.nextInt();
								rs = stmt.executeQuery("SELECT TotalMoney FROM MONEY WHERE AccountNum = " + accountNum );
								
								while(rs.next()) {
									if(rs.getInt(1)!=0)
									money = rs.getInt(1);
									//System.out.println(money);
								}
							
								System.out.println("Total Money : " + money + " To " + (money+deposit));
								money += deposit;
								stmt.executeUpdate("INSERT into Money values ('"+ StartDate2 +"','0','" + deposit + "','"+ + accountNum + 
										 "','"+money+"')");
								break;
							case 2:
								System.out.println("Enter ACCOUNT NUMBER :");
								accountNum = Key.nextInt();
								System.out.println("How much will you withdraw :");
								withdraw = Key.nextInt();
								rs = stmt.executeQuery("SELECT TotalMoney FROM MONEY WHERE AccountNum=" + accountNum );
								
								while(rs.next()) {
									if(rs.getInt(1)!=0)
									money = rs.getInt(1);
									//System.out.println(money);
								}
								
								System.out.println("Total Money : " + money + " To " + (money-withdraw));
								money -= withdraw;
								stmt.executeUpdate("INSERT into Money values ('"+ StartDate2 +"','" + withdraw + "','0','" + accountNum + 
										 "','"+money+"')");
								 break;
							case 3:
								System.out.println("Enter Accout Number :");
								accountNum = Key.nextInt();
								rs = stmt.executeQuery("SELECT * FROM MONEY WHERE AccountNum = " + accountNum);
								rsmd = rs.getMetaData();
								columnCount = rsmd.getColumnCount();
								
								while(rs.next()) {
									for(int i=1;i<=columnCount;i++) {
										System.out.print(rs.getString(i) + " ");
									}
									System.out.println();
								}
								break;
							}
						}
							break;
						}
					break;
				}
				
			}
				
			//	Using executeQuery method
			
			
			// 4. Process search query result
			
			// 5.Close DBMS connection
			rs.close();
			stmt.close();
			con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
