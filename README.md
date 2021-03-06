# clojure-gettext

Utilities to write multilingual Clojure programs, vaguely inspired by [GNU gettext](https://www.gnu.org/software/gettext/).

## Installation

To install gettext, add the following to your project map as a dependency:

```clojure
[gettext "0.1.1"]
```

## Usage

### Basic translations

The first step is to mark some strings within your program as being translatable,
by using the `gettext` function or its shorter alias `_`:

```clojure
(ns myprogram.core
  (:require [gettext.core :refer [_]]))

(println (_ "Hello, world!"))
```

Note `gettext` wraps [`format`](https://clojuredocs.org/clojure.core/format) so
you can pass replacement arguments to the call:

```clojure
(println (_ "Hello, %s!" "Facundo"))
```

Once the strings are marked for translation, a dictionary is needed to map them
to a specific translation:

```clojure
(ns myprogram.translations.spanish)

(def dictionary {"Hello, world!" "Hola, mundo!"
                 "Hello, %s" "Hola, %s"})
```

The dictionary to use for translations can be set statically in the `resources/config.clj` file.
The file should consist of a map with the `:gettext-source`:

```clojure
{:gettext-source 'myprogram.translations.spanish/dictionary}
```

Alternatively, the translations dictionary can be bound dynamically using
`with-bindings`:

```clojure
(println (_ "Hello, world!")) ; Will use the translation set at project.clj or return the key if none set

(with-bindings {#'gettext.core/*text-source* myprogram.translations.german/dictionary}
  (println (_ "Hello, world!"))) ; Will print the german translation instead

```

When a key is not found in the translations dictionary, or if no dictionary
has been set, the result of the `gettext` call will be the key string itself.

### Context based translations

There are cases when the original string is not enough to properly translate it
to another language, for example, when working with plural forms or with gender.
In those cases `pgettext` or its alias `p_` can be used.

`pgettext` takes an arbitrary context value as its first argument:

```clojure
(ns myprogram.core
  (:require [gettext.core :refer [p_]]))

(println (p_ {:gender :male} "%s is my friend." "John"))
(println (p_ {:gender :female} "%s is my friend." "Jane"))
```

When defining the translations dictionary, the key string can be mapped to a
function that will take the context and decide on the translation:

```clojure
(ns myprogram.translations.spanish)

(def dictionary {"Hello, world!" "Hola, mundo!"
                 "Hello, %s" "Hola, %s"
                 "%s is my friend." #(if (= (:gender %) :female) "%s es mi amiga." "%s es mi amigo.") })
```

`pgettext` can also be useful even when using a single language, to decouple grammar
logic from app specific logic:

```clojure
(ns myprogram.translations.english)

(defn starts-with-vowel
  [ctx]
  (let [vowel? (set "aeiouAEIOU")]
    (vowel? (first ctx))))

(def dictionary {"I'm carrying a %" #(if (starts-with-vowel %)
                                         "I'm carrying an %s"
                                         "I'm carrying a %s"))})
```

### Scan files for translatable strings
The function `gettext.scan/scan-files` takes a clojure file or directory and
extracts all strings that are passed to `gettext` in any of its flavors. The
strings are packed in a map to facilitate their translation:

```clojure
(require '[gettext.scan :refer [scan-files]])

(scan-files "/Users/facundo/dev/advenjure/src/advenjure")
; Returns
; {"%s was closed." "%s was closed.",
;  "%s was empty." "%s was empty.",
;  "%s what?" "%s what?",
;  "Bye!" "Bye!",
;  "Closed." "Closed."
;  ...}
```

## License

Copyright © 2016 Facundo Olano

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
