(ns receipts.price
  (:gen-class)
  ;(:require [receipts.reader :as reader])
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def prices (atom nil))

(defn fetch
  "Load price from CSV"
  [file]
  (def p (with-open [rdr (io/reader file)]
    (doall (map (fn [line]
                    (let [part (str/split line #",")]
                      {:code (get part 0) :price (get part 1)}))
                (line-seq rdr)))))
  (swap! prices (fn [old] p)))
