(ns arinc424.parser
  (:require [arinc424.records :refer :all]
            [arinc424.helpers :refer :all]
            [arinc424.section :refer :all]
            [arinc424.field-defs :refer :all]
            [clojure.core.match :refer [match]]
            [clojure.test :refer :all]
            [clojure.string :refer [join split-lines trim]]))

(defn has-in-field [[section subsection continu] field key]
  "Returns true if the field def contains the given key at the top-most level,
  or within the appropriate :sections"
  (or
    (contains? field key)
    (not (nil? (get-in field [:sections [section subsection] key])))))

(defn get-in-field [[section subsection continu] field key]
  "Returns the value associated with the given key in the top-most level of the
  specification, or in the appropriate :sections piece"
  (cond
    (contains? field key) (field key)
    (contains? field :sections) (get-in field [:sections [section subsection] key])))

(defn build-values-regex [values]
  "Builds a regex the will accept all of the keys of the given map"
  (join "|" (keys values)))

(defn wrap-into-group [pat]
  "Wraps the given string in ()"
  (str "(" pat ")"))

(defn field-regex [path field]
  "Returns a regex expression that will match the given field in the given section/subsection"
  (cond
    (string? field) (wrap-into-group field)

    (has-in-field path field :matches)
    (-> (get-in-field path field :matches) str wrap-into-group)

    (has-in-field path field :values)
    (-> (get-in-field path field :values) build-values-regex wrap-into-group)))

(defn get-field-spec [key]
  "Returns the appropriate field-def if passed a keyword, returns the string if passed a string"
  (match [key]
         [s :guard string?] s
         [k :guard keyword?] (field-defs k)
         [[name field-type]] (get-field-spec field-type)))

(defn get-field-name [key]
  "Returns the name of the given key field in the record entry"
  (match [key]
         [k :guard string?] :na
         [k :guard keyword?] k
         [[name _]] name))

(defn build-record-matcher [path record]
  "Builds a single regex that captures the entirety of the record definition"
  (-> (map #(field-regex path (get-field-spec %)) record) join re-pattern))

(defn field-len [path field]
  "Returns the length of the field in a given section and subsection"
  (cond
    (string? field) (count field)
    (map? field) (get-in-field path field :len)))

(defn build-record-splitter [path fields]
  "Creates a regular expression that splits the record into its field groups"
  (-> (map #(str "(.{" (field-len path (get-field-spec %)) "})") fields) join re-pattern))

(defn clean-value [val]
  "If val is a string, returns a trimmed version of the string. Otherwise the value is returned"
  (match [val]
         [(s :guard string?)] (trim val)
         :else val))

(defn parse-field [path field value]
  "Returns the parsed version of the given string with the spec provided in field"
  (clean-value
    (cond
      (string? field) value

      (has-in-field path field :values)
      ((get-in-field path field :values) value)

      (has-in-field path field :value-fn)
      ((get-in-field path field :value-fn) value)

      :else value)))

(defn parse-row [row]
  (let [records-patterns (map #(vector % (build-record-matcher % (record-layouts %))) (keys record-layouts))
        found (filter #(re-matches (last %) row) records-patterns)]
    (if (empty? found)
      nil
      (let [[path] (first found)
            fields (record-layouts path)
            splitter (build-record-splitter path fields)
            splitted (subvec (re-matches splitter row) 1)
            parsed (map #(vector
                           (get-field-name %1)
                           (parse-field path (get-field-spec %1) %2))
                        fields splitted)]
        {path (dissoc (into {} parsed) :na)}))))

(defn parse-cifp [cifp]
  (filter #(not (nil? %)) (map parse-row (split-lines cifp))))

(def cifp (slurp "data/FAACIFP15"))

; --- Tools ---

(defn describe-field [path field-name field]
  "Prints the name and length of a record layout for verification"
  {:name field-name
   :pat  (re-pattern (field-regex path field))
   :len  (field-len path field)})

(defn describe-record [path record]
  "Describes each of the records for verification"
  (map #(describe-field path % (get-field-spec %)) record))

(defn find-bad-pattern [path fields]
  "Returns the last field that matches at least one row in the cifp data"
  (if (= 0 (count (re-seq (build-record-matcher path fields) cifp)))
    (if (= 0 (count fields))
      "They're all bad"
      (find-bad-pattern path (subvec fields 0 (- (count fields) 2))))
    (str "Last good expr: " (last fields))))
