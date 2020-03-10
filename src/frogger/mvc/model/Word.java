package frogger.mvc.model;

public class Word {
    private String content;
    private int row;

    public  Word(String content, int row)
    {
        this.content=content;
        this.row=row;
    }

    public String getContent() {
        return content;
    }

    public int getRow() {
        return row;
    }

}
