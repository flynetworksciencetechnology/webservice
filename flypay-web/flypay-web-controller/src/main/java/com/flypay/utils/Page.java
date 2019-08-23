package com.flypay.utils;
public class Page<T> {

	/**
	 * 当前页，默认为1
	 */
	protected int cur = 1;

	/**
	 * 总页数
	 */
	protected int sumPages;

	/**
	 * 总条数
	 */
	protected int sumCounts;

	/**
	 * 每页条数
	 */
	protected int rows=20;

	protected int prePage;

	protected int nextPage;

	protected T Items;

	public T getItems() {
		return Items;
	}

	public void setItems(T items) {
		Items = items;
	}

	/**
	 * 分页开始的条数
	 */
	public int getStartRow() {
		// return (cur - 1) * rows;
		int offset = (cur - 1) * rows;
		if (offset > sumCounts) {
			if (rows > sumCounts)
				offset = 0;
			else
				offset = sumCounts - (rows * (cur - 1));
		}
		return offset;
	}
	public Page( int cur,int rows){
		this.cur=cur;
		this.rows=rows;
		if( this.cur > 1){
			this.prePage = this.cur - 1;
		}
	}
	public int getCur() {
		return cur;
	}
	public void setCur(int cur) {
		this.cur = cur;
	}
	public int getSumPages() {
		return sumPages;
	}
	public void setSumPages(int sumPages) {
		this.sumPages = sumPages;
	}
	public int getSumCounts() {
		return sumCounts;
	}
	public void setSumCounts(int sumCounts) {
		//这是总条数
		this.sumCounts = sumCounts;
		//总页数
		this.sumPages = this.sumCounts / this.rows + this.sumCounts % this.rows;
		if(this.cur < this.sumPages){
			this.nextPage = this.cur + 1;
		}
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getPrePage() {
		return prePage;
	}
	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
}
