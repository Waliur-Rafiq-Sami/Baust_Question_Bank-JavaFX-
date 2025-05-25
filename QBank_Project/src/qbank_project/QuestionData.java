package qbank_project;

import java.sql.Date;

public class QuestionData {

    private Integer id;
    private String questionId;
    private String questionTitle;
    private String type;
    private Integer batch;
    private Double code;
    private String status;
    private String image;
    private Date date;
    private Integer quantity;

    public QuestionData(Integer id, String questionId, String questionTitle,
            String type, Integer batch, Double code,
            String status, String image, Date date) {
        this.id = id;
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.type = type;
        this.batch = batch;
        this.code = code;
        this.status = status;
        this.image = image;
        this.date = date;
    }

    public QuestionData(Integer id, String questionId, String questionTitle,
            String type, Integer batch, Double code,
            String image, Date date) {
        this.id = id;
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.type = type;
        this.batch = batch;
        this.code = code;
        this.image = image;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public String getType() {
        return type;
    }

    public Integer getBatch() {
        return batch;
    }

    public Double getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public Date getDate() {
        return date;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
