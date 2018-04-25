(ns arinc424.serializer
  (:require [arinc424.sample-records :refer :all]
            [arinc424.helpers :refer :all]
            [clojure.data.json :as json]))

(defn folder-name [parent subs filename]
  (clojure.string/join "/" (concat [parent] subs [filename])))

(defn serialize-records [recs]
  (json/write-str recs))

(defn records-to-folders! [records folder filename]
  (let [groups (group-by (fn [r] [(-> r :vor-latitude (trun 10))
                                  (-> r :vor-longitude (trun 10))]) records)]
    (doall (map #(put-file (folder-name folder % filename)
                           (serialize-records (groups %)))
                (keys groups)))
    nil))

(records-to-folders! sample-records "output" "vors.json")