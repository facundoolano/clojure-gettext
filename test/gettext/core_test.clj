(ns gettext.core-test
  (:require [clojure.test :refer :all]
            [gettext.core :refer :all]))

(def dictionary {"Hello, world!" "Hola, mundo!"
                 "Hello, %s" "Hola, %s"
                 "the %s is closed." #(if (= :female (:gender %))
                                         "la %s esta cerrada"
                                         "el %s esta cerrado")})

(deftest gettext-test
  (with-bindings {#'gettext.core/*text-source* dictionary}
    (testing "Literal replacement"
      (is (= "Hola, mundo!" (_ "Hello, world!"))))
    (testing "Format replacement"
      (is (= "Hola, Facundo" (_ "Hello, %s" "Facundo"))))
    (testing "Ignore format argument"
      (is (= "Hola, mundo!" (_ "Hello, world!" "unused" "argument"))))
    (testing "Default to key when missing"
      (is (= "Goodbye cruel world..." (_ "Goodbye cruel world..."))))
    (testing "Use empty context when replacement is a function"
      (is (= "el auto esta cerrado" (_ "the %s is closed." "auto"))))))

(deftest pgettext-test
  (with-bindings {#'gettext.core/*text-source* dictionary}
    (testing "Change replacement based on context"
      (is (= "el auto esta cerrado" (p_ {:gender :male} "the %s is closed." "auto")))
      (is (= "la puerta esta cerrada" (p_ {:gender :female} "the %s is closed." "puerta"))))
    (testing "Ignore context when replacement is not a function"
      (is (= "Hola, Facundo" (p_ {:gender :male} "Hello, %s" "Facundo"))))))
