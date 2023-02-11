package java_source.book5;

// Book is now a subclass of Publication, which handles the author and title.

public class Book extends Publication {
  private int numberChapters;

  public Book(String t, String a, int nChaps) {
    super(t, a);
    numberChapters = nChaps;
  }

  public int getNumberChapters() {
    return numberChapters;
  }
}