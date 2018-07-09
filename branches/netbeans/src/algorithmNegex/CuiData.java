/*
 * CuiData.java
 *
 * Created on June 7, 2006, 4:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package algorithmNegex;

/**
 *
 * @author VHASLCSouthB
 */

/***To change the keywords to CUI s *****/

import java.sql.*;
import java.util.logging.Level;

//***The dsn used here is "cuidata"***//
public class CuiData
{
	private  static Connection con;
	private static Object TYPE_SCROLL_INSENSITIVE;
	static 
	{
		String dsn="jdbc:odbc:cuidata";
		String driver="sun.jdbc.odbc.JdbcOdbcDriver";

		try
		{
			Class.forName(driver);
			con=DriverManager.getConnection(dsn);

		} catch(Exception e) {
			log.LoggingToFile.log(Level.SEVERE, "Error in connection "+e);
		}


	}
	public static ResultSet display(String kw)
	{

		ResultSet rs=null;
		try {


			PreparedStatement stmt=con.prepareStatement("SELECT * FROM cui where keyword=? order by keyword");
			stmt.setString(1,kw);
			rs=stmt.executeQuery();


		} catch(Exception e) {
			log.LoggingToFile.log( Level.SEVERE, "error in selecting from table:"+e);

		}
		return rs;
	}
}