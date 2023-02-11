(ns inventory.core-test
  (:require [clojure.test :refer [deftest is testing]])
  (:require [inventory.core :as i])
  (:require [clojure.spec.test.alpha :as stest]))

(def books
  [{:title  "2001"    :author  "Clarke"  :copies 21}
   {:title  "Emma"    :author  "Austen"  :copies 10}
   {:title  "Misery"  :author  "King"    :copies 101}])

(deftest test-finding-books
  (is (not (nil? (i/find-by-title  "Emma"  books)))))

(deftest test-finding-books-better
  (is (not (nil? (i/find-by-title  "Emma"  books))))
  (is (nil? (i/find-by-title "XYZZY" books))))

(deftest test-basic-inventory
  (testing  "Finding books"
    (is (not (nil? (i/find-by-title  "Emma"  books))))
    (is (nil? (i/find-by-title  "XYZZY"  books))))
  (testing  "Copies in inventory"
    (is (= 10 (i/number-of-copies-of  "Emma"  books)))))

;; spec-driven test

(stest/check 'inventory.core/book-blurb)
;; => ({:spec
;;      #object[clojure.spec.alpha$fspec_impl$reify__2525 0x22e3263e "clojure.spec.alpha$fspec_impl$reify__2525@22e3263e"],
;;      :clojure.spec.test.check/ret {:result true, :pass? true, :num-tests 1000, :time-elapsed-ms 393, :seed 1676073029360},
;;      :sym inventory.core/book-blurb})