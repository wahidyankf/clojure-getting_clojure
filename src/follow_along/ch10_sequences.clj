(ns follow-along.ch10-sequences
  (:require [clojure.java.io :as io]))

;; one thing after another

(defn flavor [x]
  (cond
    (list? x) :list
    (vector? x) :vector
    (set? x) :set
    (map? x) :map
    (string? x) :string
    :else :unknown))
(defmulti my-count-old flavor)
(defn- list-specific-count [x] x)
(defn- vector-specific-count [x] x)
(defmethod my-count-old :list [x]
  (list-specific-count x))
(defmethod my-count-old :vector [x]
  (vector-specific-count x))

(def title-seq (seq ["Emma"   "Oliver Twist"   "Robinson Crusoe"]))
title-seq
;; => ("Emma" "Oliver Twist" "Robinson Crusoe")
(seq '("Emma"   "Oliver Twist"   "Robinson Crusoe"))
;; => ("Emma" "Oliver Twist" "Robinson Crusoe")
(seq {:title  "Emma" , :author  "Austen" , :published 1815})
;; => ([:title "Emma"] [:author "Austen"] [:published 1815])

;; Calling seq on a sequence is a noop.
(seq (seq ["Red Queen"   "The Nightingale"   "Uprooted"]))
;; => ("Red Queen" "The Nightingale" "Uprooted")

(seq [])
;; => nil
(seq '())
;; => nil
(seq {})
;; => nil

;; A universal interface

(first title-seq)
;; => "Emma"
(rest title-seq)
;; => ("Oliver Twist" "Robinson Crusoe")
(cons "Emma" (rest title-seq))
;; => ("Emma" "Oliver Twist" "Robinson Crusoe")

(defn my-count [col]
  (let [the-seq (seq col)]
    (loop [n 0 s the-seq]
      (if (seq s)
        (recur (inc n) (rest s))
        n))))
(my-count '(1 2 3))
;; => 3

;; rest function always returns a sequence
(rest [1 2 3])
;; => (2 3)
(rest {:fname "Jane" :lname "Austen"})
;; => ([:lname "Austen"])
(next {:fname "Jane" :lname "Austen"})
;; => ([:lname "Austen"])
(cons 0 [1 2 3])
;; => (0 1 2 3)
(cons 0 #{1 2 3})
;; => (0 1 3 2)

;; A rich toolkit

(def titles ["Jaws"   "Emma"   "2001"  "Dracula"])
(sort titles)
;; => ("2001" "Dracula" "Emma" "Jaws")
(reverse titles)
;; => ("Dracula" "2001" "Emma" "Jaws")
(reverse (sort titles))
;; => ("Jaws" "Emma" "Dracula" "2001")

(def titles-and-authors ["Jaws"   "Benchley"   "2001"   "Clarke"])
(partition 2 titles-and-authors)
;; => (("Jaws" "Benchley") ("2001" "Clarke"))
(partition 3 titles-and-authors)
;; => (("Jaws" "Benchley" "2001"))

(def titles-2  ["Jaws"   "2001"])
(def authors-2 '("Benchley"   "Clarke"))
(interleave titles-2 authors-2)
;; => ("Jaws" "Benchley" "2001" "Clarke")
(interleave titles authors-2)
;; => ("Jaws" "Benchley" "Emma" "Clarke")

(def scary-animals ["Lions" "Tigers" "Bears"])
(interpose "and" scary-animals)
;; => ("Lions" "and" "Tigers" "and" "Bears")

;; Made richer with functional values

(filter neg? '(1 -22 3 -99 4 5 6 -77))
;; => (-22 -99 -77)

(def books
  [{:title  "Deep Six"  :price 13.99 :genre :sci-fi :rating 6 :sales 1}
   {:title  "Dracula"  :price 1.99 :genre :horror :rating 7 :sales 1}
   {:title  "Emma"  :price 7.99 :genre :comedy :rating 9 :sales 1}
   {:title  "2001"  :price 10.50 :genre :sci-fi :rating 5 :sales 1}])
(defn cheap? [book]
  (when (<= (:price book) 9.99)
    book))
;; find all the inexpensive book
(filter cheap? books)
;; => ({:title "Dracula", :price 1.99, :genre :horror, :rating 7, :sales 1}
;;     {:title "Emma", :price 7.99, :genre :comedy, :rating 9, :sales 1})
(some cheap? books)
;; => {:title "Dracula", :price 1.99, :genre :horror, :rating 7, :sales 1}
(every? cheap? books)
;; => false
(if (some cheap? books)
  "We have cheap books for sale!"
  "Expensive is the new cheap")
;; => "We have cheap books for sale!"

;; Map

(def some-numbers [1, 53, 811])
(def doubled (map #(* 2 %) some-numbers))
doubled
;; => (2 106 1622)

(map (fn [book] (:title book)) books)
;; => ("Deep Six" "Dracula" "Emma" "2001")
(map :title books)
;; => ("Deep Six" "Dracula" "Emma" "2001")

(map (fn [book] (count (:title book))) books)
;; => (8 7 4 4)
(map (comp count :title) books)
;; => (8 7 4 4)
(for [b books]
  (count (:title b)))
;; => (8 7 4 4)

;; Reduce

(def numbers [10 20 30 40 50])
(defn add2 [a b] (+ a b))
(reduce add2 0 numbers)
;; => 150
(reduce + 0 numbers)
;; => 150

;; if we omit the initial value form the call to reduce
;; reduce will use the first element of the collection as the initial value
(reduce + numbers)
;; => 150

(defn hi-price [hi book]
  (if (> (:price book) hi)
    (:price book)
    hi))
(reduce hi-price 0 books)
;; => 13.99

;; Composing a solution

books
;; => [{:title "Deep Six", :price 13.99, :genre :sci-fi, :rating 6, :sales 1}
;;     {:title "Dracula", :price 1.99, :genre :horror, :rating 7, :sales 1}
;;     {:title "Emma", :price 7.99, :genre :comedy, :rating 9, :sales 1}
;;     {:title "2001", :price 10.5, :genre :sci-fi, :rating 5, :sales 1}]
(sort-by :rating books)
;; => ({:title "2001", :price 10.5, :genre :sci-fi, :rating 5, :sales 1}
;;     {:title "Deep Six", :price 13.99, :genre :sci-fi, :rating 6, :sales 1}
;;     {:title "Dracula", :price 1.99, :genre :horror, :rating 7, :sales 1}
;;     {:title "Emma", :price 7.99, :genre :comedy, :rating 9, :sales 1})
(reverse (sort-by :rating books))
;; => ({:title "Emma", :price 7.99, :genre :comedy, :rating 9, :sales 1}
;;     {:title "Dracula", :price 1.99, :genre :horror, :rating 7, :sales 1}
;;     {:title "Deep Six", :price 13.99, :genre :sci-fi, :rating 6, :sales 1}
;;     {:title "2001", :price 10.5, :genre :sci-fi, :rating 5, :sales 1})
(take 3 (reverse (sort-by :rating books)))
;; => ({:title "Emma", :price 7.99, :genre :comedy, :rating 9, :sales 1}
;;     {:title "Dracula", :price 1.99, :genre :horror, :rating 7, :sales 1}
;;     {:title "Deep Six", :price 13.99, :genre :sci-fi, :rating 6, :sales 1})
(map :title (take 3 (reverse (sort-by :rating books))))
;; => ("Emma" "Dracula" "Deep Six")
(interpose " // " (map :title (take 3 (reverse (sort-by :rating books)))))
;; => ("Emma" " // " "Dracula" " // " "Deep Six")
(apply str
       (interpose " // "
                  (map :title (take 3 (reverse (sort-by :rating books))))))
;; => "Emma // Dracula // Deep Six"

;; Other sources of sequences

;; Authors.txt contains
;; Emma
;; Dracula
;; Deep Six
(defn listed-author? [author]
  (with-open [r (io/reader  "authors.txt")]
    (some (partial = author) (line-seq r))))
(listed-author? "Emma")
;; => true
(listed-author? "Emmas")
;; => nil

;; A regular expression that matches Pride and Prejudice followed by anything.
(def re #"Pride and Prejudice.*")
 	;; A string that may or may not match.
(def title  "Pride and Prejudice and Zombies")
 	;; And we have a classic!
(if (re-matches  re title)
  "We have a classic!"
  "No classic for you!")
;; => "We have a classic!"
(if (re-matches  #"Pride and Prejudice.*" title)
  "We have a classic!"
  "No classic for you!")
;; => "We have a classic!"

;; regex #"\w+" matches a single word
(re-seq #"\w+" title)
;; => ("Pride" "and" "Prejudice" "and" "Zombies")

;; in the wild

(apply str
       (interpose " // "
                  (map :title (take 3 (reverse (sort-by :rating books))))))
;; => "Emma // Dracula // Deep Six"

;; there is no performance penalty on using ->>
(->> books
     (sort-by :rating)
     reverse
     (take 3)
     (map :title)
     (interpose " // ")
     (apply str))
;; => "Emma // Dracula // Deep Six"

;; ->> : pipe last
;; -> : pipe first
(->> 1
     (- 1))
;; => 0
(-> 1
    (- 1))
;; => 0

;; Staying out of trouble

(defn total-sales-recur [books]
  (loop [books books total 0]
    (if (empty? books)
      total
      (recur (next books)
             (+ total (:sales (first books)))))))
(defn total-sales [books]
  (apply + (map :sales books)))
(total-sales-recur books)
;; => 4
(total-sales books)
;; => 4

(def maze-runner {:title  "The Maze Runner"  :author  "Dashner"})

(:author maze-runner)
;; => "Dashner"
(seq maze-runner)
;; => ([:title "The Maze Runner"] [:author "Dashner"])
(:author (seq maze-runner))
;; => nil
(rest maze-runner)
;; => ([:author "Dashner"])
(:author (rest maze-runner))
;; => nil

(conj ["Emma"   "1984"   "The Maze Runner"]  "Jaws")
;; => ["Emma" "1984" "The Maze Runner" "Jaws"]
(conj '("Emma"   "1984"   "The Maze Runner")  "Jaws")
;; => ("Jaws" "Emma" "1984" "The Maze Runner")
(cons  "Jaws"  ["Emma"   "1984"   "The Maze Runner"])
;; => ("Jaws" "Emma" "1984" "The Maze Runner")
(cons  "Jaws"  '("Emma"   "1984"   "The Maze Runner"))
;; => ("Jaws" "Emma" "1984" "The Maze Runner")
