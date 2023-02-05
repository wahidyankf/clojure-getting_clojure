(ns follow-along.c04-logic)

;; The fundamental if

(defn print-greeting [preffered-customer]
  (if preffered-customer
    "Welcome back to Blotts Books!"
    "Welcome to Blotts Books!"))

(print-greeting true)
;; => "Welcome back to Blotts Books!"
(print-greeting false)
;; => "Welcome to Blotts Books!"

;; Asking questions

(= 1 1)
;; => true
(= 1 2)
;; => false
(= 1 1 1 1 1 1 1)
;; => true
(= 1 1 1 1 2 1 1)
;; => false

(not= 1 1)
;; => false
(not= 1 2)
;; => true
(not= 1 1 1 1 1 1 1)
;; => false
(not= 1 1 1 1 2 1 1)
;; => true

(> 1 2)
;; => false
(> 1 2)
;; => false

(number? 1984)
;; => true
(string? "123")
;; => true
(keyword? :test)
;; => true
(map? {:title 1984})
;; => true
(vector? [1984])
;; => true

;; Charge extra if it's an express order or oversized
;; and they are not a preferred customer.
(defn shipping-surcharge? [preferred-customer express oversized]
  (and (not preferred-customer) (or express oversized)))
(shipping-surcharge? false true true)
;; => true

;; Truthy and Falsy

(if 1 true false)
;; => true
(if "hello" true false)
;; => true
(if [1 2 3] true false)
;; => true
(if "" true false)
;; => true
(if 0 true false)
;; => true
(if -1 true false)
;; => true

;; only these 2 are falsy
(if false true false)
;; => false
(if nil true false)
;; => false

;; Do and when

(do
  (println "This is four expressions")
  (println "All grouped together as one")
  (println "That prints some stuff and then evaluates to 44")
  44)
;; => 44

(let [preffered-customer true]
  #_{:clj-kondo/ignore [:unused-value]}
  (when preffered-customer
    "Hello returncing customer!"
    "Welcome back to Blotts Books!"))
;; => "Welcome back to Blotts Books!"

;; else is just a normal keyword which evaluated as a truthy value
(defn shipping-charge [preffered-customer order-amount]
  (cond
    preffered-customer 0.0
    (< order-amount 50.0) 5.0
    (< order-amount 100.0) 10.0
    :else (* 0.1 order-amount)))
(shipping-charge false 101)
;; => 10.100000000000001

(defn customer-greeting [status]
  (case status
    :gold "Welcome, welcome, welcome back!!!"
    :preffered "Welcome back!"
    "Welcome to Blotts Books"))
(customer-greeting :gold)
;; => "Welcome, welcome, welcome back!!!"
(customer-greeting :preffered)
;; => "Welcome back!"
(customer-greeting nil)
;; => "Welcome to Blotts Books"

;; Throwing and catching

;; (/ 0 0)
;; => Execution error (ArithmeticException) at follow-along.c04-logic/eval15484 (form-init4924825596685870731.clj:127).
;;    Divide by zero

1
;; => 1

(try
  (/ 1 0)
  (catch ArithmeticException _ (println "Math problem."))
  (catch StackOverflowError _ (println "Unable to publish..")))
;; => nil

(defn publish-book [book]
  (when (not (:title book))
    (throw (ex-info "A book needs a title!" {:book book})))
  ;; Lots of publishing stuff..
  )
(publish-book {:title "Teletubbies"})
;; => nil
;; (publish-book {:author "super author"})
;; => Execution error (ExceptionInfo) at follow-along.c04-logic/publish-book (form-init4924825596685870731.clj:143).
;;    A book needs a title!

(try (publish-book {:author "Hurra!"})
     (catch clojure.lang.ExceptionInfo _  "Exception successfully catched!"))
;; => "Exception successfully catched!"

;; In the wild

(defn ensure-task-is-a-vector [task]
  (if (vector? task)
    task
    [task]))
(ensure-task-is-a-vector [1 2 3])
;; => [1 2 3]
(ensure-task-is-a-vector '(1 2 3))
;; => [(1 2 3)]

;; Staying out of trouble

(and true 1984)
;; => 1984
(and 2001 "Emma")
;; => "Emma"
(and 2001 nil "Emma")
;; => nil

