(ns clj.rccst.data-source.lookup-test
  (:require [clojure.test :refer :all]
            [schema.core :as schema]

            [rccst.data-source.lookup :as sut]
            [rccst.data-source.lookup.schema :as s]))


(deftest lookup
  (is (= sut/response (sut/lookup)))
  (is (= sut/response (schema/validate s/Lookup (sut/lookup)))))

