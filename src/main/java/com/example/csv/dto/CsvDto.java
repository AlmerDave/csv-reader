package com.example.csv.dto;

import com.opencsv.bean.CsvBindByPosition;

public class CsvDto {
	
	@CsvBindByPosition(position = 0)
	private String firstColumn;
	
	@CsvBindByPosition(position = 1)
	private String secondColumn;
	
	@CsvBindByPosition(position = 2)
	private String thirdColumn;
	
	@CsvBindByPosition(position = 3)
	private String fourthColumn;

	public CsvDto() {
		// Empty COnstructor
	}
	
	

	public String getFirstColumn() {
		return firstColumn;
	}

	public void setFirstColumn(String firstColumn) {
		this.firstColumn = firstColumn;
	}

	public String getSecondColumn() {
		return secondColumn;
	}

	public void setSecondColumn(String secondColumn) {
		this.secondColumn = secondColumn;
	}

	public String getThirdColumn() {
		return thirdColumn;
	}

	public void setThirdColumn(String thirdColumn) {
		this.thirdColumn = thirdColumn;
	}

	public String getFourthColumn() {
		return fourthColumn;
	}

	public void setFourthColumn(String fourthColumn) {
		this.fourthColumn = fourthColumn;
	}
	
	@Override
	public String toString() {
		return "CsvDto [firstColumn=" + firstColumn + ", secondColumn=" + secondColumn + ", thirdColumn=" + thirdColumn
				+ ", fourthColumn=" + fourthColumn + "]";
	}

	
}
