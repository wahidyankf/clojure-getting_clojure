(ns interop.interop-basic
  (:import java.io.File))

(def authors (java.io.File. "authors.txt"))
(if (.exists authors)
  (str "Our authors file is there.")
  (str "Our authors file is missing."))
;; => "Our authors file is there."
(if (.canRead authors)
  (str "We can read it!")
  (str "We cannot read it!"))
;; => "We can read it!"
(.setReadable authors true)
;; => true

(def rect (java.awt.Rectangle. 0 0 10 20))
(str "Width:" (.-width rect))
;; => "Width:10"
(str "Height:" (.-height rect))
;; => "Height:20"

(def authors-2 (File. "authors.txt"))
(.exists authors-2)
;; => true
