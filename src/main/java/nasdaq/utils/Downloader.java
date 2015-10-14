package nasdaq.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import nasdaq.data.Company;
import nasdaq.data.Industry;
import nasdaq.service.NasdaqTop10Service;

public final class Downloader {
	
	private static Logger log = Logger.getLogger(Downloader.class.getName());
	
	private static Stopwatch STOPWATCH;

	private Downloader() {
		
	}
	
	public static List<Industry> downloadIndustryList(String URL) throws IOException {
		Preconditions.checkNotNull(URL);
		Preconditions.checkArgument(!URL.isEmpty());
    	List<Industry> industries = new ArrayList<>();
    	Document doc = null;
    	try {
			doc = Jsoup.connect(URL).get();
			log.info("Connected to " + URL);
	    	Elements industryTable = doc.getElementsByClass("industry-table").select("a[href]");
	    	for (Element element : industryTable) {
	    		STOPWATCH = Stopwatch.createStarted();
	    		if (element.hasText()) {
	    			Industry industry = new Industry(element.text(), element.absUrl("href"));
	    			industries.add(industry);
	    			log.info("Industry \"" + industry + "\" is downloaded and created successfully: " + 
	    						industries.size() + "; elapsed time: " + STOPWATCH);
	    		}
			}
		} catch (IOException e) {
			log.severe("Can't connect to " + URL + ": " + e.getMessage());
			throw new IOException(e);
		}
		return industries;
    	
    }
	
	
	public static List<Company> downloadCompanyList(Industry industry, InputStream is) 
											throws JsonProcessingException, IOException {
		Preconditions.checkNotNull(industry);
		Preconditions.checkNotNull(industry.getUrl());
		Preconditions.checkArgument(!industry.getUrl().isEmpty());
		STOPWATCH = Stopwatch.createStarted();
		ObjectMapper mapper = new CsvMapper();
		CsvSchema schema = CsvSchema.emptySchema().withHeader();
		MappingIterator<Company> iterator = mapper.readerFor(Company.class).with(schema).readValues(is);
		List<Company> result = Lists.newArrayList(iterator.readAll());
		log.info("CSV file for industry: \"" + industry + "\" is downloaded and handled successfully; quantity:" + 
					result.size() + "; elapsed time: " + STOPWATCH);
		return result;

	}

}
