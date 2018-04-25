(ns arinc424.core
  (:gen-class)
  (:require [arinc424.parser :refer [parse-cifp]]
            [arinc424.serializer :refer [records-to-folders!]]))

(defn get-records [cifp]
  (parse-cifp cifp))

(defn get-record-with-path [records path]
  (keep #(if (= path (-> % keys first))
           (% path)
           nil)
        records))

(defn check-args [args]
  (= (count args) 2))

(defn get-vors [records]
  (get-record-with-path records [:navaid :vhf-navaid 0]))

(defn -main
  [& args]
  (if (check-args args)
    (-> args first slurp get-records get-vors (records-to-folders! (second args) "vors.json"))
    (println "Usage: airinc424 <cfip file> <output folder>")))

(-main "data/FAACIFP15" "output")