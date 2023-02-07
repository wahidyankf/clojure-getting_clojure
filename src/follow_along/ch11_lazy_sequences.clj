(ns follow-along.ch11-lazy-sequences)

;; Sequences without end

(def jack  "All work and no play makes Jack a dull boy.")
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def text [jack jack jack jack jack jack jack jack jack jack])
(def repeated-text (repeat jack))
(first repeated-text)
;; => "All work and no play makes Jack a dull boy."
(nth repeated-text 10)
;; => "All work and no play makes Jack a dull boy."
(nth repeated-text 10202)
;; => "All work and no play makes Jack a dull boy."

(take 4 repeated-text)
;; => ("All work and no play makes Jack a dull boy."
;;     "All work and no play makes Jack a dull boy."
;;     "All work and no play makes Jack a dull boy."
;;     "All work and no play makes Jack a dull boy.")

;; More interesting laziness

(take 7 (cycle [1 2 3]))
;; => (1 2 3 1 2 3 1)
(def numbers (iterate inc 1))
(first numbers)
;; => 1
(nth numbers 0)
;; => 1
(nth numbers 1)
;; => 2
(nth numbers 99)
;; => 100
(take 5 numbers)
;; => (1 2 3 4 5)

;; Lazy friends
(def many-nums (take 1000000000 (iterate inc 1)))
;; take waits to be asked before it does anything.
(take 20 (take 1000000000 (iterate inc 1)))
;; => (1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20)
(take 20 many-nums)
;; => (1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20)

;; Map is lazy
(def evens (map #(* 2 %) (iterate inc 1)))
(take 20 evens)
;; => (2 4 6 8 10 12 14 16 18 20 22 24 26 28 30 32 34 36 38 40)

;; interleave is lazy
(take 10 (interleave numbers evens))
;; => (1 2 2 4 3 6 4 8 5 10)

;; Laziness in Practice

(def titles (map #(str "Whell of Time, Book " %) numbers))
;; => #'follow-along.ch11-lazy-sequences/titles
(def first-names ["Bob" "Jane" "Chuck" "Leo"])
(def last-names ["Jordan"   "Austen"   "Dickens"   "Tolstoy"   "Poe"])
(defn combine-names [fname lname]
  (str fname " " lname))
(def authors
  (map combine-names
       (cycle first-names)
       (cycle last-names)))
(defn make-book [title author]
  {:author author :title title})
(def test-books (map make-book titles authors))
(take 5 test-books)
;; => ({:author "Bob Jordan", :title "Whell of Time, Book 1"}
;;     {:author "Jane Austen", :title "Whell of Time, Book 2"}
;;     {:author "Chuck Dickens", :title "Whell of Time, Book 3"}
;;     {:author "Leo Tolstoy", :title "Whell of Time, Book 4"}
;;     {:author "Bob Poe", :title "Whell of Time, Book 5"})

;; Behind the scenes

(lazy-seq [1 2 3])
;; => (1 2 3)

(defn chatty-vector []
  (println "Here we go!")
  [1 2 3])
(def s (lazy-seq (chatty-vector)))
;; This will cause "Here we go!" to print
(first s)
;; => 1

(defn my-repeat [x]
  (cons x (lazy-seq (my-repeat x))))
(take 5 (my-repeat 1))
;; => (1 1 1 1 1)

(defn my-map [f col]
  (when-not (empty? col)
    (cons (f (first col))
          (lazy-seq (my-map f (rest col))))))
(take 5 (my-map #(* 2 %) (iterate inc 1)))
;; => (2 4 6 8 10)

;; Staying out of trouble

;; Don't print all of the entries of lazy sequences
;; Do this instead
(set! *print-length* 10)
(print numbers)

;; Get the contents of the file as a string.​
(slurp  "chap1.txt")
(def chapters (take 10 (map slurp (map #(str  "chap"  %  ".txt") numbers))))

;; Runs down our lazy sequence, accessing each element, and returns the sequence
;; Effectively wringing the laziness out
(doall chapters)

;; Read the chapters NOW!
;; doseq realizes each item in a lazy sequence one at a time
;; but doesn't try to hold onto the whole thing
(doseq [c chapters]
  (println  "The chapter text is"  c))