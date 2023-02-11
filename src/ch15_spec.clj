(ns ch15-spec
  (:require [clojure.spec.alpha :as s]))

;; This is the data you're looking for

(s/valid? number? 44)
;; => true

(s/valid? number? :hello)
;; => false

(def n-gt-10 (s/and number? #(> % 10)))

(s/valid? n-gt-10 1)
;; => false

(s/valid? n-gt-10 10)
;; => false

(s/valid? n-gt-10 11)
;; => true

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def n-gt-10-lt-100
  (s/and number? #(> % 10) #(< % 100)))

(def n-or-s (s/or :a-number number? :a-string string?))

(s/valid? n-or-s "Hello!")
;; => true

(s/valid? n-or-s 99)
;; => true

(s/valid? n-or-s 'foo)
;; => false

(def n-gt-10-or-s (s/or :greater-10 n-gt-10 :a-symbol symbol?))

(s/valid? n-gt-10-or-s 10)
;; => false

;; Spec'ing collections

(def coll-of-strings (s/coll-of string?))
(s/valid? coll-of-strings ["a" "bc"])
;; => true
(s/valid? coll-of-strings '("a" "bc"))
;; => true

;; or a collection of numbers or strings, perhaps ["Emma" 1815 "Jaws" 1974]
(def coll-of-n-or-s (s/coll-of n-or-s))
(s/valid? coll-of-n-or-s ["a" 1 "2"])
;; => true

(def book-s
  (s/keys :req-un [:inventory.core/title
                   :inventory.core/author
                   :inventory.core/copies]))
(s/valid? book-s {:title  "Emma"  :author  "Austen"  :copies 10})
;; => true
(s/valid? book-s {:title  "Arabian Nights"  :copies 17})
;; => false
(s/valid? book-s {:title  "2001"  :author  "Clarke"  :copies 1 :published 1968})
;; => true

(def s-n-s-n (s/cat :s1 string? :n1 number? :s2 string? :n2 number?))
(s/valid? s-n-s-n ["Emma"  1815  "Jaws"  1974])
;; => true
(s/valid? s-n-s-n ["Emma"  1815  "Jaws"  1974 "a"])
;; => false

;; Registering specs

;; validate a book against the registered spec
(s/valid? :inventory.core/book {:title "Dracula" :author "Stoker" :copies 10})
;; => true
(s/valid? :inventory.core/book {:title "Dracula" :author "Stoker" :copie 10})
;; => false
(s/valid? :inventory.core/book {:title "Dracula" :author "Stoker" :copies "10"})
;; => false

;; Spec'ing Maps (again)

;; Why? Why? Why?

(s/explain :inventory.core/book {:title "Dracula" :author "Stoker" :copies 10})
;; "Success!"
;; => nil
(s/explain :inventory.core/book {:title "Dracula" :author "Stoker" :copies "10"})
;; "10" - failed: int? in: [:copies] at: [:copies] spec: :inventory.core/copies
;; => nil
(s/explain n-gt-10 1)
;; 1 - failed: (> % 10)
;; => nil
(s/explain :inventory.core/book {:author :austen :title :emma})
;; :emma - failed: string? in: [:title] at: [:title] spec: :inventory.core/title
;; {:author :austen, :title :emma} - failed: (contains? % :copies) spec: :inventory.core/book
;; => nil

(s/conform number? 1968)
;; => 1968
(s/conform number? "a")
;; => :clojure.spec.alpha/invalid
(s/conform s-n-s-n ["Emma" 1815 "Jaws" 1974])
;; => {:s1 "Emma", :n1 1815, :s2 "Jaws", :n2 1974}
(s/conform s-n-s-n ["Emma" 1815 "Jaws" 1974 "a"])
;; => :clojure.spec.alpha/invalid
