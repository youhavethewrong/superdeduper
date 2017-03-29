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
                (not (.isHidden %))) dirlist))

(defn checksum-files
  [filelist]
  (map
   (fn [file]
     (let [checksum (try
                      (sha-256 file)
                      (catch Exception e #((println e) "Error")))]
       [file checksum]))
   filelist))

(defn group-by-checksum
  [checksums]
  (reduce
   (fn [acc [file checksum]]
     (if (get acc checksum)
       (update acc checksum #(conj % file))
       (assoc acc checksum (list file))))
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

(defn display-report
  [file-map]
  (println (banner "*"))
  (doseq [[checksum files] file-map]
    (println (str "  <" checksum ">"))
    (println (banner "="))
    (doseq [f files]
      (println (.getAbsolutePath f)))
    (println (banner "~"))))

(defn -main
  [& args]
  (display-report
   (-> args
        (first)
        (list-files)
        (find-files)
        (checksum-files)
        (group-by-checksum)
        (remove-snowflakes))))
