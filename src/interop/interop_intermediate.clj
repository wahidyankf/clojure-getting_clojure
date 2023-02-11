(ns interop.interop-intermediate
  (:import #_{:clj-kondo/ignore [:unused-import]}
   (java.io File InputStream))
  (:import (interop.book5 Book Publication)))

(def authors-2 (File. "authors.txt"))
(.exists authors-2)
;; => true

(def temp-authors-file (File/createTempFile "authors_list" ".txt"))
temp-authors-file
;; => #object[java.io.File 0x58c1b70e "/tmp/authors_list7955309613931870814.txt"]

(.getNumberChapters (interop.book5.Book. "A title" "An author" 10))
;; => 10
(.getNumberChapters (Book. "A title" "An author" 10))
;; => 10
(.getTitle (Publication. "A title" "An author"))
;; => "A title"
(.getAuthor (Publication. "A title" "An author"))
;; => "An author"