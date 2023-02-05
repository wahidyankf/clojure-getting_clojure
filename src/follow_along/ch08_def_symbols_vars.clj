(ns follow-along.ch08-def-symbols-vars)

;; A global, stable place for your stuff

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def title "Emma")
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def PI 3.14)
(def ISBN-LENGTH 13)
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def COMPANY-NAME "Blotts Books")


#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn book-description [book]
  (str (:title book)
       " Written by "
       (:author book)))
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def book-description-with-fn
  (fn [book]
    (str (:title book)
         " Written by "
         (:author book))))


(def OLD-ISBN-LENGTH 10)
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def isbn-lengths [OLD-ISBN-LENGTH ISBN-LENGTH])
#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn valid-isbn [isbn]
  (or (= (count isbn) OLD-ISBN-LENGTH)
      (= (count isbn) ISBN-LENGTH)))

;; Symbols are things

;; string "Austen" and the symbol author
(def author "Austen")
'author
;; => author
(str 'author)
;; => "author"

(= 'author 'some-other-symbol)
;; => false
(= 'title 'title)
;; => true

#'author
;; => #'follow-along.ch08-def-symbols-vars/author
(def the-var #'author)
(.get the-var)
;; => "Austen"
(.-sym the-var)
;; => author

(defn some-troublesome-function-that-needs-logging [] (println ">>> yo, this is troublesome"))

(def ^:dynamic *debug-enabled* false)
(defn debug [msg]
  (if *debug-enabled*
    (println msg)
    nil))

;; Varying your vars

(binding [*debug-enabled* true]
  (debug  "Calling that darned function")
  (some-troublesome-function-that-needs-logging)
  (debug  "Back from that darned function"))

*debug-enabled*
;; => false

;; In the wild

*1
;; bound to the last result from the REPL

*2
;; bound to the second-to-last result from the REPL

*e
;; bound to the last exception

(/ 1 0)
*e
;; => #error {
;;     :cause "Divide by zero"
;;     :via
;;     [{:type java.lang.ArithmeticException
;;       :message "Divide by zero"
;;       :at [clojure.lang.Numbers divide "Numbers.java" 188]}]
;;     :trace
;;     [[clojure.lang.Numbers divide "Numbers.java" 188]
;;      [clojure.lang.Numbers divide "Numbers.java" 3901]
;;      [follow_along.ch08_def_symbols_vars$eval9040 invokeStatic "form-init12702251487211342898.clj" 78]
;;      [follow_along.ch08_def_symbols_vars$eval9040 invoke "form-init12702251487211342898.clj" 78]
;;      [clojure.lang.Compiler eval "Compiler.java" 7181]
;;      [clojure.lang.Compiler eval "Compiler.java" 7136]
;;      [clojure.core$eval invokeStatic "core.clj" 3202]
;;      [clojure.core$eval invoke "core.clj" 3198]
;;      [nrepl.middleware.interruptible_eval$evaluate$fn__967$fn__968 invoke "interruptible_eval.clj" 87]
;;      [clojure.lang.AFn applyToHelper "AFn.java" 152]
;;      [clojure.lang.AFn applyTo "AFn.java" 144]
;;      [clojure.core$apply invokeStatic "core.clj" 667]
;;      [clojure.core$with_bindings_STAR_ invokeStatic "core.clj" 1977]
;;      [clojure.core$with_bindings_STAR_ doInvoke "core.clj" 1977]
;;      [clojure.lang.RestFn invoke "RestFn.java" 425]
;;      [nrepl.middleware.interruptible_eval$evaluate$fn__967 invoke "interruptible_eval.clj" 87]
;;      [clojure.main$repl$read_eval_print__9110$fn__9113 invoke "main.clj" 437]
;;      [clojure.main$repl$read_eval_print__9110 invoke "main.clj" 437]
;;      [clojure.main$repl$fn__9119 invoke "main.clj" 458]
;;      [clojure.main$repl invokeStatic "main.clj" 458]
;;      [clojure.main$repl doInvoke "main.clj" 368]
;;      [clojure.lang.RestFn invoke "RestFn.java" 1523]
;;      [nrepl.middleware.interruptible_eval$evaluate invokeStatic "interruptible_eval.clj" 84]
;;      [nrepl.middleware.interruptible_eval$evaluate invoke "interruptible_eval.clj" 56]
;;      [nrepl.middleware.interruptible_eval$interruptible_eval$fn__998$fn__1002 invoke "interruptible_eval.clj" 152]
;;      [clojure.lang.AFn run "AFn.java" 22]
;;      [nrepl.middleware.session$session_exec$main_loop__1066$fn__1070 invoke "session.clj" 202]
;;      [nrepl.middleware.session$session_exec$main_loop__1066 invoke "session.clj" 201]
;;      [clojure.lang.AFn run "AFn.java" 22]
;;      [java.lang.Thread run "Thread.java" 829]]}
