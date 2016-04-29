(ns receipts.reader
  (:gen-class)
  (:use [clojure-csv.core :only (parse-csv)])
  (:require [receipts.price :as p]))

(defn process-line
  "Process a line of receipt"
  [line]
  (def parts (clojure.string/split line #"\s+"))
  (def paid
      (if (= (count (last parts)) 1)
        ; Has random char at the end
        (nth parts (- (count parts) 2))
        (last parts)))
  ;(println paid)
  (read-string paid))

(defn parse-store-name
  [line]
  (clojure.string/trim line))

(defn process-receipt
  "Process receipt"
  [receipt]
  (def rc (atom {}))
  (def lines (clojure.string/split-lines receipt))
  (def store-name (parse-store-name (first lines)))
  (def prices p/fetch)
  (println "Total " (last lines))
  (loop [index 1 total 0]
    (if (< index (- (count lines) 1))
      (let [[j paid]
              (if (.contains (nth lines (+ index 1)) "VOIDED PRODUCT")
                [2 0]
                [1, (process-line (nth lines index))])]
              (recur (+ index j) (+ total paid)))
      total)))

(defn scan
  "Read from data"
  [directory]
  (def d (clojure.java.io/file directory))
  (let [fs (file-seq d)
        price (p/fetch "./data/prices.csv")]
    (doseq [f fs]
      (if (and (.isFile f) (not (= "prices.csv" (.getName f))))
        (println "Receipt " (.getParent f) "/" (.getName f) (process-receipt (slurp (str (.getParent f) "/" (.getName f))))))
    )))
