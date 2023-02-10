(ns c03_maps_keywords_and_sets)

;; This goes with that

{"title" "Oliver Twist" "author" "Dickens" "published" 1838}
;; => {"title" "Oliver Twist", "author" "Dickens", "published" 1838}

(hash-map "title" "Oliver Twist"
          "author" "Dickens"
          "published" 1838)

(def book-arc
  {"title" "Oliver Twist" "author" "Dickens" "published" 1838})
(get book-arc "published")
;; => 1838
(book-arc "published")
;; => 1838
(get book-arc "published?")
;; => nil
(book-arc "published?")
;; => nil

;; Keywords

:title
;; => :title
:author
;; => :author
:published
;; => :published
:word-count
;; => :word-count
:preface&introduction
;; => :preface&introduction
:chapter-1-and-2
;; => :chapter-1-and-2

(def book
  {:title "Oliver Twist"
   :author "Dickens"
   :published 1838})
(str "Title: " (book :title))
;; => "Title: Oliver Twist"
(str "By: " (book :author))
;; => "By: Dickens"
(str "Published: " (book :published))
;; => "Published: 1838"
(str "Published: " (:published book))
;; => "Published: 1838"

;; Changing your map without changing it

(assoc book :page-count 362)
;; => {:title "Oliver Twist", :author "Dickens", :published 1838, :page-count 362}
book
;; => {:title "Oliver Twist", :author "Dickens", :published 1838}
(= book book)
;; => true
(= book (assoc book :page-count 362))
;; => false

(assoc book :page-count 362 :title "War & Peace")
;; => {:title "War & Peace", :author "Dickens", :published 1838, :page-count 362}
(dissoc book :published)
;; => {:title "Oliver Twist", :author "Dickens"}
(dissoc book :paperback :illustrator :favorite-zoo-animal)
;; => {:title "Oliver Twist", :author "Dickens", :published 1838}
(= book (dissoc book :paperback :illustrator :favorite-zoo-animal))
;; => true

;; Assoc works on vectors too
(assoc [:title :by :published] 1 :author)
;; => [:title :author :published]

;; Other handy map functions

(keys book)
;; => (:title :author :published)
(vals book)
;; => ("Oliver Twist" "Dickens" 1838)

;; Sets


(def genres #{:sci-fi :romance :mystery})
(def authors #{"Dickens" "Austen" "King"})

(contains? authors "Austen")
;; => true
(contains? genres "Austen")
;; => false

(authors "Austen")
;; => "Austen"
(genres :historical)
;; => nil

(:sci-fi genres)
;; => :sci-fi
(:historical genres)
;; => nil

(def more-authors (conj authors "Clarke"))
more-authors
;; => #{"King" "Dickens" "Clarke" "Austen"}
(disj more-authors "King")
;; => #{"Dickens" "Clarke" "Austen"}

;; Staying out of trouble

(= :title "title")
;; => false
(= (book "title") (book :title))
;; => false
(assoc book "title" "Pride and Prejudice")
;; => {:title "Oliver Twist", :author "Dickens", :published 1838, "title" "Pride and Prejudice"}

(book :some-key-that-is-clearly-not-there)
;; => nil

(def anonymous-book {:title "The Arabian Nights" :author nil})
(anonymous-book :author)
;; => nil
(contains? anonymous-book :title)
;; => true
(contains? anonymous-book :author)
;; => true
(contains? anonymous-book :favorite-color)
;; => false

(def possible-authors #{"Austen" "Dickens" nil})
(contains? possible-authors "Austen")
;; => true
(contains? possible-authors "King")
;; => false
(contains? possible-authors nil)
;; => true

book
;; => {:title "Oliver Twist", :author "Dickens", :published 1838}
(first book)
;; => [:title "Oliver Twist"]
(rest book)
;; => ([:author "Dickens"] [:published 1838])
(count book)
;; => 3
