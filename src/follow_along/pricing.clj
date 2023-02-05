(ns follow-along.pricing)

(def discount-rate 0.15)
(defn discount-price [book]
  (* (- 1.0 discount-rate) (:price book)))