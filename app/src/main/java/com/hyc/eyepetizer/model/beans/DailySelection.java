package com.hyc.eyepetizer.model.beans;

import java.util.List;

public class DailySelection {
    private long nextPublishTime;
    private Object dialog;
    private String newestIssueType;
    private String nextPageUrl;

    public List<Issue> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<Issue> issueList) {
        this.issueList = issueList;
    }

    private List<Issue> issueList;

    public long getNextPublishTime() {
        return this.nextPublishTime;
    }

    public void setNextPublishTime(long nextPublishTime) {
        this.nextPublishTime = nextPublishTime;
    }

    public Object getDialog() {
        return this.dialog;
    }

    public void setDialog(Object dialog) {
        this.dialog = dialog;
    }

    public String getNewestIssueType() {
        return this.newestIssueType;
    }

    public void setNewestIssueType(String newestIssueType) {
        this.newestIssueType = newestIssueType;
    }

    public String getNextPageUrl() {
        return this.nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }
}
