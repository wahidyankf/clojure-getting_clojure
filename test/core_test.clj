(ns core_test
  #_{:clj-kondo/ignore [:refer-all]}
  (:require [clojure.test :refer :all]
            [core :refer :all]))

(deftest a-test
  (testing "A Placeholder"
    (is (not (= 1 2)))))
