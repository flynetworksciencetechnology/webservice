package com.flypay.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Page {

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

	private HttpServletRequest request;
	private String url = "";
	private String parameter;

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
	}
	
	public Page(HttpServletRequest request, int cur,int rows){
		this.request = request;
		this.cur=cur;
		this.rows=rows;
	}
	
	
	public Page(HttpServletRequest request, int curPage, Integer count, int rows) {
		buildUrl(request, curPage, count, rows);
	}

	private void buildUrl(HttpServletRequest request, int curPage, Integer count, int rows) {
		int sumPages = new Long(Math.round(count / (rows + 0.0) + 0.49999))
				.intValue();
		if (curPage > sumPages) {
			curPage = sumPages;
		}
		curPage = this.getCurPage(curPage);
		int prePage = curPage - 1;
		int nextPage = curPage + 1;
		if (prePage <= 0) {
			prePage = 1;
		}
		if (nextPage > sumPages) {
			nextPage = sumPages;
		}
		if (sumPages == 0)
			sumPages = 1;
		this.setCur(curPage);
		this.setRows(rows);
		this.setSumPages(sumPages);
		//this.setSumCounts(count);
		this.setPrePage(prePage);
		this.setNextPage(nextPage);
		if (null != request) {
			this.setRequest(request);
			this.setParameter(this.buildParameter());
			this.setUrl(this.buildUrl());
		}
	}

	protected int getCurPage(int curPage) {
		if (curPage <= 0) {
			return 1;
		}
		return curPage;
	}

	public String buildParameter() {
//		Map map = request.getParameterMap();

		Map map = request.getParameterMap();
		Entry entry = null;
		Iterator iterator = map.entrySet().iterator();
//		String url = request.getRequestURI();
		String url = request.getPathInfo()+"?";
		
		if (null == url || url.equals(""))
			return "";
		String rs = "";
		if (url.indexOf("?") == -1) {
			rs += url + "?";
		} else {
			rs += url;
		}
		String key = "";
		while (iterator.hasNext()) {
			
			entry = (Entry) iterator.next();
			key = (String) entry.getKey();		
			if (!key.equals("curPage")) {
				rs += "&" + entry.getKey() + "="
						+ ((String[]) entry.getValue())[0];
			}

		}
		return rs;
	}

	public String buildUrl() {
		int p = this.getCur();
		int t = this.getSumPages();
		int r = this.getRows();
		int j = p;

		String rowSum = "<div class='jianyi_biaoti_kuang3 font_18 font_666 float-left'>共：" + this.getSumCounts() + "条 "+r+"条/页 "+p+"页/"+t+"页</div> ";
		StringBuffer page = new StringBuffer(" <div class='jianyi_biaoti_kuang4 font_18 font_666 float-left' ><ul style='text-align:center;list-style-type:none;position:absolute;left:25%;'>");
		String url = this.buildParameter();
		if (p != 1) {
			page.append("<li class='jianyi_fenye4 font_14 font_666 float-left'><a href='"+url+"&curPage="+this.getPrePage()+"'>上一页</a></li>");
		}
		for (int i = 1; i < 5; i++) {
			j--;
			if (j == 0) {
				break;
			}
			if (j > t) {
				break;
			}
			page.append("<li class='jianyi_fenye2 font_14 font_666 float-left'><a href='"+url+"&curPage="+j+"'>"+j+"</a></li>");
		}
		page.append("<li class='jianyi_fenye2 font_14 font_666 float-left' style='background:#46a3eb;color:white;'>"+p+"</li>");
		j = p;
		for (int i = 1; i <= 5; i++) {
			j++;
			if (j == 0) {
				break;
			}
			if (j > t) {
				break;
			}
			page.append("<li class='jianyi_fenye2 font_14 font_666 float-left'><a href='"+url+"&curPage="+j+"'>"+j+"</a></li>");
		}
		if ((t - 3) > (p + 5))
			page.append("...");
		for (int i = 2; i >= 0; i--) {
			if ((t - i) <= (p + 5))
				break;
			page.append("<li class='jianyi_fenye2 font_14 font_666 float-left'><a href='"+url+"&curPage="+(t - i)+"'>"+(t - i)+"</a></li>");
		}
		if (p > 5) {
			page.append("<li class='jianyi_fenye2 font_14 font_666 float-left'><a href='"+url+"&curPage=1'>"+1+"</a></li>...");
		}

		if (p != this.getSumPages()) {
			page.append("<li class='jianyi_fenye4 font_14 font_666 float-left'><a href='"+url+"&curPage="+this.getNextPage()+"'>下一页</a></li>");
		}
		page.append("<div class='jianyi_fenye5 font_14 font_666 float-left'>跳转到</div>");
		page.append("<input name='jianyi_biaoti_kuang4' type='text' class='jianyi_fenye6 float-left' id='goPage'/>");
		page.append("<input id='hiddenUrl' type='hidden' value='"+url+"'/>");
		page.append("<li class='jianyi_fenye2 font_14 font_666 float-left' id='gogogo'>GO</li>");
		page.append("</ul></div>");
		return rowSum + page.toString();

	}
	
	public void reBuildPage(HttpServletRequest request, int cur,
			Integer count, int rows) {

		int sumPages = (int)(Math.round(count / (rows + 0.0) + 0.49999));

		if (cur > sumPages) {
			cur = sumPages;
		}
		cur = this.getCurPage(cur);
		int prePage = cur - 1;
		int nextPage = cur + 1;
		if (prePage <= 0) {
			prePage = 1;
		}
		if (nextPage > sumPages) {
			nextPage = sumPages;
		}
		if (sumPages == 0)
			sumPages = 1;
		this.setCur(cur);
		this.setRows(rows);
		this.setSumPages(sumPages);

		this.setSumCounts(count);
		this.setPrePage(prePage);
		this.setNextPage(nextPage);
		if (null != request) {
			this.setRequest(request);
			this.setParameter(this.buildParameter());
			this.setUrl(this.buildUrl());
		}
	}
	
     
	public void setUrl(String url) {
		this.url = url;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public int getCur() {
		return cur;
	}

	public void setCur(int cur) {
		this.cur = cur;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getPrePage() {
		return prePage;
	}

	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getSumCounts() {
		return sumCounts;
	}

	public void setSumCounts(int sumCounts) {
		this.sumCounts = sumCounts;
		buildUrl(request, this.cur, this.sumCounts, rows);
	}

	public int getSumPages() {
		return sumPages;
	}

	public void setSumPages(int sumPages) {
		this.sumPages = sumPages;
	}

	public String getUrl() {
		return url;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

}
