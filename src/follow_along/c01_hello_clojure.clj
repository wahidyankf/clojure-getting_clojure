(ns follow-along.c01-hello-clojure)

;; DO two semiconlons add up to a whole colon?

(println "Hello, world!")

(str "Clo" "jure")
;; => "Clojure"
(str "Hello," " " "world" "!")
;; => "Hello, world!"
(str 3 " " 2 " " "Blast off!")
;; => "3 2 Blast off!"

(count "Hello, world")
;; => 12

(count "Hello")
;; => 5

(count "")
;; => 0

(println true)
(println false)
(println "Nobody's home:" nil)
(println "we can print many things:" true false nil)

;; Arithmetic

(+ 1 2)
;; => 3

(* 16 124)
;; => 1984

(- 2000 16)
;; => 1984

(/ 25792 13)
;; => 1984

(/ (+ 1984 2010) 2)
;; => 1997

(/ (+ 1984.0 2010) 2)
;; => 1997.0

;; Not variable assignment but close
(def first-name "Russ")
first-name
;; => "Russ"

(def the-average (/ (+ 20 40.0) 2.0))
the-average
;; => 30.0

;; a function of your own

(defn hello-world []
  (println "Hello, world!"))
(hello-world)

(defn say-welcome [what]
  (println "Welcome to" what))
(say-welcome "Clojure")

(defn average [a b]
  (/ (+ a b) 2.0))
(average 5.0 10.0)
;; => 7.5

;; (/ 100 0)
;; => Execution error (ArithmeticException) at follow-along.c01/eval8056 (form-init14787553069179530715.clj:75).
;;    Divide by zero

(.toUpperCase "test")
;; => "TEST"
(.getName String)
;; => "java.lang.String"
