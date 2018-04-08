(ns arinc424.section)

(def section-code
  {
   :mora          "A"
   :navaid        "D"
   :enroute       "E"
   :heliport      "H"
   :airport       "P"
   :company-route "R"
   :tables        "T"
   :airspace      "U"
   })

(def subsection-code
  {
   :mora {:grid-mora "S"}
   :navaid
         {
          :vhf-navaid " "
          :ndb-navaid "B"
          }
   :enroute
         {
          :waypoint           "A"
          :airway-marker      "M"
          :holding-pattern    "P"
          :airway-and-route   "R"
          :preferred-route    "T"
          :airway-restriction "U"
          :communications     "V"
          }
   :heliport
         {
          :pads               "A"
          :terminal-waypoint  "C"
          :sid                "D"
          :star               "E"
          :approach-procedure "F"
          :msa                "S"
          :communication      "V"
          }
   :airport
         {
          :reference-point       "A"
          :gates                 "B"
          :terminal              "C"
          :waypoint              "W"
          :sid                   "D"
          :star                  "E"
          :approach-procedure    "F"
          :runway                "G"
          :localizer-glide-slope "I"
          :mls                   "L"
          :localizer-marker      "M"
          :terminal-ndb          "N"
          :pathpoint             "P"
          :flt-planning-arr-dep  "R"
          :msa                   "S"
          :gls-station           "T"
          :communications        "V"
          }
   :company
         {
          :company-route   " "
          :alternate-route "A"
          }
   :tables
         {
          :cruising-table         "C"
          :geographical-reference "G"
          }
   :airspace
         {
          :controlled-airspace  "C"
          :fir-uir              "F"
          :restrictive-airspace "R"
          }
   })