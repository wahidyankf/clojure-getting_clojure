(ns follow-along.ch12-destructuring)

;; pry open your data

(def artists [:monet :austen])
(let [painter (first artists)
      novelist (second artists)]
  (str  "The painter is: "  painter
        " and the novelist is: "  novelist))
;; => "The painter is: :monet and the novelist is: :austen"

(let [[painter novelist] artists]
  (str "The painter is: " painter
       " and the novelist is: " novelist))
;; => "The painter is: :monet and the novelist is: :austen"

(def artists-2 [:monet :austen :beethoven :dickinson])
(let [[painter novelist composer poet] artists-2]
  (str  "The painter is: "  painter
        " The novelist is: "  novelist
        " The composer is: "  composer
        " The poet is: "  poet))
;; => "The painter is: :monet The novelist is: :austen The composer is: :beethoven The poet is: :dickinson"

(let [[painter novelist composer] artists]
  (str  "The painter is: "  painter
        " The novelist is: " novelist
        " The composer is: "  composer))
;; => "The painter is: :monet The novelist is: :austen The composer is: :beethoven"

;; Getting less than everything

(let [[_ _ composer poet] artists-2]
  (str   " The composer is: "  composer
         " The poet is: "  poet))
;; => " The composer is: :beethoven The poet is: :dickinson"

(def pairs [[:monet :austen] [:beethoven :dickinson]])
(let [[[painter] [composer]] pairs]
  (str "The painter is: " painter
       " The composer is: " composer))
;; => "The painter is: :monet The composer is: :beethoven"
(let [[[painter] [_ poet]] pairs]
  (str "The painter is: " painter
       " The poet is: " poet))
;; => "The painter is: :monet The poet is: :dickinson"

;; Destructuring in sequence

(def artist-list '(:monet :austen :beethoven :dickinson))
(let [[painter novelist composer] artist-list]
  (str "The painter is: " painter
       " The novelist is: " novelist
       " The compose is: " composer))
;; => "The painter is: :monet The novelist is: :austen The compose is: :beethoven"

(let [[c1 c2 c3 c4] "Jane"]
  (str c1 "-" c2 "-" c3 "=" c4))
;; => "J-a-n=e"

;; Destructuring function arguments
(defn artist-description [[novelist poet]]
  (str "The novelistis " novelist " and the poet is " poet))
(artist-description [:austen :dickinson])
;; => "The novelistis :austen and the poet is :dickinson"


(defn artist-description-2 [shout [novelist poet]]
  (let [msg (str  "Novelist is "  novelist
                  " and the poet is "  poet)]
    (if shout (.toUpperCase msg) msg)))
(artist-description-2 "yo!" [:austen :dickinson])
;; => "NOVELIST IS :AUSTEN AND THE POET IS :DICKINSON"

;; Digging into maps

(def artist-map {:painter :monet  :novelist :austen})
(let [{painter :painter writer :novelist} artist-map]
  (str  "The painter is "  painter
        " The novelist is "  writer))
;; => "The painter is :monet The novelist is :austen"

;; Diving into nested maps

(def austen {:name  "Jane Austen"
             :parents {:father  "George"  :mother  "Cassandra"}
             :dates {:born 1775 :died  1817}})
(let [{{dad :father mom :mother} :parents} austen]
  (str  "Jane Austen's dad's name was. "  dad
        " Jane Austen's mom's name was. "  mom))
;; => "Jane Austen's dad's name was. George Jane Austen's mom's name was. Cassandra"

austen
;; => {:name "Jane Austen", :parents {:father "George", :mother "Cassandra"}, :dates {:born 1775, :died 1817}}
(let [{name :name
       {mom :mother} :parents
       {dob :born} :dates} austen]
  (str name  "was born in "  dob
       name " mother's name was" mom))
;; => "Jane Austenwas born in 1775Jane Austen mother's name wasCassandra"

;; The final frontier: Mixing and matching

(def author {:name  "Jane Austen"
             :books [{:title  "Sense and Sensibility"  :published 1811}
                     {:title  "Emma"  :published 1815}]})
(let [{name :name [_ book] :books} author]
  (str  "The author is: "  name
        " One of the author's books is: "  book))
;; => "The author is: Jane Austen One of the author's books is: {:title \"Emma\", :published 1815}"

(def authors  [{:name  "Jane Austen"  :born 1775}
               {:name  "Charles Dickens"  :born 1812}])
(let [[{dob-1 :born} {dob-2 :born}] authors]
  (str  "One author was born in: "  dob-1
        " The other author was born in: "  dob-2))
;; => "One author was born in: 1775 The other author was born in: 1812"

;; Going further

(def romeo {:name "Romeo" :age 16 :gender :male})
(defn character-desc-1 [{name :name age :age gender :gender}]
  (str  "Name: "  name  " age: "  age  " gender: "  gender))
(character-desc-1 romeo)
;; => "Name: Romeo age: 16 gender: :male"
(defn character-desc [{:keys [name age gender]}]
  (str  "Name: "  name  " age: "  age  " gender: "  gender))
(character-desc romeo)
;; => "Name: Romeo age: 16 gender: :male"
(defn character-desc-misc [{:keys [name gender] age-in-years :age}]
  (str  "Name: "  name  " age: "  age-in-years  " gender: "  gender))
(character-desc-misc romeo)
;; => "Name: Romeo age: 16 gender: :male"

(defn add-greeting [character]
  (let [{:keys [name age]} character]
    (assoc character
           :greeting
           (str  "Hello, my name is "  name  " and I am "  age  "."))))
(add-greeting romeo)
;; => {:name "Romeo", :age 16, :gender :male, :greeting "Hello, my name is Romeo and I am 16."}
(defn add-greeting-2 [{:keys [name age] :as character}]
  (assoc character
         :greeting
         (str  "Hello, my name is "  name  " and I am "  age  ".")))
(add-greeting-2 romeo)
;; => {:name "Romeo", :age 16, :gender :male, :greeting "Hello, my name is Romeo and I am 16."}

;; Staying out of trouble

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def author-name
  (let [{n :name} author] n))
;; destructuring is purely a creature of local binding

;; In the wild

#_{:clj-kondo/ignore [:unused-binding]}
(defn mysql
  "Create a database specification for a
 	 mysql database. Opts should include
 	 keys for :db, :user, and :password.
 	 You can also optionally set host and port.
 	 Delimiters are automatically set to \"`\"."
  [{:keys [host port db make-pool?]
    :or {host  "localhost" , port 3306, db  "" , make-pool? true}
    :as opts}]
    ;; Do something with host, port, db, make-pool? and opts
  )
(mysql 123) ; just so the linter shutup