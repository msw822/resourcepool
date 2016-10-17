package com.hp.xo.resourcepool.request;

import org.apache.commons.lang.StringUtils;

import com.hp.xo.resourcepool.ApiConstants;
import com.hp.xo.resourcepool.model.Parameter;

public abstract class BaseListRequest extends BaseRequest {
	public static final Integer PAGESIZE_UNLIMITED = -1;
	public static final String ASC = "asc";
	public static final String DESC = "desc";

    // ///////////////////////////////////////////////////
    // ///////// BaseList API parameters /////////////////
    // ///////////////////////////////////////////////////

    @Parameter(name = ApiConstants.KEYWORD, type = FieldType.STRING, description = "List by keyword")
    private String keyword;

    @Parameter(name = ApiConstants.PAGE, type = FieldType.INTEGER)
    private Integer page = 1;

    @Parameter(name = ApiConstants.PAGE_SIZE, type = FieldType.INTEGER)
    private Integer pagesize = PAGESIZE_UNLIMITED;
    
    @Parameter(name = "orderBy", type = FieldType.STRING)
    protected String orderBy = null;

    @Parameter(name = "order", type = FieldType.STRING)
    protected String order = null;
    
    @Parameter(name = "autoCount", type = FieldType.BOOLEAN)
    private Boolean autoCount = true;
    
    @Parameter(name = ApiConstants.LIST_ALL, type = FieldType.BOOLEAN, description = "If set to false, list only resources belonging to the command's caller; if set to true - list resources that the caller is authorized to see. Default value is false")
    private Boolean listAll;


    // ///////////////////////////////////////////////////
    // ///////////////// Accessors ///////////////////////
    // ///////////////////////////////////////////////////

    public BaseListRequest() {
    	super();
    }

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}
    
	public int getFirst() {
		return ((page - 1) * pagesize) + 1;
	}

	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * Setting order fields, split ',' .
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrder() {
		return order;
	}

	public Boolean getListAll() {
		return listAll;
	}

	public void setListAll(Boolean listAll) {
		this.listAll = listAll;
	}

	/**
	 * 设置排序方式向.
	 * 
	 * @param order 可选值为desc或asc,多个排序字段时用','分隔.
	 */
	public void setOrder(final String order) {
		String lowcaseOrder = StringUtils.lowerCase(order);

		//检查order字符串的合法值
		String[] orders = StringUtils.split(lowcaseOrder, ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
				throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
			}
		}

		this.order = lowcaseOrder;
	}


	/**
	 * 是否已设置排序字段,无默认值.
	 */
	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
	}

	/**
	 * 获得查询对象时是否先自动执行count查询获取总记录数, 默认为false.
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * 设置查询对象时是否自动先执行count查询获取总记录数.
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}


	/**
	 * 是否还有上一页.
	 */
	public boolean isHasPre() {
		return (page - 1 >= 1);
	}

	/**
	 * 取得上页的页号, 序号从1开始.
	 * 当前页为首页时返回首页序号.
	 */
	public int getPrePage() {
		if (isHasPre()) {
			return page - 1;
		} else {
			return page;
		}
	}

	@Override
	public String toString() {
		return "BaseListRequest [keyword=" + keyword + ", page=" + page
				+ ", pagesize=" + pagesize + ", orderBy=" + orderBy
				+ ", order=" + order + ", autoCount=" + autoCount
				+ ", listAll=" + listAll + "]; " + super.toString();
	}
}
