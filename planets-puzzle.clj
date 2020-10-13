(require 'clojure.pprint)

(+ 1 2)

(def tiles [
             [ ;; T1
               [:M [2.2 7.35]]
               [:S [2.15 7.6]]
               [:E [2.85 7.95]]
               [:J [1.35 8.75]]
               ]
             [ ;; T2
               [:S [2.15 7.5]]
               [:M [2.25 7.35]]
               [:E [2.9 8.0]]
               [:E [1.9 7.0]]
               ]
             [ ;; T3
               [:J [1.25 8.65]]
               [:M [2.25 7.35]]
               [:S [2.35 7.75]]
               [:E [1.9 6.95]]
               ]
             [ ;; T4
               [:M [2.55 7.75]]
               [:S [2.15 7.55]]
               [:E [2.9 8.0]]
               [:J [1.35 8.75]]
               ]


             [ ;; T5
               [:J [1.25 8.65]]
               [:E [2.0 7.0]]
               [:S [2.35 7.75]]
               [:M [2.15 7.25]]
               ]
             [ ;; T6
               [:M [2.6 7.8]]
               [:J [1.25 8.65]]
               [:E [2.85 7.95]]
               [:S [2.15 7.55]]
               ]
             [ ;; T7
               [:E [2.95 8.2]]
               [:J [1.25 8.65]]
               [:M [2.55 7.75]]
               [:J [1.25 8.60]]
               ]
             [ ;; T8
               [:J [1.15 8.55]]
               [:E [1.95 7.0]]
               [:M [2.6 7.80]]
               [:S [2.15 7.50]]
               ]
             [ ;; T9
               [:S [2.40 7.80]]
               [:J [1.90 8.75]]
               [:S [2.40 7.80]]
               [:M [2.55 7.75]]
               ]
  ])


(def tile-sides
(->>
  tiles
(map-indexed
  (fn [i sides]
    [
      (keyword (str "T" (inc i)))
      (->>
        sides
        (map (fn [[planet [x y]]]
               [planet [x (- y x) (- 10.0 y)]])
             )
        (into [])
        )
      ]
    ))
  (into {})
  )
  )

tile-sides
(clojure.pprint/pprint tile-sides)

(def fuzz 0.1)

(defn almost=? [a b]
  (<= (Math/abs (- a b)) fuzz)
  )

(almost=? 1 1.05)
(Math/abs (- 1 1.05))

(defn compare-sides [[pa [xa ya za]] [pb [xb yb zb]]]
  (and
    (= pa pb)
    (almost=? ya yb)
    (almost=? xa zb)
    (almost=? za xb)
    ))


(compare-sides [:E [1 2 2.95]] [:E [3 2 1.05]])

;; verify there are matching sides

(defn find-matches [side tile-map]
    (for [
           [tile-id tile-sides] tile-map
           tile-side tile-sides
           :when (compare-sides side tile-side)
           ]
      [tile-id tile-side]
      ))




(find-matches
  (get-in tile-sides [:T1 0])
  tile-sides
  )



(for [
       [tile-id tile-edges] tile-sides
       new-tile-map (dissoc tile-sides tile-id)
       tile-side tile-edges
       ]
  [tile-id tile-side (find-matches tile-side new-tile-map)]
  )


(map (fn [{ id :id sides :sides }]
     {:id id
      :rot 0
      :sides (map (fn [[planet [x y]]] [planet [x y (- 10 x y)]]) sides)
      }
     )
     tiles
     )


(every? (partial > 6)
 (map #(Math/abs (- %1 %2)) [1 2 3 4] [5 6 7 8])
       )

(every? true?
 (map #(> 6 (Math/abs (- %1 %2))) [1 2 3 4] [5 6 7 8])
       )


;; State map:
{
  :4 {
       :id 1
       :N [:e [1.22 3.5 4.8]]
       :E [:m [4.3 6.7 3.4]]
       :S [:e [1.3 4.55 4.3]]
       :W [:j [3.45 8.65 1.8]]
       }
  }

#_"

 [0 0] [0 1] [0 2]
 [1 0] [1 1] [1 2]
 [2 0] [2 1] [2 2]

"

(def edges

(into []
      (concat
(for [
       r [0 1 2]
       c [0 1]
       ]
  [[[r c] :E] [[r (inc c)] :W]]
  )


(for [
       r [0 1]
       c [0 1 2]
       ]
  [ [[r c] :S] [[(inc r) c] :N] ]
  )
        )
      )
      )
(count edges)