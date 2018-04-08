(ns arinc424.field-defs
  (:require [arinc424.fields.route-type :refer :all]
            [arinc424.fields.latlong :refer :all]
            [arinc424.fields.navaid-class :refer :all]
            [arinc424.helpers :refer :all]))

; Types http://dev.x-plane.com/update/data/424-15s.pdf (Ch. 5, pg. 66)

; TODO: Add spec defns here
; Field structure:
; FIELDs looks like:
;
;  {
;   :len      20
;   :match    "[A-Z]{1, 4}"
;   :examples ["example1" "example2" ...]
;   :value-fn (fn (value) result)
;   }
;  or
;  {
;   :len      10
;   :examples ["example1" "example2" ...]
;   :values   {
;              'key1 :value1
;              'key2 :value2
;              ...
;              }
;   }
; if the :values struct is specified, then the field will match only keys in the :values map
; if only the match is specified, then the string will be returned when it is parsed

; Example field elements
;
; FIELDs can look like:
;
;  FIELD
;
;  or
;  {
;   :len      20
;   :examples ["example1" "example2" ...]
;   :sections {
;              'section-code         FIELD
;              'another-section-code FIELD
;              }
;   }
;  or
;  {
;   :len      20
;   :examples ["example1" "example2" ...]
;   :sections {
;              ['section-code 'subsection-code]                 FIELD
;              ['another-section-code 'another-subsection-code] FIELD
;              }
;   }

; You can either put the length in each FIELD element, or at the top-level map

(def field-defs
  {
   ; Record type (5.2)
   :record-type
   {
    :len      1
    :values
              {
               "S" :standard
               "T" :tailored
               }
    :examples ["S" "T"]
    }
   :customer-area-code
   {
    :len      3
    :values
              {
               "USA" :usa
               "EEU" :eeu
               "EUR" :eur
               "MES" :mes
               "PAC" :pac
               "CAN" :can
               "LAM" :lam
               "SPA" :spa
               "SAM" :sam
               "AFR" :afr
               }
    :examples ["EEU" "CAN" "AFR"]
    }

   ; 5.6 pg 69
   :airport-icao-ident
   {
    :len      4
    :matches  #"[A-Z0-9|\s]{4}"
    :examples ["KJFK" "DMIA" "9Y9" "CYUL" "EDDF" "53Y" "CA14"]
    }

   :route-type
   {
    :len      1
    :sections {
               ; Page 70
               [:enroute :airway-and-route]    {:values enroute-airway-route-type-values}
               ; Page 70
               [:enroute :preferred-route]     {:values enroute-preferred-route-type-values}
               ; Page 71
               [:airport :sid]                 {:values airport-heliport-sid-route-type-values}
               [:heliport :sid]                {:values airport-heliport-sid-route-type-values}
               ; Page 71
               [:airport :star]                {:values airport-heliport-star-route-type-values}
               [:heliport :star]               {:values airport-heliport-star-route-type-values}

               ; Page 71
               [:heliport :approach-procedure] {
                                                :matches  #"(A|B|E|F|G|I|J|K|L|M|N|P|R|T|U|V|W|X|Y|Z)(D|N|T|P|S| )(C|S|H| )"
                                                :examples ["ANC" "LSH" "V S" "VDS" "LDS"]
                                                :value-fn airport-heliport-approach-route-type-values}
               }
    }
   ; Page 71
   :route-ident
   {
    :sections {
               [:enroute :airway-and-route] {
                                             :len      5
                                             :matches  #"[A-Z0-9|\s]{5}"
                                             :examples ["V216" "C1150", "J380" "UA16" "UB414"]
                                             }
               [:enroute :preferred-route]  {
                                             :len      10
                                             :matches  #"[A-Z0-9|\s]{10}"
                                             :examples ["N111B" "TOS13" "S14WK" "CYYLCYYC" "TCOLAR" "KZTLKSAV" "NDICANRY"]
                                             }
               }
    }

   ; Page 74
   :icao-code
   {
    :len     2
    :matches #"[A-Z0-9|\s]{2}"
    :example ["K1" "K7" "PA" "MM" "EG" "UT"]
    }

   ; Page 74
   :continuation-record-num
   {
    :len      1
    :matches  #"[A-Z0-9]"
    :value-fn zero-through-z-value
    }

   :vor-ndb-freq
   {
    :len 5
    :sections
         {
          [:navaid :vhf-navaid]
          {
           :matches  #"[0-9]{5}"
           :value-fn #(insert-decimal % 2)
           }
          [:navaid :ndb-navaid]
          {
           :matches  #"[0-9]{5}"
           :value-fn #(insert-decimal % 1)
           }
          }
    }

   :navaid-class
   {
    :len      5
    :matches  #"[A-Z|\s]{5}"
    :value-fn navaid-class-value
    }

   ; 5.36 pg. 85
   :latitude
   {
    :len      9
    :examples ["N39513881"]
    :matches  #"(N|S)(\d{2})(\d{2})(\d{4})"
    :value-fn latitude-value
    }

   ; 5.37 pg. 85
   :longitude
   {
    :len      10
    :examples ["W104450794"]
    :matches  #"(E|W)(\d{3})(\d{2})(\d{4})"
    :value-fn longitude-value
    }

   ; 5.31 pg. 83
   :vor-ndb-ident
   {
    :len      4
    :examples ["DEN " "6YA " "PPI " "TIKX"]
    :matches  #"[A-Z0-9|\s]{4}"
    }

   ; 5.38 pg. 86
   :dme-ident
   {
    :len      4
    :examples ["MCR " "DEN " "IDVR" "DN  " "    "]
    :matches  #".{4}"
    }

   ; 5.66 pg. 91
   :station-declination
   {
    :len      5
    :examples [""]
    :matches  #"(E|W|T|G)(\d{4})"
    :value-fn station-declination-value
    }

   ; 5.40 pg. 86
   :dme-elevation
   {
    :len      5
    :examples ["00530", "-0140"]
    :matches  #"(-|\d\d{4})"
    :value-fn parse-int
    }

   ; 5.149 pg. 112
   :figure-of-merit
   {
    :len      1
    :examples ["0" "3"]
    :values   {"0" :terminal-use
               "1" :low-altitude-use
               "2" :high-altitude-use
               "3" :extended-high-altitude-use
               "9" :out-of-service}
    }

   ; 5.90 pg. 98
   :ils-dme-bias
   {
    :len      2
    :examples ["13" "91"]
    :matches  #"(\d\d)|  "
    :value-fn #(if (= "  " %) nil (insert-decimal % 1))
    }

   ; 5.150 pg. 112
   :frequency-protection-distance
   {
    :len      3
    :examples ["030" "150" "600"]
    :matches  #"\d\d\d|   "
    :value-fn parse-int
    }

   ; 5.197 pg. 95
   :datum-code
   {
    :len      3
    :matches  #"..."
    :examples ["AGD" "NAS" "WGA"]
    }

   ; 5.71 pg. 3.27
   :vor-name
   {
    :len     30
    :matches #".{30}"
    }

   ; 5.31 pg. 83
   :file-record-num
   {
    :len      5
    :matches  #"\d{5}"
    :value-fn parse-int
    }

   ; 5.32 pg. 83
   :cycle-data
   {
    :len      4
    :matches  #"\d\d\d\d"
    :value-fn (fn [val] (match-regex val
                                     [#"(\d\d)(\d\d)" #(hash-map :year (parse-int %1)
                                                                 :cycle (parse-int %2))]))
    }
   })