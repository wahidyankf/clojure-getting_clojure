package java_source.book2;

public class Book {

  // Note the fields are public.

  public String title;
  public String author;
  public int numberChapters;

  public Book(String t, String a, int nChaps) {
    title = t;
    author = a;
    numberChapters = nChaps;
  }

  public void publish() {
    // Do something to publish the book.
  }

  public void payRoyalties() {
    // Do something to pay royalties.
  }
}