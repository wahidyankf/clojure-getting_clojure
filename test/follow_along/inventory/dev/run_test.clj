(ns follow-along.inventory.dev.run-test
  (:require [clojure.test :as test]))

(require '[follow-along.inventory.core-test :as ct])

(ct/test-finding-books)

;; Three ways to run the tests in a namespace

(test/run-tests)
(test/run-tests *ns*)
;; => {:test 0, :pass 0, :fail 0, :error 0, :type :summary}
(test/run-tests 'follow-along.inventory.core-test)
;; => {:test 3, :pass 6, :fail 0, :error 0, :type :summary}
