package com.jiqu.database;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "NOTICE_TABLE".
 */
public class NoticeTable {

    private Long id;
    private String notice;

    public NoticeTable() {
    }

    public NoticeTable(Long id) {
        this.id = id;
    }

    public NoticeTable(Long id, String notice) {
        this.id = id;
        this.notice = notice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

}
