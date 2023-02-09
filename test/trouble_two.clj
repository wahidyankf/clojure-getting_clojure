(ns trouble-two
  (:require [clojure.test :refer [deftest is]]
            [clojure.test.check.clojure-test :as ctest]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]))

	;; Prevent the division by zero.​

(defn  more-complex-f [a b]
  (let [denominator (- b 863947)]
    (if (zero? denominator)
      :no-result
      (/ a denominator))))

 	;; But we still want to be sure we detect it correctly.

(deftest test-critical-value
  (is (= :no-result (more-complex-f 1 863947))))
 	;; And the function works in other cases.

(def non-critical-gen (gen/such-that (partial not= 863947) gen/pos-int))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(ctest/defspec test-other-values 10000
  (prop/for-all [a gen/pos-int
                 b non-critical-gen]
                (= (* (more-complex-f a b) (- b 863947)) a)))