package com.web.joke.enity;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/8/18.
 */
@Entity
@Table(name = "joke_page")
public class SingleAlert {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = true, name="content")
    private String content;
    @Column(nullable = true, name="prompt")
    private int prompt;//0不带回答，1带回答
    @Column(nullable = true, name="answer")
    private String answer;
    @Column(nullable = true, name="wrong")
    private String wrong;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPrompt() {
        return prompt;
    }

    public void setPrompt(int prompt) {
        this.prompt = prompt;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getWrong() {
        return wrong;
    }

    public void setWrong(String wrong) {
        this.wrong = wrong;
    }
}
