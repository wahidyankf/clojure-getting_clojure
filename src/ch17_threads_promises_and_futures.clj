(ns ch17-threads-promises-and-futures)

;; Great power...

(defn do-something-in-a-thread []
  (println "Hello from the thread.")
  (println "Good bye from thread."))
(def the-thread (Thread. do-something-in-a-thread))

(.start the-thread)

(defn do-something-else []
  (println "Hello from the thread with sleep.")
  (Thread/sleep 5000)
  (println "Good bye from the thread with sleep."))

(.start (Thread. do-something-else))

;; ...And great responsibility

(def fav-book  "Jaws")
(defn make-emma-favorite []
  (Thread/sleep (rand-int 5000))
  #_{:clj-kondo/ignore [:inline-def]}
  (def fav-book  "Emma"))
(defn make-2001-favorite []
  (Thread/sleep (rand-int 5000))
  #_{:clj-kondo/ignore [:inline-def]}
  (def fav-book  "2001"))

(do
  ;; Race condition
  (.start (Thread. make-2001-favorite))
  (.start (Thread. make-emma-favorite))
  (Thread/sleep 6000)
  fav-book)

;; Good fences make happy threads

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def inventory [{:title  "Emma"  :sold 51 :revenue 255}
                {:title  "2001"  :sold 17 :revenue 170}])
;; Clojure keeps any dynamic bindings of vars safely separated by thread.
(def ^:dynamic *favorite-book*  "Oliver Twist")
(def thread-1
  (Thread.
   #(binding [*favorite-book*  "2001"]
      (println  "My favorite book is"  *favorite-book*))))
(def thread-2
  (Thread.
   #(binding [*favorite-book*  "Emma"]
      (println  "My favorite book is"  *favorite-book*))))
;; will print My favorite book is 2001
(.start thread-1)
;; will print My favorite book is Emma
(.start thread-2)

;; Promise me a result

;; Delete a file in the background.
(.start (Thread. #(.delete (java.io.File. "temp-titles.txt"))))
;; => nil

(def del-thread (Thread. #(.delete (java.io.File. "temp-titles.txt"))))
(.start del-thread)
(.join del-thread)

(def the-result (promise))
(deliver the-result "Emma")
(str  "The value in my promise is " (deref the-result))
;; => "The value in my promise is Emma"

(defn sum-copies-sold [inv]
  (Thread/sleep 5000)
  (apply + (map :sold inv)))
(defn sum-revenue [inv]
  (Thread/sleep 5000)
  (apply + (map :revenue inv)))
(let [copies-promise (promise)
      revenue-promise (promise)]
  (.start (Thread. #(deliver copies-promise (sum-copies-sold inventory))))
  (.start (Thread. #(deliver revenue-promise (sum-revenue inventory))))
  ;; Do some other stuff in this thread
  (str "The total number of books sold is " @copies-promise " and "
       "The total number of books sold is " @revenue-promise))
;; => "The total number of books sold is 68 and The total number of books sold is 425"

;; A value with a future

(def revenue-future
  (future
    (Thread/sleep 5000)
    (apply + (map :revenue inventory))))
(def sold-future
  (future
    (Thread/sleep 5000)
    (apply + (map :revenue inventory))))
(str "The total revenue is " @revenue-future " and total sold is " @sold-future)
;; => "The total revenue is 425 and total sold is 425"

;; Staying out of trouble

(import java.util.concurrent.Executors)

(def fixed-pool (Executors/newFixedThreadPool 3))
(defn a-lot-of-work []
  (println "Simulating function that takes a long time.")
  (Thread/sleep 1000))
(defn even-more-work []
  (println "Simulating function that takes a long time.")
  (Thread/sleep 1000))
(do
  (.execute fixed-pool a-lot-of-work)
  (.execute fixed-pool even-more-work)
  (.execute fixed-pool even-more-work)
  (.execute fixed-pool even-more-work)
  (.execute fixed-pool even-more-work)
  (.execute fixed-pool even-more-work)
  (.execute fixed-pool even-more-work)
  (.execute fixed-pool even-more-work)
  (.execute fixed-pool even-more-work)
  (println "done"))

(def a-promise (promise))
(deref a-promise 1000 :oh-snap)

;; In the wild

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn toy-pmap [f coll]
  (let [futures (doall (map #(future (f %)) coll))]
    (map deref futures)))
(toy-pmap #(do
             (Thread/sleep 3000)
             (+ % 1)) [1 3 4 5 6 7 8 9 10 11 12 13 14])
;; => (2 4 5 6 7 8 9 10 11 12 13 14 15)
