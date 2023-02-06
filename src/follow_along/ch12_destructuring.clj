(ns follow-along.ch12-destructuring)

;; pry open your data

(def artists [:monet :austen])
(let [painter (first artists)
      novelist (second artists)]
  (str  "The painter is: "  painter
        " and the novelist is: "  novelist))
;; => "The painter is: :monet and the novelist is: :austen"

(let [[painter novelist a] artists]
  (str "The painter is: " painter
       " and the novelist is: " novelist))
;; => "The painter is: :monet and the novelist is: :austen"

(def artists-2 [:monet :austen :beethoven :dickinson])
(let [[painter novelist composer poet] artists-2]
  (str  "The painter is: "  painter
        " The novelist is: "  novelist
        " The composer is: "  composer
        " The poet is: "  poet))
;; => "The painter is: :monet The novelist is: :austen The composer is: :beethoven The poet is: :dickinson"

(let [[painter novelist composer] artists]
  (str  "The painter is: "  painter
        " The novelist is: " novelist
        " The composer is: "  composer))
;; => "The painter is: :monet The novelist is: :austen The composer is: :beethoven"

;; Getting less than everything

(let [[_ _ composer poet] artists-2]
  (str   " The composer is: "  composer
         " The poet is: "  poet))
;; => " The composer is: :beethoven The poet is: :dickinson"

(def pairs [[:monet :austen] [:beethoven :dickinson]])
(let [[[painter] [composer]] pairs]
  (str "The painter is: " painter
       " The composer is: " composer))
;; => "The painter is: :monet The composer is: :beethoven"
(let [[[painter] [_ poet]] pairs]
  (str "The painter is: " painter
       " The poet is: " poet))
;; => "The painter is: :monet The poet is: :dickinson"

;; Destructuring in sequence

(def artist-list '(:monet :austen :beethoven :dickinson))
(let [[painter novelist composer] artist-list]
  (str "The painter is: " painter
       " The novelist is: " novelist
       " The compose is: " composer))
;; => "The painter is: :monet The novelist is: :austen The compose is: :beethoven"

(let [[c1 c2 c3 c4] "Jane"]
  (str c1 "-" c2 "-" c3 "=" c4))
;; => "J-a-n=e"

;; Destructuring function arguments

;; Digging into maps

;; Diving into nested maps

;; The final frontier: Mixing and matching

;; Going further

;; Staying out of trouble

;; In the wild