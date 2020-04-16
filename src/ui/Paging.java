package ui;

public class Paging {

    private int page = 1;
	  private double totalPages;
	  private double numberOfRecords;
    private double resultsPerPage = 23;

    public Paging(){

    }

    public Paging(double resultsPerPage){
      setResultsPerPage(resultsPerPage);
    }
    
    public int getPage() {
		return page;
    }

    public void setPage(int updated){
        this.page = updated;
    }
    
    public void incrementPage(){
        this.page++;
    }

    public void decrementPage(){
        this.page--;
    }

	public double getTotalPages() {
		return totalPages;
    }
    
    public void setTotalPages(double updated) {
		this.totalPages = updated;
	}

	public double getNumberOfRecords() {
		return numberOfRecords;
    }
    
  public void setNumberOfRecords(double updated) {
    this.numberOfRecords = updated;
	}

	public double getResultsPerPage() {
		return resultsPerPage;
  }

  public void setResultsPerPage(double updated) {
    this.resultsPerPage = updated;
  }

}