package xyz.ersut.core.intercepter.mybits.mysqlpage;

import lombok.*;

import java.io.Serializable;

/**
 * @author 王二飞
 */
@SuppressWarnings("unused")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 当前页 */
	private int currentPage=1;
	/** 页面显示记录数 */
	private int pageSize=10;
	/** 总记录数 */
	private int totalResultSize;
	/** 总页数 */
	private int totalPageSize;
	/** 是否是首页 */
	private boolean first = false;
	/** 是否是尾页 */
	private boolean last = false;
	/** 是否采用自动分页 */
	private boolean automaticCount = true;


	public boolean isFirst() {
		 return getCurrentPage() <= 1;
	}
	public void setFirst(boolean first) {
		this.first = first;
	}
	public boolean isLast() {
		 return getCurrentPage() >= getTotalPageSize();
	}
	public void setLast(boolean last) {
		this.last = last;
	}
}
