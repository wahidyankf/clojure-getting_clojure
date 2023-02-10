(ns c02_vectors_and_lists)

;; one thing after another

[1 2 3 4]
;; => [1 2 3 4]
[1 "two" 3 "four" true]
;; => [1 "two" 3 "four" true]
[[1 true 3 "four" 5] 6]
;; => [[1 true 3 "four" 5] 6]

;; A toolkit of functions

(vector true 3 "four" 5)
;; => [true 3 "four" 5]

(def novels ["Emma" "Coma" "War and Peace"])
(count novels)
;; => 3
(first novels)
;; => "Emma"
(rest novels)
;; => ("Coma" "War and Peace")
(into [] (rest novels))
;; => ["Coma" "War and Peace"]
(rest (rest novels))
;; => ("War and Peace")

(def year-books ["1491","April 1865","1984","2001"])
(def third-book (first (rest (rest year-books))))

year-books
;; => ["1491" "April 1865" "1984" "2001"]
third-book
;; => "1984"
(nth year-books 2)
;; => "1984"
(year-books 2)
;; => "1984"

;; Growing your vectors
novels
;; => ["Emma" "Coma" "War and Peace"]
(conj novels "Carrie")
;; => ["Emma" "Coma" "War and Peace" "Carrie"]
(cons "Carrie" novels)
;; => ("Carrie" "Emma" "Coma" "War and Peace")

;; Lists

'(1 2 3)
;; => (1 2 3)
'(1 2 3 "four" 5 "six")
;; => (1 2 3 "four" 5 "six")
'([1 2 ("A" "list" "inside a" "vector")] "inside" "a" "list")
;; => ([1 2 ("A" "list" "inside a" "vector")] "inside" "a" "list")
(list 1 2 3 "four" 5 "six")
;; => (1 2 3 "four" 5 "six")

(def poems `("Iliad" "Odyssey" "Now we are six"))
(count poems)
;; => 3
(first poems)
;; => "Illiad"
(rest poems)
;; => ("Odyssey" "Now we are six")
(nth poems 2)
;; => "Now we are six"
(conj poems "Jabberwocky")
;; => ("Jabberwocky" "Illiad" "Odyssey" "Now we are six")

(def vector-poems ["Iliad" "Odyssey" "Now we are six"])
(conj vector-poems "Jabberwocky")
;; => ["Iliad" "Odyssey" "Now we are six"]

;; Staying out of trouble
novels
;; => ["Emma" "Coma" "War and Peace"]
(conj novels "Jaws")
;; => ["Emma" "Coma" "War and Peace" "Jaws"]
novels
;; => ["Emma" "Coma" "War and Peace"]

(def more-novels (conj novels "Jaws"))
more-novels
;; => ["Emma" "Coma" "War and Peace" "Jaws"]
