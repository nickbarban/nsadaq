package nasdaq.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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

	private static boolean DEBUG = true;

	public static void main(String[] args) {
		List<Industry> industries = null;
		try {
			industries = Downloader.downloadIndustryList(URL);
			for (Industry industry : industries) {
				String csvDownloadLink = industry.getUrl();
				InputStream is = null;
				if (DEBUG) {
					is = new FileInputStream(new File("/home/nick/Downloads/companylist.csv"));
				} else {
					is = new URL(csvDownloadLink).openStream();
				}
				try {
					industry.companies.addAll(Downloader.downloadCompanyList(industry, is));
					log.info("Companies added to industry: " + industry + " quantity: " + industry.companies.size());
				} catch (JsonProcessingException e) {
					log.severe("Can't handle CSV file");
					continue;
				} catch (IOException e) {
					log.severe("Can't download CSV file");
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
				return o2.getLastSale().subtract(o1.getLastSale()).toBigInteger().intValue();
			}
		};
		for (Industry industry : industries) {
			Collections.sort(industry.companies, comparatorByLastSale);
			System.out.println(industry);
			for (Company company : industry.companies) {
				System.out.println(company + "-" + company.getLastSale());
			}
			System.out.println("---------------------------------------------------------------------------------");
		}
		

	}

}
