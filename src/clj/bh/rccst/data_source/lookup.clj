(ns bh.rccst.data-source.lookup)


(def response {:item     "This is a 'Lookup' response"
               :sub-item {:a "it is composed of several Clojure data structures"
                          :b "all nested together"
                          :c #{:like :a :set}
                          :d {:or       :embedded
                              :hashmaps "and keywords all over the place"}}})


(defn lookup []
  response)


