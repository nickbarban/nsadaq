package nasdaq.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;

import nasdaq.data.Company;
import nasdaq.data.Industry;
import nasdaq.utils.Downloader;

/**
 * This is main class. The class has DEBUG variable for switching debug/undebug
 * programm running mode In dedug mode programm uses for csv dowloaded file that
 * is corrected by hand (deleted last coma from header) In undebug mode programm
 * downloads csv file by each industry link (with no correction of file)
 *
 */
public class NasdaqTop10Service {

	private static Logger log = Logger.getLogger(NasdaqTop10Service.class.getName());

	private final static String URL = "http://www.nasdaq.com/screening/industries.aspx";
	private final static String downloadCSVFileURLEnding = "&render=download";
	
	private static Stopwatch STOPWATCH;

	public static void main(String[] args) {
		List<Industry> industries = null;
		try {
			industries = Downloader.downloadIndustryList(URL);
			for (Industry industry : industries) {
				STOPWATCH = Stopwatch.createStarted();
				InputStream is = new URL(industry.getUrl() + downloadCSVFileURLEnding).openStream();
				try {
					industry.companies.addAll(Downloader.downloadCompanyList(industry, is));
					log.info("Companies added to industry: " + industry + "; quantity: " + 
								industry.companies.size() + "; elapsed time: " + STOPWATCH);
				} catch (JsonProcessingException e) {
					log.severe("Can't handle CSV file: " + e.getMessage());
					continue;
				} catch (IOException e) {
					log.severe("Can't download CSV file: " + e.getMessage());
					continue;
				}
			}
		} catch (IOException e) {
			log.severe(e.getMessage());
		}

		Preconditions.checkNotNull(industries);
		Comparator<? super Company> comparatorByLastSale = new Comparator<Company>() {

			@Override
			public int compare(Company o1, Company o2) {
				return (o2.getLastSale().subtract(o1.getLastSale()).multiply(new BigDecimal(100)).signum());
			}
		};
		List<Company> TOP10 = new ArrayList<>();
		for (Industry industry : industries) {	
			TOP10.addAll(industry.companies);
		}
		Collections.sort(TOP10, comparatorByLastSale);
		for (Company company : TOP10.subList(0, 10)) {
			System.out.println(company.getLastSale() + "\t::\t" + company);
		}
	}

}
