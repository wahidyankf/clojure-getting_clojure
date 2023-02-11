(ns inventory.core-gen-test
  (:require [clojure.test.check.generators :as gen])
  (:require [clojure.test.check :as tc])
  (:require [clojure.test.check.properties :as prop])
  (:require [clojure.test.check.clojure-test :as ctest])
  (:require [inventory.core :as i]))

(def title-gen (gen/such-that not-empty gen/string-alphanumeric))
(def author-gen (gen/such-that not-empty gen/string-alphanumeric))
(def copies-gen (gen/such-that (complement zero?) gen/nat))
(def book-gen
  (gen/hash-map :title title-gen :author author-gen :copies copies-gen))
(def inventory-gen (gen/not-empty (gen/vector book-gen)))

(def inventory-and-book-gen
  (gen/let [inventory inventory-gen
            book (gen/elements inventory)]
    {:inventory inventory :book book}))

(tc/quick-check 50
                (prop/for-all [i-and-b inventory-and-book-gen]
                              (= (i/find-by-title (-> i-and-b :book :title) (:inventory i-and-b))
                                 (:book i-and-b))))
;; => {:result true, :pass? true, :num-tests 50, :time-elapsed-ms 58, :seed 1675973107164}


#_{:clj-kondo/ignore [:unresolved-symbol]}
(ctest/defspec find-by-title-finds-books 50
  (prop/for-all [i-and-b inventory-and-book-gen]
                (= (i/find-by-title (-> i-and-b :book :title) (:inventory i-and-b))
                   (:book i-and-b))))
