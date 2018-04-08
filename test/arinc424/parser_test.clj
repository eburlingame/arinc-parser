(ns arinc424.parser-test
  (:require [arinc424.parser :refer :all]
            [arinc424.records :refer :all]
            [arinc424.helpers :refer :all]
            [arinc424.section :refer :all]
            [arinc424.field-defs :refer :all]
            [clojure.core.match :refer [match]]
            [clojure.test :refer :all]
            [clojure.string :refer [join split-lines trim]]))

; --- Tests ---

(def sample-vor "SUSAD        BRL   K3011140VDLW N40432444W090553320    N40432444W090553320E0050007271     NAWBURLINGTON                    212121703")

(deftest test-parse-row
  (is (= {[:navaid :vhf-navaid 0]
          {:icao-code                     "K3",
           :frequency-protection-distance nil,
           :dme-ident                     "",
           :vor-ndb-freq                  111.4,
           :vor-longitude                 -90.92588888888889,
           :vor-name                      "BURLINGTON",
           :vor-ndb-ident                 "BRL",
           :dme-latitude                  40.72345555555556,
           :dme-longitude                 -90.92588888888889,
           :navaid-class                  nil,
           :ils-dme-bias                  nil,
           :cycle-data                    {:cycle 3, :year 17},
           :vor-latitude                  40.72345555555556,
           :airport-icao-ident            "",
           :figure-of-merit               :low-altitude-use,
           :continuation-record-num       0,
           :section-code                  "D",
           :file-record-num               21212,
           :subsection-code               "",
           :dme-elevation                 727,
           :station-declination           '(:east-of-true-north 5.0),
           :record-type                   :standard,
           :datum-code                    "NAW",
           :customer-area-code            :usa}}
         (parse-row sample-vor)))
  (is (= nil (parse-row "doesn't fit"))))

(deftest test-build-values-regex
  (is (= "A|B|C" (build-values-regex {"A" :a "B" :b "C" :c}))))

(deftest test-field-len
  (is (= 10 (field-len [:navaid :vhf-navaid 0] {:len 10})))
  (is (= 15 (field-len [:navaid :vhf-navaid 0] {:len      15
                                                :sections {[:navaid :vhf-navaid] {:len 20}}})))
  (is (= 20 (field-len [:navaid :vhf-navaid 0] {:sections {[:navaid :vhf-navaid] {:len 20}}}))))

(deftest test-get-field
  (is (= "test" (get-field-spec "test")))
  (is (= "test" (get-field-spec [:name "test"])))
  (is (= (field-defs :airport-icao-ident) (get-field-spec :airport-icao-ident)))
  (is (= (field-defs :airport-icao-ident) (get-field-spec [:something :airport-icao-ident]))))

(def enroute-path [:enroute :airway-and-route 0])
(def vor-path [:navaid :vhf-navaid 0])

(deftest test-parse-field
  (is (approx= 40.7234555555555 (parse-field vor-path (get-field-spec :latitude) "N40432444")))
  (is (approx= -90.925888888888 (parse-field vor-path (get-field-spec :longitude) "W090553320")))
  (is (= :high-altitude-use (parse-field vor-path (get-field-spec :figure-of-merit) "2")))
  (is (= -150 (parse-field vor-path (get-field-spec :dme-elevation) "-0150")))
  (is (= 117.75 (parse-field vor-path (get-field-spec :vor-ndb-freq) "11775")))
  (is (= "ABCD" (parse-field vor-path (get-field-spec :dme-ident) "ABCD"))))

(deftest test-field-regex
  (is (= "([A-Z0-9|\\s]{5})" (field-regex enroute-path (field-defs :route-ident))))
  (is (= "([A-Z0-9|\\s]{2})" (field-regex enroute-path (field-defs :icao-code))))
  (is (= "(test)" (field-regex enroute-path "test")))
  (is (= "(0|1|2|3|9)" (field-regex enroute-path (field-defs :figure-of-merit)))))

(def navaid-path [:navaid :vhf-navaid 0])

(deftest test-build-record-splitter
  (is (= "(.{1})(.{3})(.{1})(.{1})(.{4})(.{2})(.{1})(.{4})(.{2})(.{2})(.{1})(.{5})(.{5})(.{9})(.{10})(.{4})(.{9})(.{10})(.{5})(.{5})(.{1})(.{2})(.{3})(.{3})(.{30})(.{5})(.{4})"
         (str (build-record-splitter navaid-path (record-layouts navaid-path))))))

(deftest test-build-record-regex
  (is (=
        "(S|T)(EEU|SAM|CAN|AFR|EUR|USA|MES|PAC|LAM|SPA)(D)( )"
        (str (build-record-matcher navaid-path (subvec (record-layouts navaid-path) 0 4))))))

; --- Manual ---
;
;(def vor-pat (build-record-matcher navaid-path (record-layouts navaid-path)))
;
;(describe-record navaid-path (record-layouts navaid-path))
;
;(build-record-splitter navaid-path (record-layouts navaid-path))
