(ns bh.rccst.views.giants
  (:require [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]
            [woolybear.packs.flex-panel :as flex]
            [re-com.core :as rc]

            [bh.rccst.ui-component.atom.card.flippable-card :as flippable]))


(def image-style {:width        "170px" :height "170px"
                  :display      :block
                  :margin-left  :auto
                  :margin-right :auto})


(def people [{:name  "Rich Hickey"
              :image "imgs/giants/rich-hickey.jpeg"
              :description
              "Discuss various works and presentations

[Are We There Yet?]() (2009)

[Simple Made Easy]() (2011)
              "}

             {:name        "Stu Halloway"
              :image       "imgs/giants/stu-halloway.jpeg"
              :description "Stu is a Principle at Cognitect and talks on many subjects.

[Simplicity Ain't Easy](https://www.youtube.com/watch?v=cidchWg74Y4) (2012)

[The Impedance Mismatch is Our Fault](https://www.infoq.com/presentations/Impedance-Mismatch/) (2012)

[Clojure in 10 Big Ideas](https://www.youtube.com/watch?v=noiGVQoyYHw) (2013)"}

             {:name        "David Nolen"
              :image       "imgs/giants/david-nolen.png"
              :description "David has been the lead developer of [Clojurescript]() for many years.

[ClojureScript: Lisp's Revenge](https://www.youtube.com/watch?v=MTawgp3SKy8) (2013)

[ClojureScript in the Age of TypeScript](https://www.youtube.com/watch?v=3HxVMGaiZbc) (2021)"}

             {:name        "Dan Holmsand"
              :image       "imgs/giants/dan-holmsand.jpeg"
              :description "Dan started [Reagent](https://reagent-project.github.io)"}

             {:name        "Bret Victor"
              :image       "imgs/giants/bret-victor.jpg"
              :description "Bret has worked at Magic Ink and Apple, focusing on human-centric design.

[Drawing Dynamic Visualizations](http://worrydream.com/#!/DrawingDynamicVisualizationsTalk) (2013)

[Media for the Unthinkable](http://worrydream.com/#!/MediaForThinkingTheUnthinkable) (2013)"}

             {:name        "James Reeves"
              :image       "imgs/giants/james-reeves.jpg"
              :description "James is the developer of [Compojure](https://github.com/weavejester/compojure) and [Hiccup](https://github.com/weavejester/hiccup) and many others.

[Transparency through data](https://www.youtube.com/watch?v=zznwKCifC1A) (2017)"}

             {:name        "Dave Martin"
              :image       "imgs/giants/dave-martin.jpeg"
              :description "Dave started the development of [Willa](https://github.com/DaveWM/willa), a data-driven way to develop Kafka Topologies.

[Kafka Streams, the Clojure way](https://blog.davemartin.me/posts/kafka-streams-the-clojure-way/) (2019)"}

             {:name        "Aysylu Greenberg"
              :image       "imgs/giants/aysylu-greenberg.jpeg"
              :description "Aysylu developed [Loom](https://github.com/aysylu/loom), a library for working with 'graph data' in Clojure.

[Loom and Graphs in Clojure](https://www.youtube.com/watch?v=wEEutxTYQQU) (2014)

[+ Loom years 2](https://www.youtube.com/watch?v=eadPwx-bVS8) (2016)"}

             {:name        "Stuart Sierra"
              :image       "imgs/giants/stuart-sierra.jpg"
              :description "Stuart is the developer of [Component](https://github.com/stuartsierra/component), a Clojure library for working with stateful-infrastructure within an application.

[Components Just Enough Structure](https://www.youtube.com/watch?v=13cmHf_kt-Q) (2014)]"}

             {:name        "Mark Nutter"
              :image       "imgs/giants/mark-nutter.jpeg"
              :description "Mark developed [Woolybear](https://github.com/manutter51/woolybear) as an experiment in Atomic Design for Clojurescript UIs.

[Re-usable GUI Components with Re-frame and Atomic Design](https://www.youtube.com/watch?v=JCY_cHzklRs) (2018)"}

             {:name        "Sean Corfield"
              :image       "imgs/giants/sean-corfield.jpeg"
              :description "Sean is a professional Clojure developer. He maintains a number of OSS libraries for the Clojure community
              including [next.jdbc](https://github.com/seancorfield/next-jdbc).

[Real World Clojure Doing Boring Stuff With An Exciting Language](https://www.youtube.com/watch?v=75U3W8Y2zzw) (2020)

[REPL Driven Development, Clojure's Superpower ](https://www.youtube.com/watch?v=gIoadGfm5T8) (2021)"}

             {:name        "Mark Bastian"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Alex Engelberg"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Martin Kleppmann"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Thomas Heller"
              :image       ""
              :description "Discuss [Shadow-Cljs]()]"}

             {:name        "Eric Normand"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Will Byrd"
              :image       ""
              :description "Discuss the various works and presentations"}

             {:name        "Paul deGrandis"
              :image       ""
              :description "Discuss the various works and presentations"}])


(defn- personality
  "Returns a 'flippable' card for a 'person' to be presented on the 'Giants' page. Uses the
  [react-ui-cards](https://github.com/nukeop/react-ui-cards) react component library, specifically
  the [flipping card](https://github.com/nukeop/react-ui-cards#flipping-card)
  variant (there are several provided by the library).

  ---

  - name : (string) the person's name
  - description : (markdown string) whatever you want to say about this person, including things like hyperlinks
  - image : (filepath) [optional] name of the file to show as an image (defaults to imgs/hammer-icon-16x16.png)
  "
  [name description & [image]]
  (log/info "personality" image)
  [flippable/card
   :style {:width 270 :height 400}
   :front [:div {:style flippable/default-style}
           [rc/v-box :src (rc/at)
            :gap "10px"
            :align :center
            :justify :center
            :children [[:figure.image
                        [:img.is-rounded {:src (or (if (empty? image) nil image) "imgs/hammer-icon-16x16.png")}]]
                       [:p.title.is-4.has-text-centered name]]]]
   :back [:div {:style flippable/default-style}
          [layout/section
           [layout/markdown-block description]]]])


(defn columns
  "Builds a react 'fragment' of Bulma [columns](), which have some number of
  [column]() `div`s that each hold a ([[personality]]). Determines how many
  `column` in each `columns` via the `row` parameter. Adds rows of columns
  as needed.

  ___

  - people : (vector of entities) collection of hash-maps that define each person's data (see ([[personality]]))
  - row : (integer) number of columns/cards ro put in each row.
  "
  [people row]
  (into [:<>]
    (for [p (partition-all row people)]
      (into [:div.columns.is-1]
        (for [{:keys [name description image]} p]
          [:div.column
           [personality name description image]])))))


(defn view []
  [layout/page {:extra-classes :rccst}
   [flex/flex-panel {:height "calc(100vh - 2rem)"}
    [flex/flex-top
     [layout/page-header {:extra-classes :rccst}
      [layout/page-title "'Giants'"]
      [layout/markdown-block "It has been said that [\"we stand on the shoulders of giants\"](https://www.phrases.org.uk/meanings/268025.html).
      The following people may not all qualify as 'giants', but we certainly have
      looked to them for insight, inspiration, guidance, and general thoughtfulness."]
      [layout/section]]]

    [columns people 4]]])



