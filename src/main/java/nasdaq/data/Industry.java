package nasdaq.data;

import java.util.ArrayList;
import java.util.List;

public final class Industry {
	
	public List<Company> companies = new ArrayList<>();
	private String name;
	private String url;
	
	public Industry(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return name;
	}
	
	

}
