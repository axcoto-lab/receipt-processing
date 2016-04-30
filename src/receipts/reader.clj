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
  (Float/parseFloat paid))

(defn parse-store-name
  [line]
  (last (clojure.string/split (clojure.string/trim line) #"#")))

(defn process-receipt
  "Process receipt"
  [receipt]
  (def rc (atom {}))
  (def lines (clojure.string/split-lines receipt))
  (def store-name (parse-store-name (first lines)))
  (def prices p/fetch)
  (loop [index 1 total 0]
    (if (< index (- (count lines) 1))
      (let [[j paid]
              (if (.contains (nth lines (+ index 1)) "VOIDED PRODUCT")
                [2 0]
                [1, (process-line (nth lines index))])]
              (recur (+ index j) (+ total paid)))
      total)))

(defn report
  "Read from data"
  [directory]
  (def d (clojure.java.io/file directory))
  (let [fs (file-seq d)
        price (p/fetch "./data/prices.csv")]
    (doseq [f fs]
      (if (and (.isFile f) (not (= "prices.csv" (.getName f))))
        (println (.getParent f) "/" (.getName f) (process-receipt (slurp (str (.getParent f) "/" (.getName f)))) "\n" ))
    )))




(defn diff-line
  "Process a line of receipt"
  [line prices]
  (def parts (clojure.string/split line #"\s+"))
  (let [[code paid]
      (if (= (count (last parts)) 1)
        ; Has random char at the end
        [(nth parts (- (count parts) 3)) (nth parts (- (count parts) 2))]
        [(nth parts (- (count parts) 2)) (last parts)])]
      (- (get prices code) (Float/parseFloat paid))))


(defn diff-receipt
  "Process receipt"
  [receipt prices]
  (def rc (atom {}))
  (def lines (clojure.string/split-lines receipt))
  (def store (parse-store-name (first lines)))
  (def plusminus (loop [index 1 total 0]
    (if (< index (- (count lines) 1))
      (let [[j paid]
              (if (.contains (nth lines (+ index 1)) "VOIDED PRODUCT")
                [2 0]
                [1, (diff-line (nth lines index) prices)])]
        (recur (+ index j) (+ total paid)))
      total)))
  {store plusminus})

(defn display
  [store]
  (println store)
  )

(defn scan
  "Read from data"
  [directory]
  (def d (clojure.java.io/file directory))
  (let [fs (file-seq d)
        prices (p/fetch "./data/prices.csv")]
    (println (apply merge-with +
      (map (fn [f]
          (if (and (.isFile f) (not (= "prices.csv" (.getName f))))
            (let [csv-path (str (.getParent f) "/" (.getName f))]
              (diff-receipt (slurp csv-path) prices))))
         fs)))))


