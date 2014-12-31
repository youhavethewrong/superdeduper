(ns superdeduper.core-test
  (:require [clojure.test :refer :all]
            [superdeduper.core :refer :all]))

(deftest list-files-in-test-dir
  (testing "Should list all the files in this directory."
    (is (= "core_test.clj"
           (.getName (first (next (list-files "test/superdeduper"))))))))
