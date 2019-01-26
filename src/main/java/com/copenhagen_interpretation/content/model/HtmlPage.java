package com.copenhagen_interpretation.content.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "htmlPage")
public class HtmlPage {
    private String breadcrumb;
    private String content;
    private String description;
    private String keywords;
    private String title;

    public String getBreadcrumb() {
        return breadcrumb;
    }

    @XmlElement
    public void setBreadcrumb(String breadcrumb) {
        this.breadcrumb = breadcrumb.trim();
    }

    public String getContent() {
        return content;
    }

    @XmlElement
    public void setContent(String content) {
        this.content = content.trim();
    }

    public String getDescription() {
        return description;
    }

    @XmlElement
    public void setDescription(String description) {
        this.description = description.trim();
    }

    public String getKeywords() {
        return keywords;
    }

    @XmlElement
    public void setKeywords(String keywords) {
        this.keywords = keywords.trim();
    }

    public String getTitle() {
        return title;
    }

    @XmlElement
    public void setTitle(String title) {
        this.title = title.trim();
    }
}
