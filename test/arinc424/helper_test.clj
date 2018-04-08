(ns arinc424.helper-test
  (:require [clojure.string :refer [replace trim lower-case]]
            [clojure.core.match :refer [match]]
            [clojure.test :refer :all]
            [arinc424.helpers :refer :all]))

; --- Tests ---
(deftest test-zero-through-z-value
  (is (= 11 (zero-through-z-value "B")))
  (is (= 35 (zero-through-z-value "Z")))
  (is (= 0 (zero-through-z-value "0")))
  (is (= 5 (zero-through-z-value "5")))
  (is (= 9 (zero-through-z-value "9"))))

(deftest test-insert-decimal
  (is (= 117.3 (insert-decimal "11730" 2)))
  (is (= 11730.0 (insert-decimal "11730" 0)))
  (is (= 1.0 (insert-decimal "1" 0)))
  (is (= 1.1731 (insert-decimal "11731" 4))))

(deftest test-segmented-value
  (is (= [1 4] (segmented-value [{"A" 1 "B" 2} {"A" 3 "D" 4}] "AD"))))