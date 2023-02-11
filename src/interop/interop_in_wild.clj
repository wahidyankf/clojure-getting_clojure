(ns interop.interop-in-wild
  (:import com.google.gson.Gson))

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

