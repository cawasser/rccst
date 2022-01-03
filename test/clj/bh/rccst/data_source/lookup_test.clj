(ns clj.bh.rccst.data-source.lookup-test
  (:require [clojure.test :refer :all]
            [schema.core :as schema]

            [bh.rccst.data-source.lookup :as sut]
            [bh.rccst.data-source.lookup.schema :as s]))


(deftest lookup
  (is (= sut/response (sut/lookup)))
  (is (= sut/response (schema/validate s/Lookup (sut/lookup)))))

