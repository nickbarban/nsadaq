package nasdaq.data;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/*
 * CSV-file header:
 * "Symbol","Name","LastSale","MarketCap","ADR TSO","IPOyear","Sector","Industry","Summary Quote"
 */
@JsonPropertyOrder({"Symbol","Name","LastSale","MarketCap","ADR TSO","IPOyear","Sector","Industry","Summary Quote"})
@JsonIgnoreProperties(ignoreUnknown=true)
public final class Company {
	
	private String symbol;
	private String name;
	private BigDecimal lastSale;
	private String marketCap;
	private String adrTso;
	private String ipoYear;
	private String sector;
	private String industry;
	private String summaryURL;
	
	


	public Company() {
	}


	
	@JsonProperty("Symbol")
	public String getSymbol() {
		return symbol;
	}



	@JsonProperty("Symbol")
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}



	@JsonProperty("Name")
	public String getName() {
		return name;
	}



	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}



	@JsonProperty("LastSale")
	public BigDecimal getLastSale() {
		return lastSale;
	}



	@JsonProperty("LastSale")
	public void setLastSale(String lastSale) {
		try {
			this.lastSale = new BigDecimal(lastSale).setScale(2, RoundingMode.HALF_UP);
		} catch (NumberFormatException e) {
			this.lastSale = new BigDecimal(0.0).setScale(2, RoundingMode.HALF_UP);
		}
	}



	@JsonProperty("MarketCap")
	public String getMarketCap() {
		return marketCap;
	}



	@JsonProperty("MarketCap")
	public void setMarketCap(String marketCap) {
		this.marketCap = marketCap;
	}



	@JsonProperty("ADR TSO")
	public String getAdrTso() {
		return adrTso;
	}



	@JsonProperty("ADR TSO")
	public void setAdrTso(String adrTso) {
		this.adrTso = adrTso;
	}



	@JsonProperty("IPOyear")
	public String getIpoYear() {
		return ipoYear;
	}



	@JsonProperty("IPOyear")
	public void setIpoYear(String ipoYear) {
		this.ipoYear = ipoYear;
	}



	@JsonProperty("Sector")
	public String getSector() {
		return sector;
	}



	@JsonProperty("Sector")
	public void setSector(String sector) {
		this.sector = sector;
	}



	@JsonProperty("Industry")
	public String getIndustry() {
		return industry;
	}



	@JsonProperty("Industry")
	public void setIndustry(String industry) {
		this.industry = industry;
	}



	@JsonProperty("Summary Quote")
	public String getSummaryURL() {
		return summaryURL;
	}



	@JsonProperty("Summary Quote")
	public void setSummaryURL(String summaryURL) {
		this.summaryURL = summaryURL;
	}




	@Override
	public String toString() {
		return "\"" + symbol + " " + name + "\"";
	}
	
	
	
	
}
