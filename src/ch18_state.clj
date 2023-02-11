(ns ch18-state)

;; it's made of atoms

;; this will not work!
;; (def counter 0)
;; (defn greeting-message [req]
;;   (if (zero? (mod counter 100))
;;     (str "Congrats! You are the " counter " visitor!")
;;     (str "Welcome to Blotts Books!")))

(def counter (atom 0))
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var :unused-binding]}
(defn greeting-message [req]
  (swap! counter inc)
  (if (= @counter 500)
    (str "Congrats! You are the " @counter " visitor!")
    (str "Welcome to Blotts Books!")))

;; swapping maps

(def by-title (atom {}))
(defn add-book [{title :title :as book}]
  (swap! by-title #(assoc % title book)))
(defn del-book [title]
  (swap! by-title #(dissoc % title)))
(defn find-book [title]
  (get @by-title title))
(find-book "Emma")
;; => nil
(add-book {:title "1984", :copies 1948})
(add-book {:title "Emma", :copies 100})
(del-book "1984")
(find-book "Emma")
;; => {:title "Emma", :copies 100}
(find-book "1984")
;; => nil

;; refs: team-oriented atoms

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def by-title-ref (ref {}))
(def total-copies (ref 0))
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn add-book-ref [{title :title :as book}]
  ;; this will make sure that the by-title and total-copies in sync
  (dosync
   (alter by-title #(assoc % title book))
   (alter total-copies + (:copies book))))

;; agents

(def by-title-agent (agent {}))
(defn add-book-agent [{title :title :as book}]
  (send
   by-title-agent
   (fn [by-title-map]
     (assoc by-title-map title book))))

;; Queue up and add book request.
(add-book-agent {:title "War and Peace" :copies 25})
by-title-agent
;; => #<Agent@7d32382a: {"War and Peace" {:title "War and Peace", :copies 25}}>

;; at this point the agent may or may not have been updated

(defn- notify-inventory-change [arg1 book]
  (println arg1 book))
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn add-book-agent-with-notif [{title :title :as book}]
  (send
   by-title-agent
   (fn [by-title-map]
     (notify-inventory-change :add book)
     (assoc by-title-map title book))))

;; in the wild

(defn blurb [book]
  (Thread/sleep 5000)
  (str "Don't miss the exciting new book, "
       (:title book)
       " by "
       (:author book)))
(def memoized-blurb (memoize blurb))
(def emma {:title "Emma" :author "Austen"})
(memoized-blurb emma)
;; => "Don't miss the exciting new book, Emma by Austen"
(memoized-blurb emma)
;; => "Don't miss the exciting new book, Emma by Austen"
(memoized-blurb emma)
;; => "Don't miss the exciting new book, Emma by Austen"

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn memoize-code
  "Returns a memoized version of a referentially
 	  transparent function. The memoized version of
 	  the function keeps a cache of the mapping from
 	  arguments to results and, when calls with the
 	  same arguments are repeated often, has
 	  higher performance at the expense of higher
 	  memory use."
  [f]
  (let [mem (atom {})]
    (fn [& args]
      (if-let [e (find @mem args)]
        (val e)
        (let [ret (apply f args)]
          (swap! mem assoc args ret)
          ret)))))

;; staying out of trouble