(ns airinc424.fields.latlong
  (:require [airinc424.helpers :refer :all]
            [clojure.core.match :refer [match]]
            [clojure.test :refer :all]))

(defn dms [degrees minutes seconds]
  (+ degrees (/ minutes 60.0) (/ seconds 3600.0)))

(defn latitude-value [val]
  (match-regex val
               [#"(N|S)(\d{2})(\d{2})(\d{4})"
                #(* (dms (parse-int %2)
                         (parse-int %3)
                         (insert-decimal %4 2))
                    (if (= "N" %1) 1 -1))]))

(defn longitude-value [val]
  (match-regex val
               [#"(E|W)(\d{3})(\d{2})(\d{4})"
                #(* (dms (parse-int %2)
                         (parse-int %3)
                         (insert-decimal %4 2))
                    (if (= "N" %1) 1 -1))]))

(defn station-declination-value [val]
  (match-regex val
               [#"(E|W|T|G)(\d{4})" #(list ({"E" :east-of-true-north
                                             "W" :west-of-true-north
                                             "T" :oriented-to-true-north-and-local-variation-not-zero
                                             "G" :oriented-to-grid-north} %1)
                                           (insert-decimal %2 1))]))
