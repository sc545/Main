import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Server {
	public static void main(String[] args) throws Exception {
		int PORT = 1113;
		String name[] = new String[10];
		int score[] = new int[10];
		
		while(true){
			ServerSocket ss = new ServerSocket(PORT);
			System.out.println("���� Ȱ��ȭ!!");
			Socket s = ss.accept();
			System.out.println("���� ����!!");
			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			InputStream is = s.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
			String flag = (String) ois.readObject();
			System.out.println(flag);
			
			Connection connect=null;
			Statement st=null;
			ResultSet rs=null;
			try{
				Class.forName("com.mysql.jdbc.Driver");
			}catch(ClassNotFoundException ce){
				System.out.println(ce);
				System.out.println("Ŭ���� �ε� �Ұ�");
			}
			if(flag.equals("read")){
				System.out.println("��ŷ �˻�");
				try{
					connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","Alltogether", "");					
					
					st = connect.createStatement();
					rs = st.executeQuery("select * from rank order by score desc");
					for(int i=0; i<10; i++){
						if(rs.next()){
							name[i] = rs.getString("name");
							score[i] = rs.getInt("score");
							System.out.println(name +" "+ score);
						}else{
							name[i] = null;
							score[i] = 0;
						}
						
					}
					
					oos.writeObject(name);
					oos.writeObject(score);
					
					
					if(rs!=null)rs.close();
					if(st!=null)st.close();
					if(connect!=null)connect.close();
					
				}catch(SQLException se){
					System.out.println(se);
				}
				
			}
			if(flag.equals("write")){
				System.out.println("��ŷ ���");
				try{
					connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","Alltogther", "");					
					st = connect.createStatement();
					rs = st.executeQuery("select * from rank");
					
					String tmpName = (String) ois.readObject();
					int tmpScore = (int) ois.readObject();
					
					st.executeUpdate("insert into rank values ('"+tmpName+"', "+tmpScore+")");
					System.out.println(name+" "+score);
					System.out.println("��ϿϷ�");
					
					if(rs!=null)rs.close();
					if(st!=null)st.close();
					if(connect!=null)connect.close();
					
				}catch(SQLException se){
					System.out.println(se);
				}
				
			}
			
			
			s.close();
			ss.close();
			
		}
		
	}

}