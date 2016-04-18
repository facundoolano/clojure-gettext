# clojure-gettext

Utilities to write multilingual Clojure programs, vaguely inspired by [GNU gettext](https://www.gnu.org/software/gettext/).

## Installation

To install gettext, add the following to your project map as a dependency:

```clojure
[gettext "0.1.0"]
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

The dictionary to use for translations can be set statically with the
`:gettext-source` keyword in the `project.clj`:

```clojure
(defproject myprogram "0.1.0-SNAPSHOT"
  ...
  :gettext-source myprogram.translations.spanish/dictionary)
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

// pluralization, gender

// note also for english rules as 'a'/'an'

### Scan files for translatable strings

## License

Copyright © 2016 Facundo Olano

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
