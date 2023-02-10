(ns ch14_tests)

;; Spotting bugs with clojure.test

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def book 	[{:title "2001"   :author "Clarke" :copies 21}
            {:title "Emma"   :author "Austen" :copies 10}
            {:title "Misery" :author "King"   :copies 101}])

;; Testing namespaces and projects

;; Property-based testing

;; Checking properties

;; Staying out of trouble

;; In the wild