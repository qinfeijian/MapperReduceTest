package com.yunforge.mapreduce.demo1;

public class IndexClass {
	
    private String id;
	private String title;
	private String content;
	private String image;
	private String url;
	private String pcurl;
	private String type;
	private String typeName;
	private String createDate;
	private String create_index_date;
	private String come_from_table;
	
	public IndexClass() {
		super();
	}
	public IndexClass(String title, String content) {
		super();
		this.title = title;
		this.content = content;
	}
	public String getId() {
	    return id;
	}
	public void setId(String id) {
	    this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
    public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getPcurl() {
		return pcurl;
	}
	public void setPcurl(String pcurl) {
		this.pcurl = pcurl;
	}
	public String getCreate_index_date() {
		return create_index_date;
	}
	public void setCreate_index_date(String create_index_date) {
		this.create_index_date = create_index_date;
	}
	public String getCome_from_table() {
		return come_from_table;
	}
	public void setCome_from_table(String come_from_table) {
		this.come_from_table = come_from_table;
	}
	@Override
	public String toString() {
		return "IndexClass [id=" + id + ", title=" + title + ", content=" + content + ", image=" + image + ", url="
				+ url + ", pcurl=" + pcurl + ", type=" + type + ", typeName=" + typeName + ", createDate=" + createDate
				+ ", create_index_date=" + create_index_date + ", come_from_table=" + come_from_table + "]";
	}
	
	
	
	

}
