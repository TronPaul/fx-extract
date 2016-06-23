(ns fx-extract.ofx-test
  (:require [clojure.test :refer :all]
            [fx-extract.ofx :refer :all]
            [clojure.java.io :as io]))

(deftest read-ofx-file-test
  (testing "Test read ofx file"
    (let [ts (read-ofx-file (io/resource "sample.ofx"))]
      (is (= 3 (count ts)))
      (is (= true (every? (comp nil? :correction-id) ts))))))
