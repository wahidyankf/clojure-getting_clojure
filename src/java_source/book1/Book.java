package java_source.book1;

public class Book {

  // The fields. Every Book instance has its own title, author, and chapter.

  public String title;
  public String author;
  public int numberChapters;

  // Constructor method.

  public Book(String t, String a, int nChaps) {
    title = t;
    author = a;
    numberChapters = nChaps;
  }
}