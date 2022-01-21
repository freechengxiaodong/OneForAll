package com.spring.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "cms_news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "intro")
    private String intro;

    public News(){

    }

    public News(int id, String title, String intro) {
        this.id = id;
        this.title = title;
        this.intro = intro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
