package application;

import java.time.LocalDate;

public class ResearchPaper {
    private int paperId;
    private int atomicNumber;
    private int userId;
    private String title;
    private String author; 
    private LocalDate publicationDate;
    private String abstractText;
    private String url;

    public ResearchPaper(int paperId, int atomicNumber, int userId, String title, String author, 
                         LocalDate publicationDate, String abstractText, String url) {
        this.paperId = paperId;
        this.atomicNumber = atomicNumber;
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.abstractText = abstractText;
        this.url = url;
    }

    // Getters and setters
    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public int getAtomicNumber() {
        return atomicNumber;
    }

    public void setAtomicNumber(int atomicNumber) {
        this.atomicNumber = atomicNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}