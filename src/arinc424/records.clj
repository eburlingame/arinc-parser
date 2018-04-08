(ns arinc424.records
  (:require [arinc424.section :refer :all]
            [arinc424.field-defs :refer :all]))

(def record-layouts
  {
   ; 4.1.2.1 VHF NAVAID Primary Records pg. 26
   [:navaid :vhf-navaid 0]
   [:record-type
    :customer-area-code
    [:section-code (-> section-code :navaid)]
    [:subsection-code (-> subsection-code :navaid :vhf-navaid)]
    :airport-icao-ident
    :icao-code
    " "
    :vor-ndb-ident
    "  "
    :icao-code
    :continuation-record-num
    :vor-ndb-freq
    :navaid-class
    [:vor-latitude :latitude]
    [:vor-longitude :longitude]
    :dme-ident
    [:dme-latitude :latitude]
    [:dme-longitude :longitude]
    :station-declination
    :dme-elevation
    :figure-of-merit
    :ils-dme-bias
    :frequency-protection-distance
    :datum-code
    :vor-name
    :file-record-num
    :cycle-data
    ]
   })