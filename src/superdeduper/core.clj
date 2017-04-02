(ns superdeduper.core
  (:require [clojure.java.io :as io]
            [digest :refer [sha-256]])
  (:gen-class :main true))

(defn list-files
  [directory]
  (file-seq (io/file directory)))

(defn find-files
  "Finds file-like structures that are not hidden according to the underlying OS."
  [dirlist]
  (filter #(and (.isFile %)
                (not (.isHidden %)))
          dirlist))

(defn checksum-files
  [filelist]
  (map
   (fn [file]
     [file (sha-256 file)])
   filelist))

(defn group-by-checksum
  [checksums]
  (reduce
   (fn [acc [file checksum]]
     (if (get acc checksum)
       (update acc checksum #(conj % (.getAbsolutePath file)))
       (assoc acc checksum (list (.getAbsolutePath file)))))
   {}
   checksums))

(defn remove-snowflakes
  [checksums]
  (filter
   (fn [[k v]]
     (> (count v) 1))
   checksums))

(defn banner
  [x]
  (clojure.string/join (repeat 70 x)))

(defn store-report
  [file-map output-location]
  (spit output-location (prn-str file-map))
  file-map)

(defn display-report
  [file-map]
  (println (banner "*"))
  (doseq [[checksum files] file-map]
    (println (str "  <" checksum ">"))
    (println (banner "="))
    (doseq [f files]
      (println f))
    (println (banner "~"))))

(defn -main
  [& args]
  (-> args
      (first)
      (list-files)
      (find-files)
      (checksum-files)
      (group-by-checksum)
      (remove-snowflakes)
      (store-report "report.edn")
      (display-report)))
