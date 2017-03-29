(ns superdeduper.core-test
  (:require [clojure.test :refer :all]
            [superdeduper.core :refer [list-files find-files checksum-files]]))

(def license-checksum
  "be3a4413df84d4739c6caed110e9351cafc60dacc845b7a8779ff4feaef05e0d")

(def test-checksum
  "f2ca1bb6c7e907d06dafe4687e579fce76b37e4e93b7605022da52e6ccc26fd2")

(deftest list-everything-in-test-dir
  (testing "Should list everything in this directory, including the directory."
    (is (= (list "superdeduper" "core_test.clj")
           (map #(.getName %) (list-files "test/superdeduper"))))))

(deftest files-only
  (testing "Should only list files in this directory."
    (is (= (list "core_test.clj"))
        (map #(.getName %) (find-files (list-files "test/superdeduper"))))))

(deftest checksum
  (testing "Should checksum the license file."
    (is (= license-checksum
           (first (keys (first
            (checksum-files (find-files (list-files "LICENSE")) {}))))))))

(deftest find-duplicates
  (testing "Should find two files with the same checksum."
    (let [filemap (-> "dev-resources"
                      (list-files)
                      (find-files)
                      (checksum-files))]
          (is (= 1 (count filemap)))
          (is (= 2 (count (get filemap test-checksum))))
      )))
