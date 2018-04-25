(ns arinc424.helpers
  (:require [clojure.string :refer [replace trim lower-case]]
            [clojure.core.match :refer [match]]
            [clojure.test :refer :all]))

(defn pow [b e] (Math/pow b e))

(defn kw [str]
  (keyword (lower-case (replace (trim (replace (replace str #"\(.+\)" "") #",|-|_" " ")) #"\s+" "-"))))

(defn parse-int [number-string]
  (try (Integer/parseInt number-string)
       (catch Exception e nil)))

(defn parse-double [number-string]
  (try (Double/parseDouble number-string)
       (catch Exception e nil)))

(defn pattern-matches? [pat value]
  (not (nil? (re-matches (re-pattern pat) value))))

(defn ascii-val [str]
  (first (map int str)))

(defn match-regex [val forms]
  "Takes a string value and list of regex/function pairs. The first regex to match the
  string will have its corresponding function applied with the groups of the regexp"
  (let [valid-form (first (filter #(pattern-matches? (first %) val) (partition 2 forms)))
        [valid-regex valid-value-fn] valid-form]
    (if (nil? valid-form)
      nil
      (apply valid-value-fn (subvec (re-find valid-regex val) 1)))))

(defn zero-through-z-value [s]
  "Returns the index of a single-char string where 0-9 come first,
   then A represents 10, B is 11, and so on."
  (match [s]
         [(_ :guard #(pattern-matches? #"[A-Z]" %))] (+ 10 (- (ascii-val s) (ascii-val "A")))
         [(_ :guard #(pattern-matches? #"[0-9]" %))] (parse-int s)))

(defn insert-decimal [str place]
  "Inserts a decimal place in a given string where the place
   is given from the right"
  (/ (parse-double str) (pow 10 place)))

(defn segmented-value [maps value]
  "Gets a value from a segmented set of values. Each character corresponds
   a key in the list of maps given."
  (map (fn [m c] (m (str c))) maps value))

(defn approx= [exp act]
  (let [tolerance 0.00000001]
    (and (< exp (+ act tolerance))
         (> exp (- act tolerance)))))

(defn file-exists [f]
  (.exists (clojure.java.io/as-file f)))

(defn put-file [f c]
  (let [file-name f]
    (clojure.java.io/make-parents f)
    (spit f c)))

(defn trun
  ([v] (quot v 1))
  ([v g] (* g (quot (/ v g) 1))))