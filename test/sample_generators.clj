(ns sample_generators
  (:require [clojure.test.check.generators :as gen])
  (:require [clojure.test.check :as tc])
  (:require [clojure.test.check.properties :as prop]))

(gen/sample gen/string-alphanumeric)
;; => ("" "8" "" "901" "" "" "C6gl72" "LkYV0A" "lo3" "78")

(tc/quick-check 50 (prop/for-all [i gen/nat]
                                 (< i (inc i))))
;; => {:result true, :pass? true, :num-tests 50, :time-elapsed-ms 1, :seed 1675973072554}

