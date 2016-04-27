(ns receipts.reader
  (:gen-class)
  (:use [clojure-csv.core :only (parse-csv)])
  (:require [receipts.price :as p]))

(defn scan
  "Read from data"
  [directory]
  (def d (clojure.java.io/file directory))
  (let [fs (file-seq d)
        price (p/fetch "./data/prices.csv")]
    (doseq [f fs]
      (if (and (.isFile f) (not (= "prices.csv" (.getName f))))
        (process-receipt (slurp (str (.getParent f) "/" (.getName f)))))
    )))

(defn process-line
  "Process a line of receipt"
  [line]
  (let [parts (clojure.string/split line #"\s+")]
    (println parts)
    (read-string (if (= (count (last parts)) 1)
      ; Has random char at the end
      (nth parts (- (count parts) 2))
      (last parts)))))

(defn parse-store-name
  [line]
  (clojure.string/trim line))

(defn process-receipt
  "Process receipt"
  [receipt]
  (println receipt)
  (def rc (atom {}))
  (def lines (clojure.string/split-lines "test \n string"))
  (def store-name (parse-store-name (first lines)))

  (doseq [item (subvec lines 1 (- (count lines) 1))]
    (println (process-line (item)))
  ))
