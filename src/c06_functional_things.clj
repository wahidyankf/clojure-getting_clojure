(ns c06_functional_things)

;; Functions are values

(def dracula {:title "Dracula"
              :author "Stoker"
              :price 1.99
              :genre :horror})
(defn cheap? [book]
  (when (<= (:price book) 9.99)
    book))
(defn pricey? [book]
  (when (> (:price book) 9.99)
    book))
(cheap? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(pricey? dracula)
;; => nil

(defn horror? [book]
  (when (= (:genre book) :horror)
    book))
(defn adventure? [book]
  (when (= (:genre book) :adventure)
    book))
(horror? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(adventure? dracula)
;; => nil

(defn cheap-horror? [book]
  (when (and (cheap? book)
             (horror? book))
    book))
(defn pricy-adventure? [book]
  (when (and (pricey? book)
             (adventure? book))
    book))
(cheap-horror? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(pricy-adventure? dracula)
;; => nil

(def reasonably-priced? cheap?)
(reasonably-priced? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}

(defn run-with-dracula [f]
  (f dracula))
(run-with-dracula pricey?)
;; => nil
(run-with-dracula horror?)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}

(defn both? [first-predicate-f second-predicate-f book]
  (when (and (first-predicate-f book)
             (second-predicate-f book))
    book))
(both? cheap? horror? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(both? pricey? adventure? dracula)
;; => nil

;; Functions on the fly

(fn [n] (* 2 n))
(str "A function:" (fn [n] (* 2 n)))
;; => "A function:follow_along.c06_functional_things$eval7991$fn__7992@d5cc0de"
(def double-it (fn [n] (* 2 n)))
(double-it 10)
;; => 20
((fn [n] (* 2 n)) 10)
;; => 20

(defn cheaper-f [max-price]
  (fn [book]
    (when (<= (:price book) max-price)
      book)))
;; define some helpful functions.
(def real-cheap? (cheaper-f 1.00))
(def kind-of-cheap? (cheaper-f 1.99))
(def marginally-cheap? (cheaper-f 5.99))
(real-cheap? dracula)
;; => nil
(kind-of-cheap? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(marginally-cheap? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}

(defn both-f [first-predicate-f second-predicate-f]
  (fn [book]
    (when (and (first-predicate-f book)
               (second-predicate-f book))
      book)))
(def cheap-horror-1? (both-f cheap? horror?))
(def real-cheap-adventure? (both-f real-cheap? adventure?))
(def real-cheap-horror? (both-f real-cheap? horror?))
(cheap-horror-1? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(real-cheap-adventure? dracula)
;; => nil
(real-cheap-horror? dracula)
;; => nil

(def cheap-horror-possession?
  (both-f cheap-horror?
          (fn [book] (= (:title book) "Possession"))))
(cheap-horror-possession? dracula)
;; => nil

;; A functional toolkit

(+ 1 2 3 4)
;; => 10

(def the-function +)
(def args [1 2 3 4])
(apply the-function args)
;; => 10

(def v ["The number " 2 " best selling " "book."])
(apply str v)
;; => "The number 2 best selling book."
(apply list v)
;; => ("The number " 2 " best selling " "book.")
(apply vector (apply list v))
;; => ["The number " 2 " best selling " "book."]

(defn my-inc [n] (+ 1 n))
(def my-inc-p (partial + 1))
(my-inc 2)
;; => 3
(my-inc-p 2)
;; => 3

(defn cheaper-than [max-price book]
  (when (<= (:price book) max-price)
    book))
(def real-cheap-p? (partial cheaper-than 1.00))
(def kind-of-cheap-p? (partial cheaper-than 1.99))
(def marginally-cheap-p? (partial cheaper-than 5.99))
(real-cheap-p? dracula)
;; => nil
(kind-of-cheap-p? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(marginally-cheap-p? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}

(defn adventure-p? [book]
  (when (= (:genre book) :adventure)
    book))
(defn not-adventure? [book] (not (adventure-p? book)))
(def not-adventure-c? (complement adventure-p?))
(not-adventure? dracula)
;; => true
(not-adventure-c? dracula)
;; => true

;; (def cheap-horror-2? (every-pred cheap? horror?))
(def cheap-horror-possession-e?
  (every-pred
   cheap?
   horror?
   (fn [book] (= (:title book) "Possession"))))
(cheap-horror-possession-e? dracula)
;; => false

;; Function literals

(#(when (= (:genre %1) :horror) %1) dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(#(+ %1 %2 %3) 1 2 3)
;; => 6
(#(count %) "Count this string length")
;; => 24

;; In the wild

(defn say-welcome [what]
  (str "Welcome to " what "!"))
(def say-welcome-with-def
  (fn [what] (str "Welcome to " what "!")))
(say-welcome "home")
;; => "Welcome to home!"
(say-welcome-with-def "home")
;; => "Welcome to home!"

(def book {:title "Emma" :copies 1000})
book
;; => {:title "Emma", :copies 1000}
(update book :copies inc)
;; => {:title "Emma", :copies 1001}

(def by-author
  {:name "Jane Austen"
   :book {:title "Emma" :copies 1000}})
(update-in by-author [:book :copies] inc)
;; => {:name "Jane Austen", :book {:title "Emma", :copies 1001}}