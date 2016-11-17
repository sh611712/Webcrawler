import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
 
public class Main {
	
	public static DB db = new DB();
	public static Set<String> urlSet = new HashSet<String>();
	
	public static void main(String[] args) throws SQLException, IOException {
		db.runSql2("TRUNCATE Record;");
		processPage("http://wiprodigital.com");
		CrunchifyCreateXMLDOM.xmlPattern(urlSet);
	}
	
	 
	public static Set<String> processPage(String URL) throws SQLException, IOException{
		
		//check if the given URL is already in database
		String sql = "select * from Record where URL = '"+URL+"'";
		ResultSet rs = db.runSql(sql);
		if(rs.next()){
 
		}else{
			//store the URL to database to avoid parsing again
			sql = "INSERT INTO  `Crawler`.`Record` " + "(`URL`) VALUES " + "(?);";
			PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, URL);
			stmt.execute();
 
			//get useful information
			Document doc = Jsoup.connect("http://wiprodigital.com/").get();
 
			//if(doc.text().contains("research")){
			urlSet.add(URL);
			
			//}
 
			//get all links and recursively call the processPage method
			Elements questions = doc.select("a[href]");
			for(Element link: questions){
				if(link.attr("href").contains("wiprodigital.com"))
					processPage(link.attr("abs:href"));
			}
		}
		return urlSet;
	}
}