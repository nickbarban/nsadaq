package nasdaq.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Lists;

import nasdaq.data.Company;
import nasdaq.data.Industry;
import nasdaq.service.NasdaqTop10Service;

public final class Downloader {
	
	private static Logger log = Logger.getLogger(Downloader.class.getName());

	private Downloader() {
		
	}
	
	public static List<Industry> downloadIndustryList(String URL) throws IOException {
    	List<Industry> industries = new ArrayList<>();
    	Document doc = null;
    	try {
			doc = Jsoup.connect(URL).get();
			log.info("Connected to " + URL);
	    	Elements industryTable = doc.getElementsByClass("industry-table").select("a[href]");
	    	for (Element element : industryTable) {
	    		if (element.hasText()) {
	    			Industry industry = new Industry(element.text(), element.absUrl("href"));
	    			industries.add(industry);
	    			log.info("Industry \"" + industry + "\" is downloaded and created successfully: " + industries.size());
	    		}
			}
		} catch (IOException e) {
			log.severe("Can't connect to " + URL);
			throw new IOException(e);
		}
		return industries;
    	
    }
	
	public static List<Company> downloadCompanyList(Industry industry, InputStream is) 
											throws JsonProcessingException, IOException {
		CsvMapper mapper = new CsvMapper();
		CsvSchema schema = CsvSchema.emptySchema().withHeader();
		//schema.withColumnSeparator(',');
		MappingIterator<Company> iterator = null;
		iterator = mapper.reader(Company.class).with(schema).readValues(is);
		log.info("CSV file for industry: \"" + industry + "\" is downloaded and handled successfully");
		return Lists.newArrayList(iterator);

	}

}
