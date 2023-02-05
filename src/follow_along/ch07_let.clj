(ns follow-along.ch07-let)

;; ---
;; A local, temporary place for your stuff
;; ---

(defn compute-discount-amount-original [amount discount-percent min-charge]
  (if (> (* amount (- 1.0 discount-percent)) min-charge)
    (* amount (- 1.0 discount-percent))
    min-charge))
(compute-discount-amount-original 1 2 3)
;; => 3

(defn compute-discount-amount [amount discount-percent min-charge]
  (let [discount (* amount discount-percent)
        discounted-amount (- amount discount)]
    (println  "Discount:"  discount)
    (println  "Discounted amount"  discounted-amount)
    (if (> discounted-amount min-charge)
      discounted-amount
      min-charge)))
(compute-discount-amount 1 2 3)
;; => 3

(def user-discounts
  {"Nicholas"  0.10  "Jonathan"  0.07  "Felicia"  0.05})
(defn mk-discount-price-f [user-name user-discounts min-charge]
  (let [discount-percent (user-discounts user-name)]
    (fn [amount]
      (let [discount (* amount discount-percent)
            discounted-amount (- amount discount)]
        (if (> discounted-amount min-charge)
          discounted-amount
          min-charge)))))
;; get a price function for Felicia
(def computer-felicia-price (mk-discount-price-f "Felicia" user-discounts 10.0))
;; ...and sometime later compute a price
(computer-felicia-price 20.0)
;; => 19.0

;; Variations on the Theme

(def anonymous-book
  {:title "Sir Gawain and the Green Knight"})
(def with-author
  {:title "Once and Future King" :author "White"})
(defn uppercase-author [book]
  (let [author (:author book)]
    (if author
      (.toUpperCase author)
      "ANONYMOUS")))
(uppercase-author anonymous-book)
;; => "ANONYMOUS"
(uppercase-author with-author)
;; => "WHITE"

(defn uppercase-author-iflet [book]
  (if-let [author (:author book)]
    (.toUpperCase author) "ANONYMOUS"))
(uppercase-author-iflet anonymous-book)
;; => "ANONYMOUS"
(uppercase-author-iflet with-author)
;; => "WHITE"

(defn uppercase-author-whenlet [book]
  (when-let [author (:author book)]
    (.toUpperCase author)))
(uppercase-author-whenlet anonymous-book)
;; => nil
(uppercase-author-whenlet with-author)
;; => "WHITE"

;; in the wild