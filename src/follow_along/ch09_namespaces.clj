(ns follow-along.ch09-namespaces
  (:require
   clojure.data
   [follow-along.pricing :as pricing
    :refer [discount-rate]]))

;; A place for your vars

(pricing/discount-price {:title  "Emma"  :price 9.99})
;; => 8.4915

;; => 8.4915

;; Loading namespaces

(def literature ["Emma" "Oliver Twist" "Possession"])
(def horror ["It" "Carry" "Possession"])

(clojure.data/diff literature horror)
;; => [["Emma" "Oliver Twist"] ["It" "Carry"] [nil nil "Possession"]]

discount-rate
;; => 0.15

;; Namespaces, symbols, and keywords

(str "Current ns:" *ns*)
;; => "Current ns:follow-along.ch09-namespaces"

(ns-map (find-ns 'user)) ; include all the predefined vars
(ns-map 'user) ; do the same as above

discount-rate
;; => 0.15
(ns-unmap *ns* 'discount-rate)
;; discount-rate
;; error

;; don't execute it more than once
;; :reload will not affect this
;; change it with ns-unmap
;; (defonce some-value (function-with-side-effects))