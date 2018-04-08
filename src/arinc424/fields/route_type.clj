(ns arinc424.fields.route-type
  (:require [arinc424.helpers :refer :all]))

(def enroute-airway-route-type-values
  {
   "A" :airline-airway
   "C" :control
   "D" :direct-route
   "H" :helicopter-airways
   "O" :officially-designated-airways
   "R" :rnav-airways
   "S" :undesignated-ats-route
   })

(def enroute-preferred-route-type-values
  {
   "C" :north-american-routes-common-portion
   "D" :preferential-routes
   "J" :pacific-oceanic-transition-routes
   "M" :tacan-routes-australia
   "N" :north-american-routes-non-common-portion
   "0" :preferential-overflight-routes
   "P" :preferred-routes
   "S" :traffic-orientation-system-routes
   "T" :tower-enroute-control-routes
   })

(def airport-heliport-sid-route-type-values
  {
   "0" (kw "Engine Out SID")
   "1" (kw "SID Runway Transition")
   "2" (kw "SID or SID Common Route")
   "3" (kw "SID Enroute Transition")
   "4" (kw "RNAV SID Runway Transition")
   "5" (kw "RNAV SID or SID Common Route")
   "6" (kw "RNAV SID Enroute Transition")
   "F" (kw "FMS SID Runway Transition")
   "M" (kw "FMS SID or SID Common Route")
   "S" (kw "FMS SID Enroute Transition")
   "T" (kw "Vector SID Runway Transition")
   "V" (kw "Vector SID Enroute Transition")
   })

(def airport-heliport-star-route-type-values
  {
   "1" (kw "STAR Enroute Transition")
   "2" (kw "STAR or STAR Common Route")
   "3" (kw "STAR Runway Transition")
   "4" (kw "RNAV STAR Enroute Transition")
   "5" (kw "RNAV STAR or STAR Common Route")
   "6" (kw "RNAV STAR Runway Transition")
   "7" (kw "Profile Descent Enroute Transition")
   "8" (kw "Profile Descent Common Route")
   "9" (kw "Profile Descent Runway Transition")
   "F" (kw "FMS STAR Enroute Transition")
   "M" (kw "FMS STAR or STAR Common Route")
   "S" (kw "FMS STAR Runway Transition")
   })


(defn airport-heliport-approach-route-type-values [val]
  (segmented-value
    [
     ;route-type-fields
     {
      "A" (kw "Approach Transition")
      "B" (kw "Localizer/Backcourse Approach")
      "E" (kw "RNAV, GPS Required Approach")
      "F" (kw "Flight Management System (FMS) Approach")
      "G" (kw "Instrument Guidance System (IGS) Approach")
      "I" (kw "Instrument Landing System (ILS) Approach")
      "J" (kw "LAAS-GPS/GLS Approach")
      "K" (kw "WAAS-GPS Approach")
      "L" (kw "Localizer Only (LOC) Approach")
      "M" (kw "Microwave Landing System (MLS) Approach")
      "N" (kw "Non-Directional Beacon (NDB) Approach")
      "P" (kw "Global Positioning System (GPS) Approach")
      "R" (kw "Area Navigation (RNAV) Approach")
      "T" (kw "TACAN Approach")
      "U" (kw "Simplified Directional Facility (SDF) Approach")
      "V" (kw "VOR Approach")
      "W" (kw "Microwave Landing System (MLS), Type A Approach")
      "X" (kw "Localizer Directional Aid (LDA) Approach")
      "Y" (kw "Microwave Landing System (MLS), Type B and C Approach")
      "Z" (kw "Missed Approach")
      }
     ; qualifier-1
     {
      "D" (kw "DME Required for Procedure")
      "N" (kw "DME Not Required for Procedure")
      "T" (kw "DME/DME Required for Procedure")
      "P" (kw "Primary Missed Approach")
      "S" (kw "Secondary Missed Approach")
      }
     ; qualifier-2
     {
      "C" (kw "Procedure with Circle-To-Land Minimums")
      "S" (kw "Procedure with Straight-in Minimums")
      "H" (kw "Procedure Designed for Helicopter to Runway")
      }] val))