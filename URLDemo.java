import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class URLDemo {

	String url;
	
	public String getUrl() {
		return url;
	}

	@XmlElement
	public void setUrl(String url) {
		this.url = url;
	}
}
