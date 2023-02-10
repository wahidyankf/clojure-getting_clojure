(ns c05_more_capable_functions
  (:require [clojure.repl :refer [doc]]))

;; One function, different parameters

(defn greet1
  ([to-whom] (str "Welcome to Blotts Books" " " to-whom))
  ([message to-whom] (str message " " to-whom)))
(greet1 "it may concern")
;; => "Welcome to Blotts Books it may concern"
(greet1 "hola" "it may concern")
;; => "hola it may concern"

(defn greet
  ([to-whom] (greet "Welcome to Blotts Books" to-whom))
  ([message to-whom] (str message " " to-whom)))

(greet "it may concern")
;; => "Welcome to Blotts Books it may concern"
(greet "hola" "it may concern")
;; => "hola it may concern"

;; Arguments with wild abandon

(defn print-any-args [& args]
  (str "My arguments are:" " " args))
(print-any-args 7 true nil)
;; => "My arguments are: (7 true nil)"

(defn first-argument [& args]
  (first args))
(first-argument 1 2 3 4)
;; => 1

(defn new-first-argument [x & _] x)
(new-first-argument 4 3 2 1)
;; => 4

;; Multimethods

(def book ["War and Peace"  "Tolstoy"])
(defn normalize-book-1 [book]
  (if (vector? book)
    {:title (first book) :author (second book)}
    (if (contains? book :title)
      book
      {:title (:book book) :author (:by book)})))
(normalize-book-1 book)
;; => {:title "War and Peace", :author "Tolstoy"}

(defn dispatch-book-format [book]
  (cond
    (vector? book) :vector-book
    (contains? book :title) :standard-map
    (contains? book :book) :alternative-map))
(defmulti normalize-book dispatch-book-format)
(defmethod normalize-book :vector-book [book]
  {:title (first book) :author (second book)})
(defmethod normalize-book :standard-map [book]
  book)
(defmethod normalize-book :alternative-map [book]
  {:title (:book book) :author (:by book)})
(normalize-book {:title "War and Peace" :author "Tolstoy"})
;; => {:title "War and Peace", :author "Tolstoy"}
(normalize-book {:book "War and Peace" :by "Tolstoy"})
;; => {:title "War and Peace", :author "Tolstoy"}
(normalize-book ["War and Peace" "Tolstoy"])
;; => {:title "War and Peace", :author "Tolstoy"}

(def books-1 [{:title  "Pride and Prejudice"  :author  "Austen"  :genre :romance}
              {:title  "World War Z"  :author  "Brooks"  :genre :zombie}])
 	;; Remember you can use keys like :genre like functions on maps.
(defmulti book-description :genre)
(defmethod book-description :romance [book]
  (str  "The heart warming new romance by "  (:author book)))
(defmethod book-description :zombie [book]
  (str  "The heart consuming new zombie adventure by "  (:author book)))
(book-description (first books-1))
;; => "The heart warming new romance by Austen"
(book-description (second books-1))
;; => "The heart consuming new zombie adventure by Brooks"
(def ppz {:title  "Pride and Prejudice and Zombies"
          :author "Grahame-Smith"
          :genre :zombie-romance})
(defmethod book-description :zombie-romance [book]
  (str  "The heart warming and consuming new romance by "  (:author book)))
(book-description ppz)
;; => "The heart warming and consuming new romance by Grahame-Smith"

;; Deeply recursive

(def books
  [{:title  "Jaws"   :copies-sold 2000000}
   {:title  "Emma"   :copies-sold 3000000}
   {:title  "2001"   :copies-sold 4000000}])
(defn sum-copies
  ([books] (sum-copies books 0))
  ([books total]
   (if (empty? books)
     total
     (sum-copies
      (rest books)
      (+ total (:copies-sold (first books)))))))
(sum-copies books)
;; => 9000000

(defn better-sum-copies [books]
  (loop [books books total 0]
    (if (empty? books)
      total
      (recur
       (rest books)
       (+ total (:copies-sold (first books)))))))
(better-sum-copies books)
;; => 9000000

(defn even-better-sum-copies [books] (apply + (map :copies-sold books)))
(even-better-sum-copies books)
;; => 9000000

;; Docstring

(defn average
  "Return the average of the two parameters"
  [a b]
  (/ (+ a b) 2.0))
(average 10 20)
;; => 15.0

(defn multi-average
  "return the average of 2 or 3 numbers"
  ([a b]
   (/ (+ a b) 2.0))
  ([a b c]
   (/ (+ a b c) 3.0)))
(multi-average 10 20)
;; => 15.0
(multi-average 10 20 30)
;; => 20.0

(doc multi-average)

(defn- print-book [_])
(defn- ship-book [_])

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn publish-book-old [book]
  (when-not (contains? book :title)
    (throw (ex-info "Books must contain :title" {:book book})))
  (print-book book)
  (ship-book book))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(print-book book)
(ship-book book)
;; the value should be vector-expression for :pre condition

(defn publish-book [book]
  {:pre [(:title book) (:author book)]
   :post [(boolean? %)]}
  (print-book book)
  (ship-book book)
  (boolean? book))

;; (publish-book {:title "Yo"})
;; => Execution error (AssertionError) at follow-along.c05-more-capable-functions/publish-book (c05_more_capable_functions.clj:168).
;;    Assert failed: (:author book)

(publish-book {:title "Yo" :author 122})
;; => false