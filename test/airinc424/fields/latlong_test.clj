(ns airinc424.fields.latlong-test
  (:require [airinc424.fields.latlong :refer :all]
            [airinc424.helpers :refer :all]
            [clojure.test :refer :all]))

; --- Tests ---

(deftest dms-test
  (is (approx= 40.33611111 (dms 40 20 10))))

(deftest latitude-value-test
  (is (approx= 39.86078056 (latitude-value "N39513881")))
  (is (approx= -78.86078056 (latitude-value "S78513881"))))

(deftest longitude-value-test
  (is (approx= -90.359255555 (longitude-value "W090213332")))
  (is (approx= -120.35925555 (longitude-value "E120213332")))
  (is (approx= -104.75220555 (longitude-value "W104450794"))))

(deftest station-declination-value-test
  (is (= [:east-of-true-north 0.0] (station-declination-value "E0000")))
  (is (= [:west-of-true-north 1.0] (station-declination-value "W0010")))
  (is (= [:oriented-to-grid-north 101.0] (station-declination-value "G1010"))))