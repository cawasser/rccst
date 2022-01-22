(ns bh.rccst.ui-component.utils)


(defn hex->rgba
  "convert a color in hexidcemial (stirng) into a hash-map of RGBA

  ---

  - hex-color : (string) hex encoded color, such as \"#ff0000\" (red) or \"#00CED1\" (dark turquoise)

  returns hash-map containing:

  | key  | type    | range   | description    |
  |:-----|:-------:|:-------:|:---------------|
  | `:r` | integer | 0-255   | red value      |
  | `:g` | integer | 0-255   | green value    |
  | `:b` | integer | 0-255   | blue value     |
  | `:a` | float   | 1.0     | alpha channel value, always returned as 1.0 |
  "
  [hex-color]

  (let [stripped (apply str (rest hex-color))
        [r g b] (re-seq #"\w\w" stripped)]
    {:r (js/parseInt r 16)
     :g (js/parseInt g 16)
     :b (js/parseInt b 16)
     :a 1.0}))


(defn rgba->hex
  "convert a color hash-map of RGBA into a hexidcemial (string)

  ---

  - rgba-color : (hash-map) containineg:

  | key  | type    | range   | description    |
  |:-----|:-------:|:-------:|:---------------|
  | `:r` | integer | 0-255   | red value      |
  | `:g` | integer | 0-255   | green value    |
  | `:b` | integer | 0-255   | blue value     |
  | `:a` | float   | 1.0     | alpha channel value, always returned as 1.0 |

  returns string containing the encoded color, such as \"#ff0000\" (red) or \"#00CED1\" (dark turquoise)
  "
  [{:keys [r g b] :as rgba-color}]

  (let [convertFn (fn [x]
                    (let [s (.toString (js/Number. x) 16)]
                      (if (= (count s) 1)
                        (str  "0" s)
                        s)))]
    (str "#" (convertFn r) (convertFn g) (convertFn b))))


(defn hash->rgba
  "converts a color represented as a ClojureScript hash-map into a format compatible with
  Javascript, HTML, and CSS.

  ---

  - hash-color : (hash-map) containing:

  | key  | type    | range   | description    |
  |:-----|:-------:|:-------:|:---------------|
  | `:r` | integer | 0-255   | red value      |
  | `:g` | integer | 0-255   | green value    |
  | `:b` | integer | 0-255   | blue value     |
  | `:a` | float   | 0-1.0   | alpha channel  |

  returns a Javascript command (string) that various HMTL `:style`s will treat as an rgba color
  "
  [{:keys [r g b a] :as hash-color}]

  (str "rgba(" r "," g "," b "," a ")"))


(defn relative-luminance
  "computes _relative luminance_ per the [W3C](https://www.w3.org/TR/WCAG20/#relativeluminancedef)

  typically this value is uses to determine the proper color (`:white` or `:black`) ot use with a
  colored background.

  ---
  - color : (hash-map) containing:

  | key  | type    | range   | description    |
  |:-----|:-------:|:-------:|:---------------|
  | `:r` | integer | 0-255   | red value      |
  | `:g` | integer | 0-255   | green value    |
  | `:b` | integer | 0-255   | blue value     |

  returns (long) - the relative luminance of the color"

  [{:keys [r g b]}]

  (let [normalFn (fn [x] (/ x 255))
        linearizeFn (fn [x] (if (< x 0.03928)
                              (/ x 12.92)
                              (.pow js/Math (/ (+ x 0.055) 1.055) 2.4)))]
    (+ (* 0.2126 (linearizeFn (normalFn r)))
      (* 0.7152 (linearizeFn (normalFn g)))
      (* 0.0722 (linearizeFn (normalFn b))))))


(defn best-text-color
  "return `\"white\"` or `\"black\"` as the best color for text to be placed 'over'
  the given color.

  ---

  - rgba-color : (hash-map) containing:

  | key  | type    | range   | description    |
  |:-----|:-------:|:-------:|:---------------|
  | `:r` | integer | 0-255   | red value      |
  | `:g` | integer | 0-255   | green value    |
  | `:b` | integer | 0-255   | blue value     |
  | `:a` | float   | 0-1.0   | alpha channel  |

  return `\"white\"` or `\"black\"`
  "
  [rgba-color]

  (if (<= (relative-luminance rgba-color) 0.1833)
    "white"
    "black"))


(defn best-text-color-alpha
  "return `\"white\"` or `\"black\"` as the best color for text to be placed 'over'
  the given color, including it's [alpha channel](https://www.techopedia.com/definition/1945/alpha-channel).

  ---

  - rgba-color : (hash-map) containing:

  | key  | type    | range   | description    |
  |:-----|:-------:|:-------:|:---------------|
  | `:r` | integer | 0-255   | red value      |
  | `:g` | integer | 0-255   | green value    |
  | `:b` | integer | 0-255   | blue value     |
  | `:a` | float   | 0-1.0   | alpha channel  |

  return `\"white\"` or `\"black\"`
  "
  [{a :a :as rgba-color}]

  (if (<= (relative-luminance rgba-color) 0.1833)
    (if (<= 0.25 a) "white" "black")
    "black"))



; how well does relative-luminance work?
(comment
  (do
    (def black {:r 0 :g 0 :b 0 :a 1.0})
    (def white {:r 255 :g 255 :b 255 :a 1.0})
    (def red {:r 255 :g 0 :b 0 :a 1.0})
    (def green {:r 0 :g 255 :b 0 :a 1.0})
    (def blue {:r 0 :g 0 :b 255 :a 1.0}))

  (relative-luminance black)
  (relative-luminance white)
  (relative-luminance red)
  (relative-luminance green)
  (relative-luminance blue)

  ())


; how well does best-text-color work?
(comment
  (do
    (def black {:r 0 :g 0 :b 0 :a 1.0})
    (def white {:r 255 :g 255 :b 255 :a 1.0})
    (def red {:r 255 :g 0 :b 0 :a 1.0})
    (def green {:r 0 :g 255 :b 0 :a 1.0})
    (def blue {:r 0 :g 0 :b 255 :a 1.0}))

  (best-text-color black)
  (best-text-color white)
  (best-text-color red)
  (best-text-color green)
  (best-text-color blue)

  ())
