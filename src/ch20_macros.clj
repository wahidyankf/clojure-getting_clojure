(ns ch20-macros)

;; There are three kinds of numbers in the world

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn show-rating-0 [rating]
  (cond
    (pos? rating)  (str  "Good book!")
    (zero? rating) (str  "Totally indifferent.")
    :else          (str  "Run away!")))
(defn arithmetic-if-0 [n pos zero neg]
  (cond
    (pos? n) pos
    (zero? n) zero
    (neg? n) neg))
(defn show-rating-1 [rating]
  (arithmetic-if-0 rating
                   (str  "Good book!")
                   (str  "Totally indifferent.")
                   (str  "Run away!")))
(show-rating-1 10)
;; => "Good book!"

(defn arithmetic-if-1 [n pos-f zero-f neg-f]
  (cond
    (pos? n) (pos-f)
    (zero? n) (zero-f)
    (neg? n) (neg-f)))
(defn show-rating [rating]
  (arithmetic-if-1 rating
                   #(str  "Good book!")
                   #(str  "Totally indifferent.")
                   #(str  "Run away!")))
(show-rating 10)
;; => "Good book!"

(defn arithmetic-if->cond [n pos zero neg]
  (list 'cond (list 'pos? n) pos
        (list 'zero? n) zero
        :else neg))
(arithmetic-if->cond 'rating
                     '(println  "Good book!")
                     '(println  "Totally indifferent.")
                     '(println  "Run away!"))
;; => (cond (pos? rating) (println "Good book!") (zero? rating) (println "Totally indifferent.") :else (println "Run away!"))

;; Macros to the rescue

(def rating 10)
(defmacro arithmetic-if-macro-v1 [n pos zero neg]
  (list 'cond (list 'pos? n) pos
        (list 'zero? n) zero
        :else neg))
(arithmetic-if-macro-v1 rating :loved-it :meh :hated-it)
;; => :loved-it

(defmacro print-it [something]
  (list 'str  "Something is "  something))
(print-it (+ 10 20))
;; => "Something is 30"

;; Easier macros with syntax quoting

`(:a :syntax  "quoted"  :list 1 2 3 4)
;; for the case above, it is the same as:
'(:a :syntax  "quoted"  :list 1 2 3 4)

;; Set up some values.
(def n 100)
(def pos  "It's positive!")
(def zero  "It's zero!")
(def neg  "It's negative")
;; And plug them in the cond.
`(cond
   (pos? ~n) ~pos
   (zero? ~n) ~zero
   :else ~neg)
;; => (clojure.core/cond (clojure.core/pos? 100) "It's positive!" (clojure.core/zero? 100) "It's zero!" :else "It's negative")

(defmacro arithmetic-if-macro-v2 [n pos zero neg]
  `(cond
     (pos? ~n) ~pos
     (zero? ~n) ~zero
     :else ~neg))

(arithmetic-if-macro-v2 rating :loved-it :meh :hated-it)
;; => :loved-it

(=
 (arithmetic-if-macro-v1 rating :loved-it :meh :hated-it)
 (arithmetic-if-macro-v2 rating :loved-it :meh :hated-it))
;; => true

;; In the wild

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defmacro our-when
  [test & body]
  (list 'if test (cons 'do body)))
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defmacro our-cond
  [& clauses]
  (when clauses
    (list 'if (first clauses)
          (if (next clauses)
            (second clauses)
            (throw (IllegalArgumentException.
                    "cond requires an even number of forms")))
          (cons 'clojure.core/cond (next (next clauses))))))
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defmacro our-and
  ([] true)
  ([x] x)
  ([x & next]
   `(let [and# ~x]
      (if and# (and ~@next) and#))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defmacro our-defn-oops [name args & body]
  `(def ~name (fn ~args ~body)))
#_{:clj-kondo/ignore [:unresolved-symbol]}
(our-defn-oops add2 [a b] (+ a b))
;; will expanded into
;; (def add2 (fn [a b] ((+ a b))))

(defmacro our-defn [name args & body]
  `(def ~name (fn ~args ~@body)))
#_{:clj-kondo/ignore [:unresolved-symbol]}
(our-defn add2 [a b] (+ a b))
#_{:clj-kondo/ignore [:unresolved-symbol]}
(add2 2 2)
;; will expanded into
;; (def add2 (fn [a b] (+ a b)))
;; => 4

;; Staying out of trouble

(defmacro mark-the-times []
  (println  "This is code that runs when the macro is expanded.")
  `(println  "This is the generated code."))
;; Expand the macro and you get the 1st println
;; but not the 2nd.
(defn use-the-macro []
  (mark-the-times))
;; Here we will get the second println, which is in the generated
;; code, twice.
(use-the-macro)
(use-the-macro)

(macroexpand-1 '(arithmetic-if 100 :pos :zero :neg))
;; => (cond (pos? 100) :pos (zero? 100) :zero :else :neg)
(macroexpand-1 '(mark-the-times))
;; => (clojure.core/println "This is the generated code.")

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defmacro describe-it [it]
  `(let [value# ~it]
     (cond
       (list? value#) :a-list
       (vector? value#) :a-vector
       (number? value#) :a-number
       :else :no-idea)))
;; (map describe-it [10 "a string" [1 2 3]])
;; => Syntax error compiling at (src/ch20_macros.clj:162:1).
;;    Can't take value of a macro: #'ch20-macros/describe-it

;; write it a function instead
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn describe-it-fn [it]
  (cond
    (list? it) :a-list
    (vector? it) :a-vector
    (number? it) :a-number
    :else :no-idea))


;; Wrapping up