(ns airinc424.fields.navaid-class-test
  (:require [airinc424.fields.navaid-class :refer :all]
            [clojure.core.match :refer [match]]
            [clojure.test :refer :all]))

; --- Tests ---

(deftest navaid-class-value-test
  (is (= [:vor :tacan-channels-17-59-70-126] (navaid-class-value "VT   ")))
  (is (= nil (navaid-class-value "VTga")))
  (is (= [:ils-dme-ils-tacan :terminal :biased-ils-dme-or-ils-tacan :colocated-dme] (navaid-class-value " ITD ")))
  (is (= [:mls-dme-p :high-altitude :scheduled-weather-brdcst :non-colocated-vor-and-tacan-or-dme]
         (navaid-class-value " PHBN")))
  (is (= [:ndb :inner-marker :less-than-25-watts :automatic-trans-weather-brdcst :locator-marker-non-colocated]
         (navaid-class-value "HILAN"))))