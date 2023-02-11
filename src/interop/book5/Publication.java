package interop.book5;

// By default, Publication is a subclass of java.lang.Object.

public class Publication {
  private String title;
  private String author;

  public Publication(String t, String a) {
    title = t;
    author = a;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }
}