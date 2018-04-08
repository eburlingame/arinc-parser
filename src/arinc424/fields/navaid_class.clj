(ns arinc424.fields.navaid-class
  (:require [arinc424.helpers :refer :all]
            [clojure.core.match :refer [match]]
            [clojure.test :refer :all]))

(defn navaid-class-value [val]
  ; Page 84
  (match-regex val
               ; VHF NAVAID
               [#"(V)(T|M|D|)   " #(list ({"V" :vor} %1)
                                         ({"T" :tacan-channels-17-59-70-126
                                           "M" :tacan-1-16-and-80-69
                                           "D" :dme} %2))
                #" (I|N|P)(T|L|H|U|C)(D|A|B|W)(N| )" #(list ({"I" :ils-dme-ils-tacan
                                                              "N" :mls-dme-n
                                                              "P" :mls-dme-p} %1)
                                                            ({"T" :terminal
                                                              "L" :low-altitude
                                                              "H" :high-altitude
                                                              "U" :unrestricted
                                                              "C" :tacan-part-of-ils-tacan} %2)
                                                            ({"D" :biased-ils-dme-or-ils-tacan
                                                              "A" :automatic-transweather-brdcst
                                                              "B" :scheduled-weather-brdcst
                                                              "W" :no-voice-on-navaid-freq} %3)
                                                            ({"N" :non-colocated-vor-and-tacan-or-dme
                                                              " " :colocated-dme} %4))
                ; L/MF NAVAID
                #"(H|S|M)(I|M|O|C)(H|M|L)(A|B|W)(B|A|N)" #(list ({"H" :ndb
                                                                  "S" :sabh
                                                                  "M" :marine-beacon} %1)
                                                                ({"I" :inner-marker
                                                                  "M" :middle-marker
                                                                  "O" :outer-marker
                                                                  "C" :backcourse-marker} %2)
                                                                ({"H" :2000-watts-or-more
                                                                  "M" :25-to-50-watts
                                                                  "L" :less-than-25-watts} %3)
                                                                ({"A" :automatic-trans-weather-brdcst
                                                                  "B" :scheduled-weather-brdcst
                                                                  "W" :no-voice-on-navaid-freq} %4)
                                                                ({"B" :bfo-operation-required
                                                                  "A" :locator-marker-colocated
                                                                  "N" :locator-marker-non-colocated} %5))]))
