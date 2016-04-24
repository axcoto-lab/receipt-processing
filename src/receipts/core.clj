(ns receipts.core
  (:gen-class)
  (:require [[receipts.reader :as reader]
            [receipts.price :as p]]))

(defn -main
  "Report receipt"
  [& args]
  (println (reader/scan (get args 0))))
