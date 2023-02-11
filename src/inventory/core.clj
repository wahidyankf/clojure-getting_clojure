(ns inventory.core
  (:require [clojure.spec.alpha :as s])
  (:require [clojure.spec.test.alpha :as st]))

{:title  "Getting Clojure"  :author  "Olsen"  :copies 1000000}

(defn find-by-title-old
  "Search for a book by title,
   where title is a string and books is a collection of book maps, each of which must have a :title entry"
  [title books]
  (some #(when (= (:title %) title) %) books))
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn number-of-copies-of
  "Return the number of copies in inventory of the
 	  given title, where title is a string and books is a collection
 	  of book maps each of which must have a :title entry"
  [title books]
  (:copies (find-by-title-old title books)))

;; (s/def
;;   :inventory.core/book
;;   (s/keys
;;    :req-un
;;    [:inventory.core/title
;;     :inventory.core/author
;;     :inventory.core/copies]))

;; this will give the same effect as above commented code
(s/def ::book (s/keys :req-un [::title ::author ::copies]))
(s/def ::title string?)
(s/def ::author string?)
(s/def ::copies int?)
(s/def ::book (s/keys :req-un [::title ::author ::copies]))

;; Register a handy spec: An inventory is a collection of books.
(s/def ::inventory
  (s/coll-of ::book))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn find-by-title-with-spec
  [title inventory]
  {:pre [(s/valid? ::title title)
         (s/valid? ::inventory inventory)]}
  (some #(when (= (:title %) title) %) inventory))

(defn find-by-title
  [title inventory]
  (some #(when (= (:title %) title) %) inventory))
(s/fdef find-by-title
  :args (s/cat :title ::title
               :inventory ::inventory))
(def books
  [{:title  "2001"    :author  "Clarke"  :copies 21}
   {:title  "Emma"    :author  "Austen"  :copies 10}
   {:title  "Misery"  :author  "King"    :copies 101}])
(find-by-title "Emma" books)
;; => {:title "Emma", :author "Austen", :copies 10}
(find-by-title "Emma" ["Emma" "2001" "Jaws"])
;; => nil
(st/instrument 'inventory.core/find-by-title)
;; (find-by-title "Emma" ["Emma" "2001" "Jaws"])
;; => Execution error - invalid arguments to inventory.core/find-by-title at (form-init3079279510701688910.clj:61).
;;    "Emma" - failed: map? at: [:inventory] spec: :inventory.core/book
;;    "2001" - failed: map? at: [:inventory] spec: :inventory.core/book
;;    "Jaws" - failed: map? at: [:inventory] spec: :inventory.core/book

(defn book-blurb [book]
  (str  "The best selling book "  (:title book)  " by "  (:author book)))
(defn check-return [{:keys [args ret]}]
  (let [author (-> args :book :author)]
    (not (neg? (.indexOf ret author)))))
(s/fdef book-blurb
  :args (s/cat :book ::book)
  :ret (s/and string? (partial re-find #"The best selling"))
  :fn check-return)

(book-blurb (get books 0))
;; => "The best selling book 2001 by Clarke"
