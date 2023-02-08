(ns follow-along.ch13-records-and-protocols)


;; The trouble with Maps

(defn- get-watson-1 []
  {:name "John Watson"
   :appears-in "Sign of the Four"
   :author "Doyle"})
(defn- get-watson-2 []
  {:cpu "Power7" :no-cpus 2880 :storage-gb 4000})

(let [watson-1 (get-watson-1)
      watson-2 (get-watson-2)]
  ;; Do something with out watsons...
  (println watson-1)
  (println watson-2))

;; Striking a more specific bargain with records

(defrecord FictionalCharacter [name appears-in author])
(def watson (->FictionalCharacter "John Watson" "Sign of the Four" "Doyle"))
watson
;; => {:name "John Watson", :appears-in "Sign of the Four", :author "Doyle"}
(def elizabeth (map->FictionalCharacter
                {:name "Elizabeth Bennet"
                 :appears-in "Pride & Prejudice"
                 :author "Austen"}))
elizabeth
;; => {:name "Elizabeth Bennet", :appears-in "Pride & Prejudice", :author "Austen"}

;; Records are maps

(:name elizabeth)
;; => "Elizabeth Bennet"
(:appears-in watson)
;; => "Sign of the Four"

(count elizabeth)
;; => 3
(keys watson)
;; => (:name :appears-in :author)

(def specific-watson (assoc watson :appears-in "Sign of the Four"))
specific-watson
;; => {:name "John Watson", :appears-in "Sign of the Four", :author "Doyle"}

(def more-about-watson (assoc watson :address "221B Baker Street"))
more-about-watson
;; => {:name "John Watson", :appears-in "Sign of the Four", :author "Doyle", :address "221B Baker Street"}
;; address don't have a "speed boost" like the original record field

;; The record advantage

(def irene {:name "Irene Adler"
            :appears-in "A Scandal in Bohemia"
            :author "Doyle"})
(:name watson) ;; this will be faster than below
;; => "John Watson"
(:name irene) ;; this will be slower than above
;; => "Irene Adler"

(defrecord SuperComputer [cpu no-cpus storage-gb])

(def watson-1 (->FictionalCharacter
               "John Watson"
               "Sign of the Four"
               "Doyle"))
(def watson-2 (->SuperComputer "Power7" 2880 4000))
watson-1
;; => {:name "John Watson", :appears-in "Sign of the Four", :author "Doyle"}
watson-2
;; => {:cpu "Power7", :no-cpus 2880, :storage-gb 4000}
(class watson-1)
;; => follow_along.ch13_records_and_protocols.FictionalCharacter
(class watson-2)
;; => follow_along.ch13_records_and_protocols.SuperComputer
(instance? FictionalCharacter watson-1)
;; => true
(instance? SuperComputer irene)
;; => false
(instance? FictionalCharacter irene)
;; => false

;; Protocols

(defprotocol Person
  (full-name [this])
  (greeting [this msg])
  (description [this]))

(defrecord Employee [first-name last-name department]
  Person
  (full-name [this] (:first-name this))
  (greeting [this msg] (str msg " " (:first-name this)))
  (description [this]
    (str (:first-name this) " works in " (:department this))))
(defrecord FictionalCharacterWithP [name appears-in author]
  Person
  (full-name [this] (:name this))
  (greeting [this msg] (str msg  " "  (:name this)))
  (description [this]
    (str (:name this)  " is a character in " (:appears-in this))))

(def sofia (->Employee  "Sofia"   "Diego"   "Finance"))
sofia
;; => {:first-name "Sofia", :last-name "Diego", :department "Finance"}
(def sam (->FictionalCharacterWithP  "Sam Weller"   "The Pickwick Papers"   "Dickens"))
sam
;; => {:name "Sam Weller", :appears-in "The Pickwick Papers", :author "Dickens"}

(full-name sofia)
;; => "Sofia"
(description sam)
;; => "Sam Weller is a character in The Pickwick Papers"
(greeting sofia "Hello!")
;; => "Hello! Sofia"
(:author sam)
;; => "Dickens"

;; Decentralized polymorphism

(defprotocol Marketable
  (make-slogan [this]))
(extend-protocol Marketable
  Employee
  (make-slogan [e] (str (:first-name e) " is the BEST employee!"))
  FictionalCharacterWithP
  (make-slogan [fc] (str (:name fc) " is the GREATEST character!"))
  SuperComputer
  (make-slogan [sc] (str "This computer has " (:no-cpus sc) " CPUs! ")))
(extend-protocol Marketable
  String
  (make-slogan [s] (str \" s \" " is a string! WOW!"))
  Boolean
  (make-slogan [b] (str b " is one of the two surviving Booleans!")))

(make-slogan sofia)
;; => "Sofia is the BEST employee!"
(make-slogan watson-2)
;; => "This computer has 2880 CPUs! "
(make-slogan sam)
;; => "Sam Weller is the GREATEST character!"
(make-slogan "yo")
;; => "\"yo\" is a string! WOW!"
(make-slogan true)
;; => "true is one of the two surviving Booleans!"
