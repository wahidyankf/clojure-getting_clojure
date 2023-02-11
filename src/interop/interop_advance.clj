(ns interop.interop-advance
  (:import com.google.gson.Gson)
  (:import (java.io File)))

(def gson-obj (Gson.))
(.toJson gson-obj 44)
;; => "44"
(.toJson gson-obj {:title "1984" :author "Orwell"})
;; => "{\":title\":\"1984\",\":author\":\"Orwell\"}"
(.toJson gson-obj {"title" "1984" "author" "Orwell"})
;; => "{\"title\":\"1984\",\"author\":\"Orwell\"}"

(def v [1 2 3])
(.count v)
;; => 3

(def author "Dickens")
(def the-var #'author)
;; calling the get method from the-var
(.get the-var)
;; => "Dickens"
;; calling the static method from the-var
(.-sym the-var)
;; => author

(def c (cons 99 [1 2 3]))
(class c)
;; => clojure.lang.Cons
;; And call into the Java
(.first c)
;; => 99
(.more c)
;; => (1 2 3)

;; Staying out of trouble
(def files [(File. "authors.txt") (File. "titles.txt")])
(map (memfn exists) files)
;; => (true false)

;; Many of java objects are mutable
(def jv-favorite-books (java.util.Vector.))
(.addElement jv-favorite-books "Emma")
jv-favorite-books
;; => ["Emma"]
(.addElement jv-favorite-books "Andromeda Strain")
jv-favorite-books
;; => ["Emma" "Andromeda Strain"]
(.addElement jv-favorite-books "2001")
jv-favorite-books
;; => ["Emma" "Andromeda Strain" "2001"]

(def thankfully-immutable-books (vec jv-favorite-books))
thankfully-immutable-books
;; => ["Emma" "Andromeda Strain" "2001"]
(.addElement jv-favorite-books "Hola")
jv-favorite-books
;; => ["Emma" "Andromeda Strain" "2001" "Hola"]
thankfully-immutable-books
;; => ["Emma" "Andromeda Strain" "2001"]
