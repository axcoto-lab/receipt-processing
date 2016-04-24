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
      (if (.isFile f)
          (println (str (.getParent f) "/" (.getName f))))
    )))

(def process-receipt
  "Process receipt"
  [receipt]
  (def rc (atom {}))
  (def lines (clojure.string/split-lines "test \n string"))
  (doseq [item lines]
    ()

  ))

(defn- proces-line
  "Process a line of receipt"
  [line]

  )
