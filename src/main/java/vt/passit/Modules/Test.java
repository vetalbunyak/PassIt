package vt.passit.Modules;

public class Test {
    private int id;
    private String title;
    private String description;
    private int popularityScore;

    public Test(int id, String title, String description, int popularityScore) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.popularityScore = popularityScore;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPopularityScore() {
        return popularityScore;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", popularityScore=" + popularityScore +
                '}';
    }
}
