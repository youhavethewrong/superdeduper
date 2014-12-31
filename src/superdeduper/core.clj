(ns superdeduper.core
  (:require [clojure.java.io :as io])
  (:gen-class :main true))

(defn list-files
  [directory]
  (file-seq (io/file directory)))

(defn find-files
  [dirlist]
  (filter #(.isFile %) dirlist))

(defn md5
  [file]
  (let [token (slurp file)
        hash-bytes (doto
                       (java.security.MessageDigest/getInstance "MD5")
                     (.reset)
                     (.update (.getBytes token)))]
       (.toString
         (new java.math.BigInteger 1 (.digest hash-bytes)) 16)))

(defn checksum-files
  [filelist file-map]
  (map (fn [file]
         (assoc @file-map file (md5 file)))
       filelist))

(def file-map (atom {}))

(defn -main
  [& args]
  (println (checksum-files
            (find-files (list-files (first args)))
            file-map)))
