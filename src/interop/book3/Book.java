package interop.book3;

public class Book {

  // Make the fields private, accessible only inside this class.

  private String title;
  private String author;
  private int numberChapters;

  public Book(String t, String a, int nChaps) {
    title = t;
    author = a;
    numberChapters = nChaps;
  }

  // Add getter methods to make fields available to the outside world.

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public int getNumberChapters() {
    return numberChapters;
  }
}
