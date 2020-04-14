package ui;

public class Paging {

    private int page = 1;
	  private double totalPages;
	  private double numberOfRecords;
    private final double resultsPerPage = 23;
    
    public int getPage() {
		return page;
    }

    public void setPage(int updated){
        page = updated;
    }
    
    public void incrementPage(){
        page++;
    }

    public void decrementPage(){
        page--;
    }

	public double getTotalPages() {
		return totalPages;
    }
    
    public void setTotalPages(double updated) {
		totalPages = updated;
	}

	public double getNumberOfRecords() {
		return numberOfRecords;
    }
    
    public void setNumberOfRecords(double updated) {
		numberOfRecords = updated;
	}

	public double getResultsPerPage() {
		return resultsPerPage;
    }

}