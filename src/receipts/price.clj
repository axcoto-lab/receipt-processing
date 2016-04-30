(ns receipts.price
  (:gen-class)
  ;(:require [receipts.reader :as reader])
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn fetch
  "Load price from CSV"
  [file]
  (def p (with-open [rdr (io/reader file)]
    (doall (map (fn [line]
                    (let [part (str/split line #",")]
                      ;(swap! prices (fn [old] (get part 0) (get part 1)
                      [(get part 0) (Float/parseFloat (get part 1))]))
                (line-seq rdr)))))
  (into {} p))

;(defn find-by-code
;  "Find price for an item"
;  [code]
; (println prices)
;  (get prices code))
