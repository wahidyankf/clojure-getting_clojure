(ns ch19-read-and-eval
  (:require [clojure.java.io :as io]))

;; You got data on my code!

'(helvetica times-roman [comic-sans]
            (futura gil-sans
                    (courier "All the fonts I have loved!")))

;; Still just data -- note the quote.

'(defn print-greeting [preferred-customer]
   (if preferred-customer
     (println "Welcome back!")))

;; Now this is code!

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn print-greeting [preferred-customer]
  (if preferred-customer
    (println "Welcome back!")
    nil))

;; Reading and evaluating

(read)

 	;; A complicated string with some escaped quotes.

(def s
  "(defn print-greeting [preferred-customer]
 	     (if preferred-customer (println \"Welcome back!\")))")

;; A four-element list.
(read-string s)

;; a three element list
(def a-data-structure '(+ 2 2))
;; The number 4
(eval a-data-structure)
;; => 4

 	;; Bind some-data to a list

(def some-data
  '(defn print-greeting [preferred-customer]
     (if preferred-customer (str  "Welcome back!"))))

 	;; At this point we have some-data defined,
 	;; but not the print-greeting function.
 	;; Now let's eval some-data...

(eval some-data)

;; And now print-greeting is defined!
(print-greeting true)
;; => "Welcome back!"

(eval 55)
(eval :hello)
(eval "hello")

(def title "For Whom the Bell Tolls")
;; Get hold of the unevaluated symbol 'title...
(def the-symbol 'title)
;; and evaluate it
(eval the-symbol)
;; => "For Whom the Bell Tolls
;; While a list gets evaluated as a function call
(eval '(count title))
;; => 23

(def fn-name 'print-greeting)
(def args (vector 'preferred-customer))
(def the-str (list 'str  "Welcome back!"))
(def body (list 'if 'preferred-customer the-str))
(eval (list 'defn fn-name args body))
(eval (list 'print-greeting true))

;; The homoiconic advantage

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn read-source [path]
  (with-open [r (java.io.PushbackReader. (io/reader path))]
    (loop [result []]
      (let [expr (read r false :eof)]
        (if (= expr :eof)
          result
          (recur (conj result expr)))))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn russ-repl []
  (loop []
    (println (eval read))
    (recur)))
;; (russ-repl)

;; An eval of your own

(declare reval)
(defn eval-symbol [expr]
  (.get (ns-resolve *ns* expr)))
(defn eval-vector [expr]
  (vec (map reval expr)))
(defn eval-list [expr]
  (let [evaled-items (map reval expr)
        f (first evaled-items)
        args (rest evaled-items)]
    (apply f args)))
(defn reval [expr]
  (cond
    (string? expr) expr
    (keyword? expr) expr
    (number? expr) expr
    (symbol? expr) (eval-symbol expr)
    (vector? expr) (eval-vector expr)
    (list? expr) (eval-list expr)
    :else :completely-confused))
(reval "test")
;; => "test"
(reval (str "a" "b"))
;; => "ab"
(reval 1)
;; => 1
(reval the-symbol)
;; => "For Whom the Bell Tolls"
(reval [1 2 3])
;; => [1 2 3]
;; (reval '(1 2 3))

;; In the wild

(def books1 (with-meta ["Emma" "1984"] {:favorite-books true}))
books1
;; => ["Emma" "1984"]
(def books2 ^:favorite-books ["Emma" "1984"])
books2
;; => ["Emma" "1984"]
(meta books2)
;; => {:favorite-books true}
(def books3 (with-meta ["Emma" "1984"] {:favorite-books false}))
(= books2 books3)
;; => true

(defn add2
  "Return the sum of two numbers"
  [a b]
  (+ a b))
(meta #'add2)
;; => {:arglists ([a b]),
;;     :doc "Return the sum of two numbers",
;;     :line 145,
;;     :column 1,
;;     :file "/home/wkf/wkf-repos/learn/clojure/clojure-getting_clojure/src/ch19_read_and_eval.clj",
;;     :name add2,
;;     :ns #namespace[ch19-read-and-eval]}
(def md (meta books3))
(count md)
;; => 1
(vals md)
;; => (false)

;; Staying out of trouble
;; don't use eval code

(def s-1  "#=(str \"All your bases...\")")
(read-string s-1)
;; => "All your bases..."

;; Wrapping up