(ns arinc424.core
  (:gen-class)
  (:require [arinc424.parser :refer [parse-cifp]]))


(def cifp (slurp "data/FAACIFP15"))


(defn get-records []
  (parse-cifp cifp))

(def vors (filter #(= [:navaid :vhf-navaid 0] (first %)) (get-records)))

(def group-by #())

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

